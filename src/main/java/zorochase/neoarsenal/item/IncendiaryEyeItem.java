package zorochase.neoarsenal.item;

import net.minecraft.item.FireChargeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zorochase.neoarsenal.NeoArsenal;

public class IncendiaryEyeItem extends FireChargeItem {

    public IncendiaryEyeItem() {
        super(new Item.Properties().group(NeoArsenal.MOD_GROUP).maxStackSize(16));
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return 40000;
    }
}
