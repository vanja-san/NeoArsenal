package zorochase.oneirocraft.world.features;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ScatteredPlantFeature;
import zorochase.oneirocraft.block.CustomBerryBushBlock;
import zorochase.oneirocraft.registry.ModBlocks;

import java.util.Random;

public class OneiricBerryBushFeature extends ScatteredPlantFeature {

    public OneiricBerryBushFeature() {
        super(NoFeatureConfig::deserialize, ModBlocks.ONEIRIC_BERRY_BUSH_BLOCK.getDefaultState().with(CustomBerryBushBlock.AGE, 2));
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int i = 0;

        for(int j = 0; j < 8; ++j) {
            BlockPos blockpos = pos.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(4) - rand.nextInt(4));
            if (worldIn.isAirBlock(blockpos) && worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS_BLOCK) {
                worldIn.setBlockState(blockpos, this.plant, 2);
                ++i;
            }
        }

        return i > 0;
    }
}
