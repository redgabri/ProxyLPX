package it.redgabri.lpxproxy.bungee;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import it.redgabri.lpxproxy.bungee.commands.ProxyLPXCommand;
import it.redgabri.lpxproxy.bungee.listeners.AlertsListener;
import it.redgabri.lpxproxy.bungee.listeners.PlayerListener;
import it.redgabri.lpxproxy.commons.managers.AlertManager;
import it.redgabri.lpxproxy.commons.player.ILPXPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public final class ProxyLPX extends Plugin {

    private static ProxyLPX instance;
    private YamlDocument config;
    private AlertManager alertsManager;
    private ILPXPlayer.PlayerManager playerManager;

    @Override
    public void onEnable(){
        instance = this;

        this.playerManager = new ILPXPlayer.PlayerManager();

        PluginManager pluginManager = this.getProxy().getPluginManager();

        Arrays.asList(new ProxyLPXCommand("lpxproxy"), new ProxyLPXCommand("proxylpx"))
                .forEach(command -> pluginManager.registerCommand(this, command));

        Arrays.asList(new PlayerListener(this), new AlertsListener(this))
                .forEach(listener -> pluginManager.registerListener(this, listener));

        this.getProxy().registerChannel("lpxproxy:alerts");


        try {
            config = YamlDocument.create(new File(this.getProxy().getPluginsFolder(), "ProxyLPX/config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
                    GeneralSettings.builder().setKeyFormat(GeneralSettings.KeyFormat.OBJECT).build(),
                    LoaderSettings.DEFAULT,
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
            );

            config.update();
            config.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        this.getProxy().unregisterChannel("lpxproxy:alerts");
    }

    public static ProxyLPX getInstance() {
        return instance;
    }

    public YamlDocument getConfig() {
        return config;
    }

    public AlertManager getAlertsManager() {
        if (alertsManager == null) {
            alertsManager = new AlertManager();
        }
        return alertsManager;
    }

    public ILPXPlayer.PlayerManager getPlayerManager() {
        return playerManager;
    }
}
