package zorochase.neoarsenal.registry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import zorochase.neoarsenal.NeoArsenal;
import zorochase.neoarsenal.block.CustomBerryBushBlock;

public class ModBlocks {

    public static final Block ENERGETIC_BERRY_BUSH_BLOCK = new CustomBerryBushBlock(
            () -> ModItems.ENERGETIC_BERRIES, false,
            CustomBerryBushBlock.Properties
                    .create(Material.PLANTS)
                    .tickRandomly()
                    .doesNotBlockMovement()
                    .sound(SoundType.SWEET_BERRY_BUSH)
                    .lightValue(7)
    ).setRegistryName(new ResourceLocation(NeoArsenal.MOD_ID, "energetic_berry_bush"));

}
