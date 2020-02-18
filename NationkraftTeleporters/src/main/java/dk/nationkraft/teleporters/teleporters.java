package dk.nationkraft.teleporters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class teleporters extends JavaPlugin{
	
	public void onEnable(){
		getLogger().info("NKT enabled");
	}
	

	public void onDisable(){
		getLogger().info("NKT disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("nkt")) {
		    sender.sendMessage(ChatColor.YELLOW + "------------------Science V1.0.0------------------");
		    sender.sendMessage(ChatColor.AQUA + "By benjamin1313");
		    sender.sendMessage("");
		    sender.sendMessage(ChatColor.AQUA + "This plugin adds teleporters to Nationkraft.");
			return true;
		}
		return false; 
	}
}