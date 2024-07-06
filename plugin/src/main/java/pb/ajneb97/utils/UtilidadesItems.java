package pb.ajneb97.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import pb.ajneb97.juego.PaintballPlayer;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class UtilidadesItems {

    @SuppressWarnings("deprecation")
    public static ItemStack crearItem(YamlDocument config, String path) {
        String id = config.getString(path + ".item");
        String[] idsplit = new String[2];
        int DataValue = 0;
        ItemStack stack = null;
        if (id.contains(":")) {
            idsplit = id.split(":");
            String stringDataValue = idsplit[1];
            DataValue = Integer.valueOf(stringDataValue);
            Material mat = Material.getMaterial(idsplit[0].toUpperCase());
            stack = new ItemStack(mat, 1, (short) DataValue);
        } else {
            Material mat = Material.getMaterial(id.toUpperCase());
            stack = new ItemStack(mat, 1);
        }
        ItemMeta meta = stack.getItemMeta();
        if (config.contains(path + ".name")) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(path + ".name")));
        }
        if (config.contains(path + ".lore")) {
            List<String> lore = config.getStringList(path + ".lore");
            for (int c = 0; c < lore.size(); c++) {
                lore.set(c, ChatColor.translateAlternateColorCodes('&', lore.get(c)));
            }
            meta.setLore(lore);

        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS);
        if (Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")
                || Bukkit.getVersion().contains("1.18") || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            meta.setUnbreakable(true);
        }
        stack.setItemMeta(meta);

        return stack;
    }

    public static void crearItemKillstreaks(PaintballPlayer jugador, YamlDocument config) {
        if (config.getString("killstreaks_item_enabled").equals("true")) {
            int coins = jugador.getCoins();
            ItemStack item = UtilidadesItems.crearItem(config, "killstreaks_item");
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("killstreaks_item.name").replace("%amount%", coins + "")));
            item.setItemMeta(meta);
            if (coins <= 1) {
                item.setAmount(1);
            } else if (coins >= 64) {
                item.setAmount(64);
            } else {
                item.setAmount(coins);
            }
            jugador.getPlayer().getInventory().setItem(8, item);
        }
    }

    public static ItemStack getCabeza(ItemStack item, String textura) {
        return ItemStackUtils.decode(textura);
    }

    protected ItemStack playerHead() {
        return NMSUtils.isNewVersion() ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(Material.matchMaterial("SKULL"), 1, (byte) 3);
    }

    public ItemStack playerHead(ItemStack itemStack, OfflinePlayer player) {

        String name = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : null;

        if (NMSUtils.isNewVersion()) {
            if (itemStack.getType().equals(Material.PLAYER_HEAD) && name != null && name.startsWith("HEAD")) {
                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                name = name.replace("HEAD", "");
                if (name.length() == 0) {
                    meta.setDisplayName(null);
                } else {
                    meta.setDisplayName(name);
                }
                meta.setOwningPlayer(player);
                itemStack.setItemMeta(meta);
            }
        } else {
            if (itemStack.getType().equals(Material.matchMaterial("SKULL")) && itemStack.getData().getData() == 3 && name != null && name.startsWith("HEAD")) {
                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                name = name.replace("HEAD", "");
                if (name.length() == 0) {
                    meta.setDisplayName(null);
                } else {
                    meta.setDisplayName(name);
                }
                meta.setOwner(player.getName());
                itemStack.setItemMeta(meta);
            }
        }
        return itemStack;
    }

    protected ItemStack createSkull(String url) {

        if (url == null) return null;

        ItemStack head = playerHead();
        if (url.isEmpty()) {
            return head;
        }

        if (NMSUtils.isNewHeadApi()) {
            this.applyTextureUrl(head, url);
        } else {
            this.applyTexture(head, url);
        }

        return head;
    }

    private void applyTextureUrl(ItemStack itemStack, String url) {
        SkullMeta headMeta = (SkullMeta) itemStack.getItemMeta();
        if (headMeta != null) {
            headMeta.setOwnerProfile((PlayerProfile) getProfile(url));
        }
        itemStack.setItemMeta(headMeta);
    }

    private PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID()); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            // urlObject = new URL(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
            urlObject = getUrlFromBase64(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }

    public URL getUrlFromBase64(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        // We simply remove the "beginning" and "ending" part of the JSON, so we're left with only the URL. You could use a proper
        // JSON parser for this, but that's not worth it. The String will always start exactly with this stuff anyway
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }

    protected void applyTexture(ItemStack itemStack, String url) {
        SkullMeta headMeta = (SkullMeta) itemStack.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), "paintball_head");
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        itemStack.setItemMeta(headMeta);
    }
}
