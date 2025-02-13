package pb.ajneb97.listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pb.ajneb97.PaintballBattle;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.listeners.customevents.PreJoinGameEvent;
import pb.ajneb97.managers.GameHandler;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.SignManager;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.LocationUtils;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.List;
import java.util.Objects;

public class SignsListener implements Listener {

    @Inject
    private PaintballBattle plugin;
    @Inject
    private GameManager gameManager;
    @Inject
    private SignManager signManager;
    @Inject
    private GameHandler gameHandler;

    @Inject
    @Named("config")
    private YamlDocument config;
    @Inject
    @Named("messages")
    private YamlDocument messages;

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("paintball.admin")) return;

        String line = Objects.requireNonNull(event.getLine(0));
        if (line.equalsIgnoreCase("[Paintball]")) {

            String arena = Objects.requireNonNull(event.getLine(1));
            if (gameManager.gameExists(arena)) {

                Game game = gameManager.getGame(arena);
                String state = switch (game.getState()) {
                    case PLAYING -> messages.getString("signStatusIngame");
                    case STARTING -> messages.getString("signStatusStarting");
                    case WAITING -> messages.getString("signStatusWaiting");
                    case DISABLED -> messages.getString("signStatusDisabled");
                    case ENDING -> messages.getString("signStatusFinishing");
                };

                List<String> signFormat = messages.getStringList("signFormat").stream().limit(4).toList();
                signFormat.forEach(string ->
                        event.setLine(signFormat.indexOf(string), MessageUtils.translateColor(string
                                .replace("%arena%", arena)
                                .replace("%status%", state)
                                .replace("%max_players%", String.valueOf(game.getMaxPlayers()))
                                .replace("%current_players%", String.valueOf(game.getCurrentPlayersSize()))
                        )));
                
                signManager.addSignLocation(game, event.getBlock().getLocation());
                player.sendMessage("Created a new sign selector.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        // If block is a sign return, other event will handle that.
        if (block.getType().name().contains("SIGN")) return;

        signManager.getLocationsSet().forEach(signLocation -> {
            for (Location face : new LocationUtils.LocationFacesBuilder(signLocation).buildHorizontal()) {
                if (blockLocation.equals(face)) {
                    player.sendMessage("Cannot break blocks near!");
                    event.setCancelled(true);
                    return;
                }
            }
        });
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Prevents placing other blocks than signs if near a Selector Sign.
        if (block.getType().name().contains("SIGN")) return;

        Location blockLocation = block.getLocation();
        signManager.getLocationsSet().forEach(signLocation -> {
            for (Location face : new LocationUtils.LocationFacesBuilder(signLocation).buildHorizontal()) {
                if (blockLocation.equals(face)) {
                    player.sendMessage("Cannot place blocks near a selection sign!");
                    event.setCancelled(true);
                    return;
                }
            }
        });
    }

    @EventHandler
    public void onSignBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        // If not selector signs return.
        if (signManager.getLocationMap().isEmpty()) return;

        // If not a sign return.
        if (!block.getType().name().contains("SIGN")) return;

        if (signManager.isSignLocation(blockLocation)) {

            // Needs permission and shift to break.
            if (!player.isSneaking() || !player.hasPermission("paintball.admin")) {
                event.setCancelled(true);
                return;
            }

            signManager.removeLocation(blockLocation);
            player.sendMessage("Removed sign selector.");
        }
    }

    @EventHandler
    public void onSignClickJoin(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!block.getType().name().contains("SIGN")) return;

        if (gameManager.isPlaying(player)) {
            player.sendMessage(MessageUtils.translateColor(messages.getString("alreadyInArena")));
            event.setCancelled(true);
            return;
        }

        signManager.getLocationMap().forEach((arena, locationSet) ->
                locationSet.stream()
                        // Filter if clicked block is a selector sign
                        .filter(location -> location.equals(block.getLocation()))
                        // Find first
                        .findFirst()
                        // If present
                        .ifPresent(location -> {
                            Game game = gameManager.getGame(arena);
                            new PreJoinGameEvent(game, player).call();
                            event.setCancelled(true);
                        }));

    }
}
