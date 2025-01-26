package pb.ajneb97.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.StringJoiner;

public class LocationUtils {

    public static Location[] getFaces(Location start) {
        return new LocationFacesBuilder(start).build();
    }

    public static String serialize(Location location) {
        if (location == null) return "null";
        return new StringJoiner(":")
                .add(location.getWorld().getName())
                .add(String.valueOf(location.getX()))
                .add(String.valueOf(location.getY()))
                .add(String.valueOf(location.getZ()))
                .add(String.valueOf(location.getYaw()))
                .add(String.valueOf(location.getPitch()))
                .toString();
    }

    public static Location deserialize(String source) {
        if (source == null) return null;
        String[] split = source.split(":");

        World world = Bukkit.getWorld(split[0]);
        if (world == null)
            throw new IllegalArgumentException("Location deserialization went wrong, world doesn't exist.");

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    public static String serializeBlockLocation(Location location) {
        if (location == null) return "null";
        return new StringJoiner(":")
                .add(location.getWorld().getName())
                .add(String.valueOf(location.getBlockX()))
                .add(String.valueOf(location.getBlockY()))
                .add(String.valueOf(location.getBlockZ()))
                .toString();
    }

    public static Location deserializeBlockLocation(String source) {
        if (source == null) return null;
        String[] split = source.split(":");

        World world = Bukkit.getWorld(split[0]);
        if (world == null)
            throw new IllegalArgumentException("Location deserialization went wrong, world doesn't exist.");

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    public static boolean isSameBlockLocation(Location l1, Location l2) {
        return ((l1.getBlockX() == l2.getBlockX()) && (l1.getBlockY() == l2.getBlockY()) && (l1.getBlockZ() == l2.getBlockZ()));
    }

    public static class LocationFacesBuilder {
        private final Location start;

        private final Location up;
        private final Location down;
        private final Location east;
        private final Location west;
        private final Location north;
        private final Location south;

        public LocationFacesBuilder(Location start) {
            this.start = start;
            this.up = new Location(start.getWorld(), start.getX(), start.getY() + 1, start.getZ());
            this.down = new Location(start.getWorld(), start.getX(), start.getY() - 1, start.getZ());
            this.east = new Location(start.getWorld(), start.getX() - 1, start.getY(), start.getZ());
            this.west = new Location(start.getWorld(), start.getX() + 1, start.getY(), start.getZ());
            this.north = new Location(start.getWorld(), start.getX(), start.getY(), start.getZ() + 1);
            this.south = new Location(start.getWorld(), start.getX(), start.getY(), start.getZ() - 1);
        }

        public Location getWest() {
            return west;
        }

        public Location getEast() {
            return east;
        }

        public Location getUp() {
            return up;
        }

        public Location getDown() {
            return down;
        }

        public Location getNorth() {
            return north;
        }

        public Location getSouth() {
            return south;
        }

        public Location[] build() {
            return new Location[]{getWest(), getEast(), getUp(), getDown(), getNorth(), getSouth()};
        }

        public Location[] buildHorizontal() {
            return new Location[]{getWest(), getEast(), getNorth(), getSouth()};
        }

        public Location[] buildVertical() {
            return new Location[]{getUp(), getDown()};
        }
    }
}