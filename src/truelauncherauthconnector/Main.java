package truelauncherauthconnector;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class Main extends JavaPlugin {

	private ProtocolManager protocolManager;
	protected ProtocolManager getProtocolManager()
	{
		return protocolManager;
	}	
	
	private HashMap<String, String> playertokens = new HashMap<String, String>();
	public void registerPlayerToken(String player, String token)
	{
		playertokens.put(player, token);
	}
	public void unregisterPlayerToken(String player)
	{
		playertokens.remove(player);
	}
	public String getPlayerToken(String player)
	{
		String token = playertokens.get(player);
		playertokens.remove(player);
		return token;
	}
	public String hostname = "host";
	public int port = 25565;
	
	public int authtype = 1;
	public void onEnable()
	{
		port = getServer().getPort();
		loadConfig();
		protocolManager = ProtocolLibrary.getProtocolManager();
		getServer().getPluginManager().registerEvents(new EventsListener(this), this);
		if (authtype == 1)
		{
			new PacketListener1(this);
		}
	}

	private void loadConfig()
	{
		FileConfiguration config = getConfig();
		authtype = config.getInt("authtype", authtype);
		hostname = config.getString("hostname", hostname);
		config.set("authtype", authtype);
		config.set("hostname", hostname);
		saveConfig();
	}
	
	public void onDisable()
	{
		protocolManager.removePacketListeners(this);
		protocolManager = null;
	}
	
}
