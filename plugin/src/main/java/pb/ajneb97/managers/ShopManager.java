package pb.ajneb97.managers;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import com.mojang.authlib.GameProfile;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.realized.tokenmanager.api.TokenManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.utils.message.MessageUtils;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.List;
import java.util.Objects;

//TODO NEEDS MENU
public class ShopManager {

    @Inject
    private PaintballBattle plugin;
    @Inject
    @Named("shop")
    private YamlDocument shop;
    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    public void openMainInventory(Player player) {

        Section shopSection = shop.getSection("shop_items");

        Gui inventory = Gui.gui()
                .title(Component.text("GUI Title!"))
                .rows(6)
                .create();
        for (String key : shopSection.getRoutesAsStrings(false)) {
            Section section = shopSection.getSection(key);

            String id = section.getString("item");
            String name = section.getString("name");
            List<String> lore = section.getStringList("lore");

            XMaterial material = XMaterial.matchXMaterial(id).orElse(XMaterial.STONE);

            GuiItem item = ItemBuilder.from(material.parseMaterial())
                    .name(Component.text(name))
                    //.lore(lore)
                    .unbreakable()
                    .flags(ItemFlag.HIDE_ATTRIBUTES)
                    .asGuiItem();

            item.setAction(event -> {
                if (event.getSlot() == section.getInt("slot")) {
                    switch (key) {
                        case "perks_items" -> openPerksInventory(player);
                        case "hats_items" -> openHatsInventory(player);
                    }
                }
            });

            int slot = section.getInt("slot");
            if (slot != -1) {
                inventory.setItem(slot, item);
            }

        }
        inventory.open(player);
    }

    public void openPerksInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, MessageUtils.translateColor(shop.getString("shopPerksInventoryTitle")));

        Gui menu = Gui.gui()
                .title(Component.text(shop.getString("shopPerksInventoryTitle")))
                .rows(6)
                .create();

        Section perkSection = shop.getSection("perks_items");
        //TODO
