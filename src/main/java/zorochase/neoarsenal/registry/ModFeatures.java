package zorochase.neoarsenal.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import zorochase.neoarsenal.NeoArsenal;

public class ModFeatures {

    public static Feature<NoFeatureConfig> ENERGETIC_BERRY_BUSH_FEATURE;

    public static <V extends R, R extends IForgeRegistryEntry<R>> V register(IForgeRegistry<R> registry, V feature, String name) {
        feature.setRegistryName(new ResourceLocation(NeoArsenal.MOD_ID, name));
        registry.register(feature);
        return feature;
    }

    public static void addFeatures() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (
                    (
                            BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.FOREST) ||
                            BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.HILLS) ||
                            BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.PLAINS)
                    )
                            && !BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.CONIFEROUS)
            ) {
                biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(ENERGETIC_BERRY_BUSH_FEATURE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(1)));
            }
        }
    }
}
