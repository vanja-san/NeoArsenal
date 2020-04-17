package zorochase.neoarsenal.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import zorochase.neoarsenal.registry.ModItems;

import javax.annotation.Nullable;
import java.util.List;

import static zorochase.neoarsenal.util.LoreHelper.loreString;

public class ToolSchematicItem extends SchematicItem {

    public ToolSchematicItem() {
        super(ImmutableSet.of("Sword", "Pickaxe", "Axe", "Shovel"));
    }

    public static String crafts(Item item) {
        if (item == Items.WOODEN_SWORD) {
            return "Sword";
        } else if (item == Items.WOODEN_PICKAXE) {
            return "Pickaxe";
        } else if (item == Items.WOODEN_AXE) {
            return "Axe";
        } else if (item == Items.WOODEN_SHOVEL) {
            return "Shovel";
        } else {
            return "";
        }
    }

    public static Item getCraftedItem(String type) {
        switch (type) {
            case "Sword":
                return ModItems.NEO_SWORD.get();
            case "Pickaxe":
                return ModItems.NEO_PICKAXE.get();
            case "Axe":
                return ModItems.NEO_AXE.get();
            case "Shovel":
                return ModItems.NEO_SHOVEL.get();
            default:
                return null;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!stack.getOrCreateTag().contains(TAG, Constants.NBT.TAG_STRING)) return;
        tooltip.add(loreString("neoarsenal.lore.tool_schematic_type").appendSibling(loreString("neoarsenal.tooltype." + stack.getOrCreateTag().getString(TAG).toLowerCase(), TextFormatting.AQUA)));
    }
}
