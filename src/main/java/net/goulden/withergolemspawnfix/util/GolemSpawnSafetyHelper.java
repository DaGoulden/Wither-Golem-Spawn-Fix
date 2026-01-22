package net.goulden.withergolemspawnfix.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GolemSpawnSafetyHelper {
    
    public static void handleSpawnSuffocation(IronGolem golem, double offsets) {
        Level level = golem.level();
        if (level.isClientSide()) return;
        BlockPos golemPos = BlockPos.containing(golem.getX(), Math.ceil(golem.getY()), golem.getZ());

        boolean north = hasBlockIn(level, golemPos, 0, -1);
        boolean south = hasBlockIn(level, golemPos, 0, 1);
        boolean east = hasBlockIn(level, golemPos, 1, 0);
        boolean west = hasBlockIn(level, golemPos, -1, 0);
        boolean ne = hasBlockIn(level, golemPos, 1, -1);
        boolean nw = hasBlockIn(level, golemPos, -1, -1);
        boolean se = hasBlockIn(level, golemPos, 1, 1);
        boolean sw = hasBlockIn(level, golemPos, -1, 1);

        boolean hasOnlyInNorth = (north || ne || nw) && !(south || east || west || se || sw);
        boolean hasOnlyInSouth = (south || se || sw) && !(north || east || west || ne || nw);
        boolean hasOnlyInEast = (east || ne || se) && !(north || south || west || nw || sw);
        boolean hasOnlyInWest = (west || nw || sw) && !(north || south || east || ne || se);

        int xSign = 0, zSign = 0;

        if (!(north || south || east || west || ne || nw || se || sw)) {
            return;
        } else if (hasOnlyInNorth) {
            zSign = 1;
        } else if (hasOnlyInSouth) {
            zSign = -1;
        } else if (hasOnlyInEast) {
            xSign = -1;
        } else if (hasOnlyInWest) {
            xSign = 1;
        } else {
            int northEastCount = (north ? 1 : 0) + (east ? 1 : 0) + (ne ? 1 : 0);
            int northWestCount = (north ? 1 : 0) + (west ? 1 : 0) + (nw ? 1 : 0);
            int southEastCount = (south ? 1 : 0) + (east ? 1 : 0) + (se ? 1 : 0);
            int southWestCount = (south ? 1 : 0) + (west ? 1 : 0) + (sw ? 1 : 0);

            int minBlockCount = Math.min(Math.min(northEastCount, northWestCount), Math.min(southEastCount, southWestCount));

            java.util.List<String> bestCorners = new java.util.ArrayList<>();
            if (northEastCount == minBlockCount) bestCorners.add("NE");
            if (northWestCount == minBlockCount) bestCorners.add("NW");
            if (southEastCount == minBlockCount) bestCorners.add("SE");
            if (southWestCount == minBlockCount) bestCorners.add("SW");

            switch (bestCorners.getFirst()) {
                case "NE" -> { xSign = 1; zSign = -1; }
                case "NW" -> { xSign = -1; zSign = -1; }
                case "SE" -> { xSign = 1; zSign = 1; }
                case "SW" -> { xSign = -1; zSign = 1; }
            }

            breakBlocksInCorner(level, golemPos, xSign, zSign);
        }

        if (level instanceof ServerLevel) {
            golem.teleportTo(golem.getX() + offsets * xSign, golem.getY(), golem.getZ() + offsets * zSign);
        } else {
        golem.setPos(golem.getX() + offsets * xSign, golem.getY(), golem.getZ() + offsets * zSign);
        }
    }

    private static boolean hasBlockIn(Level level, BlockPos pos, int offsetX, int offsetZ) {
        BlockPos checkPos = pos.offset(offsetX, 2, offsetZ);
        return level.getBlockState(checkPos).isSuffocating(level, checkPos);
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
}

// configs
// compatibilidades