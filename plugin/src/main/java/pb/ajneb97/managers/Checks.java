package pb.ajneb97.managers;


import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import pb.ajneb97.PaintballBattle;

public class Checks {

    //TODO tf does this do?
    public static boolean checkTodo(PaintballBattle plugin, CommandSender jugador) {
        YamlDocument messages = plugin.getMessagesDocument();
        YamlDocument config = plugin.getConfigDocument();
        String nombre = ChatColor.translateAlternateColorCodes('&', messages.getString("prefix")) + " ";
        String mensaje = nombre + messages.getString("materialNameError");

        //Check config.yml
        if (!comprobarMaterial(config.getString("leave_item.item"), jugador, mensaje) &&
                !comprobarMaterial(config.getString("killstreaks_item.item"), jugador, mensaje)
                && !comprobarMaterial(config.getString("play_again_item.item"), jugador, mensaje)) {
            return false;
        }
        for (String key : config.getSection("teams").getRoutesAsStrings(false)) {
            if (!comprobarMaterial(config.getString("teams." + key + ".item"), jugador, mensaje)) {
                return false;
            }
        }

        for (String key : config.getSection("killstreaks_items").getRoutesAsStrings(false)) {
            if (!comprobarMaterial(config.getString("killstreaks_items." + key + ".item"), jugador, mensaje)) {
                return false;
            }
        }
        for (String key : config.getSection("hats_items").getRoutesAsStrings(false)) {
            if (!comprobarMaterial(config.getString("hats_items." + key + ".item"), jugador, mensaje)) {
                return false;
            }
        }
        YamlDocument shop = plugin.getShopDocument();
        for (String key : shop.getSection("shop_items").getRoutesAsStrings(false)) {
            if (!comprobarMaterial(shop.getString("shop_items." + key + ".item"), jugador, mensaje)) {
                return false;
            }
        }
        for (String key : shop.getSection("perks_items").getRoutesAsStrings(false)) {
            if (!comprobarMaterial(shop.getString("perks_items." + key + ".item"), jugador, mensaje)) {
                return false;
            }
        }
        for (String key : shop.getSection("hats_items").getRoutesAsStrings(false)) {
            if (!comprobarMaterial(shop.getString("hats_items." + key + ".item"), jugador, mensaje)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings({"deprecation", "unused"})
    public static boolean comprobarMaterial(String key, CommandSender jugador, String mensaje) {
        try {
            if (key.contains(":")) {
                String[] idsplit = key.split(":");
                String stringDataValue = idsplit[1];
                short DataValue = Short.valueOf(stringDataValue);
                Material mat = Material.getMaterial(idsplit[0].toUpperCase());
                ItemStack item = new ItemStack(mat, 1, (short) DataValue);
            } else {
                ItemStack item = new ItemStack(Material.getMaterial(key), 1);
            }

            return true;
        } catch (Exception e) {
            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replace("%material%", key)));
            return false;
        }
    }

    public static void modificarPath(FileConfiguration config, String path, String idNueva) {
        if (config.contains(path)) {
            config.set(path, idNueva);
        }
    }
}
