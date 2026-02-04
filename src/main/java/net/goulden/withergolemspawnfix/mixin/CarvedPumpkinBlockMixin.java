package net.goulden.withergolemspawnfix.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.goulden.withergolemspawnfix.util.FixedStructurePatterns.allowGolemPatternFix;
import static net.goulden.withergolemspawnfix.util.FixedStructurePatterns.createFixedIronGolemPattern;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin {

    @Inject(
            method = "getOrCreateIronGolemFull",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void useFixedIronGolemPattern(CallbackInfoReturnable<BlockPattern> cir) {
        if (!allowGolemPatternFix) return;
        BlockPattern flexiblePattern = createFixedIronGolemPattern();
        cir.setReturnValue(flexiblePattern);
    }

    @Inject(
            method = "clearPatternBlocks",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void customClearPatternBlocks(Level level, BlockPattern.BlockPatternMatch match, CallbackInfo ci) {

        BlockState center = match.getBlock(1, 1, 0).getState();

        for (int i = 0; i < match.getWidth(); i++) {
            for (int j = 0; j < match.getHeight(); j++) {
                boolean shouldRemove = false;

                if (center.is(BlockTags.WITHER_SUMMON_BASE_BLOCKS)) {
                    if (j == 0) {
                        shouldRemove = true;
                    } else if (j == 1) {
                        shouldRemove = true;
                    } else if (j == 2) {
                        shouldRemove = (i == 1);
                    }
                } else {
                    if (j == 0) {
                        shouldRemove = (i == 1);
                    } else if (j == 1) {
                        shouldRemove = true;
                    } else if (j == 2) {
                        shouldRemove = (i == 1);
                    }
                }

                if (shouldRemove) {
                    BlockInWorld blockinworld = match.getBlock(i, j, 0);
                    level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
                }
            }
        }

        ci.cancel();
    }
}