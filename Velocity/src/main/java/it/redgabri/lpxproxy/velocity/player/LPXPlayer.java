package it.redgabri.lpxproxy.velocity.player;

import it.redgabri.lpxproxy.commons.player.ILPXPlayer;
import it.redgabri.lpxproxy.velocity.ProxyLPX;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.UUID;

public class LPXPlayer extends ILPXPlayer {
    public LPXPlayer(String name){
        this.name = name;
    }
    @Override
    public void sendMessage(String message) {
        ProxyLPX.getInstance().getProxyServer().getPlayer(this.name).get().sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    @Override
    public String getServerName() {
        return ProxyLPX.getInstance().getProxyServer().getPlayer(this.name).get().getCurrentServer().get().getServerInfo().getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return ProxyLPX.getInstance().getProxyServer().getPlayer(this.name).get().hasPermission(permission);
    }
}
