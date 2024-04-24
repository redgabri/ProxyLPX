package it.redgabri.lpxproxy.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
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
                ProxyLPX.getInstance().getAlertsManager().sendAlert(message, ProxyLPX.getInstance().getPlayerManager().getPlayers());
            }
        }
    }

    @Subscribe
    public void onJoin(PostLoginEvent e){
        if (e.getPlayer().hasPermission("lpxproxy.alerts")){
            ProxyLPX.getInstance().getAlertsManager().setEnabled(ProxyLPX.getInstance().getPlayerManager().getPlayer(e.getPlayer().getUsername()), true);
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(ProxyLPX.getInstance().getConfig().getString(!ProxyLPX.getInstance().getAlertsManager().alertsPlayer.contains(e.getPlayer().getUsername()) ? "MESSAGES.ALERTS.DISABLED" : "MESSAGES.ALERTS.ENABLED")));
        }
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent e){
        ProxyLPX.getInstance().getAlertsManager().alertsPlayer.remove(e.getPlayer().getUsername());
    }
}
