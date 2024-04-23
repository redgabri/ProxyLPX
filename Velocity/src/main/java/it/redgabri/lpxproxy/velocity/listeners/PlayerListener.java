package it.redgabri.lpxproxy.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import it.redgabri.lpxproxy.velocity.ProxyLPX;
import it.redgabri.lpxproxy.velocity.player.LPXPlayer;

public class PlayerListener {
    @Subscribe
    public void onJoin(LoginEvent e){
        ProxyLPX.getInstance().getPlayerManager().getPlayers().add(new LPXPlayer(e.getPlayer().getUniqueId()));
    }

    @Subscribe
    public void onLeave(DisconnectEvent e){
        ProxyLPX.getInstance().getPlayerManager().getPlayers().remove(new LPXPlayer(e.getPlayer().getUniqueId()));
    }
}
