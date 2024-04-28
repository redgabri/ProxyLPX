package it.redgabri.lpxproxy.bungee.listeners;

import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.player.LPXPlayer;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import it.redgabri.lpxproxy.commons.managers.AlertManager;
import it.redgabri.lpxproxy.commons.player.ILPXPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlertsListener implements Listener {

    private final ProxyLPX plugin;

    public AlertsListener(ProxyLPX plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        // Make sure the server is the source since we don't
        // want players spoofing fake alerts
        if (event.getSender() instanceof ProxiedPlayer) return;

        if (event.getTag().equals("lpxproxy:alerts")) {
            String message = new String(event.getData());

            if (message.startsWith("#LPX#")) {
                plugin.getAlertsManager().sendAlert(message, plugin.getPlayerManager().getPlayers());
            }
        }
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission("lpxproxy.alerts")) {
            AlertManager alertManager = plugin.getAlertsManager();
            ILPXPlayer.PlayerManager playerManager = plugin.getPlayerManager();

            String message = plugin.getConfig().getString(!alertManager.getAlertsPlayer().contains(player.getName())
                    ? "MESSAGES.ALERTS.DISABLED"
                    : "MESSAGES.ALERTS.ENABLED");

            alertManager.setEnabled(playerManager.getPlayer(player.getName()), true);
            player.sendMessage(Utils.format(message));
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        plugin.getAlertsManager().getAlertsPlayer().remove(event.getPlayer().getName());
    }
}
