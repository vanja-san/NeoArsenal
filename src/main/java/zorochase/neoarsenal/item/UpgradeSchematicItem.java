package zorochase.neoarsenal.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import zorochase.neoarsenal.api.CycledTraitHandler;
import zorochase.neoarsenal.api.NeoArsenalTraits;
import zorochase.neoarsenal.registry.ModItems;
import zorochase.neoarsenal.registry.ModSoundEvents;

import javax.annotation.Nullable;
import java.util.List;

import static zorochase.neoarsenal.util.LoreHelper.loreString;

public class UpgradeSchematicItem extends SchematicItem {

    public UpgradeSchematicItem() {
        super(ImmutableSet.of(
                NeoArsenalTraits.DEADLY.getIdentifier(),
                NeoArsenalTraits.SILKY.getIdentifier(),
                NeoArsenalTraits.LUCKY.getIdentifier(),
                NeoArsenalTraits.INCENDIARY.getIdentifier(),
                NeoArsenalTraits.EFFICIENT.getIdentifier(),
                NeoArsenalTraits.MASSIVE.getIdentifier()
                )
        );
    }

    public static String applies(Item item) {
        if (item == Items.QUARTZ) {
            return NeoArsenalTraits.DEADLY.getIdentifier();
        } else if (item == Items.STRING) {
            return NeoArsenalTraits.SILKY.getIdentifier();
        } else if (item == Items.DIAMOND) {
            return NeoArsenalTraits.LUCKY.getIdentifier();
        } else if (item == ModItems.INCENDIARY_EYE.get()) {
            return NeoArsenalTraits.INCENDIARY.getIdentifier();
        } else if (item == Items.GOLD_INGOT) {
            return NeoArsenalTraits.EFFICIENT.getIdentifier();
        } else if (item == Items.IRON_BLOCK) {
            return NeoArsenalTraits.MASSIVE.getIdentifier();
        } else {
            return "";
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack main = playerIn.getHeldItem(handIn);
        ItemStack off = playerIn.getHeldItemOffhand();
        CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();

        if (!ItemStack.areItemsEqual(main, playerIn.getHeldItemMainhand())) {
            playerIn.sendStatusMessage(loreString("Hold the schematic in your MAIN hand!", TextFormatting.RED), true);
            playerIn.playSound(ModSoundEvents.ADD_TRAIT_DENIED, 1.0F, 1.0F);
            return new ActionResult<>(ActionResultType.FAIL, main);
        }

        if (!main.hasTag()) {
            playerIn.sendStatusMessage(loreString("This schematic is empty!", TextFormatting.RED), true);
            playerIn.playSound(ModSoundEvents.ADD_TRAIT_DENIED, 1.0F, 1.0F);
            return new ActionResult<>(ActionResultType.FAIL, main);
        }

        if (cycledTraitHandler.hasTraits(off)) {
            if (cycledTraitHandler.toolSupportsTrait(off, main.getOrCreateTag().getString(TAG))) {
                if (!cycledTraitHandler.getTraitsTag(off).contains(main.getOrCreateTag().getString(TAG), Constants.NBT.TAG_INT)) {
                    cycledTraitHandler.addTrait(off, main.getOrCreateTag().getString(TAG));
                    playerIn.sendStatusMessage(loreString("Applied trait: " + main.getOrCreateTag().getString(TAG), TextFormatting.AQUA), true);
                    playerIn.playSound(ModSoundEvents.ADD_TRAIT, 1.0F, 1.0F);
                    playerIn.inventory.deleteStack(main);
                    return new ActionResult<>(ActionResultType.SUCCESS, main);
                } else {
                    playerIn.sendStatusMessage(loreString("That tool already has that trait!", TextFormatting.RED), true);
                    playerIn.playSound(ModSoundEvents.ADD_TRAIT_DENIED, 1.0F, 1.0F);
                }
            } else {
                if (!(off.getItem() instanceof PortableChargerItem)) {
                    playerIn.sendStatusMessage(loreString("That trait is not supported by that tool!", TextFormatting.RED), true);
                    playerIn.playSound(ModSoundEvents.ADD_TRAIT_DENIED, 1.0F, 1.0F);
                }
            }
        }
        return new ActionResult<>(ActionResultType.FAIL, main);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!stack.getOrCreateTag().contains(TAG, Constants.NBT.TAG_STRING)) return;
        tooltip.add(loreString("Trait to apply: ").appendSibling(loreString(stack.getOrCreateTag().getString(TAG), TextFormatting.AQUA)));
    }
}
