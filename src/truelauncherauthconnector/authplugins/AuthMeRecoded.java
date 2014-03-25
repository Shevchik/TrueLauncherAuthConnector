package truelauncherauthconnector.authplugins;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.xephi.authme.api.RecodedAPI;
import truelauncherauthconnector.Auth;
import truelauncherauthconnector.Main;

public class AuthMeRecoded implements Auth {

	public void doAuth(Main main, String playername, String password) {
		Player player = Bukkit.getPlayerExact(playername);
		if (player == null) {return;}
		if (RecodedAPI.isRegistered(playername)) {
			if (RecodedAPI.checkPassword(playername, password)) {
				RecodedAPI.forceLogin(player);
			} else {
				player.kickPlayer("Неправильный пароль");
			}
		} else {
			if (RecodedAPI.canRegister(player)) {
				RecodedAPI.registerPlayer(playername, password);
				RecodedAPI.forceLogin(player);
			} else {
				player.kickPlayer("С данного ip адреса уже зарегестрирован аккаунт");
			}
		}
	}

}
