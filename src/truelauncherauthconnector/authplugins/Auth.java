package truelauncherauthconnector.authplugins;

import truelauncherauthconnector.Main;

public interface Auth {

	public void doAuth(Main main, String playername, String password);

}
