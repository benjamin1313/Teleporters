package dk.nationkraft.teleporters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dk.nationkraft.teleporters.TeleporterTool;

public class Teleporters extends JavaPlugin{
	
	public void onEnable(){
		getLogger().info("NKT enabled");
	}
	

	public void onDisable(){
		getLogger().info("NKT disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Sorry but this plugin is ment to be used in game.");
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("nkt")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.YELLOW + "------------------Teleporters V1.0.0------------------");
				sender.sendMessage(ChatColor.AQUA + "By benjamin1313");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.AQUA + "This plugin adds teleporters to Nationkraft.");
				sender.sendMessage(ChatColor.AQUA + "Use /nkt help for more information.");
				return true;
			}
			else {
				if(args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.AQUA + "Welcome to the help section.");
					sender.sendMessage(ChatColor.AQUA + "/nkt help will show you this page.");
					sender.sendMessage(ChatColor.AQUA + "/nkt tool gives you a magic wand to create teleporters with.");
					sender.sendMessage(ChatColor.AQUA + "/nkt list shows you all teleporters on the server.");
					return true;
				}
				else if(args[0].equalsIgnoreCase("tool")) {
					sender.sendMessage(ChatColor.AQUA + "Here you go a magic wand for you.");
					final Player senderPlayer = (sender instanceof Player) ? (Player)sender : null;
					TeleporterTool.giveTool(senderPlayer);
					return true;
				}
				else if(args[0].equalsIgnoreCase("list")) {
					sender.sendMessage(ChatColor.AQUA + "This is going to show all sets of teleporters on the server.");
					return true;
				} else {
					sender.sendMessage(ChatColor.AQUA + "Invallind command.");
					return true;
				}
			}
		}
		return false; 
	}
}