package zorochase.neoarsenal.item.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import zorochase.neoarsenal.NeoArsenal;
import zorochase.neoarsenal.api.CycledTraitHandler;
import zorochase.neoarsenal.item.SchematicItem;
import zorochase.neoarsenal.item.ToolSchematicItem;
import zorochase.neoarsenal.registry.ModBlocks;
import zorochase.neoarsenal.registry.ModItems;

public class NeoToolRecipe extends SpecialRecipe {

    public static final SpecialRecipeSerializer<NeoToolRecipe> SERIALIZER = new Serializer();

    public NeoToolRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ModBlocks.COMPACTED_NEO.get().asItem() && !flag) {
                    flag = true;
                } else if (stack.getItem() == ModItems.TOOL_CORE.get() && !flag1) {
                    flag1 = true;
                } else if (stack.getItem() == ModItems.TOOL_SCHEMATIC.get() && !flag2) {
                    flag2 = true;
                } else {
                    if (stack.getItem() != ModItems.FABRICATOR.get() || flag3) {
                        return false;
                    }

                    flag3 = true;
                }
            }
        }

        return flag2 && flag && flag1 && flag3;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack schematicStack = ItemStack.EMPTY;
        ItemStack toolStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (!slotStack.isEmpty() && slotStack.getItem() == ModItems.TOOL_SCHEMATIC.get()) {
                if (slotStack.hasTag()) {
                    if (slotStack.getTag().contains(SchematicItem.TAG, Constants.NBT.TAG_STRING)) {
                        String toCraft = slotStack.getTag().getString(SchematicItem.TAG);
                        if (toCraft.equals("Sword") || toCraft.equals("Pickaxe") || toCraft.equals("Axe") || toCraft.equals("Shovel")) {
                            schematicStack = slotStack;
                        }
                    }
                }
            }
        }

        if (schematicStack != ItemStack.EMPTY) {
            CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();
            toolStack = new ItemStack(ToolSchematicItem.getCraftedItem(schematicStack.getTag().getString(SchematicItem.TAG)));
            cycledTraitHandler.initialize(toolStack);
        }

        return toolStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private static class Serializer extends SpecialRecipeSerializer<NeoToolRecipe> {
        public Serializer() {
            super(NeoToolRecipe::new);
            setRegistryName(NeoArsenal.MOD_ID, "neo_tool_recipe");
        }
    }
}
