package zorochase.neoarsenal.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.NetherBiome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import zorochase.neoarsenal.registry.ModBlocks;

import static net.minecraft.world.biome.Biome.createDecoratedFeature;

public class OreGen {

    public static void setup() {
        NetherBiome biome = (NetherBiome) ForgeRegistries.BIOMES.getValue(new ResourceLocation("minecraft", "nether"));
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.NEO_ORE.get().getDefaultState(), 4), Placement.COUNT_RANGE, new CountRangeConfig(2, 0, 0, 64)));
    }
}
