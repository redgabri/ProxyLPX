package it.redgabri.lpxproxy.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.google.common.io.Resources.getResource;

@Plugin(
        id = "velocity",
        name = "Velocity",
        version = "1.0"
)
public class ProxyLPX {
    private static ProxyLPX instance;
    private Logger logger;
    private ProxyServer proxyServer;
    private Path path;
    private YamlDocument config;

    @Inject
    public ProxyLPX(Logger logger, ProxyServer proxyServer, @DataDirectory Path path) {
        instance = this;
        this.logger = logger;
        this.proxyServer = proxyServer;
        this.path = path;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
        config = YamlDocument.create(new File("config.yml"),
                getResource("config.yml").openStream(),
                GeneralSettings.builder().setKeyFormat(GeneralSettings.KeyFormat.OBJECT).build(),
                LoaderSettings.DEFAULT,
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
        );
        config.update();
        config.reload();
    }

    public static ProxyLPX getInstance() {
        return instance;
    }
    public ProxyServer getProxyServer(){
        return proxyServer;
    }
    public YamlDocument getConfig(){
        return config;
    }
}
