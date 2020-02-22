package dk.nationkraft.teleporters;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeleporterTool implements Listener{
	
	public HashMap<Player, Integer[]> toolUsers = new HashMap<>();

	static String toolName = "Magisk Teleporter Tryllestav";
	static String toolLore1 = "En magisk tryllestav som kan bøjge tid og rum.";
	static String toolLore2 = "Hvilket tillader brugeren at lave teleporters";
	
	
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
	
	
	// holder øje med om spiller bruger tool og laver en portal hvis de gør. 
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
			Player player = event.getPlayer();
			
			if(Material.GOLD_BLOCK == event.getClickedBlock().getType() && player.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_HOE) {
				
				if(!toolUsers.containsKey(player)) {toolUsers.put(player, new Integer[] {0, 0, 0, 0,});} //stage, pos1x, pos1y pos1z
				
				if(toolUsers.get(player)[0]==0) {
					
					
					int posx = event.getClickedBlock().getX();
					int posy = event.getClickedBlock().getY();
					int posz = event.getClickedBlock().getZ();
					
					if(FileManager.isTeleporter(new Integer[] {posx, posy, posz})) {
						player.sendMessage(ChatColor.DARK_RED + "Denne block er allerede linket til en anden teleporter.");
					}else {
						player.sendMessage(ChatColor.AQUA + "Første position til Teleporter sat.");
						toolUsers.replace(player, new Integer[]{1, posx, posy, posz});
					}
					
				}else if(toolUsers.get(player)[0]==1) {
					
					player.sendMessage(ChatColor.AQUA + "Anden position til Teleporter sat.");
					
					int posx = event.getClickedBlock().getX();
					int posy = event.getClickedBlock().getY();
					int posz = event.getClickedBlock().getZ();
					
					Integer[] pos1 = toolUsers.get(player);
					
					// Kan ikke bruges i nether og end pt. det vil break pluginet
					Block block1 = Bukkit.getServer().getWorld("world").getBlockAt(pos1[1], pos1[2]+1, pos1[3]);
					Block block2 = Bukkit.getServer().getWorld("world").getBlockAt(posx, posy+1, posz);
					block1.setType(Material.LIME_CARPET);
					block2.setType(Material.LIME_CARPET);
					
					 
					FileManager.addTeleporter(new Integer[]{pos1[1], pos1[2], pos1[3]}, new Integer[]{posx, posy, posz});
					FileManager.addTeleporter(new Integer[]{posx, posy, posz}, new Integer[]{pos1[1], pos1[2], pos1[3]});

					toolUsers.replace(player, new Integer[]{0, 0 ,0 ,0});
					
					player.sendMessage(ChatColor.AQUA + "Du har nu succesfuld linket to teleportere.");
				}
			}
		}
	}
}
