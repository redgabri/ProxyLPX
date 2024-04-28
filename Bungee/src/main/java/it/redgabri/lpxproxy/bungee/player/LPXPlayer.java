package it.redgabri.lpxproxy.bungee.player;

import it.redgabri.lpxproxy.bungee.utils.Utils;
import it.redgabri.lpxproxy.commons.player.ILPXPlayer;
import net.md_5.bungee.api.ProxyServer;

public class LPXPlayer extends ILPXPlayer {

    private final ProxyServer proxyServer;

    public LPXPlayer(String name, ProxyServer proxyServer){
        this.proxyServer = proxyServer;
        this.name = name;
    }

    @Override
    public void sendMessage(String message) {
        proxyServer.getPlayer(this.name).sendMessage(Utils.format(message));
    }

    @Override
    public String getServerName() {
        return proxyServer.getPlayer(this.name).getServer().getInfo().getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return proxyServer.getPlayer(this.name).hasPermission(permission);
    }
}
