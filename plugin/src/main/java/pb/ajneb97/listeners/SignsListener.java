package pb.ajneb97.listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
import pb.ajneb97.core.utils.message.MessageBuilder;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.MessageUtils;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.listeners.customevents.PreJoinGameEvent;
import pb.ajneb97.managers.GameManager;
import pb.ajneb97.managers.SignManager;
import pb.ajneb97.managers.controller.GameController;
import pb.ajneb97.structures.game.Game;
import pb.ajneb97.utils.LocationUtils;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.util.ArrayList;
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
    private GameController gameController;

    @Inject
    @Named("config")
    private YamlDocument config;

    @Inject
    private MessageHandler messageHandler;

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("paintball.admin")) return;

        String line = Objects.requireNonNull(event.getLine(0));
        if (line.equalsIgnoreCase("[Paintball]")) {

            String arena = Objects.requireNonNull(event.getLine(1));
            if (gameManager.gameExists(arena)) {

                Game game = gameManager.getGame(arena);
                String state = gameManager.getState(game);

                List<Placeholder> placeholderList = new ArrayList<>();
                placeholderList.add(new Placeholder("%arena%", arena));
                placeholderList.add(new Placeholder("%status%", state));
                placeholderList.add(new Placeholder("%max_players%", game.getMaxPlayers()));
                placeholderList.add(new Placeholder("%current_players%", game.getCurrentPlayersSize()));

                List<String> signFormat = new MessageBuilder(messageHandler)
                        .withMessage(Messages.SIGN_FORMAT)
                        .withPlaceholders(placeholderList)
                        .buildList();

                signFormat.forEach(string -> event.setLine(signFormat.indexOf(string), string));

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
        if (event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();
        if (block == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (!block.getType().name().contains("SIGN")) return;
        if (!signManager.isSignLocation(block.getLocation())) return;

        Sign sign = (Sign) block.getState();
        String line = MessageUtils.strip(sign.getLine(1));
        if (!gameManager.gameExists(line)) return;

        Game game = gameManager.getGame(line);
        new PreJoinGameEvent(game, player).call();
        event.setCancelled(true);
    }
}
