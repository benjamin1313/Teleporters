package dk.nationkraft.teleporters;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeleporterTool implements Listener{

	static String toolName = "Magic Teleporter Wand";
	static String toolLore1 = "A magic wand that bends time and space.";
	static String toolLore2 = "Allowing the user to crate teleporters";
	
	
	public static void giveTool(Player player) {
		ItemStack item = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toolName);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(toolLore1);
		lore.add(toolLore2);
		meta.setLore(lore);
		item.setItemMeta(meta);
		player.getInventory().addItem(item);
	}
}
