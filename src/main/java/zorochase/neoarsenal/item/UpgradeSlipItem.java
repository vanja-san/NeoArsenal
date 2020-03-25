package zorochase.neoarsenal.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import zorochase.neoarsenal.NeoArsenal;
import zorochase.neoarsenal.api.CycledTraitHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static zorochase.neoarsenal.util.LoreHelper.loreString;

public class UpgradeSlipItem extends Item {

    private final CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();
    private final Set<String> ALL_POTENTIAL_TRAITS = ImmutableSet.of(cycledTraitHandler.DEADLY, cycledTraitHandler.SILKY, cycledTraitHandler.LUCKY, cycledTraitHandler.INCENDIARY, cycledTraitHandler.EFFICIENT, cycledTraitHandler.MASSIVE);

    public UpgradeSlipItem() {
        super(
                new Item.Properties()
                .group(NeoArsenal.MOD_GROUP)
                .maxStackSize(1)
                .rarity(Rarity.UNCOMMON)
        );
        setRegistryName(NeoArsenal.MOD_ID, "upgrade_slip");
    }

    @Override
    public String getHighlightTip(ItemStack item, String displayName) {
        String activeName = !cycledTraitHandler.getActiveTraitIdentifier(item).equals(cycledTraitHandler.NONE) ? " (" + cycledTraitHandler.getActiveTraitIdentifier(item) + ")" : "";
        return displayName + activeName;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack toAdd = new ItemStack(this);
            cycledTraitHandler.initialize(toAdd, ALL_POTENTIAL_TRAITS, false);
            items.add(toAdd);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return cycledTraitHandler.getCooldown(oldStack) > 0;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.hasTag() || !cycledTraitHandler.hasTraits(stack)) cycledTraitHandler.initialize(stack, ALL_POTENTIAL_TRAITS, false);
        cycledTraitHandler.setCooldown(stack, cycledTraitHandler.getCooldown(stack) - 1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack slipStack = playerIn.getHeldItem(handIn);
        ItemStack offhandStack = playerIn.getHeldItemOffhand();

        if (!ItemStack.areItemsEqual(slipStack, playerIn.getHeldItemMainhand())) {
            playerIn.sendStatusMessage(loreString("Hold the Wish Slip in your MAIN hand!", TextFormatting.RED), true);
            playerIn.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            return new ActionResult<>(ActionResultType.FAIL, slipStack);
        }
        if (cycledTraitHandler.hasTraits(offhandStack)) {
            if (cycledTraitHandler.toolSupportsTrait(offhandStack, cycledTraitHandler.getActiveTraitIdentifier(slipStack))) {
                if (!cycledTraitHandler.getTraitsTag(offhandStack).contains(cycledTraitHandler.getActiveTraitIdentifier(slipStack), Constants.NBT.TAG_INT)) {
                    cycledTraitHandler.addTrait(offhandStack, cycledTraitHandler.getActiveTraitIdentifier(slipStack));
                    playerIn.sendStatusMessage(loreString("Applied trait: " + cycledTraitHandler.getActiveTraitIdentifier(slipStack), TextFormatting.AQUA), true);
                    playerIn.playSound(SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH, 1.0F, 1.0F);
                    playerIn.inventory.deleteStack(slipStack);
                    return new ActionResult<>(ActionResultType.SUCCESS, slipStack);
                } else {
                    playerIn.sendStatusMessage(loreString("That tool already has that trait!", TextFormatting.RED), true);
                    playerIn.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                }
            } else {
                playerIn.sendStatusMessage(loreString("That trait is not supported by that tool!", TextFormatting.RED), true);
                playerIn.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            }
        }
        return new ActionResult<>(ActionResultType.FAIL, slipStack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!cycledTraitHandler.hasTraits(stack)) return;
        tooltip.add(loreString("Trait to apply: ").appendSibling(loreString(cycledTraitHandler.getActiveTraitIdentifier(stack), TextFormatting.AQUA)));
    }
}
