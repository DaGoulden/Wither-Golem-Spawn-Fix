package net.goulden.withergolemspawnfix.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;

import java.util.Objects;

public class FixedStructurePatterns {

    public static boolean allowWitherPatternFix;
    public static boolean allowGolemPatternFix;

    public static BlockPattern createFixedWitherPattern() {
        return BlockPatternBuilder.start()
                .aisle("^^^", "###", "~#~")
                .where('#', BlockInWorld.hasState(state -> state.is(BlockTags.WITHER_SUMMON_BASE_BLOCKS)))
                .where('^', BlockInWorld.hasState(state -> (state.is(Blocks.WITHER_SKELETON_SKULL) || state.is(Blocks.WITHER_SKELETON_WALL_SKULL))))
                .where('~', BlockInWorld.hasState(Objects::nonNull))
                .build();
    }

    public static BlockPattern createFixedIronGolemPattern() {
        return BlockPatternBuilder.start()
                .aisle("~^~", "###", "~#~")
                .where('^', BlockInWorld.hasState(state -> (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN))))
                .where('#', BlockInWorld.hasState(state -> state.is(Blocks.IRON_BLOCK)))
                .where('~', BlockInWorld.hasState(Objects::nonNull))
                .build();
    }
}
