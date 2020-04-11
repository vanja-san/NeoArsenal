package zorochase.neoarsenal.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import zorochase.neoarsenal.NeoArsenal;
import zorochase.neoarsenal.api.CycledTraitHandler;
import zorochase.neoarsenal.api.NeoArsenalTraits;
import zorochase.neoarsenal.capability.ItemEnergyStorage;
import zorochase.neoarsenal.registry.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static zorochase.neoarsenal.util.LoreHelper.*;

public class PortableChargerItem extends Item {

    private final Set<Item> FUEL_ITEMS = ImmutableSet.of(Items.COAL, Items.CHARCOAL, ModItems.INCENDIARY_EYE.get());
    private final Set<Item> CHARGEABLE_ITEMS = ImmutableSet.of(ModItems.NEO_SWORD.get(), ModItems.NEO_PICKAXE.get(), ModItems.NEO_AXE.get(), ModItems.NEO_SHOVEL.get());

    private final Set<String> ALL_MODES = ImmutableSet.of(NeoArsenalTraits.BURNING.getIdentifier(), NeoArsenalTraits.CHARGING.getIdentifier());

    private final CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();
    private final String ENERGY_TAG = "Energy";
    private final String BURN_TIME_TAG = "BurnTime";
    private final String CHARGE_TIME_TAG = "ChargeTime";

    private final int CAPACITY = 30_000;
    private final int MAX_RECEIVE = 1;
    private final int MAX_EXTRACT = NeoToolCommon.MAX_TRANSFER;

    public PortableChargerItem() {
        super(new Item.Properties().group(NeoArsenal.MOD_GROUP).maxStackSize(1));
        addPropertyOverride(new ResourceLocation(NeoArsenal.MOD_ID, "trait"), (stack, world, entity) -> cycledTraitHandler.identifierAsFloat(stack));
    }

    private boolean isFuel(Item item) {
        return FUEL_ITEMS.contains(item);
    }

    private boolean canBeCharged(Item item) {
        return CHARGEABLE_ITEMS.contains(item);
    }

    private ItemStack getFuelToBurn(PlayerInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (!slotStack.isEmpty() && isFuel(slotStack.getItem())) {
                return slotStack;
            }
        }
        return ItemStack.EMPTY;
    }

    private NonNullList<ItemStack> getChargeableStacks(PlayerInventory inv) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (!slotStack.isEmpty() && canBeCharged(slotStack.getItem())) stacks.add(slotStack);
        }
        return stacks;
    }

    // Counts the number of items to charge
    private int getCountToCharge(PlayerInventory inv) {
        int count = 0;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (canBeCharged(stack.getItem())) {
                LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
                if (lazy.isPresent()) {
                    IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                    if (storage.getEnergyStored() < storage.getMaxEnergyStored()) ++count;
                }
            }
        }
        return count;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return newStack.getItem() != oldStack.getItem();
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt(ENERGY_TAG, CAPACITY);
            cycledTraitHandler.initialize(stack, ALL_MODES, true);
            items.add(stack);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.hasTag() || !cycledTraitHandler.hasTraits(stack)) {
            cycledTraitHandler.initialize(stack, ALL_MODES, true);
        }
        cycledTraitHandler.setCooldown(stack, cycledTraitHandler.getCooldown(stack) - 1);
        if (entityIn instanceof PlayerEntity) {
            PlayerInventory inv = ((PlayerEntity) entityIn).inventory;
            LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
            if (cycledTraitHandler.getCooldown(stack) == 0 && lazy.isPresent()) {
                IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                if (cycledTraitHandler.getActiveTraitIdentifier(stack).equals(NeoArsenalTraits.BURNING.getIdentifier()) && storage.getEnergyStored() != CAPACITY) {
                    if (cycledTraitHandler.getCooldown(stack, BURN_TIME_TAG) == 0) {
                        ItemStack fuelStack = getFuelToBurn(inv);
                        if (!fuelStack.isEmpty()) {
                            int count = fuelStack.getCount();
                            int burnTime = ForgeHooks.getBurnTime(fuelStack);
                            int finalBurnTime = (count * burnTime) / 20;
                            cycledTraitHandler.setCooldown(stack, finalBurnTime / 2, BURN_TIME_TAG);
                            inv.deleteStack(fuelStack);
                        }
                    } else {
                        cycledTraitHandler.setCooldown(stack, cycledTraitHandler.getCooldown(stack, BURN_TIME_TAG) - 1, BURN_TIME_TAG);
                        storage.receiveEnergy(MAX_RECEIVE, false);
                    }
                } else if (cycledTraitHandler.getActiveTraitIdentifier(stack).equals(NeoArsenalTraits.CHARGING.getIdentifier()) && storage.getEnergyStored() > 0) {
                    int countToCharge = getCountToCharge(inv);
                    if (countToCharge > 0) {
                        if (cycledTraitHandler.getCooldown(stack, CHARGE_TIME_TAG) == 0) {
                            NonNullList<ItemStack> stacks = getChargeableStacks(inv);
                            if (stacks.size() > 0) {
                                for (ItemStack toCharge : stacks) {
                                    LazyOptional<IEnergyStorage> lazy1 = toCharge.getCapability(CapabilityEnergy.ENERGY);
                                    if (lazy1.isPresent()) {
                                        IEnergyStorage storage1 = lazy1.orElseThrow(AssertionError::new);
                                        storage.extractEnergy(MAX_EXTRACT, false);
                                        storage1.receiveEnergy(MAX_EXTRACT, false);
                                    }
                                }
                            }
                            int delay = getCountToCharge(inv) * 5;
                            cycledTraitHandler.setCooldown(stack, delay, CHARGE_TIME_TAG);
                        } else {
                            cycledTraitHandler.setCooldown(stack, cycledTraitHandler.getCooldown(stack, CHARGE_TIME_TAG) - 1, CHARGE_TIME_TAG);
                        }
                    }
                } else {
                    cycledTraitHandler.setCooldown(stack, 0, CHARGE_TIME_TAG);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (CapabilityEnergy.ENERGY == null) return;

        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            tooltip.add(chargeRatio(stack));
            tooltip.add(newLine());
            tooltip.add(activeTrait(stack, cycledTraitHandler, "Mode: "));
            tooltip.add(loreString("Burn time: ").appendSibling(loreInt(cycledTraitHandler.getCooldown(stack, BURN_TIME_TAG))));
            tooltip.add(loreString("Items to charge: ").appendSibling(loreInt(getCountToCharge(Minecraft.getInstance().player.inventory))));

        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return NeoToolCommon.showDurabilityBar(stack);
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
                return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> new ItemEnergyStorage(stack, CAPACITY, MAX_RECEIVE, MAX_EXTRACT)).cast() : LazyOptional.empty();
            }
        };
    }
}
