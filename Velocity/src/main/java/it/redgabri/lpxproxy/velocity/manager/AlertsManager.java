package it.redgabri.lpxproxy.velocity.manager;

import com.velocitypowered.api.proxy.Player;
import dev.dejvokep.boostedyaml.YamlDocument;
import it.redgabri.lpxproxy.velocity.ProxyLPX;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlertsManager {
    public List<String> alertsPlayer = new ArrayList<>();
    public void toggle(String player){
        if (alertsPlayer.contains(player)){
            alertsPlayer.remove(player);
        } else {
            alertsPlayer.add(player);
        }
    }
    YamlDocument config = ProxyLPX.getInstance().getConfig();
    public void sendAlert(String message) {
        String[] info = message.split("#LPX#");
        String[] checkInfo = info[2].split("_");

        String playerName = info[1].trim();
        String check = checkInfo[0].trim().toLowerCase();
        String type = checkInfo[1].trim();
        String maxVL = info[3].trim();
        String Vl = info[4].trim();
        String server = null;


        Optional<Player> player = ProxyLPX.getInstance().getProxyServer().getPlayer(playerName);
        if (player.isPresent()) {
            server = player.get().getCurrentServer().get().getServerInfo().getName();
        } else {
            System.out.println("[PROXYLPX] [ERROR] Player " + playerName + " not found while sending an alert");
            return;
        }

        for (Player all : ProxyLPX.getInstance().getProxyServer().getAllPlayers()) {
            if (!all.getCurrentServer().get().getServerInfo().getName().equals(server)) {
                if (all.hasPermission("lpxproxy.alerts") && alertsPlayer.contains(all.getUsername())) {
                    all.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("MESSAGES.ALERTS.ALERT_MESSAGE")
                            .replaceAll("%player%", playerName)
                            .replaceAll("%server%", server)
                            .replaceAll("%check%", check)
                            .replaceAll("%type%", type)
                            .replaceAll("%vl%", Vl)
                            .replaceAll("%maxvl%", maxVL)
                    ));
                }
            }
        }
    }
}
