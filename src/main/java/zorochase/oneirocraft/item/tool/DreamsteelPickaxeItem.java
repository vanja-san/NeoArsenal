package zorochase.oneirocraft.item.tool;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import zorochase.oneirocraft.Oneirocraft;
import zorochase.oneirocraft.api.CycledTraitHandler;
import zorochase.oneirocraft.capability.ItemEnergyStorage;
import zorochase.oneirocraft.api.MultiBlockBreaker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static zorochase.oneirocraft.util.LoreHelper.*;

public class DreamsteelPickaxeItem extends PickaxeItem {

    private final CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();
    private final Set<String> ALL_POTENTIAL_TRAITS = ImmutableSet.of(cycledTraitHandler.SILKY, cycledTraitHandler.LUCKY, cycledTraitHandler.EFFICIENT, cycledTraitHandler.MASSIVE);

    private final int capacity = 3400;
    private final int maxReceive = 10;
    private final int maxExtract = maxReceive;

    public DreamsteelPickaxeItem() {
        super(
                ItemTier.DIAMOND, 1, -2.8F,
                new Item.Properties()
                        .group(Oneirocraft.MOD_GROUP)
                        .defaultMaxDamage(0)
                        .maxDamage(0)
                        .rarity(Rarity.RARE)
        );
        setRegistryName(Oneirocraft.MOD_ID, "dreamsteel_pickaxe");
        addPropertyOverride(new ResourceLocation(Oneirocraft.MOD_ID, "trait"), (stack, world, entity) -> cycledTraitHandler.identifierAsFloat(stack));
    }

    private static float getChargeRatio(ItemStack stack) {
        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(IllegalStateException::new);
            return (float) storage.getEnergyStored() / storage.getMaxEnergyStored();
        }
        return 0;
    }

    @Override
    public String getHighlightTip(ItemStack item, String displayName) {
        String activeName = !cycledTraitHandler.getActiveTraitIdentifier(item).equals(cycledTraitHandler.NONE) ? " (" + cycledTraitHandler.getActiveTraitIdentifier(item) + ")" : "";
        return displayName + activeName;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return cycledTraitHandler.getActiveTraitIdentifier(stack).equals(cycledTraitHandler.NONE) ? Rarity.UNCOMMON : Rarity.RARE;
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
            ItemStack empty = new ItemStack(this);
            cycledTraitHandler.initialize(empty);
            items.add(empty);

            ItemStack filled = new ItemStack(this);
            filled.getOrCreateTag().putInt("Energy", capacity);
            cycledTraitHandler.initialize(filled, ALL_POTENTIAL_TRAITS, true);
            items.add(filled);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.hasTag() || !cycledTraitHandler.hasTraits(stack)) cycledTraitHandler.initialize(stack);
        cycledTraitHandler.setCooldown(stack, cycledTraitHandler.getCooldown(stack) - 1);
        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
            if (storage.getEnergyStored() == 0) {
                cycledTraitHandler.setActiveTraitIndex(stack, cycledTraitHandler.NONE);
                stack.getEnchantmentTagList().clear();
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return cycledTraitHandler.getCooldown(oldStack) > 0;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            if (!player.isCreative()) {
                stack.getOrCreateTag().putInt("JustHitEntity", 1);
                cycledTraitHandler.setCooldown(stack, 20);
                LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
                if (lazy.isPresent()) {
                    IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                    if (storage.getEnergyStored() > 0) {
                        storage.extractEnergy(maxExtract - 2, false);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        ItemStack held = player.getHeldItem(Hand.MAIN_HAND);
        LazyOptional<IEnergyStorage> lazy = held.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);

            int radius = (cycledTraitHandler.getActiveTraitIdentifier(held).equals(cycledTraitHandler.MASSIVE)) ? 1 : -2;
            float centerHardness = worldIn.getBlockState(pos).getBlockHardness(null, null);
            if (held.canHarvestBlock(worldIn.getBlockState(pos))) {
                MultiBlockBreaker.breakBlocks(worldIn, player, radius,
                        (breakState) -> {
                            if (player.isCreative()) return true;
                            double hardness = breakState.getBlockHardness(null, null);
                            boolean effective = held.canHarvestBlock(breakState);
                            boolean valid = hardness < centerHardness * 5 && hardness > 0;
                            return effective && valid;
                        }
                );
            }

            return (player.isCreative() || storage.getEnergyStored() > 0);
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
                        if (cycledTraitHandler.getActiveTraitIdentifier(stack).equals(cycledTraitHandler.NONE)) {
                            storage.extractEnergy(maxExtract - 8, false);
                        } else {
                            storage.extractEnergy(maxExtract - 6, false);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (CapabilityEnergy.ENERGY == null) return;

        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
            tooltip.add(chargeRatio(stack));
            if (storage.getEnergyStored() == 0)
                tooltip.add(loreString("No energy stored! Energy is required to be useful!", TextFormatting.RED));
            tooltip.add(newLine());
        }
        if (cycledTraitHandler.hasTraits(stack)) {
            if (cycledTraitHandler.getActiveTraitIdentifier(stack).equals(cycledTraitHandler.NONE)) {
                tooltip.add(loreString("No trait currently applied...", TextFormatting.GRAY));
                if (cycledTraitHandler.getTraitsTag(stack).size() < 2) {
                    tooltip.add(loreString("Tip: you'll need a Wish Slip to add a trait!", TextFormatting.DARK_GRAY));
                } else {
                    tooltip.add(loreString("Available traits: "));
                    for (String s : cycledTraitHandler.getTraitsTag(stack).keySet()) {
                        if (!s.equals(cycledTraitHandler.NONE)) tooltip.add(loreString(" - " + s));
                    }
                }
            } else {
                tooltip.add(loreString("Trait applied: ").appendSibling(loreString(cycledTraitHandler.getActiveTraitIdentifier(stack), TextFormatting.AQUA)));
            }
        }
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
        return 1 - getChargeRatio(stack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return MathHelper.hsvToRGB(Math.max(0.0F, (float) (1.0F - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (CapabilityEnergy.ENERGY == null) return null;
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> new ItemEnergyStorage(stack, capacity, maxReceive, maxExtract)).cast() : LazyOptional.empty();
            }
        };
    }

}
