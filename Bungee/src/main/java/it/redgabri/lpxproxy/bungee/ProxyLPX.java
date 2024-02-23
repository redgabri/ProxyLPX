package it.redgabri.lpxproxy.bungee;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import it.redgabri.lpxproxy.bungee.commands.ProxyLPXCommand;
import it.redgabri.lpxproxy.bungee.listeners.AlertsListener;
import it.redgabri.lpxproxy.bungee.manager.AlertsManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class ProxyLPX extends Plugin {
    private static ProxyLPX instance;
    private YamlDocument config;
    private AlertsManager alertsManager;

    @Override
    public void onEnable(){
        instance = this;
        this.getProxy().getPluginManager().registerCommand(this, new ProxyLPXCommand("lpxproxy"));
        this.getProxy().getPluginManager().registerCommand(this, new ProxyLPXCommand("proxylpx"));
        this.getProxy().registerChannel("lpxproxy");
        this.getProxy().getPluginManager().registerListener(this, new AlertsListener());
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
        this.getProxy().unregisterChannel("lpxproxy");
    }
    public static ProxyLPX getInstance() {
        return instance;
    }
    public YamlDocument getConfig() {
        return config;
    }
    public AlertsManager getAlertsManager() {
        if (alertsManager == null) {
            alertsManager = new AlertsManager();
        }
        return alertsManager;
    }
}
