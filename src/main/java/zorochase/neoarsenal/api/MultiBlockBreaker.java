package zorochase.neoarsenal.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockBreaker {

    public static List<BlockPos> getBreakable(World worldIn, PlayerEntity playerIn, int radius) {
        Vec3d playerEyePos = playerIn.getEyePosition(1);
        Vec3d playerRotation = playerIn.getLook(1);
        Vec3d combined = playerEyePos.add(playerRotation.getX() * 5, playerRotation.getY() * 5, playerRotation.getZ() * 5);

        ArrayList<BlockPos> possiblyBroken = new ArrayList<>();
        BlockRayTraceResult result = worldIn.rayTraceBlocks(new RayTraceContext(playerEyePos, combined, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, playerIn));

        if (result.getType() == RayTraceResult.Type.BLOCK) {
            Direction.Axis axis = result.getFace().getAxis();
            ArrayList<BlockPos> positions = new ArrayList<>();
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        positions.add(new BlockPos(x, y, z));
                    }
                }
            }

            BlockPos origin = result.getPos();
            for (BlockPos pos : positions) {
                if (axis == Direction.Axis.Y) {
                    if (pos.getY() == 0) {
                        possiblyBroken.add(origin.add(pos));
                    }
                } else if (axis == Direction.Axis.X) {
                    if (pos.getX() == 0) {
                        possiblyBroken.add(origin.add(pos));
                    }
                } else if (axis == Direction.Axis.Z) {
                    if (pos.getZ() == 0) {
                        possiblyBroken.add(origin.add(pos));
                    }
                }
            }
        }
        return possiblyBroken;
    }

    public static void breakBlocks(World worldIn, PlayerEntity playerIn, int radius, IValidator validator) {
        if (!worldIn.isRemote) {
            List<BlockPos> toBreak = getBreakable(worldIn, playerIn, radius);
            for (BlockPos pos : toBreak) {
                BlockState state = worldIn.getBlockState(pos);
                if (validator.isBreakable(state)) {
                    worldIn.destroyBlock(pos, !playerIn.isCreative());
                }
            }
        }
    }

    public interface IValidator {
        boolean isBreakable(BlockState state);
    }
}
