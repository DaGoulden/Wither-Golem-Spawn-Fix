package net.goulden.withergolemspawnfix.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigBuilder {

    public static final ModConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        SERVER = new Server(builder);
        SERVER_SPEC = builder.build();
    }

    public static class Server {

        protected final ModConfigSpec.BooleanValue witherPatternFix;
        protected final ModConfigSpec.BooleanValue golemPatternFix;
        protected final ModConfigSpec.BooleanValue golemSpawnTeleporter;
        protected final ModConfigSpec.BooleanValue golemSpawnBlockBreaker;

        Server(ModConfigSpec.Builder builder) {

            witherPatternFix = builder
                    .comment("Does the Wither have his structure pattern fixed?")
                    .define("witherPatternFix", true);

            golemPatternFix = builder
                    .comment("Does the Golem have his structure pattern fixed?")
                    .define("golemPatternFix", true);

            golemSpawnTeleporter = builder
                    .comment("Does the Golem teleport to a safer location when spawned?")
                    .define("golemSpawnTeleporter", true);

            golemSpawnBlockBreaker = builder
                    .comment("Does the Golem break blocks that can suffocate him when spawned?")
                    .define("golemSpawnBlockBreaker", true);

        }
    }
}