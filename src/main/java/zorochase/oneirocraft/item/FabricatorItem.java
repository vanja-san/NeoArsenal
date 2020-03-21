package zorochase.oneirocraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import zorochase.oneirocraft.Oneirocraft;

public class FabricatorItem extends Item {

    public FabricatorItem() {
        super(
                new Item.Properties()
                .group(Oneirocraft.MOD_GROUP)
                .maxStackSize(1)
                .rarity(Rarity.UNCOMMON)
        );
        setRegistryName(Oneirocraft.MOD_ID, "fabricator");
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
