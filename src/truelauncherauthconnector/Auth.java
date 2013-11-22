package truelauncherauthconnector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.xephi.authme.api.API;

public class Auth {
	
	public static void doAuth(Main main, String playername, String address, String token, String password)
	{
		String knowntoken = main.getPlayerToken(playername, address);
		if (knowntoken != null && knowntoken.equals(token))
		{
			Player player = Bukkit.getPlayerExact(playername);
			if (API.isRegistered(playername))
			{
				if (API.checkPassword(playername, password))
				{
					API.forceLogin(player);
				} else
				{
					player.kickPlayer("Неправильный пароль");
				}
			} else 
			{
				API.registerPlayer(playername, password);
				API.forceLogin(player);
			}
		}
	}

}
