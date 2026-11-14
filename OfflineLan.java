package com.example.lanoffline;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

@Mod(OfflineLan.MODID)
public class OfflineLan {
    public static final String MODID = "lanoffline";
    private static final Logger LOGGER = LogUtils.getLogger();

    private int tickCounter = 0;

    public OfflineLan() {
        ModLoadingContext ctx = ModLoadingContext.get();
        // Host-side only: clients without this mod can still connect.
        ctx.registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));
        ctx.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    // The integrated (singleplayer/LAN) server switches authentication on during startup.
    // Switch it back off once startup is done so offline-mode players can log in.
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        if (shouldApply(server)) {
            server.setUsesAuthentication(false);
            LOGGER.info("[lanoffline] Authentication disabled on the integrated server; offline-mode players can join over LAN.");
        }
    }

    // Large modpacks sometimes contain other mods that touch hosting (e.g. Essential).
    // Once a second, make sure nothing has switched authentication back on.
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (++tickCounter < 20) return;
        tickCounter = 0;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null && shouldApply(server) && server.usesAuthentication()) {
            server.setUsesAuthentication(false);
            LOGGER.warn("[lanoffline] Something re-enabled authentication; switched it off again.");
        }
    }

    private static boolean shouldApply(MinecraftServer server) {
        return !server.isDedicatedServer() && Config.allowOffline();
    }
}
