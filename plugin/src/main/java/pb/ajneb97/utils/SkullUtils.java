package pb.ajneb97.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import pb.ajneb97.core.logger.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

public class SkullUtils {

    public static ItemStack getCustomSkull(String base64) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        if (base64 == null || base64.isEmpty()) {
            return skull;
        }

        URL urlObject;
        try {
            urlObject = getUrlFromBase64(base64);
        } catch (MalformedURLException e) {
            Logger.info("Error while trying to get textured head: " + e.getMessage(), Logger.LogType.WARNING);
            return skull;
        }

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "paintball_head");
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        Objects.requireNonNull(meta).setOwnerProfile(profile); // Set the owning player of the head to the player profile
        skull.setItemMeta(meta);

        return skull;
    }

    public static URL getUrlFromBase64(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        // We simply remove the "beginning" and "ending" part of the JSON, so we're left with only the URL. You could use a proper
        // JSON parser for this, but that's not worth it. The String will always start exactly with this stuff anyway
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }
}
