package it.redgabri.lpxproxy.bungee.commands;

import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.UUID;

public class ProxyLPXCommand extends Command {
    public ProxyLPXCommand(String name) {
        super(name, "lpxproxy.alerts");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("alerts")) {
                ProxyLPX.getInstance().getAlertsManager().toggle((ProxiedPlayer) sender);
                sender.sendMessage(Utils.format(ProxyLPX.getInstance().getConfig().getString(!ProxyLPX.getInstance().getAlertsManager().alertsPlayer.contains(((ProxiedPlayer) sender).getUniqueId()) ? "MESSAGES.ALERTS.DISABLED" : "MESSAGES.ALERTS.ENABLED")));
            }
        } else {
            sender.sendMessage(Utils.format(ProxyLPX.getInstance().getConfig().getString("MESSAGES.ERRORS.INVALID_ARGS")));
        }
    }
}
