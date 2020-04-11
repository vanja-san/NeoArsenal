package zorochase.neoarsenal.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InconsumableItem extends Item {

    public InconsumableItem(Item.Properties builder) {
        super(builder);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this);
    }
}
