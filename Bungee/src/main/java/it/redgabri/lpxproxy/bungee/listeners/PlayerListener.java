package it.redgabri.lpxproxy.bungee.listeners;

import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.player.LPXPlayer;
import it.redgabri.lpxproxy.commons.player.ILPXPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static net.md_5.bungee.event.EventPriority.LOWEST;

public class PlayerListener implements Listener {

    private final ProxyLPX plugin;

    public PlayerListener(ProxyLPX plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = LOWEST)
    public void onJoin(PostLoginEvent event) {
        plugin.getPlayerManager().getPlayers().add(new LPXPlayer(event.getPlayer().getName(), plugin.getProxy()));
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        ILPXPlayer.PlayerManager playerManager = plugin.getPlayerManager();
        playerManager.getPlayers().remove(playerManager.getPlayer(event.getPlayer().getName()));
    }
}
