package zorochase.neoarsenal.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import zorochase.neoarsenal.NeoArsenal;
import zorochase.neoarsenal.api.CycledTraitHandler;
import zorochase.neoarsenal.api.NeoArsenalTraits;
import zorochase.neoarsenal.capability.ItemEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class NeoAxeItem extends AxeItem {

    private final CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();
    private final Set<String> ALL_POTENTIAL_TRAITS = ImmutableSet.of(NeoArsenalTraits.DEADLY.getIdentifier(), NeoArsenalTraits.SILKY.getIdentifier(), NeoArsenalTraits.LUCKY.getIdentifier(), NeoArsenalTraits.INCENDIARY.getIdentifier(), NeoArsenalTraits.EFFICIENT.getIdentifier());

    public NeoAxeItem() {
        super(
                ItemTier.DIAMOND, 7, -3.0F,
                new Item.Properties()
                        .group(NeoArsenal.MOD_GROUP)
                        .defaultMaxDamage(0)
                        .maxDamage(0)
                        .rarity(Rarity.RARE)
                        .setNoRepair()
        );
        addPropertyOverride(new ResourceLocation(NeoArsenal.MOD_ID, "trait"), (stack, world, entity) -> cycledTraitHandler.identifierAsFloat(stack));
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return NeoToolCommon.getRarity(cycledTraitHandler, stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack filled = new ItemStack(this);
            filled.getOrCreateTag().putInt(NeoToolCommon.ENERGY_TAG, NeoToolCommon.CAPACITY);
            cycledTraitHandler.initialize(filled, ALL_POTENTIAL_TRAITS, true);
            items.add(filled);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        NeoToolCommon.inventoryTick(cycledTraitHandler, stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return NeoToolCommon.shouldCauseReequipAnimation(cycledTraitHandler, oldStack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        return NeoToolCommon.onWeaponLeftclickEntity(stack, player);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            if (!player.isCreative()) {
                cycledTraitHandler.setCooldown(stack, 20);
                LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
                if (lazy.isPresent()) {
                    IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                    if (storage.getEnergyStored() > 0) {
                        if (cycledTraitHandler.getActiveTraitIdentifier(stack).equals(NeoArsenalTraits.NONE.getIdentifier()))
                            storage.extractEnergy(NeoToolCommon.MAX_TRANSFER - 8, false);
                        else {
                            storage.extractEnergy(NeoToolCommon.MAX_TRANSFER - 2, false);
                        }
                    }
                }
            }
        }
        return true;
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        Block block = BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
        if (block != null) {
            PlayerEntity playerentity = context.getPlayer();
            if (playerentity != null) {
                if (!playerentity.isCreative()) {
                    ItemStack held = context.getItem();
                    LazyOptional<IEnergyStorage> lazy = held.getCapability(CapabilityEnergy.ENERGY);
                    if (lazy.isPresent()) {
                        IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                        if (storage.getEnergyStored() > 0) {
                            world.playSound(playerentity, blockpos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            if (!world.isRemote) {
                                world.setBlockState(blockpos, block.getDefaultState().with(RotatedPillarBlock.AXIS, blockstate.get(RotatedPillarBlock.AXIS)), 11);
                            }
                            storage.extractEnergy(NeoToolCommon.MAX_TRANSFER - 9, false);
                        } else {
                            world.playSound(playerentity, blockpos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                } else {
                    world.playSound(playerentity, blockpos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!world.isRemote) {
                        world.setBlockState(blockpos, block.getDefaultState().with(RotatedPillarBlock.AXIS, blockstate.get(RotatedPillarBlock.AXIS)), 11);
                    }
                }
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        if (player.isCreative()) return true;
        ItemStack held = player.getHeldItem(Hand.MAIN_HAND);
        LazyOptional<IEnergyStorage> lazy = held.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
            return storage.getEnergyStored() > 0;
        }
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
            if (!(storage.getEnergyStored() > 0)) return 0.0F;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!stack.hasTag()) return false;
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (!player.isCreative()) {
                LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
                if (lazy.isPresent()) {
                    IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                    if (state.getBlockHardness(worldIn, pos) != 0.0F) {
                        if (cycledTraitHandler.getActiveTraitIdentifier(stack).equals(NeoArsenalTraits.NONE.getIdentifier())) {
                            storage.extractEnergy(NeoToolCommon.MAX_TRANSFER - 8, false);
                        } else {
                            storage.extractEnergy(NeoToolCommon.MAX_TRANSFER - 6, false);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        NeoToolCommon.addInformation(cycledTraitHandler, stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
            return storage.getEnergyStored() != storage.getMaxEnergyStored();
        }
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return NeoToolCommon.getDurabilityForDisplay(stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (CapabilityEnergy.ENERGY == null) return null;
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> new ItemEnergyStorage(stack, NeoToolCommon.CAPACITY, NeoToolCommon.MAX_TRANSFER)).cast() : LazyOptional.empty();
            }
        };
    }

}
