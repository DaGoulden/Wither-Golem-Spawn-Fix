package net.goulden.withergolemspawnfix.config;

import net.goulden.withergolemspawnfix.WitherGolemSpawnFix;
import net.goulden.withergolemspawnfix.util.FixedStructurePatterns;
import net.goulden.withergolemspawnfix.util.GolemSpawnSafetyHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import static net.goulden.withergolemspawnfix.config.ConfigBuilder.SERVER;

@Mod.EventBusSubscriber(modid = WitherGolemSpawnFix.MODID)
public class ConfigEvent {

    @SubscribeEvent
    public static void configLoading(ModConfigEvent.Loading event) {
        refreshConfig();
    }
    @SubscribeEvent
    public static void configReloading(ModConfigEvent.Reloading event) {
        refreshConfig();
    }

    private static void refreshConfig() {
        FixedStructurePatterns.allowWitherPatternFix = SERVER.witherPatternFix.get();
        FixedStructurePatterns.allowGolemPatternFix = SERVER.golemPatternFix.get();
        GolemSpawnSafetyHelper.allowGolemTeleport = SERVER.golemSpawnTeleporter.get();
        GolemSpawnSafetyHelper.allowGolemBlockBreaker = SERVER.golemSpawnBlockBreaker.get();
    }
}