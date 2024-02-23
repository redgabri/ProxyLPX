package it.redgabri.lpxproxy.bungee.manager;

import dev.dejvokep.boostedyaml.YamlDocument;
import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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


        Optional<ProxiedPlayer> player = Optional.ofNullable(ProxyLPX.getInstance().getProxy().getPlayer(playerName));
        if (player.isPresent()) {
            server = player.get().getServer().getInfo().getName();
        } else {
            System.out.println("[PROXYLPX] [ERROR] Player " + playerName + " not found while sending an alert");
            return;
        }

        for (ProxiedPlayer all : ProxyLPX.getInstance().getProxy().getPlayers()) {
            if (!all.getServer().getInfo().getName().equals(server)) {
                if (all.hasPermission("lpxproxy.alerts") && alertsPlayer.contains(all.getName())) {
                    all.sendMessage(Utils.format(config.getString("MESSAGES.ALERTS.ALERT_MESSAGE")
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
