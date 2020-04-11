package zorochase.neoarsenal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zorochase.neoarsenal.registry.ModItems;

import java.util.Arrays;
import java.util.List;

public class NeoArsenalItemGroup extends ItemGroup {

    public NeoArsenalItemGroup() {
        super(NeoArsenal.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.NEO_BAR.get());
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        final List<Item> FILL_ORDER = Arrays.asList(
                ModItems.NEO_ORE.get(),
                ModItems.COMPACTED_NEO.get(),
                ModItems.NEO_BAR.get(),
                ModItems.NEO_CHUNK.get(),
                ModItems.PROCESSOR.get(),
                ModItems.BLANK_SLIP.get(),
                ModItems.BLANK_SHEET.get(),
                ModItems.FABRICATOR.get(),
                ModItems.TOOL_CORE.get(),
                ModItems.TRAIT_ANALYZER.get(),
                ModItems.INCENDIARY_EYE.get(),
                ModItems.PORTABLE_CHARGER.get(),
                ModItems.NEO_SWORD.get(),
                ModItems.NEO_PICKAXE.get(),
                ModItems.NEO_AXE.get(),
                ModItems.NEO_SHOVEL.get(),
                ModItems.TOOL_SCHEMATIC.get(),
                ModItems.UPGRADE_SCHEMATIC.get()
        );
        for (Item item : FILL_ORDER) {
            item.fillItemGroup(this, items);
        }
    }
}
