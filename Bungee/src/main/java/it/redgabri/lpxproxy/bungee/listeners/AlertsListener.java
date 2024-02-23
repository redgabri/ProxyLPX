package it.redgabri.lpxproxy.bungee.listeners;

import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class AlertsListener implements Listener {

    @EventHandler
    public void onMessage(PluginMessageEvent e){
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
            List<String> alertPlayers = ProxyLPX.getInstance().getAlertsManager().alertsPlayer;
            ProxyLPX.getInstance().getAlertsManager().toggle(e.getPlayer().getName());
            e.getPlayer().sendMessage(Utils.format(ProxyLPX.getInstance().getConfig().getString(!alertPlayers.contains(e.getPlayer().getName()) ? "MESSAGES.ALERTS.DISABLED" : "MESSAGES.ALERTS.ENABLED")));
        }
    }
}
