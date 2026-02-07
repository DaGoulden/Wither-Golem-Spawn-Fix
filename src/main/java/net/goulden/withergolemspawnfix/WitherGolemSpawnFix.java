package net.goulden.withergolemspawnfix;

import net.goulden.withergolemspawnfix.config.ConfigBuilder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(WitherGolemSpawnFix.MODID)
public class WitherGolemSpawnFix {

    public static final String MODID = "withergolemspawnfix";

    public WitherGolemSpawnFix() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigBuilder.SERVER_SPEC, "withergolemspawnfix-server.toml");

    }
}
