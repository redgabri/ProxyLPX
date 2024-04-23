package it.redgabri.lpxproxy.bungee.manager;

import dev.dejvokep.boostedyaml.YamlDocument;
import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import okhttp3.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AlertsManager {

    public List<UUID> alertsPlayer = new ArrayList<>();
    private final YamlDocument config = ProxyLPX.getInstance().getConfig();
    public void toggle(ProxiedPlayer player){
        setEnabled(player, !alertsPlayer.contains(player.getUniqueId()));
    }

    public void setEnabled(ProxiedPlayer player, boolean enabled){
        if (enabled){
            alertsPlayer.remove(player.getUniqueId());
        } else {
            alertsPlayer.add(player.getUniqueId());
        }
    }

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
            ProxyLPX.getInstance().getLogger().severe("Player " + playerName + " not found while sending an alert");
            return;
        }

        sendToDiscord(config.getString("DISCORD.MESSAGE")
                .replaceAll("%player%", playerName)
                .replaceAll("%server%", server)
                .replaceAll("%check%", check)
                .replaceAll("%type%", type)
                .replaceAll("%vl%", Vl)
                .replaceAll("%maxvl%", maxVL)
        );

        for (ProxiedPlayer all : ProxyLPX.getInstance().getProxy().getPlayers()) {
            if (!all.getServer().getInfo().getName().equals(server)) {
                if (all.hasPermission("lpxproxy.alerts") && alertsPlayer.contains(all.getUniqueId())) {
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

    public void sendToDiscord(String message) {
        if(config.getString("DISCORD.URL").isEmpty()) return;
        CompletableFuture.runAsync(() -> {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"content\": \"" + message + "\"}");

            Request request = new Request.Builder()
                    .url(config.getString("DISCORD.URL"))
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    ProxyLPX.getInstance().getLogger().info("An error occurred while sending discord webhook. ");
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        });
    }
}
