package pb.ajneb97;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pb.ajneb97.api.ExpansionPaintballBattle;
import pb.ajneb97.api.PaintballAPI;
import pb.ajneb97.juego.GameEdit;
import pb.ajneb97.listeners.game.PartidaListener;
import pb.ajneb97.listeners.game.PartidaListenerNew;
import pb.ajneb97.listeners.shop.ShopListeners;
import pb.ajneb97.listeners.signs.SignsListener;
import pb.ajneb97.managers.game.GameManager;
import pb.ajneb97.managers.inventory.InventarioAdmin;
import pb.ajneb97.managers.inventory.InventarioHats;
import pb.ajneb97.managers.perks.CooldownKillstreaksActionbar;
import pb.ajneb97.managers.shop.ShopManager;
import pb.ajneb97.managers.signs.SignManager;
import pb.ajneb97.services.CommandService;
import pb.ajneb97.utils.NMSUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public final class PaintballBattle extends JavaPlugin {

    public String version = getDescription().getVersion();
    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&b&lPaintball Battle&8] ");

    private YamlDocument shopDocument;
    private YamlDocument arenaDocument;
    private YamlDocument configDocument;
    private YamlDocument messagesDocument;

    private GameManager gameManager;
    private ShopManager shopManager;
    private SignManager signManager;

    private CommandService commandService;

    private GameEdit gameEdit;
    private static Economy econ;
    private PaintballAPI API;

    public void onEnable() {

        createConfigFiles();

        gameManager = new GameManager(this);
        shopManager = new ShopManager(this);
        signManager = new SignManager(this);

        setupEconomy();
        registerEvents();

        //Todo Player data manager
//        registerPlayers();
        gameManager.cargarPartidas();
        signManager.loadSavedSigns();

        signManager.runTask(this);

        this.commandService = new CommandService(this);
        this.commandService.start();

        //Todo Player data manager
//        cargarJugadores();

        CooldownKillstreaksActionbar c = new CooldownKillstreaksActionbar(this);
        c.crearActionbars();

        API = new PaintballAPI(this);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ExpansionPaintballBattle(this).register();
        }

        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Has been enabled!");
        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + "Thanks for using my plugin!  " + ChatColor.WHITE + "~Ajneb97");
    }

    @Override
    public void onDisable() {
        commandService.finish();

        gameManager.shutdownGames();
        gameManager.guardarPartidas();

        signManager.saveLoadedSigns();
        signManager.cancelTask();

        //Todo Player data manager
//        guardarJugadores();

        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + "Has been disabled! " + ChatColor.WHITE + "Version: " + version);
    }

    private void createConfigFiles() {
        try {
            this.arenaDocument = YamlDocument.create(new File(getDataFolder(), "arenas.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.configDocument = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.messagesDocument = YamlDocument.create(new File(getDataFolder(), "messages.yml"), getResource("messages.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("lang-version")).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean firstTimeShop = !new File(getDataFolder() + File.separator + "shop").exists();
        if (firstTimeShop) {
            getLogger().info("Creating default shop configuration.");
            InputStream defaultShop = NMSUtils.isOldVersion() ? getResource("legacyshop.yml") : getResource("shop.yml");
            try {
                YamlDocument defaultConfig = YamlDocument.create(new File(getDataFolder(), "shop.yml"), defaultShop);

                defaultConfig.update();
                defaultConfig.save();
                this.shopDocument = defaultConfig;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                this.shopDocument = YamlDocument.create(new File(getDataFolder(), "shop.yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public PaintballAPI getAPI() {
        return API;
    }

    public void setPartidaEditando(GameEdit p) {
        this.gameEdit = p;
    }

    public void removerPartidaEditando() {
        this.gameEdit = null;
    }

    public GameEdit getPartidaEditando() {
        return this.gameEdit;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PartidaListener(this), this);
        if (NMSUtils.isNewVersion()) {
            pm.registerEvents(new PartidaListenerNew(this), this);
        }
        pm.registerEvents(new SignsListener(this), this);
        pm.registerEvents(new InventarioAdmin(this), this);
        pm.registerEvents(new ShopListeners(this), this);
        pm.registerEvents(new InventarioHats(this), this);
    }

    public Economy getEconomy() {
        return econ;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public SignManager getSignManager() {
        return signManager;
    }

    public YamlDocument getShopDocument() {
        return shopDocument;
    }

    public YamlDocument getArenaDocument() {
        return arenaDocument;
    }

    public YamlDocument getConfigDocument() {
        return configDocument;
    }

    public YamlDocument getMessagesDocument() {
        return messagesDocument;
    }
}
