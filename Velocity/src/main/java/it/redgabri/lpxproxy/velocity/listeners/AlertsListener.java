package it.redgabri.lpxproxy.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import it.redgabri.lpxproxy.velocity.ProxyLPX;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class AlertsListener {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent e){
        // Make sure the server is the source since we don't
        // want players spoofing fake alerts
        if (e.getSource() instanceof Player) return;
        byte[] data = e.getData();
        String message = new String(data);
        if(e.getIdentifier().equals(ProxyLPX.getInstance().getChannel())){
            if(message.startsWith("#LPX#")){
                e.setResult(PluginMessageEvent.ForwardResult.handled());
                ProxyLPX.getInstance().getAlertsManager().sendAlert(message);
            }
        }
    }

    @Subscribe
    public void onJoin(LoginEvent e){
        if (e.getPlayer().hasPermission("lpxproxy.alerts")){
            List<String> alertPlayers = ProxyLPX.getInstance().getAlertsManager().alertsPlayer;
            ProxyLPX.getInstance().getAlertsManager().toggle(e.getPlayer().getUsername());
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(ProxyLPX.getInstance().getConfig().getString(!alertPlayers.contains(e.getPlayer().getUsername()) ? "MESSAGES.ALERTS.DISABLED" : "MESSAGES.ALERTS.ENABLED")));
        }
    }
}
