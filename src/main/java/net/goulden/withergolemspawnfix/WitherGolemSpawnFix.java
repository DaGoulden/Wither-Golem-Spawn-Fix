package net.goulden.withergolemspawnfix;

import net.goulden.withergolemspawnfix.config.ConfigBuilder;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(WitherGolemSpawnFix.MODID)
public class WitherGolemSpawnFix {

    public static final String MODID = "withergolemspawnfix";

    public WitherGolemSpawnFix(ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.SERVER, ConfigBuilder.SERVER_SPEC);

    }
}