//        for (String key : perkSection.getRoutesAsStrings(false)) {
//            ItemStack item = UtilidadesItems.crearItem(shop, "perks_items." + key);
//            if (key.equals("coins_info")) {
//                ItemMeta meta = item.getItemMeta();
//                if (config.getString("economy_used").equals("vault")) {
//                    Economy econ = plugin.getEconomy();
//                    meta.setDisplayName(MessageUtils.translateColor(meta.getDisplayName().replace("%coins%", econ.getBalance(player) + "")));
//                } else if (config.getString("economy_used").equals("token_manager")) {
//                    TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
//                    meta.setDisplayName(MessageUtils.translateColor(meta.getDisplayName().replace("%coins%", tokenManager.getTokens(player).orElse(0) + "")));
//                } else {
//                    meta.setDisplayName(MessageUtils.translateColor(meta.getDisplayName().replace("%coins%", PaintballAPI.getCoins(player) + "")));
//                }
//
//                item.setItemMeta(meta);
//            }
//            if (shop.contains("perks_items." + key + ".slot")) {
//                int slot = Integer.valueOf(shop.getString("perks_items." + key + ".slot"));
//                if (slot != -1) {
//                    inv.setItem(slot, item);
//                }
//            }
//
//        }
//        ItemStack item = UtilidadesItems.crearItem(shop, "perks_items.decorative_item");
//        for (int i = 0; i <= 8; i++) {
//            inv.setItem(i, item);
//        }
//        for (int i = 36; i <= 44; i++) {
//            inv.setItem(i, item);
//        }
//
//        int levelExtraLives = PaintballAPI.getPerkLevel(player, "extra_lives");
//        List<String> lista = shop.getStringList("perks_upgrades.extra_lives");
//        for (int i = 0; i < lista.size(); i++) {
//            if (i > levelExtraLives - 1) {
//                item = UtilidadesItems.crearItem(shop, "perks_items.extra_lives_perk_item");
//            } else {
//                item = UtilidadesItems.crearItem(shop, "perks_items.extra_lives_bought_perk_item");
//            }
//            ItemMeta meta = item.getItemMeta();
//            String[] separados = lista.get(i).split(";");
//            meta.setDisplayName(MessageUtils.translateColor(meta.getDisplayName().replace("%name%", separados[2])));
//            List<String> lore = meta.getLore();
//            for (int c = 0; c < lore.size(); c++) {
//                lore.set(c, lore.get(c).replace("%amount%", separados[0]).replace("%cost%", separados[1]));
//            }
//            meta.setLore(lore);
//            item.setItemMeta(meta);
//            inv.setItem(9 + i, item);
//
//            if (i == 8) {
//                break;
//            }
//        }
//
//        int levelInitialKillcoins = PaintballAPI.getPerkLevel(player, "initial_killcoins");
//        lista = shop.getStringList("perks_upgrades.initial_killcoins");
//        for (int i = 0; i < lista.size(); i++) {
//            if (i > levelInitialKillcoins - 1) {
//                item = UtilidadesItems.crearItem(shop, "perks_items.initial_killcoins_perk_item");
//            } else {
//                item = UtilidadesItems.crearItem(shop, "perks_items.initial_killcoins_bought_perk_item");
//            }
//            ItemMeta meta = item.getItemMeta();
//            String[] separados = lista.get(i).split(";");
//            meta.setDisplayName(MessageUtils.translateColor(meta.getDisplayName().replace("%name%", separados[2])));
//            List<String> lore = meta.getLore();
//            for (int c = 0; c < lore.size(); c++) {
//                lore.set(c, lore.get(c).replace("%amount%", separados[0]).replace("%cost%", separados[1]));
//            }
//            meta.setLore(lore);
//            item.setItemMeta(meta);
//            inv.setItem(18 + i, item);
//
//            if (i == 8) {
//                break;
//            }
//        }
//
//        int levelExtraKillcoins = PaintballAPI.getPerkLevel(player, "extra_killcoins");
//        lista = shop.getStringList("perks_upgrades.extra_killcoins");
//        for (int i = 0; i < lista.size(); i++) {
//            if (i > levelExtraKillcoins - 1) {
//                item = UtilidadesItems.crearItem(shop, "perks_items.extra_killcoins_perk_item");
//            } else {
//                item = UtilidadesItems.crearItem(shop, "perks_items.extra_killcoins_bought_perk_item");
//            }
//            ItemMeta meta = item.getItemMeta();
//            String[] separados = lista.get(i).split(";");
//            meta.setDisplayName(MessageUtils.translateColor(meta.getDisplayName().replace("%name%", separados[2])));
//            List<String> lore = meta.getLore();
//            for (int c = 0; c < lore.size(); c++) {
//                lore.set(c, lore.get(c).replace("%amount%", separados[0]).replace("%cost%", separados[1]));
//            }
//            meta.setLore(lore);
//            item.setItemMeta(meta);
//            inv.setItem(27 + i, item);
//
//            if (i == 8) {
//                break;
//            }
//        }

        player.openInventory(inv);
    }

    public void openHatsInventory(Player player) {
        Gui menu = Gui.gui().title(Component.text(shop.getString("shopHatsInventoryTitle"))).rows(6).create();

        String ecoType = config.getString("economy_used");
        Section hatSection = shop.getSection("hats_items");

        for (String key : hatSection.getRoutesAsStrings(false)) {

            Section section = hatSection.getSection(key);
            String id = section.getString("item");
            String name = section.getString("name");
            List<String> lore = section.getStringList("lore");

            ItemStack stack = getItemStack(section, id);
            GuiItem item = ItemBuilder.from(Objects.requireNonNull(stack))
                    .setName(name)
                    .setLore(MessageUtils.translateColor(lore))
                    .unbreakable() // Not sure why?
                    .asGuiItem();

            configureItemsClickAction(player, key, item, ecoType, name, section, messages);

            int slot = section.getInt("slot");
            if (slot != -1) {
                menu.setItem(slot, item);
            }
        }

        menu.open(player);
    }

    private @Nullable ItemStack getItemStack(Section section, String id) {
        ItemStack stack;

        if (section.contains("skull_texture") && !section.getString("skull_texture").isEmpty()) {
            String texture = section.getString("skull_texture");
            //PlayerProfiles.signXSeries(ProfileInputType.BASE64.getProfile())
            GameProfile gameProfile = ProfileInputType.BASE64.getProfile(texture);
            stack = XSkull.createItem().profile(Profileable.of(gameProfile)).apply();
        } else {
            stack = XMaterial.matchXMaterial(id).orElse(XMaterial.STONE).parseItem();
        }
        return stack;
    }

    private void configureItemsClickAction(Player player, String key, GuiItem item, String ecoType, String name, Section section, YamlDocument messages) {
        switch (key) {
            case "go_to_menu" ->
                    item.setAction(event -> ShopManager.this.openMainInventory((Player) event.getWhoClicked()));
            case "coins_info" -> configureCoinsInfo(player, item, ecoType, name);
            default -> configureDefaultItems(player, key, section, item, messages);
        }
    }

    private void configureCoinsInfo(Player player, GuiItem item, String ecoType, String name) {
        String amount;
//        switch (ecoType) {
//            case "vault" -> {
//                Economy econ = plugin.getEconomy();
//                amount = String.valueOf(econ.getBalance(player));
//            }
//            case "token_manager" -> {
//                TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
//                amount = String.valueOf(tokenManager.getTokens(player).orElse(0));
//            }
//            default -> amount = "0";
//        }

        //item.editor().setName(name.replace("%coins%", amount)).done();
    }

    private void configureDefaultItems(Player player, String key, Section section, GuiItem item, YamlDocument messages) {
//        if (PaintballAPI.hasHat(player, key)) {
//            List<String> boughtLore = section.getStringList("bought_lore");
//            //item.editor().setLore(boughtLore).done();
//        }
//
//        item.setAction(event -> {
//            if (PaintballAPI.hasHat(player, key)) {
//                player.sendMessage(MessageUtils.translateColor(messages.getString("hatErrorBought")));
//                return;
//            }
//            handlePurchase(player, section);
//        });
    }

    private void handlePurchase(Player player, Section section) {
        String ecoType = config.getString("economy_used");
        int cost = section.getInt("cost");
        double money;

        switch (ecoType) {
//            case "vault" -> {
//                Economy econ = plugin.getEconomy();
//                money = econ.getBalance(player);
//                if (money < cost) {
//                    player.sendMessage(MessageUtils.translateColor(messages.getString("buyNoSufficientCoins")));
//                    return;
//                }
//                econ.withdrawPlayer(player, cost);
//            }
            case "token_manager" -> {
                TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
                float dineroF = tokenManager.getTokens(player).orElse(0);
                if (dineroF < cost) {
                    player.sendMessage(MessageUtils.translateColor(messages.getString("buyNoSufficientCoins")));
                    return;
                }
                tokenManager.removeTokens(player, cost);
            }
//            default -> {
//                money = PaintballAPI.getCoins(player);
//                if (money < cost) {
//                    player.sendMessage(MessageUtils.translateColor(messages.getString("buyNoSufficientCoins")));
//                    return;
//                }
//                PaintballAPI.removeCoins(player, cost);
//            }
        }
    }
}
