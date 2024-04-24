package it.redgabri.lpxproxy.bungee.player;

import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import it.redgabri.lpxproxy.commons.player.ILPXPlayer;

import java.util.UUID;

public class LPXPlayer extends ILPXPlayer {
    public LPXPlayer(String name){
        this.name = name;
    }
    @Override
    public void sendMessage(String message) {
        ProxyLPX.getInstance().getProxy().getPlayer(this.name).sendMessage(Utils.format(message));
    }

    @Override
    public String getServerName() {
        return ProxyLPX.getInstance().getProxy().getPlayer(this.name).getServer().getInfo().getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return ProxyLPX.getInstance().getProxy().getPlayer(this.name).hasPermission(permission);
    }
}
