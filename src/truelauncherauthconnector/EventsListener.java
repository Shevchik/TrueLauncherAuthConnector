package truelauncherauthconnector;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsListener implements Listener {

	private Main main;

	public EventsListener(Main main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent e) {
		String name = e.getPlayer().getName();
		String token = UUID.randomUUID().toString();
		main.registerPlayerToken(name, token);
		// loginsystem string format:
		// AuthConnector|authtype|protocolversion|host|port|nick|token|
		e.getPlayer().sendMessage(ChatColor.GRAY + "AuthConnector|" + main.authtype + "|" + main.protocolversion + "|" + main.hostname + "|" + main.port + "|" + e.getPlayer().getName() + "|" + token);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onQuit(PlayerQuitEvent e) {
		main.getPlayerToken(e.getPlayer().getName());
	}

}
