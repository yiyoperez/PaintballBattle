package pb.ajneb97.listeners;

public class PerkListeners {

    //        if (nuke) {
//            String equipoAtacanteName = config.getString("teams." + teamAtacante.getTeamName() + ".name");
//            String targetTeamName = config.getString("teams." + targetTeam.getTeamName() + ".name");
//            for (PaintballPlayer j : game.getPlayers()) {
//                if (!j.getPlayer().getName().equals(killer.getPlayer().getName())) {
//                    j.getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("nukeKillMessage").replace("%team_player1%", targetTeamName)
//                            .replace("%player1%", dead.getPlayer().getName()).replace("%team_player2%", equipoAtacanteName)
//                            .replace("%player2%", killer.getPlayer().getName())));
//                }
//            }
//        }

    //        if (!nuke) {
//            separados = config.getString("killSound").split(";");
//            try {
//                Sound sound = Sound.valueOf(separados[0]);
//                killer.getPlayer().playSound(killer.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//            } catch (Exception ex) {
//                Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
//            }
//        }

    //    public static void crearItemKillstreaks(PaintballPlayer jugador, YamlDocument config) {
//        if (config.getString("killstreaks_item_enabled").equals("true")) {
//            int coins = jugador.getCoins();
//            ItemStack item = UtilidadesItems.crearItem(config, "killstreaks_item");
//            ItemMeta meta = item.getItemMeta();
//            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("killstreaks_item.name").replace("%amount%", coins + "")));
//            item.setItemMeta(meta);
//            if (coins <= 1) {
//                item.setAmount(1);
//            } else if (coins >= 64) {
//                item.setAmount(64);
//            } else {
//                item.setAmount(coins);
//            }
//            jugador.getPlayer().getInventory().setItem(8, item);
//        }
//    }

    //crearItemKillstreaks(killer, config);
}
