package truelauncherauthconnector.authplugins;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import truelauncherauthconnector.Auth;
import truelauncherauthconnector.Main;
import fr.xephi.authme.api.API;
import fr.xephi.authme.settings.Settings;

public class AuthMeReloaded implements Auth {

	public void doAuth(Main main, String playername, String password) {
		Player player = Bukkit.getPlayerExact(playername);
		if (player == null) {return;}
		if (API.isRegistered(playername)) {
			if (API.checkPassword(playername, password)) {
				API.forceLogin(player);
			} else {
				player.kickPlayer("Неправильный пароль");
			}
		} else {
			String ip = player.getAddress().getAddress().getHostAddress();
			if (API.database.getAllAuthsByIp(ip).size() >= Settings.getmaxRegPerIp) {
				player.kickPlayer("С данного ip адреса уже зарегестрирован аккаунт");
			} else {
				API.registerPlayer(playername, password);
				API.forceLogin(player);
			}
		}
	}

}
