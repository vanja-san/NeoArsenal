package zorochase.neoarsenal.block;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class CustomOreBlock extends OreBlock {

    private final int MINIMUM;
    private final int MAXIMUM;

    public CustomOreBlock(int minimum, int maximum, Block.Properties properties) {
        super(properties);
        this.MINIMUM = minimum;
        this.MAXIMUM = maximum;
    }

    @Override
    protected int getExperience(Random p_220281_1_) {
        return MathHelper.nextInt(p_220281_1_, this.MINIMUM, this.MAXIMUM);
    }
}
