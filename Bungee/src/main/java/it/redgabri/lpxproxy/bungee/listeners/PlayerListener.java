package it.redgabri.lpxproxy.bungee.listeners;

import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.player.LPXPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static net.md_5.bungee.event.EventPriority.LOWEST;

public class PlayerListener implements Listener {
    @EventHandler(priority = LOWEST)
    public void onJoin(PostLoginEvent e){
        ProxyLPX.getInstance().getPlayerManager().getPlayers().add(new LPXPlayer(e.getPlayer().getName()));
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e){
        ProxyLPX.getInstance().getPlayerManager().getPlayers().remove(ProxyLPX.getInstance().getPlayerManager().getPlayer(e.getPlayer().getName()));
    }
}
