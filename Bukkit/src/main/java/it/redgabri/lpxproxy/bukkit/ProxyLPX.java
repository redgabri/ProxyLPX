package it.redgabri.lpxproxy.bukkit;

import it.ytnoos.lpx.api.check.enums.CheckType;
import it.ytnoos.lpx.api.event.LPXAlertEvent;
import it.ytnoos.lpx.api.player.ProtocolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProxyLPX extends JavaPlugin implements Listener {

    @Override
    public void onEnable(){
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
    public void onFlag(LPXAlertEvent e){
        Player player = e.getProtocolPlayer().getPlayer();
        CheckType type = e.getCheck().getType();
        int vl = e.getCheck().getOptions().getVl();
        int maxVL = e.getCheck().getOptions().getCheckOptions().getMaxVL();

        String message = "#LPX# " + player.getName() + " #LPX# " + type + " #LPX# " + maxVL + " #LPX# " + vl;
        player.sendPluginMessage(this, "lpxproxy:alerts", message.getBytes());
    }
}
