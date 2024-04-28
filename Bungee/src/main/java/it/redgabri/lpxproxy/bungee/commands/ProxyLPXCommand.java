package it.redgabri.lpxproxy.bungee.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import it.redgabri.lpxproxy.bungee.ProxyLPX;
import it.redgabri.lpxproxy.bungee.utils.Utils;
import it.redgabri.lpxproxy.commons.managers.AlertManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.UUID;

public class ProxyLPXCommand extends Command {

    public ProxyLPXCommand(String name) {
        super(name, "lpxproxy.alerts");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxyLPX plugin = ProxyLPX.getInstance();
        AlertManager alertManager = plugin.getAlertsManager();

        YamlDocument config = plugin.getConfig();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("alerts")) {
                alertManager.toggle(plugin.getPlayerManager().getPlayer(sender.getName()));
                sender.sendMessage(Utils.format(config.getString(!alertManager.getAlertsPlayer().contains(sender.getName())
                        ? "MESSAGES.ALERTS.DISABLED"
                        : "MESSAGES.ALERTS.ENABLED")));
            } else {
                sender.sendMessage(Utils.format(config.getString("MESSAGES.ERRORS.INVALID_ARGS")));
            }
        }
    }
}
