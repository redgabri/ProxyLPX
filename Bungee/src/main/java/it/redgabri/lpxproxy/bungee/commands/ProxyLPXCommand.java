package it.redgabri.lpxproxy.bungee.commands;

import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class ProxyLPXCommand extends Command {
    public ProxyLPXCommand(String name) {
        super(name, "lpxproxy.alerts");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("alerts")) {
                List<String> alertPlayers = ProxyLPX.getInstance().getAlertsManager().alertsPlayer;
                ProxyLPX.getInstance().getAlertsManager().toggle(sender.getName());
                sender.sendMessage(Utils.format(ProxyLPX.getInstance().getConfig().getString(!alertPlayers.contains(sender.getName()) ? "MESSAGES.ALERTS.DISABLED" : "MESSAGES.ALERTS.ENABLED")));
            }
        } else {
            sender.sendMessage(Utils.format(ProxyLPX.getInstance().getConfig().getString("MESSAGES.ERRORS.INVALID_ARGS")));
        }
    }
}
