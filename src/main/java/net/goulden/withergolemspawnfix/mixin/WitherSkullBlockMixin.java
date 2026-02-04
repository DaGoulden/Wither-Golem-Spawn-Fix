package net.goulden.withergolemspawnfix.mixin;

import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.goulden.withergolemspawnfix.util.FixedStructurePatterns.allowWitherPatternFix;
import static net.goulden.withergolemspawnfix.util.FixedStructurePatterns.createFixedWitherPattern;

@Mixin(WitherSkullBlock.class)
public class WitherSkullBlockMixin {

    @Inject(
            method = "getOrCreateWitherFull",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void useFixedWitherPattern(CallbackInfoReturnable<BlockPattern> cir) {
        if (!allowWitherPatternFix) return;
        BlockPattern flexiblePattern = createFixedWitherPattern();
        cir.setReturnValue(flexiblePattern);
    }
}