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

    //REMAINING STUFF IS MORE RELATED TO PERKS

//
//
//    @EventHandler
//    public void clickearItemKillstreak(PlayerInteractEvent event) {
//        Player jugador = event.getPlayer();
//        Game partida = gameManager.getPlayerGame(jugador.getName());
//        if (partida != null) {
//            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
//                if (event.getItem() != null && event.getItem().hasItemMeta()) {
//                    if (jugador.getInventory().getHeldItemSlot() == 8 && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
//                        if (config.getString("killstreaks_item_enabled").equals("true")) {
//                            if (partida.getState().equals(GameState.PLAYING)) {
//                                event.setCancelled(true);
//                                Inventory inv = Bukkit.createInventory(null, 18, MessageUtils.translateColor(config.getString("killstreaks_inventory_title")));
//                                jugador.openInventory(inv);
//                                InventarioKillstreaks i = new InventarioKillstreaks(plugin);
//                                i.actualizarInventario(jugador, partida);
//                            }
//                        }
//                    }
//
//                }
//            }
//        }
//    }
//
//    @EventHandler
//    public void alShiftear(PlayerToggleSneakEvent event) {
//        if (event.isSneaking()) {
//            Player jugador = event.getPlayer();
//            Game partida = gameManager.getPlayerGame(jugador.getName());
//            if (partida != null && partida.getState().equals(GameState.PLAYING)) {
//                PaintballPlayer j = partida.getJugador(jugador.getName());
//                String hat = j.getSelectedHat();
//                if (hat.equals("guardian_hat") || hat.equals("jump_hat")) {
//                    if (!j.isEfectoHatEnCooldown()) {
//                        int duration = Integer.valueOf(config.getString("hats_items." + hat + ".duration"));
//                        int cooldown = Integer.valueOf(config.getString("hats_items." + hat + ".cooldown"));
//                        if (hat.equals("jump_hat")) {
//                            jugador.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * duration, 1, false, false));
//                        } else if (hat.equals("guardian_hat")) {
//                            jugador.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * duration, 2, false, false));
//                            CooldownHats c = new CooldownHats(plugin);
//                            c.durationHat(j, partida, duration);
//                        }
//                        j.setEfectoHatActivado(true);
//                        j.setEfectoHatEnCooldown(true);
//                        j.getPlayer().sendMessage(MessageUtils.translateColor(messages.getString("hatAbilityActivated")));
//                        String[] separados = config.getString("hatAbilityActivatedSound").split(";");
//                        try {
//                            Sound sound = Sound.valueOf(separados[0]);
//                            j.getPlayer().playSound(j.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//                        } catch (Exception ex) {
//                            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
//                        }
//                        CooldownHats c = new CooldownHats(plugin);
//                        c.cooldownHat(j, partida, cooldown);
//                    } else {
//                        jugador.sendMessage(MessageUtils.translateColor(messages.getString("hatCooldownError").replace("%time%", j.getTiempoEfectoHat() + "")));
//                    }
//                }
//
//            }
//        }
//    }
//
//    @EventHandler
//    public void clickInventarioKillstreak(InventoryClickEvent event) {
//        YamlDocument config = plugin.getStreaksDocument();
//        String pathInventory = MessageUtils.translateColor(config.getString("killstreaks_inventory_title"));
//        String pathInventoryM = ChatColor.stripColor(pathInventory);
//        //String prefix = MessageUtils.translateColor( messages.getString("prefix"))+" ";
//        if (ChatColor.stripColor(event.getView().getTitle()).contains(pathInventoryM)) {
//            if (event.getCurrentItem() == null) {
//                event.setCancelled(true);
//                return;
//            }
//            if ((event.getSlotType() == null)) {
//                event.setCancelled(true);
//                return;
//            } else {
//                Player jugador = (Player) event.getWhoClicked();
//                event.setCancelled(true);
//                if (event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
//                    Game partida = gameManager.getPlayerGame(jugador.getName());
//                    PaintballPlayer j = partida.getJugador(jugador.getName());
//                    if (partida != null) {
//                        int slot = event.getSlot();
//                        for (String key : config.getSection("killstreaks_items").getRoutesAsStrings(false)) {
//                            if (slot == Integer.valueOf(config.getString("killstreaks_items." + key + ".slot"))) {
//                                if (j.getKillstreak(key) != null) {
//                                    jugador.sendMessage(MessageUtils.translateColor(messages.getString("killstreakAlreadyActivated")));
//                                    return;
//                                }
//                                if (config.contains("killstreaks_items." + key + ".permission")) {
//                                    if (!jugador.hasPermission(config.getString("killstreaks_items." + key + ".permission"))) {
//                                        jugador.sendMessage(MessageUtils.translateColor(config.getString("killstreaks_items." + key + ".permissionError")));
//                                        return;
//                                    }
//                                }
//
//                                int cost = Integer.valueOf(config.getString("killstreaks_items." + key + ".cost"));
//                                if (j.getCoins() >= cost) {
//                                    if (key.equalsIgnoreCase("nuke") && partida.isNuke()) {
//                                        jugador.sendMessage(MessageUtils.translateColor(messages.getString("nukeError")));
//                                        return;
//                                    }
//
//                                    j.decreaseCoins(cost);
//                                    String name = MessageUtils.translateColor(config.getString("killstreaks_items." + key + ".name"));
//                                    String teamName = config.getString("teams." + partida.getPlayerTeam(jugador.getName()).getName() + ".name");
//
//                                    for (PaintballPlayer player : partida.getPlayers()) {
//                                        if (!player.getPlayer().getName().equals(jugador.getName())) {
//                                            String msg = MessageUtils.translateColor(messages.getString("killstreakActivatedPlayer").replace("%player%", jugador.getName()).replace("%team%", teamName)
//                                                    .replace("%killstreak%", name));
//                                            player.getPlayer().sendMessage(msg);
//                                        }
//                                    }
//
//                                    jugador.sendMessage(MessageUtils.translateColor(messages.getString("killstreakActivated").replace("%killstreak%", name)));
//
//
//                                    if (key.equalsIgnoreCase("strong_arm") || key.equalsIgnoreCase("triple_shoot") || key.equalsIgnoreCase("fury")) {
//                                        int duration = Integer.valueOf(config.getString("killstreaks_items." + key + ".duration"));
//                                        if (j.getSelectedHat().equals("time_hat")) {
//                                            duration = duration + 5;
//                                        }
//                                        Killstreak k = new Killstreak(key, duration);
//                                        j.agregarKillstreak(k);
//                                        CooldownKillstreaks cooldown = new CooldownKillstreaks(plugin);
//                                        cooldown.cooldownKillstreak(j, partida, key, duration);
//                                    } else {
//                                        PartidaManager.killstreakInstantanea(key, jugador, partida, plugin);
//                                    }
//
//                                    String[] separados = config.getString("killstreaks_items." + key + ".activateSound").split(";");
//                                    try {
//                                        Sound sound = Sound.valueOf(separados[0]);
//                                        if (separados.length >= 4) {
//                                            if (separados[3].equalsIgnoreCase("global")) {
//                                                for (PaintballPlayer player : partida.getPlayers()) {
//                                                    player.getPlayer().playSound(player.getPlayer().getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//                                                }
//                                            } else {
//                                                jugador.playSound(jugador.getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//                                            }
//                                        } else {
//                                            jugador.playSound(jugador.getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
//                                        }
//
//                                    } catch (Exception ex) {
//                                        Bukkit.getConsoleSender().sendMessage(MessageUtils.translateColor(PaintballBattle.prefix + "&7Sound Name: &c" + separados[0] + " &7is not valid."));
//                                    }
//
//                                    UtilidadesItems.crearItemKillstreaks(j, config);
//                                } else {
//                                    jugador.sendMessage(MessageUtils.translateColor(messages.getString("noSufficientCoins")));
//                                }
//                                return;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
}
