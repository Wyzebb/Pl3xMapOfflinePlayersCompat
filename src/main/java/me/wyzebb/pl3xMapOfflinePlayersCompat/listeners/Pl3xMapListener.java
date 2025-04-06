package me.wyzebb.pl3xMapOfflinePlayersCompat.listeners;

import de.snap20lp.offlineplayers.OfflinePlayer;
import de.snap20lp.offlineplayers.events.OfflinePlayerDeathEvent;
import de.snap20lp.offlineplayers.events.OfflinePlayerDespawnEvent;
import de.snap20lp.offlineplayers.events.OfflinePlayerSpawnEvent;
import me.wyzebb.pl3xMapOfflinePlayersCompat.configuration.WorldConfig;
import me.wyzebb.pl3xMapOfflinePlayersCompat.markers.Icon;
import me.wyzebb.pl3xMapOfflinePlayersCompat.markers.OfflineLayer;
import me.wyzebb.pl3xMapOfflinePlayersCompat.markers.OfflineLoc;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.event.EventHandler;
import net.pl3x.map.core.event.EventListener;
import net.pl3x.map.core.event.server.Pl3xMapEnabledEvent;
import net.pl3x.map.core.event.server.ServerLoadedEvent;
import net.pl3x.map.core.event.world.WorldLoadedEvent;
import net.pl3x.map.core.event.world.WorldUnloadedEvent;
import net.pl3x.map.core.registry.Registry;
import net.pl3x.map.core.world.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class Pl3xMapListener implements EventListener, Listener {
    public static final Registry<OfflineLoc> Pl3xMapOfflinePlayersCompat = new Registry<>();

    public Pl3xMapListener() {
        Pl3xMap.api().getEventRegistry().register(this);
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOfflinePlayerLeave(@NotNull OfflinePlayerSpawnEvent event) {
        OfflinePlayer player = event.getOfflinePlayer();
        World world = Pl3xMap.api().getWorldRegistry().get(player.getCloneEntity().getWorld().getName());

        if (world != null && world.isEnabled() && world.getLayerRegistry().has(OfflineLayer.KEY)) {
            Pl3xMapOfflinePlayersCompat.register(new OfflineLoc(player));
        }
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOfflinePlayerJoin(@NotNull OfflinePlayerDespawnEvent event) {
        OfflinePlayer player = event.getOfflinePlayer();
        World world = Pl3xMap.api().getWorldRegistry().get(player.getCloneEntity().getWorld().getName());

        if (world != null && world.isEnabled() && world.getLayerRegistry().has(OfflineLayer.KEY)) {
            if (Pl3xMapOfflinePlayersCompat.has(player.getOfflinePlayer().getUniqueId().toString())) {
                Pl3xMapOfflinePlayersCompat.unregister(player.getOfflinePlayer().getUniqueId().toString());
            }
        }
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOfflinePlayerDeath(@NotNull OfflinePlayerDeathEvent event) {
        OfflinePlayer player = event.getOfflinePlayer();
        World world = Pl3xMap.api().getWorldRegistry().get(player.getCloneEntity().getWorld().getName());

        if (world != null && world.isEnabled() && world.getLayerRegistry().has(OfflineLayer.KEY)) {
            if (Pl3xMapOfflinePlayersCompat.has(player.getOfflinePlayer().getUniqueId().toString())) {
                Pl3xMapOfflinePlayersCompat.unregister(player.getOfflinePlayer().getUniqueId().toString());
            }
        }
    }

    @EventHandler
    public void onPl3xMapEnabled(@NotNull Pl3xMapEnabledEvent event) {
        Icon.register();
    }

    @EventHandler
    public void onServerLoaded(@NotNull ServerLoadedEvent event) {
        Icon.register();
        Pl3xMap.api().getWorldRegistry().forEach(this::registerWorld);
    }

    @EventHandler
    public void onWorldLoaded(@NotNull WorldLoadedEvent event) {
        registerWorld(event.getWorld());
    }

    @EventHandler
    public void onWorldUnloaded(@NotNull WorldUnloadedEvent event) {
        try {
            event.getWorld().getLayerRegistry().unregister(OfflineLayer.KEY);
        } catch (Throwable ignore) {
        }
    }

    private void registerWorld(@NotNull World world) {
        world.getLayerRegistry().register(new OfflineLayer(new WorldConfig(world)));
    }

    public static void shutdown() {
        Pl3xMap.api().getWorldRegistry().forEach(world -> {
            try {
                world.getLayerRegistry().unregister(OfflineLayer.KEY);
            } catch (Throwable ignore) {
            }
        });
    }
}