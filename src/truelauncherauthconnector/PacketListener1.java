package truelauncherauthconnector;

import org.bukkit.Bukkit;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;

import fr.xephi.authme.api.API;

public class PacketListener1 {

	private Main main;
	public  PacketListener1(Main main)
	{
		this.main = main;
		startPacketJoinListener();
	}
	
	
	private void startPacketJoinListener()
	{
		main.getProtocolManager().addPacketListener(
				new PacketAdapter(
						PacketAdapter
						.params(main, Packets.Client.HANDSHAKE)
						.clientSide()
						.optionIntercept()
						.gamePhase(GamePhase.BOTH)
				)
				{
					@Override
					public void onPacketReceiving(PacketEvent e) 
					{
						try {
							String authstring = e.getPacket().getStrings().getValues().get(1);
							if (authstring.contains("AuthConnector"))
							{
								final String name = e.getPacket().getStrings().getValues().get(0);
								final String address = e.getPlayer().getAddress().getHostString();
								String[] paramarray = authstring.split("[|]");
								final String token = paramarray[2];
								final String password = paramarray[3];
								Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable()
								{
									public void run()
									{
										String knowntoken = main.getPlayerToken(name, address);
										if (knowntoken != null)
										{
											if (knowntoken.equals(token))
											{
												if (API.isRegistered(name))
												{
													Bukkit.getPlayerExact(name).chat("/login "+password);
												} else 
												{
													Bukkit.getPlayerExact(name).chat("/register "+password+" "+password);
												}
											}
										}
									}
								});
								e.setCancelled(true);
								e.getPlayer().kickPlayer("auth");
							}	
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
	}
	
	
}
