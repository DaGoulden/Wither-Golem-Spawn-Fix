package net.goulden.withergolemspawnfix.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(WitherSkullBlock.class)
public class WitherSkullBlockMixin {

    @Inject(
            method = "getOrCreateWitherFull",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void useFixedWitherPattern(CallbackInfoReturnable<BlockPattern> cir) {
        BlockPattern flexiblePattern = createFixedWitherPattern();
        cir.setReturnValue(flexiblePattern);
    }

    private static BlockPattern createFixedWitherPattern() {
        return BlockPatternBuilder.start()
                .aisle("^^^", "###", "~#~")
                .where('#', BlockInWorld.hasState(state -> state.is(BlockTags.WITHER_SUMMON_BASE_BLOCKS)))
                .where('^', BlockInWorld.hasState(state -> (state.is(Blocks.WITHER_SKELETON_SKULL) || state.is(Blocks.WITHER_SKELETON_WALL_SKULL))))
                .where('~', BlockInWorld.hasState(Objects::nonNull))
                .build();
    }
}