package truelauncherauthconnector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.xephi.authme.api.API;
import fr.xephi.authme.settings.Settings;

public class Auth {
	
	public static void doAuth(Main main, String playername, String token, String password)
	{
		String knowntoken = main.getPlayerToken(playername);
		System.out.println(knowntoken);
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
				String ip = player.getAddress().getAddress().getHostAddress();
				if (API.database.getAllAuthsByIp(ip).size() >= Settings.getmaxRegPerIp)
				{
					player.kickPlayer("С данного ip адреса уже зарегестрирован аккаунт");
				} else
				{
					API.registerPlayer(playername, password);
					API.forceLogin(player);
				}
			}
		}
	}

}
