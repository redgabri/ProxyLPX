package it.redgabri.lpxproxy.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import it.redgabri.lpxproxy.commons.managers.AlertManager;
import it.redgabri.lpxproxy.commons.player.ILPXPlayer;
import it.redgabri.lpxproxy.velocity.commands.ProxyLPXCommand;
import it.redgabri.lpxproxy.velocity.listeners.AlertsListener;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(
        id = "proxylpx-velocity",
        name = "ProxyLPX",
        version = "1.4",
        authors = "redgabri",
        description = "Adds LPX alerts on proxy"
)
public class ProxyLPX {
    private Logger logger;
    private ProxyServer proxyServer;
    private Path path;
    private YamlDocument config;
    private static ProxyLPX instance;
    private AlertManager alertsManager;
    private ILPXPlayer.PlayerManager playerManager;
    private final ChannelIdentifier channel = new LegacyChannelIdentifier("lpxproxy:alerts");
    @Inject
    public ProxyLPX(Logger logger, ProxyServer proxyServer, @DataDirectory Path path) {
        this.logger = logger;
        this.proxyServer = proxyServer;
        this.path = path;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
        instance = this;
        playerManager = new ILPXPlayer.PlayerManager();
        config = YamlDocument.create(new File(path.toFile(), "config.yml"),
                Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
                GeneralSettings.builder().setKeyFormat(GeneralSettings.KeyFormat.OBJECT).build(),
                LoaderSettings.DEFAULT,
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
        );
        config.update();
        config.reload();
        proxyServer.getChannelRegistrar().register(channel);
        proxyServer.getEventManager().register(this, new AlertsListener());
        alertsManager = new AlertManager();
        proxyServer.getCommandManager().register("lpxproxy", new ProxyLPXCommand(), "proxylpx");
    }

    public static ProxyLPX getInstance() {
        return instance;
    }
    public Logger getLogger() {
        return logger;
    }
    public ProxyServer getProxyServer(){
        return proxyServer;
    }
    public YamlDocument getConfig(){
        return config;
    }
    public ChannelIdentifier getChannel(){
        return channel;
    }
    public AlertManager getAlertsManager() {
        return alertsManager;
    }
    public ILPXPlayer.PlayerManager getPlayerManager() {
        return playerManager;
    }
}
