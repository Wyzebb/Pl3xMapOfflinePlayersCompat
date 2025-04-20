package me.wyzebb.pl3xMapOfflinePlayersCompat.listeners;

import me.wyzebb.pl3xMapOfflinePlayersCompat.configuration.WorldConfig;
import me.wyzebb.pl3xMapOfflinePlayersCompat.markers.Icon;
import me.wyzebb.pl3xMapOfflinePlayersCompat.markers.DeathsLayer;
import me.wyzebb.pl3xMapOfflinePlayersCompat.markers.DeathLoc;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class Pl3xMapListener implements EventListener, Listener {
    public static final Registry<DeathLoc> Pl3xMapOfflinePlayersCompat = new Registry<>();

    public Pl3xMapListener() {
        Pl3xMap.api().getEventRegistry().register(this);
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        Player player = event.getPlayer();
        World world = Pl3xMap.api().getWorldRegistry().get(player.getWorld().getName());

        if (world != null && world.isEnabled() && world.getLayerRegistry().has(DeathsLayer.KEY)) {
            if (Pl3xMapOfflinePlayersCompat.has(player.getUniqueId().toString())) {
                Pl3xMapOfflinePlayersCompat.unregister(player.getUniqueId().toString());
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
            event.getWorld().getLayerRegistry().unregister(DeathsLayer.KEY);
        } catch (Throwable ignore) {
        }
    }

    private void registerWorld(@NotNull World world) {
        world.getLayerRegistry().register(new DeathsLayer(new WorldConfig(world)));
    }

    public static void shutdown() {
        Pl3xMap.api().getWorldRegistry().forEach(world -> {
            try {
                world.getLayerRegistry().unregister(DeathsLayer.KEY);
            } catch (Throwable ignore) {
            }
        });
    }
}