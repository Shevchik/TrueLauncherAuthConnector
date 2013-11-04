package truelauncherauthconnector;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class EventsListener implements Listener {

	private Main main;
	public EventsListener(Main main)
	{
		this.main = main;
	}
	
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onJoin(PlayerJoinEvent e)
	{
		String name = e.getPlayer().getName();
		String token = UUID.randomUUID().toString();
		main.registerPlayerToken(name, e.getPlayer().getAddress().getHostString(), token);
		//loginsystem string format: AuthMeSocketLoginSystem|authtype|host|port|nick|token
		e.getPlayer().sendMessage(ChatColor.GRAY+"AuthConnector|"+main.authtype+"|"+main.hostname+"|"+main.port+"|"+e.getPlayer().getName()+"|"+token);
	}
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onQuit(PlayerQuitEvent e)
	{
		main.unregisterPlayerToken(e.getPlayer().getName());
	}
	
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onConsoleCommand(ServerCommandEvent e)
	{
		if (e.getCommand().equalsIgnoreCase("authconnector reload"))
		{
			main.loadConfig();
		}
	}
	
}
