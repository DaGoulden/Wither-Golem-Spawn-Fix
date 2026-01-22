package net.goulden.withergolemspawnfix.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.animal.IronGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.goulden.withergolemspawnfix.util.GolemSpawnSafetyHelper.handleSpawnSuffocation;

@Mixin(IronGolem.class)
public class IronGolemMixin {

    @Inject(
            method = "hurt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        IronGolem golem = (IronGolem) (Object) this;

        if (source.is(DamageTypes.IN_WALL) && !golem.getPersistentData().getBoolean("SpawnChecked")) {
            cir.cancel();
        }
    }

    @Inject(
            method = "aiStep",
            at = @At("HEAD")
    )
    private void onFirstTick(CallbackInfo ci) {
        IronGolem golem = (IronGolem) (Object) this;

        if (!golem.getPersistentData().getBoolean("SpawnChecked") && golem.onGround()) {
            golem.getPersistentData().putBoolean("SpawnChecked", true);
            handleSpawnSuffocation(golem, 0.4);
        }
    }
}