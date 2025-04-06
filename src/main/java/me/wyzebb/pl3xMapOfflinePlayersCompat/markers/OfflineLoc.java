package me.wyzebb.pl3xMapOfflinePlayersCompat.markers;

import java.util.Objects;
import java.util.UUID;

import de.snap20lp.offlineplayers.OfflinePlayer;
import net.pl3x.map.core.Keyed;
import net.pl3x.map.core.markers.Point;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OfflineLoc extends Keyed {
    private final UUID uuid;
    private final Point point;
    private final long time;
    private final String name;
//    private final PlayerTexture skin;

    public OfflineLoc(@NotNull OfflinePlayer player) {
        super(player.getOfflinePlayer().getUniqueId().toString());

        Location loc = player.getOfflinePlayer().getLocation();
        this.uuid = player.getOfflinePlayer().getUniqueId();
        this.point = Point.of(loc.getX(), loc.getZ());
        this.time = System.currentTimeMillis();
        this.name = player.getOfflinePlayer().getName();
//        this.skin = new PlayerTexture(Pl3xMap.api().getPlayerRegistry().get(player.getUniqueId()));
    }

//    public @NotNull PlayerTexture getSkin() {
//        return this.skin;
//    }

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
        OfflineLoc other = (OfflineLoc) o;
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
        return "OfflineLoc{"
                + "uuid=" + getUUID()
                + ",name=" + getName()
                + ",point=" + getPoint()
                + "}";
    }
}