package zorochase.neoarsenal.registry;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zorochase.neoarsenal.block.CustomOreBlock;

import java.util.function.Supplier;

import static zorochase.neoarsenal.NeoArsenal.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);

    public static final RegistryObject<Block> NEO_ORE = newBlock(
            "neo_ore",
            () -> new CustomOreBlock(
                    3, 7,
                    OreBlock.Properties
                            .create(Material.ROCK)
                            .hardnessAndResistance(7.0F, 3.0F)
                            .harvestTool(ToolType.PICKAXE)
                            .harvestLevel(3)
            )
    );

    public static final RegistryObject<Block> COMPACTED_NEO = newBlock(
            "compacted_neo",
            () -> new Block(
                    Block.Properties
                            .create(Material.ROCK)
                            .hardnessAndResistance(15.0F, 1200.0F)
                            .harvestTool(ToolType.PICKAXE)
                            .harvestLevel(3)
                            .sound(new SoundType(
                                    1.0F, 1.0F,
                                    ModSoundEvents.COMPACTED_NEO_BREAK,
                                    ModSoundEvents.COMPACTED_NEO_STEP,
                                    ModSoundEvents.COMPACTED_NEO_PLACE,
                                    ModSoundEvents.COMPACTED_NEO_HIT,
                                    SoundEvents.BLOCK_STONE_FALL)
                            )
            )
    );

    public static <B extends Block> RegistryObject<B> newBlock(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        return block;
    }

}
