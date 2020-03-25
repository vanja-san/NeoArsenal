package zorochase.neoarsenal.registry;

import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import zorochase.neoarsenal.NeoArsenal;
import zorochase.neoarsenal.item.FabricatorItem;
import zorochase.neoarsenal.item.UpgradeSlipItem;
import zorochase.neoarsenal.item.tool.MorphingSteelAxeItem;
import zorochase.neoarsenal.item.tool.MorphingSteelPickaxeItem;
import zorochase.neoarsenal.item.tool.MorphingSteelShovelItem;
import zorochase.neoarsenal.item.tool.MorphingSteelSwordItem;

import java.util.Arrays;
import java.util.List;

public class ModItems {

    public static final Item ENERGETIC_BERRIES = new BlockNamedItem(
            ModBlocks.ENERGETIC_BERRY_BUSH_BLOCK,
            new Item.Properties()
            .group(NeoArsenal.MOD_GROUP)
            .food(ModFoods.ENERGETIC_BERRIES)
            .rarity(Rarity.UNCOMMON)
    ).setRegistryName(NeoArsenal.MOD_ID, "energetic_berries");

    public static final Item STEEL_INGOT = new Item(
            new Item.Properties()
            .group(NeoArsenal.MOD_GROUP)
    ).setRegistryName(NeoArsenal.MOD_ID, "steel_ingot");

    public static final Item STEEL_NUGGET = new Item(
            new Item.Properties()
            .group(NeoArsenal.MOD_GROUP)
    ).setRegistryName(NeoArsenal.MOD_ID, "steel_nugget");

    public static final Item STEEL_ROD = new Item(
            new Item.Properties()
            .group(NeoArsenal.MOD_GROUP)
    ).setRegistryName(NeoArsenal.MOD_ID, "steel_rod");

    public static final Item FABRICATOR = new FabricatorItem();

    public static final UpgradeSlipItem WISH_SLIP = new UpgradeSlipItem();

    public static final MorphingSteelSwordItem MORPHING_STEEL_SWORD = new MorphingSteelSwordItem();
    public static final MorphingSteelPickaxeItem MORPHING_STEEL_PICKAXE= new MorphingSteelPickaxeItem();
    public static final MorphingSteelAxeItem MORPHING_STEEL_AXE = new MorphingSteelAxeItem();
    public static final MorphingSteelShovelItem MORPHING_STEEL_SHOVEL = new MorphingSteelShovelItem();

    public static final List<Item> ITEMS = Arrays.asList(
      ENERGETIC_BERRIES, FABRICATOR, WISH_SLIP, STEEL_INGOT, STEEL_NUGGET, STEEL_ROD,
            MORPHING_STEEL_SWORD, MORPHING_STEEL_PICKAXE, MORPHING_STEEL_AXE, MORPHING_STEEL_SHOVEL
    );
}
