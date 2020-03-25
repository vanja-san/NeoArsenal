package zorochase.neoarsenal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import zorochase.neoarsenal.registry.ModItems;

public class NeoArsenalItemGroup extends ItemGroup {

    public NeoArsenalItemGroup() { super("NeoArsenal"); }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.ENERGETIC_BERRIES);
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        for (Item item : ModItems.ITEMS) {
            item.fillItemGroup(this, items);
        }
    }
}
