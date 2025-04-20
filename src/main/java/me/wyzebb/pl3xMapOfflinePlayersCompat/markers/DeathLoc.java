package me.wyzebb.pl3xMapOfflinePlayersCompat.markers;

import java.util.Objects;
import java.util.UUID;

import net.pl3x.map.core.Keyed;
import net.pl3x.map.core.markers.Point;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeathLoc extends Keyed {
    private final UUID uuid;
    private final Point point;
    private final long time;
    private final String name;

    public DeathLoc(@NotNull Player player) {
        super(player.getUniqueId().toString());

        Location loc = player.getLocation();
        this.uuid = player.getUniqueId();
        this.point = Point.of(loc.getX(), loc.getZ());
        this.time = System.currentTimeMillis();
        this.name = player.getName();
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull Point getPoint() {
        return this.point;
    }

    public long getTime() {
        return this.time;
    }

    public boolean expired(int seconds) {
        return getTime() + (seconds * 1000L) < System.currentTimeMillis();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        DeathLoc other = (DeathLoc) o;
        return getUUID().equals(other.getUUID()) &&
                getName().equals(other.getName()) &&
                getPoint().equals(other.getPoint());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUUID(), getName(), getPoint());
    }

    @Override
    public @NotNull String toString() {
        return "DeathLoc{"
                + "uuid=" + getUUID()
                + ",name=" + getName()
                + ",point=" + getPoint()
                + "}";
    }
}