package it.redgabri.lpxproxy.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import it.redgabri.lpxproxy.velocity.ProxyLPX;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class ProxyLPXCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1){
            source.sendMessage(MiniMessage.miniMessage().deserialize(ProxyLPX.getInstance().getConfig().getString("MESSAGES.ERRORS.INVALID_ARGS")));
            return;
        }
        if (!(source instanceof Player)){
            source.sendMessage(MiniMessage.miniMessage().deserialize(ProxyLPX.getInstance().getConfig().getString("MESSAGES.ERRORS.ONLY_PLAYERS")));
            return;
        }
        if (args[0].equalsIgnoreCase("alerts")){
            List<String> alertPlayers = ProxyLPX.getInstance().getAlertsManager().alertsPlayer;
            Player player = (Player) source;
            source.sendMessage(MiniMessage.miniMessage().deserialize(ProxyLPX.getInstance().getConfig().getString(alertPlayers.contains(player.getUsername()) ? "MESSAGES.ALERTS.DISABLED" : "MESSAGES.ALERTS.ENABLED")));
            ProxyLPX.getInstance().getAlertsManager().toggle(player.getUsername());
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("lpxproxy.alerts");
    }
}
