package pb.ajneb97.structures;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pb.ajneb97.PaintballBattle;

import java.io.File;
import java.io.IOException;


public class PlayerConfig {

    private FileConfiguration config;
    private File configFile;
    private String filePath;
    private PaintballBattle plugin;

    public PlayerConfig(String filePath, PaintballBattle plugin) {
        this.config = null;
        this.configFile = null;
        this.filePath = filePath;
        this.plugin = plugin;
    }

    public String getPath() {
        return this.filePath;
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadPlayerConfig();
        }
        return this.config;
    }

    public void registerPlayerConfig() {
        configFile = new File(plugin.getDataFolder() + File.separator + "players", filePath);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadPlayerConfig() {
        if (config == null) {
            configFile = new File(plugin.getDataFolder() + File.separator + "players", filePath);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        if (configFile != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(configFile);
            config.setDefaults(defConfig);
        }
    }
}
