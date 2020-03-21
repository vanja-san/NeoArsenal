package zorochase.oneirocraft.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemEnergyStorage extends EnergyStorage implements ICapabilityProvider {

    private final LazyOptional<ItemEnergyStorage> lazyOptional;
    private final ItemStack stack;

    public ItemEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.stack = stack;
        this.lazyOptional = LazyOptional.of(() -> this);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyStored = getEnergyStored();
        int energyReceived = Math.min(capacity - energyStored, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            setEnergyStored(energyStored + energyReceived);
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyStored = getEnergyStored();
        int energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            setEnergyStored(energyStored - energyExtracted);
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return stack.getOrCreateTag().getInt("Energy");
    }

    private void setEnergyStored(int amount) {
        stack.getOrCreateTag().putInt("Energy", amount);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, lazyOptional.cast());
    }
}
