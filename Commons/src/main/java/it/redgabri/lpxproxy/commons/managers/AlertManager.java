package it.redgabri.lpxproxy.commons.managers;

import com.velocitypowered.api.proxy.Player;
import dev.dejvokep.boostedyaml.YamlDocument;
import it.redgabri.lpxproxy.commons.player.ILPXPlayer;
import okhttp3.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class AlertManager {
    public Set<UUID> alertsPlayer = new HashSet<>();
    private final YamlDocument config;
    private final boolean isVelocity;

    public AlertManager() {
        isVelocity = Arrays.asList(Object.class.getInterfaces()).contains(Player.class);
        try {
            config = YamlDocument.create(this.getClass().getResourceAsStream("/config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toggle(ILPXPlayer player){
        setEnabled(player, !alertsPlayer.contains(player.uuid));
    }

    public void setEnabled(ILPXPlayer player, boolean enabled){
        if (enabled){
            alertsPlayer.remove(player.uuid);
        } else {
            alertsPlayer.add(player.uuid);
        }
    }

    public void sendAlert(String message, List<ILPXPlayer> players) {
        String[] info = message.split("#LPX#");
        String[] checkInfo = info[2].split("_");

        String playerName = info[1].trim();
        String check = checkInfo[0].trim().toLowerCase();
        String type = checkInfo[1].trim();
        String maxVL = info[3].trim();
        String Vl = info[4].trim();
        String server = null;

        sendToDiscord(config.getString("DISCORD.MESSAGE")
                .replaceAll("%player%", playerName)
                .replaceAll("%server%", server)
                .replaceAll("%check%", check)
                .replaceAll("%type%", type)
                .replaceAll("%vl%", Vl)
                .replaceAll("%maxvl%", maxVL)
        );

        players.forEach(all -> {
            if (!all.getServerName().equals(server)) {
                if (all.hasPermission("lpxproxy.alerts") && alertsPlayer.contains(all.uuid)) {
                    all.sendMessage(config.getString("MESSAGES.ALERTS.ALERT_MESSAGE")
                            .replaceAll("%player%", playerName)
                            .replaceAll("%server%", server)
                            .replaceAll("%check%", check)
                            .replaceAll("%type%", type)
                            .replaceAll("%vl%", Vl)
                            .replaceAll("%maxvl%", maxVL)
                    );
                }
            }
        });
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
                    System.out.println("[PROXYLPX] An error occurred while sending the webhook!");
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        });
    }
}
