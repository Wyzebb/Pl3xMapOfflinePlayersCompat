package me.wyzebb.pl3xMapOfflinePlayersCompat.markers;


import java.util.Collection;
import java.util.stream.Collectors;

import me.wyzebb.pl3xMapOfflinePlayersCompat.Pl3xMapOfflinePlayersCompat;
import me.wyzebb.pl3xMapOfflinePlayersCompat.configuration.WorldConfig;
import me.wyzebb.pl3xMapOfflinePlayersCompat.listeners.Pl3xMapListener;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class OfflineLayer extends WorldLayer {
    public static final String KEY = "Pl3xMapOfflinePlayersCompat";

    private final WorldConfig config;
    private final Options options;

    public OfflineLayer(@NotNull WorldConfig config) {
        super(KEY, config.getWorld(), () -> config.LAYER_LABEL);
        this.config = config;

        setShowControls(config.LAYER_SHOW_CONTROLS);
        setDefaultHidden(config.LAYER_DEFAULT_HIDDEN);
        setUpdateInterval(config.LAYER_UPDATE_INTERVAL);
        setPriority(config.LAYER_PRIORITY);
        setZIndex(config.LAYER_ZINDEX);

        this.options = new Options.Builder()
                .tooltipPane(config.ICON_TOOLTIP_PANE)
                .tooltipOffset(config.ICON_TOOLTIP_OFFSET)
                .tooltipDirection(config.ICON_TOOLTIP_DIRECTION)
                .tooltipPermanent(config.ICON_TOOLTIP_PERMANENT)
                .tooltipSticky(config.ICON_TOOLTIP_STICKY)
                .tooltipOpacity(config.ICON_TOOLTIP_OPACITY)
                .popupPane(config.ICON_POPUP_PANE)
                .popupOffset(config.ICON_POPUP_OFFSET)
                .popupMaxWidth(config.ICON_POPUP_MAX_WIDTH)
                .popupMinWidth(config.ICON_POPUP_MIN_WIDTH)
                .popupMaxHeight(config.ICON_POPUP_MAX_HEIGHT)
                .popupShouldAutoPan(config.ICON_POPUP_SHOULD_AUTO_PAN)
                .popupAutoPanPaddingTopLeft(config.ICON_POPUP_AUTO_PAN_PADDING_TOP_LEFT)
                .popupAutoPanPaddingBottomRight(config.ICON_POPUP_AUTO_PAN_PADDING_BOTTOM_RIGHT)
                .popupAutoPanPadding(config.ICON_POPUP_AUTO_PAN_PADDING)
                .popupShouldKeepInView(config.ICON_POPUP_SHOULD_KEEP_IN_VIEW)
                .popupCloseButton(config.ICON_POPUP_CLOSE_BUTTON)
                .popupShouldAutoClose(config.ICON_POPUP_SHOULD_AUTO_CLOSE)
                .popupShouldCloseOnEscapeKey(config.ICON_POPUP_SHOULD_CLOSE_ON_ESCAPE_KEY)
                .popupShouldCloseOnClick(config.ICON_POPUP_SHOULD_CLOSE_ON_CLICK)
                .build();

        Bukkit.getScheduler().runTaskTimerAsynchronously(Pl3xMapOfflinePlayersCompat.getPlugin(Pl3xMapOfflinePlayersCompat.class), () ->
                Pl3xMapListener.Pl3xMapOfflinePlayersCompat.values().removeIf(next -> next.expired(config.SECONDS_TO_SHOW)), 20L, 20L);
    }

    @Override
    public @NotNull Collection<Marker<?>> getMarkers() {
        String marker = Icon.MARKER.getKey();
        String shadow = Icon.SHADOW.getKey();

        return Pl3xMapListener.Pl3xMapOfflinePlayersCompat.values().stream().map(spot -> {
            net.pl3x.map.core.markers.marker.@NotNull Icon icon = Marker.icon(KEY + "_" + spot.getName(), spot.getPoint(), marker, this.config.ICON_SIZE)
                    .setAnchor(config.ICON_ANCHOR)
                    .setRotationAngle(config.ICON_ROTATION_ANGLE)
                    .setRotationOrigin(config.ICON_ROTATION_ORIGIN)
                    .setShadow(shadow)
                    .setShadowSize(config.ICON_SHADOW_SIZE)
                    .setShadowAnchor(config.ICON_SHADOW_ANCHOR);
            Options.Builder builder = this.options.asBuilder();
            if (config.ICON_POPUP_CONTENT != null) {
                builder.popupContent(config.ICON_POPUP_CONTENT.replace("<player>", spot.getName()));
            }
            if (config.ICON_TOOLTIP_CONTENT != null) {
                builder.tooltipContent(config.ICON_TOOLTIP_CONTENT.replace("<player>", spot.getName()));
            }
            return icon.setOptions(builder.build());
        }).collect(Collectors.toList());
    }
}