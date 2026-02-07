package net.goulden.withergolemspawnfix.config;


import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigBuilder {

    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        SERVER = new Server(builder);
        SERVER_SPEC = builder.build();
    }

    public static class Server {

        protected final ForgeConfigSpec.BooleanValue witherPatternFix;
        protected final ForgeConfigSpec.BooleanValue golemPatternFix;
        protected final ForgeConfigSpec.BooleanValue golemSpawnTeleporter;
        protected final ForgeConfigSpec.BooleanValue golemSpawnBlockBreaker;

        Server(ForgeConfigSpec.Builder builder) {

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