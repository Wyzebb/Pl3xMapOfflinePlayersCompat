package me.wyzebb.pl3xMapOfflinePlayersCompat;

import me.wyzebb.pl3xMapOfflinePlayersCompat.listeners.Pl3xMapListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pl3xMapOfflinePlayersCompat extends JavaPlugin {
    private boolean hasHook;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().isPluginEnabled("Pl3xMap")) {
            getLogger().info("Found Pl3xMap. Hooking into plugin.");
            getServer().getPluginManager().registerEvents(new Pl3xMapListener(), this);
            this.hasHook = true;
        } else {
            getLogger().info("Could not find Pl3xMap. Skipping hook.");
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        if (this.hasHook) {
            Pl3xMapListener.shutdown();
        }
    }
}
