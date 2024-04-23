package it.redgabri.lpxproxy.bungee.listeners;

import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class AlertsListener implements Listener {

    @EventHandler
    public void onMessage(PluginMessageEvent e){
        // Make sure the server is the source since we don't
        // want players spoofing fake alerts
        if (e.getSender() instanceof ProxiedPlayer) return;
        if(e.getTag().equals("lpxproxy")){
            String message = new String(e.getData());
            if(message.startsWith("#LPX#")){
                ProxyLPX.getInstance().getAlertsManager().sendAlert(message);
            }
        }
    }

    @EventHandler
    public void onJoin(PostLoginEvent e){
        if (e.getPlayer().hasPermission("lpxproxy.alerts")){
            ProxyLPX.getInstance().getAlertsManager().setEnabled(e.getPlayer(), true);
            e.getPlayer().sendMessage(Utils.format(ProxyLPX.getInstance().getConfig().getString(!ProxyLPX.getInstance().getAlertsManager().alertsPlayer.contains(e.getPlayer().getUniqueId()) ? "MESSAGES.ALERTS.DISABLED" : "MESSAGES.ALERTS.ENABLED")));
        }
    }
}
