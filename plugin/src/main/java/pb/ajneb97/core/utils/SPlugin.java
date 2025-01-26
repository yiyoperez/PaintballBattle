package pb.ajneb97.core.utils;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import pb.ajneb97.core.logger.Logger;
import revxrsal.zapper.ZapperJavaPlugin;

import java.util.Arrays;

public class SPlugin extends ZapperJavaPlugin {

    private long enableTime;
    private final Logger logger = new Logger(this.getDescription().getPrefix());
    private final PluginManager pluginManager = getServer().getPluginManager();

    protected void preEnable() {
        this.enableTime = System.currentTimeMillis();

        this.logger.log("# ~ # ENABLING PLUGIN # ~ #");
        this.logger.log(getPluginName() + " v" + getVersion(), Logger.LogType.INFO);
    }

    protected void failedEnable() {
        getLogger().severe("Something went wrong while enabling plugin " + getPluginName() + " (v" + getVersion() + ")");
        getLogger().severe("Please notify developer about it, expecting error below.");
    }

    protected void postEnable() {
        this.logger.log("# ~ # PLUGIN ENABLED (" + Math.abs(enableTime - System.currentTimeMillis()) + "ms) # ~ #");
    }

    protected void preDisable() {
        this.enableTime = System.currentTimeMillis();
        this.logger.log("# ~ # DISABLING PLUGIN # ~ #");
        this.logger.log("Since plugin is disabling, I guess this is goodbye.");
    }

    protected void postDisable() {
        this.logger.log(
                "# ~ # PLUGIN DISABLED <&>7(" + Math.abs(enableTime - System.currentTimeMillis()) + "ms) # ~ #");
    }

    protected <T> T getProvider(Class<T> clazz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(clazz);
        if (provider == null) {
            logger.log("Unable to find provider " + clazz.toString(), Logger.LogType.WARNING);
            return null;
        }
        return provider.getProvider() != null ? (T) provider.getProvider() : null;
    }

    public void addListener(Listener listener) {
        pluginManager.registerEvents(listener, this);
    }

    public void addListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(this::addListener);
    }

    public boolean isPluginEnabled(String pluginName) {
        Plugin plugin = getPlugin(pluginName);
        return plugin == null ? false : plugin.isEnabled();
    }

    protected Plugin getPlugin(String pluginName) {
        return pluginManager.getPlugin(pluginName);
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public String getPluginName() {
        return getDescription().getName();
    }

    public String getVersion() {
        return getDescription().getVersion();
    }
}
