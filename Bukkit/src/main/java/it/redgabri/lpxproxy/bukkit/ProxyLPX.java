package it.redgabri.lpxproxy.bukkit;

import it.ytnoos.lpx.api.check.enums.CheckType;
import it.ytnoos.lpx.api.event.LPXAlertEvent;
import it.ytnoos.lpx.api.player.ProtocolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
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
        getServer().getMessenger().registerOutgoingPluginChannel(this, "lpxproxy");
    }

    @EventHandler
    public void onFlag(LPXAlertEvent e){
        ProtocolPlayer protocolPlayer = e.getProtocolPlayer();
        Player player = protocolPlayer.getPlayer();
        CheckType type = e.getCheck().getType();

        String message = "#player " + player.getName() + " #type " + type + " #maxvl " + e.getCheck().getOptions().getCheckOptions().getMaxVL() + " #vl " + e.getCheck().getOptions().getVl();
        System.out.println("[DEBUG] " + message);
        player.sendPluginMessage(this, "lpxproxy", message.getBytes());
    }
}
