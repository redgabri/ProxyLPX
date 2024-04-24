package it.redgabri.lpxproxy.commons.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ILPXPlayer {
    public String name;
    public abstract void sendMessage(String message);
    public abstract String getServerName();
    public abstract boolean hasPermission(String permission);
    public static class PlayerManager {
        public List<ILPXPlayer> players = new ArrayList<>();
        public List<ILPXPlayer> getPlayers() {
            return players;
        }
        public ILPXPlayer getPlayer(String name){
            return players.stream().filter(player -> player.name.equals(name)).findFirst().get();
        }
    }
}
