package zorochase.neoarsenal.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zorochase.neoarsenal.NeoArsenal;

import java.util.Set;

public class SchematicItem extends Item {

    public static final String TAG = "Type";

    private final Set<String> ALL_TYPES;

    public SchematicItem(ImmutableSet<String> types) {
        super(new Item.Properties().group(NeoArsenal.MOD_GROUP));
        this.ALL_TYPES = types;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            for (String tool : ALL_TYPES) {
                ItemStack stack = new ItemStack(this);
                stack.getOrCreateTag().putString(TAG, tool);
                items.add(stack);
            }
        }
    }

}
