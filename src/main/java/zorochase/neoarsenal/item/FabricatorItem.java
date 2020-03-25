package zorochase.neoarsenal.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import zorochase.neoarsenal.NeoArsenal;

public class FabricatorItem extends Item {

    public FabricatorItem() {
        super(
                new Item.Properties()
                .group(NeoArsenal.MOD_GROUP)
                .maxStackSize(1)
                .rarity(Rarity.UNCOMMON)
        );
        setRegistryName(NeoArsenal.MOD_ID, "fabricator");
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
