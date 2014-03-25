package truelauncherauthconnector;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketListener2 {

	private Main main;

	public PacketListener2(Main main) {
		this.main = main;
		startPacketJoinListener();
	}

	private void startPacketJoinListener() {
		main.getProtocolManager().addPacketListener(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Handshake.Client.SET_PROTOCOL)
				.clientSide()
				.listenerPriority(ListenerPriority.LOWEST)
				.optionManualGamePhase().loginPhase()
				.optionIntercept()
			) {
				@Override
				public void onPacketReceiving(PacketEvent e) {
					try {
						// authpacket(AuthConnector + nick + token +
						// password)
						String authstring = e.getPacket().getStrings().getValues().get(0);
						if (authstring.contains("AuthConnector")) {
							String[] paramarray = authstring.split("[|]");
							if (paramarray.length == 4) {
								final String playername = paramarray[1];
								final String token = paramarray[2];
								final String password = paramarray[3];
								String knowntoken = main.getPlayerToken(playername);
								if (knowntoken != null && knowntoken.equals(token)) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(
										main,
										new Runnable() {
											public void run() {
												main.getAuth().doAuth(main, playername, password);
											}
										}
									);
								}
							}
							e.setCancelled(true);
							e.getPlayer().kickPlayer("auth");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		);
	}

}
