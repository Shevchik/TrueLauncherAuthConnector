package truelauncherauthconnector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;

public class Commands implements CommandExecutor {

	private Main main;

	public Commands(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				main.loadConfig();
				sender.sendMessage("Config reloaded");
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have enough permissions to do this");
			return true;
		}
		return false;
	}

}
