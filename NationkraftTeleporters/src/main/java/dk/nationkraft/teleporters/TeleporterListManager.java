package dk.nationkraft.teleporters;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TeleporterListManager {
	private static File file;
	private static FileConfiguration customFile;
	
	public static void setupTeleporterList() {
		file = new File(Bukkit.getServer().getPluginManager().getPlugin("NationkraftTeleporters").getDataFolder(), "TeleporterList.yml");
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Der var Problemer med at oprette eller loade Teleporter listen.");
			}
			
		}
		
		customFile = YamlConfiguration.loadConfiguration(file);
	}
	
	
	public static FileConfiguration getTeleporterList() {
		return customFile;
	}
	
	public static void saveTeleporterList() {
		try {
			customFile.save(file);
		} catch (IOException e) {
			System.out.println("Kunne ikke gemme TeleporterListen.");
		}
	}
	
	public static void addTeleporter(Integer[] location, Integer[] destenation) {// X, Y, Z
		customFile.set("teleporterX"+location[0]+"Y"+location[1]+"Z"+location[2]+".destenation.x", destenation[0]);
		customFile.set("teleporterX"+location[0]+"Y"+location[1]+"Z"+location[2]+".destenation.y", destenation[1]);
		customFile.set("teleporterX"+location[0]+"Y"+location[1]+"Z"+location[2]+".destenation.z", destenation[2]);
		saveTeleporterList();
	}
	
	public static boolean isTeleporter(Integer[] location) {
		return customFile.isSet("teleporterX"+location[0]+"Y"+location[1]+"Z"+location[2]);
	}
	
	public static Integer[] getTeleporterCords(Integer[] location) {
		Integer[] destenation = {customFile.getInt("teleporterX"+location[0]+"Y"+location[1]+"Z"+location[2]+".destenation.x"),
				customFile.getInt("teleporterX"+location[0]+"Y"+location[1]+"Z"+location[2]+".destenation.y"),
				customFile.getInt("teleporterX"+location[0]+"Y"+location[1]+"Z"+location[2]+".destenation.z")};
		return destenation;
	}
	
	public static void deleteTeleportres(Integer[] location) { // hvis du har cords på en af teleporterne sletter denne begge dem som er linked sammen
		Integer[] destenation = getTeleporterCords(location);
		customFile.set("teleporterX"+location[0]+"Y"+location[1]+"Z"+location[2], null);
		customFile.set("teleporterX"+destenation[0]+"Y"+destenation[1]+"Z"+destenation[2], null);
		saveTeleporterList();
	}
	
	// TODO: Make them show up on multiple pages.
	public static void showListOfTeleporters(CommandSender sender){
		Set<String> teleporters = customFile.getKeys(false);
		
		int amount = teleporters.size();
		
		sender.sendMessage(ChatColor.AQUA + "Her er er en liste med alle "+amount+" teleporters på serveren.");
		
		for(String teleporter : teleporters) {
			sender.sendMessage(teleporter);
		}
	}
	
}
