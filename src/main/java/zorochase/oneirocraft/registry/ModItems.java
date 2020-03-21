package zorochase.oneirocraft.registry;

import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import zorochase.oneirocraft.Oneirocraft;
import zorochase.oneirocraft.item.FabricatorItem;
import zorochase.oneirocraft.item.WishSlipItem;
import zorochase.oneirocraft.item.tool.DreamsteelAxeItem;
import zorochase.oneirocraft.item.tool.DreamsteelPickaxeItem;
import zorochase.oneirocraft.item.tool.DreamsteelShovelItem;
import zorochase.oneirocraft.item.tool.DreamsteelSwordItem;

import java.util.Arrays;
import java.util.List;

public class ModItems {

    public static final Item ONEIRIC_BERRIES = new BlockNamedItem(
            ModBlocks.ONEIRIC_BERRY_BUSH_BLOCK,
            new Item.Properties()
            .group(Oneirocraft.MOD_GROUP)
            .food(ModFoods.ONEIRIC_BERRIES)
            .rarity(Rarity.UNCOMMON)
    ).setRegistryName(Oneirocraft.MOD_ID, "oneiric_berries");

    public static final Item DREAMSTEEL_INGOT = new Item(
            new Item.Properties()
            .group(Oneirocraft.MOD_GROUP)
            .rarity(Rarity.UNCOMMON)
    ).setRegistryName(Oneirocraft.MOD_ID, "dreamsteel_ingot");

    public static final Item DREAMSTEEL_NUGGET = new Item(
            new Item.Properties()
            .group(Oneirocraft.MOD_GROUP)
            .rarity(Rarity.UNCOMMON)
    ).setRegistryName(Oneirocraft.MOD_ID, "dreamsteel_nugget");

    public static final Item DREAMSTEEL_ROD = new Item(
            new Item.Properties()
            .group(Oneirocraft.MOD_GROUP)
            .rarity(Rarity.UNCOMMON)
    ).setRegistryName(Oneirocraft.MOD_ID, "dreamsteel_rod");

    public static final Item FABRICATOR = new FabricatorItem();

    public static final WishSlipItem WISH_SLIP = new WishSlipItem();

    public static final DreamsteelSwordItem DREAMSTEEL_SWORD = new DreamsteelSwordItem();
    public static final DreamsteelPickaxeItem DREAMSTEEL_PICKAXE = new DreamsteelPickaxeItem();
    public static final DreamsteelAxeItem DREAMSTEEL_AXE = new DreamsteelAxeItem();
    public static final DreamsteelShovelItem DREAMSTEEL_SHOVEL = new DreamsteelShovelItem();

    public static final List<Item> ITEMS = Arrays.asList(
      ONEIRIC_BERRIES, FABRICATOR, WISH_SLIP, DREAMSTEEL_INGOT, DREAMSTEEL_NUGGET, DREAMSTEEL_ROD,
      DREAMSTEEL_SWORD, DREAMSTEEL_PICKAXE, DREAMSTEEL_AXE, DREAMSTEEL_SHOVEL
    );
}
