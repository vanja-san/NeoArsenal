package zorochase.neoarsenal.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zorochase.neoarsenal.item.*;

import java.util.function.Supplier;

import static zorochase.neoarsenal.NeoArsenal.MOD_GROUP;
import static zorochase.neoarsenal.NeoArsenal.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> NEO_BAR = newItem(
            "neo_bar",
            () -> new Item(new Item.Properties().group(MOD_GROUP).rarity(Rarity.UNCOMMON))
    );

    public static final RegistryObject<Item> NEO_CHUNK = newItem(
            "neo_chunk",
            () -> new Item(new Item.Properties().group(MOD_GROUP).rarity(Rarity.UNCOMMON))
    );

    public static final RegistryObject<Item> PROCESSOR = newItem(
            "processor",
            () -> new Item(new Item.Properties().group(MOD_GROUP))
    );

    public static final RegistryObject<Item> BLANK_SLIP = newItem(
            "blank_slip",
            () -> new Item(new Item.Properties().group(MOD_GROUP))
    );

    public static final RegistryObject<Item> BLANK_SHEET = newItem(
            "blank_sheet",
            () -> new Item(new Item.Properties().group(MOD_GROUP))
    );

    public static final RegistryObject<Item> FABRICATOR = newItem(
            "fabricator",
            () -> new InconsumableItem(new Item.Properties().group(MOD_GROUP).maxStackSize(1))
    );

    public static final RegistryObject<Item> TOOL_CORE = newItem(
            "tool_core",
            () -> new Item(new Item.Properties().group(MOD_GROUP).maxStackSize(16))
    );

    public static final RegistryObject<Item> TRAIT_ANALYZER = newItem(
            "trait_analyzer",
            () -> new InconsumableItem(new Item.Properties().group(MOD_GROUP).maxStackSize(1))
    );

    public static final RegistryObject<Item> TOOL_SCHEMATIC = newItem(
            "tool_schematic", ToolSchematicItem::new
    );

    public static final RegistryObject<Item> UPGRADE_SCHEMATIC = newItem(
            "upgrade_schematic", UpgradeSchematicItem::new
    );

    // Tools
    public static final RegistryObject<Item> INCENDIARY_EYE = newItem("incendiary_eye", IncendiaryEyeItem::new);

    public static final RegistryObject<Item> NEO_SWORD = newItem("neo_sword", NeoSwordItem::new);

    public static final RegistryObject<Item> NEO_PICKAXE = newItem("neo_pickaxe", NeoPickaxeItem::new);

    public static final RegistryObject<Item> NEO_AXE = newItem("neo_axe", NeoAxeItem::new);

    public static final RegistryObject<Item> NEO_SHOVEL = newItem("neo_shovel", NeoShovelItem::new);

    public static final RegistryObject<Item> PORTABLE_CHARGER = newItem("portable_charger", PortableChargerItem::new);


    // Blocks
    public static final RegistryObject<Item> NEO_ORE = newItem(
            "neo_ore",
            () -> new BlockItem(ModBlocks.NEO_ORE.get(), new Item.Properties().group(MOD_GROUP).rarity(Rarity.UNCOMMON))
    );

    public static final RegistryObject<Item> COMPACTED_NEO = newItem(
            "compacted_neo",
            () -> new BlockItem(ModBlocks.COMPACTED_NEO.get(), new Item.Properties().group(MOD_GROUP).rarity(Rarity.UNCOMMON))
    );

    public static <I extends Item> RegistryObject<I> newItem(String registryName, Supplier<? extends I> supplier) {
        RegistryObject<I> item = ITEMS.register(registryName, supplier);
        return item;
    }
}
