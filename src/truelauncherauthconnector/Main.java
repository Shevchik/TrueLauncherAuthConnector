package truelauncherauthconnector;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import truelauncherauthconnector.authplugins.Auth;
import truelauncherauthconnector.authplugins.AuthMeRecoded;
import truelauncherauthconnector.authplugins.AuthMeReloaded;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class Main extends JavaPlugin {

	private ProtocolManager protocolManager;

	protected ProtocolManager getProtocolManager() {
		return protocolManager;
	}

	private Auth auth;
	protected Auth getAuth() {
		return auth;
	}

	@Override
	public void onEnable() {
		port = getServer().getPort();
		protocolManager = ProtocolLibrary.getProtocolManager();
		getServer().getPluginManager().registerEvents(new EventsListener(this), this);
		getCommand("authconnector").setExecutor(new Commands(this));
		loadConfig();
	}

	@Override
	public void onDisable() {
		protocolManager.removePacketListeners(this);
		protocolManager = null;
	}

	private ConcurrentHashMap<String, String> playertokens = new ConcurrentHashMap<String, String>();

	public void registerPlayerToken(String player, String token) {
		playertokens.put(player, token);
	}

	public synchronized String getPlayerToken(String player) {
		String token = playertokens.get(player);
		playertokens.remove(player);
		return token;
	}

	public String hostname = "host";
	public int port = 25565;
	public int authtype = 2;
	public int protocolversion = -1;

	public void loadConfig() {
		FileConfiguration config = getConfig();
		authtype = config.getInt("authtype", authtype);
		hostname = config.getString("hostname", hostname);
		protocolversion = config.getInt("protocolversion", protocolversion);
		config.set("authtype", authtype);
		config.set("hostname", hostname);
		config.set("protocolversion", protocolversion);
		protocolManager.removePacketListeners(this);
		if (authtype == 1) {
			new PacketListener1(this);
		} else if (authtype == 2) {
			new PacketListener2(this);
		}
		auth = detectAndHookInstalledAuthPlugin();
		saveConfig();
	}

	private Auth detectAndHookInstalledAuthPlugin() {
		try {
			Class.forName("fr.xephi.authme.api.RecodedAPI", false, getClassLoader());
			return new AuthMeRecoded();
		} catch (ClassNotFoundException e) {
		}
		try {
			Class.forName("fr.xephi.authme.api.API", false, getClassLoader());
			return new AuthMeReloaded();
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

}
