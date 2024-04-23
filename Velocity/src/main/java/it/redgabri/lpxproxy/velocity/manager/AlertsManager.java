package it.redgabri.lpxproxy.velocity.manager;

import com.velocitypowered.api.proxy.Player;
import dev.dejvokep.boostedyaml.YamlDocument;
import it.redgabri.lpxproxy.velocity.ProxyLPX;
import net.kyori.adventure.text.minimessage.MiniMessage;
import okhttp3.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class AlertsManager {

    public Set<UUID> alertsPlayer = new HashSet<>();
    private final YamlDocument config = ProxyLPX.getInstance().getConfig();

    public void toggle(Player player){
        setEnabled(player, !alertsPlayer.contains(player.getUniqueId()));
    }

    public void setEnabled(Player player, boolean enabled){
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


        Optional<Player> player = ProxyLPX.getInstance().getProxyServer().getPlayer(playerName);
        if (player.isPresent()) {
            server = player.get().getCurrentServer().get().getServerInfo().getName();
        } else {
            ProxyLPX.getInstance().getLogger().error("Player {} not found while sending an alert", playerName);
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

        for (Player all : ProxyLPX.getInstance().getProxyServer().getAllPlayers()) {
            if (!all.getCurrentServer().get().getServerInfo().getName().equals(server)) {
                if (all.hasPermission("lpxproxy.alerts") && alertsPlayer.contains(all.getUniqueId())) {
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
