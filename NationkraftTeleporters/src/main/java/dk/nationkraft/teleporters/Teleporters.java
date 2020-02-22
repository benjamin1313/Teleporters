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
		getServer().getPluginManager().registerEvents(new TeleporterTool(), this);
		getServer().getPluginManager().registerEvents(new TeleportPlayer(), this);
		
		// Generere en tom default config fil
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		// setup for custom configfile med liste over teleporters på severen
		FileManager.setupTeleporterList();
		
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
				sender.sendMessage(ChatColor.AQUA + "Lavet af benjamin1313");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.AQUA + "Dette plugin tilføjer teleporters til Nationkraft.");
				sender.sendMessage(ChatColor.AQUA + "brug /nkt help for mere information.");
				return true;
			}
			else {
				if(args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.AQUA + "Velkommen til hjælp sectionen.");
					sender.sendMessage(ChatColor.AQUA + "/nkt help viser dig denne side.");
					sender.sendMessage(ChatColor.AQUA + "/nkt tool giver giver dig en tryllestav som kan lave teleporters.");
					sender.sendMessage(ChatColor.AQUA + "/nkt list hviser dig alle teleporters på Nationkraft.");
					return true;
				}
				else if(args[0].equalsIgnoreCase("tool")) {
					sender.sendMessage(ChatColor.AQUA + "Værseartig en tryllestav til dig.");
					final Player senderPlayer = (sender instanceof Player) ? (Player)sender : null;
					TeleporterTool.giveTool(senderPlayer);
					return true;
				}
				else if(args[0].equalsIgnoreCase("list")) {
					sender.sendMessage(ChatColor.AQUA + "Her er er en liste med alle teleporters på serveren.");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "ugyldig kommando.");
					return true;
				}
			}
		}
		return false; 
	}
}