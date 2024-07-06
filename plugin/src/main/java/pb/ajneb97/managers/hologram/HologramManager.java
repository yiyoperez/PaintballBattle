package pb.ajneb97.managers.hologram;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import pb.ajneb97.PaintballBattle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HologramManager {

    private final PaintballBattle plugin;
    private YamlDocument hologramDocument;
    private ArrayList<TopHologram> topHologramas = new ArrayList<>();
    private TopHologramAdmin hologramasTask;

    public HologramManager(PaintballBattle plugin) {
        this.plugin = plugin;

        hologramasTask = new TopHologramAdmin(plugin);
        hologramasTask.actualizarHologramas();

        try {
            this.hologramDocument = YamlDocument.create(new File(plugin.getDataFolder(), "holograms.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void recargarHologramas() {
        int taskID = hologramasTask.getTaskID();
        Bukkit.getScheduler().cancelTask(taskID);
        hologramasTask = new TopHologramAdmin(plugin);
        hologramasTask.actualizarHologramas();
    }

    public void agregarTopHolograma(TopHologram topHologram) {
        this.topHologramas.add(topHologram);
    }

    public boolean eliminarTopHologama(String nombre) {
        for (int i = 0; i < topHologramas.size(); i++) {
            if (topHologramas.get(i).getName().equals(nombre)) {
                topHologramas.get(i).removeHologram();
                topHologramas.remove(i);
                return true;
            }
        }
        return false;
    }

    public TopHologram getTopHologram(String nombre) {
        for (int i = 0; i < topHologramas.size(); i++) {
            if (topHologramas.get(i).getName().equals(nombre)) {
                return topHologramas.get(i);
            }
        }
        return null;
    }

    public void guardarTopHologramas() {
        hologramDocument.set("Holograms", null);
        for (int i = 0; i < topHologramas.size(); i++) {
            Location l = topHologramas.get(i).getHologram().getLocation();
            String name = topHologramas.get(i).getName();
            String type = topHologramas.get(i).getType();
            String period = topHologramas.get(i).getPeriod();
            hologramDocument.set("Holograms." + name + ".type", type);
            hologramDocument.set("Holograms." + name + ".period", period);
            hologramDocument.set("Holograms." + name + ".x", l.getX() + "");
            hologramDocument.set("Holograms." + name + ".y", topHologramas.get(i).getyOriginal() + "");
            hologramDocument.set("Holograms." + name + ".z", l.getZ() + "");
            hologramDocument.set("Holograms." + name + ".world", l.getWorld().getName());
        }

        try {
            hologramDocument.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cargarTopHologramas() {
        if (hologramDocument.contains("Holograms")) {
            for (String name : hologramDocument.getSection("Holograms").getRoutesAsStrings(false)) {
                String type = hologramDocument.getString("Holograms." + name + ".type");
                double x = Double.valueOf(hologramDocument.getString("Holograms." + name + ".x"));
                double y = Double.valueOf(hologramDocument.getString("Holograms." + name + ".y"));
                double z = Double.valueOf(hologramDocument.getString("Holograms." + name + ".z"));
                World world = Bukkit.getWorld(hologramDocument.getString("Holograms." + name + ".world"));
                Location location = new Location(world, x, y, z);
                String period = "global";
                if (hologramDocument.contains("Holograms." + name + ".period")) {
                    period = hologramDocument.getString("Holograms." + name + ".period");
                }
                TopHologram topHologram = new TopHologram(name, type, location, plugin, period);
                topHologram.spawnHologram(plugin);
                this.agregarTopHolograma(topHologram);
            }
        }
    }

    public ArrayList<TopHologram> getTopHologramas() {
        return this.topHologramas;
    }
}
