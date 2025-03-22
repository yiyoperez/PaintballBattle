package pb.ajneb97.listeners;

public class HatListeners {

    /*if (dead.getSelectedHat().equals("guardian_hat") && dead.isEfectoHatActivado()) {
        return;
    }
        if (dead.getSelectedHat().equals("protector_hat")) {
        Random r = new Random();
        int num = r.nextInt(100);
        if (num >= 80) {
            return;
        }
    }*/

    /*
        if (dead.getSelectedHat().equals("explosive_hat")) {
            Random r = new Random();
            int num = r.nextInt(100);
            if (num >= 80) {
                if (Bukkit.getVersion().contains("1.8")) {
                    dead.getPlayer().getWorld().playEffect(dead.getPlayer().getLocation(), Effect.valueOf("EXPLOSION_LARGE"), 2);
                } else {
                    dead.getPlayer().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, dead.getPlayer().getLocation(), 2);
                }
                separados = config.getString("explosiveHatSound").split(";");
                try {
                    Sound sound = Sound.valueOf(separados[0]);
                    dead.getPlayer().getWorld().playSound(dead.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
                } catch (Exception ex) {
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
                }
                Collection<Entity> entidades = dead.getPlayer().getWorld().getNearbyEntities(dead.getPlayer().getLocation(), 5, 5, 5);
                for (Entity e : entidades) {
                    if (e != null && e.getType().equals(EntityType.PLAYER)) {
                        Player player = (Player) e;
                        PaintballPlayer secondVictimPlayer = game.getJugador(player.getName());
                        if (secondVictimPlayer != null) {
                            PartidaManager.muereJugador(game, dead, secondVictimPlayer, plugin, false, false);
                        }
                    }
                }
            }
        }
*/

    /*if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            if (dead.getSelectedHat().equals("chicken_hat")) {
                dead.getPlayer().getInventory().removeItem(new ItemStack(Material.EGG));
            } else {
                dead.getPlayer().getInventory().removeItem(new ItemStack(Material.SNOWBALL));
            }
        } else {
            if (dead.getSelectedHat().equals("chicken_hat")) {
                dead.getPlayer().getInventory().removeItem(new ItemStack(Material.EGG));
            } else {
                dead.getPlayer().getInventory().removeItem(new ItemStack(Material.valueOf("SNOW_BALL")));
            }
        }*/

    //        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
//                || Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
//                || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
//            if (killer.getSelectedHat().equals("chicken_hat")) {
//                killer.getPlayer().getInventory().addItem(new ItemStack(Material.EGG, snowballs));
//            } else {
//                killer.getPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL, snowballs));
//            }
//
//        } else {
//            if (killer.getSelectedHat().equals("chicken_hat")) {
//                killer.getPlayer().getInventory().addItem(new ItemStack(Material.EGG, snowballs));
//            } else {
//                killer.getPlayer().getInventory().addItem(new ItemStack(Material.valueOf("SNOW_BALL"), snowballs));
//            }
//
//        }
}
