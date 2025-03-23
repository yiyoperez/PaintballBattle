package pb.ajneb97.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pb.ajneb97.core.utils.message.MessageHandler;
import pb.ajneb97.core.utils.message.Placeholder;
import pb.ajneb97.listeners.customevents.GameDeathEvent;
import pb.ajneb97.utils.enums.Messages;
import team.unnamed.inject.Inject;

public class GameDeathListener implements Listener {

    @Inject
    private MessageHandler messageHandler;

    @EventHandler
    public void onPlayerDeath(GameDeathEvent event) {
        Player dead = event.getDead();
        Player killer = event.getKiller();


        ///  DEATH PLAYER

        //dead.increaseDeaths();
        messageHandler.sendMessage(dead, Messages.KILLED_BY, new Placeholder("%player%", killer.getPlayer().getName()));
        //TODO player killedBySound
        //dead.setRecentDeathMillis(System.currentTimeMillis());
        //dead.setLastKilledBy(killer.getPlayer().getName());

        ///  KILLER

        //killer.increaseKills();
        messageHandler.sendMessage(killer, Messages.KILL, new Placeholder("%player%", dead.getPlayer().getName()));

        //TODO: Give player coins per kill
        //  givePlayerCoins(killer);
    }
}
