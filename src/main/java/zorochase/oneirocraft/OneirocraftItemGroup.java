package zorochase.oneirocraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import zorochase.oneirocraft.registry.ModItems;

public class OneirocraftItemGroup extends ItemGroup {

    public OneirocraftItemGroup() { super("Oneirocraft"); }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.ONEIRIC_BERRIES);
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        for (Item item : ModItems.ITEMS) {
            item.fillItemGroup(this, items);
        }
    }
}
