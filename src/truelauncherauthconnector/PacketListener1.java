package truelauncherauthconnector;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketListener1 {

	private Main main;

	public PacketListener1(Main main) {
		this.main = main;
		startPacketJoinListener();
	}

	private void startPacketJoinListener() {
		main.getProtocolManager().addPacketListener(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Handshake.Client.SET_PROTOCOL)
				.listenerPriority(ListenerPriority.LOWEST)
			) {
				@Override
				public void onPacketReceiving(PacketEvent e) {
					try {
						String name = e.getPacket().getStrings().getValues().get(0);
						if (name.equals("AuthConnector")) {
							// authpacket(nick + token + password)
							String authstring = e.getPacket().getStrings().getValues().get(1);
							String[] paramarray = authstring.split("[|]");
							if (paramarray.length == 3) {
								final String playername = paramarray[0];
								final String token = paramarray[1];
								final String password = paramarray[2];
								String knowntoken = main.removePlayerToken(playername);
								if (knowntoken != null && knowntoken.equals(token)) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(main,
										new Runnable() {
											public void run() {
												AuthMeRecoded.doAuth(main, playername, password);
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
