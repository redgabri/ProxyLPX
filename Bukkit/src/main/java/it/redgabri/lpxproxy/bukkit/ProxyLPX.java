package it.redgabri.lpxproxy.bukkit;

import it.ytnoos.lpx.api.check.abstraction.Check;
import it.ytnoos.lpx.api.check.enums.CheckType;
import it.ytnoos.lpx.api.event.LPXAlertEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProxyLPX extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("LPX")) {
            Bukkit.getLogger().severe("LPX plugin is not installed. Please install it and try again.");
            getServer().getPluginManager().disablePlugin(this);

            return;
        }

        Bukkit.getLogger().info("LPX plugin is installed, enabling ProxyLPX...");

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "lpxproxy:alerts");
    }

    @EventHandler
    public void onFlag(LPXAlertEvent event) {
        Check<?> check = event.getCheck();

        Player player = event.getProtocolPlayer().getPlayer();
        CheckType type = check.getType();

        int vl = check.getOptions().getVl();
        int maxVL = check.getOptions().getCheckOptions().getMaxVL();

        // Use .join - xEcho1337
        // + "#LPX# " + player.getName() + " #LPX# " + type + " #LPX# " + maxVL + " #LPX# " + vl
        String message = String.join("#LPX#", player.getName(), type.toString(), String.valueOf(maxVL), String.valueOf(vl));
        player.sendPluginMessage(this, "lpxproxy:alerts", message.getBytes());
    }
}
