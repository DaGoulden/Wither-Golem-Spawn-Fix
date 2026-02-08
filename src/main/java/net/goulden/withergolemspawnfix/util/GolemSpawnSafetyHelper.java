package net.goulden.withergolemspawnfix.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GolemSpawnSafetyHelper {

    public static boolean allowGolemTeleport;
    public static boolean allowGolemBlockBreaker;

    public static void handleSpawnSuffocation(IronGolem golem, double offsets) {
        if (!(allowGolemTeleport || allowGolemBlockBreaker)) return;
        Level level = golem.level();
        if (level.isClientSide()) return;

        BlockPos golemPos = BlockPos.containing(golem.getX(), Math.ceil(golem.getY()), golem.getZ());

        boolean north = hasBlockIn(level, golemPos, 0, -1);
        boolean south = hasBlockIn(level, golemPos, 0, 1);
        boolean east  = hasBlockIn(level, golemPos, 1, 0);
        boolean west  = hasBlockIn(level, golemPos, -1, 0);
        boolean ne = hasBlockIn(level, golemPos, 1, -1);
        boolean nw = hasBlockIn(level, golemPos, -1, -1);
        boolean se = hasBlockIn(level, golemPos, 1, 1);
        boolean sw = hasBlockIn(level, golemPos, -1, 1);

        boolean hasOnlyInNorth = (north || ne || nw) && !(south || east || west || se || sw);
        boolean hasOnlyInSouth = (south || se || sw) && !(north || east || west || ne || nw);
        boolean hasOnlyInEast  = (east  || ne || se) && !(north || south || west || nw || sw);
        boolean hasOnlyInWest  = (west  || nw || sw) && !(north || south || east || ne || se);

        int xSign = 0, zSign = 0;

        if (!(north || south || east || west || ne || nw || se || sw)) {
            return;
        } else if (hasOnlyInNorth && allowGolemTeleport) {
            zSign = 1;
        } else if (hasOnlyInSouth && allowGolemTeleport) {
            zSign = -1;
        } else if (hasOnlyInEast && allowGolemTeleport) {
            xSign = -1;
        } else if (hasOnlyInWest && allowGolemTeleport) {
            xSign = 1;
        } else {

            CornerData neData = analyzeCorner(level, golemPos,  1, -1);
            CornerData nwData = analyzeCorner(level, golemPos, -1, -1);
            CornerData seData = analyzeCorner(level, golemPos,  1,  1);
            CornerData swData = analyzeCorner(level, golemPos, -1,  1);

            int minBlocks = 5;

            if (!neData.isUnbreakable && neData.blockCount < minBlocks) {
                xSign = 1;
                zSign = -1;
                minBlocks = neData.blockCount;
            }
            if (!nwData.isUnbreakable && nwData.blockCount < minBlocks) {
                xSign = -1;
                zSign = -1;
                minBlocks = nwData.blockCount;
            }
            if (!seData.isUnbreakable && seData.blockCount < minBlocks) {
                xSign = 1;
                zSign = 1;
                minBlocks = seData.blockCount;
            }
            if (!swData.isUnbreakable && swData.blockCount < minBlocks) {
                xSign = -1;
                zSign = 1;
            }

            if (allowGolemBlockBreaker && allowGolemTeleport) {
                breakBlocksInCorner(level, golemPos, xSign, zSign);
            } else if (allowGolemBlockBreaker && !(neData.isUnbreakable || nwData.isUnbreakable || seData.isUnbreakable || swData.isUnbreakable)) {
                breakAllBlocks(level, golemPos);
            }
        }

        if (allowGolemTeleport) {
            golem.teleportTo(golem.getX() + offsets * xSign, golem.getY(), golem.getZ() + offsets * zSign);
        }
    }

    private static boolean hasBlockIn(Level level, BlockPos golemPos, int offsetX, int offsetZ) {
        BlockPos pos = golemPos.offset(offsetX, 2, offsetZ);
        BlockState state = level.getBlockState(pos);
        return state.isSuffocating(level, pos);
    }

    private static CornerData analyzeCorner(Level level, BlockPos basePos, int xSign, int zSign) {
        int count = 0;
        boolean unbreakable = false;

        for (int x = 0; x <= 1; x++) {
            for (int z = 0; z <= 1; z++) {
                BlockPos pos = basePos.offset(x * xSign, 2, z * zSign);
                BlockState state = level.getBlockState(pos);

                if (state.isSuffocating(level, pos)) {
                    count++;
                    if (state.getDestroySpeed(level, pos) < 0) {
                        unbreakable = true;
                    }
                }
            }
        }

        return new CornerData(count, unbreakable);
    }

    private static void breakBlocksInCorner(Level level, BlockPos golemPos, int xSign, int zSign) {
        for (int x = 0; x <= 1; x++) {
            for (int z = 0; z <= 1; z++) {
                BlockPos pos = golemPos.offset(x * xSign, 2, z * zSign);
                BlockState state = level.getBlockState(pos);
                if (state.isSuffocating(level, pos) && state.getDestroySpeed(level, pos) >= 0) {
                    level.destroyBlock(pos, true);
                }
            }
        }
    }

    private static void breakAllBlocks(Level level, BlockPos golemPos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos pos = golemPos.offset(x, 2, z);
                BlockState state = level.getBlockState(pos);
                if (state.isSuffocating(level, pos) && state.getDestroySpeed(level, pos) >= 0) {
                    level.destroyBlock(pos, true);
                }
            }
        }
    }

    private record CornerData(int blockCount, boolean isUnbreakable) {}
}