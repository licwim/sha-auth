package com.licwim.shaauth;

import com.dgrissom.webbukkit.WebBukkit;
import com.licwim.shaauth.configs.Config;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ShaAuth extends JavaPlugin {
    public static final String PluginName = "ShaAuth";
    @Getter
    private ShaAuth instance;
    private WebBukkit server;
    private Logger logger = getLogger();

    @Override
    public void onEnable() {
        logger.log(Level.INFO, "Plugin enabled!");

        instance = this;

        // Initialize config.
        Config.init();

        try {
            server = WebBukkit.getInstance();
            int port = Config.get().getInt("port");

            if (port != 0) {
                server.start(port);
            } else {
                logger.log(Level.WARNING, "Server is not running! Incorrect port");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        server.stop();
        logger.log(Level.INFO, "Plugin disabled!");
    }
}
