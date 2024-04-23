package it.redgabri.lpxproxy.commons.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ILPXPlayer {
    public UUID uuid;
    public abstract void sendMessage(String message);
    public abstract String getServerName();
    public abstract boolean hasPermission(String permission);
    public static class PlayerManager {
        public List<ILPXPlayer> players = new ArrayList<>();

        public List<ILPXPlayer> getPlayers() {
            return players;
        }
        public ILPXPlayer getPlayer(UUID uuid){
            return players.stream().filter(player -> player.uuid.equals(uuid)).findFirst().get();
        }
    }
}
