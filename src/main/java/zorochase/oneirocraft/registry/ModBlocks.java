package zorochase.oneirocraft.registry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import zorochase.oneirocraft.Oneirocraft;
import zorochase.oneirocraft.block.CustomBerryBushBlock;

public class ModBlocks {

    public static final Block ONEIRIC_BERRY_BUSH_BLOCK = new CustomBerryBushBlock(
            () -> ModItems.ONEIRIC_BERRIES, false,
            CustomBerryBushBlock.Properties
                    .create(Material.PLANTS)
                    .tickRandomly()
                    .doesNotBlockMovement()
                    .sound(SoundType.SWEET_BERRY_BUSH)
                    .lightValue(7)
    ).setRegistryName(new ResourceLocation(Oneirocraft.MOD_ID, "oneiric_berry_bush"));

}
