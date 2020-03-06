package dk.benjamin1313.teleporters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeleporterManager implements Listener{
	
	public HashMap<Player, Integer[]> toolUsers = new HashMap<>();

	static Material toolType;
	static String toolName;
	static List<String> toolLore;
	
	
	public static void giveTool(Player player) {
		ItemStack item = new ItemStack(toolType);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(toolName);
		ArrayList<String> lore = new ArrayList<String>();
		for (int i = 0; i < toolLore.size(); i++) {
			lore.add(toolLore.get(i));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		player.getInventory().addItem(item);
	}
	
	public static void setToolVars(FileConfiguration config) {
		toolType = Material.getMaterial(config.getString("ToolMaterial").toUpperCase());
		toolName = config.getString("ToolName");
		toolLore = config.getStringList("ToolLore");
	}
	
	
	// holder øje med om spiller bruger tool og laver en portal hvis de gør. 
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
			Player player = event.getPlayer();
			
			// pt vil pluginet ikke virke ordenligt andet en den normale verden så tjekker efter vi er det inden vi gør noget andet.
			
			if(Material.GOLD_BLOCK == event.getClickedBlock().getType() && player.getEquipment().getItemInMainHand().getType() == toolType) {
				
				if(!player.hasPermission("teleporter.createTeleporter")) { // hvis spilleren ikke har perm til at lave en teleporter sker der intet.
					return;
				}
				
				if(!player.getWorld().equals(Bukkit.getServer().getWorld("world"))) {
					player.sendMessage(ChatColor.AQUA + "Teleportere er ikke tilladt i denne verden.");
					return;
				}
				
				if(!toolUsers.containsKey(player)) {toolUsers.put(player, new Integer[] {0, 0, 0, 0,});} //stage, pos1x, pos1y pos1z
				
				if(toolUsers.get(player)[0]==0) {
					
					
					int posx = event.getClickedBlock().getX();
					int posy = event.getClickedBlock().getY();
					int posz = event.getClickedBlock().getZ();
					
					if(TeleporterListManager.isTeleporter(new Integer[] {posx, posy, posz})) {
						player.sendMessage(ChatColor.DARK_RED + "Denne block er allerede linket til en anden teleporter.");
						return;
					}
					
					player.sendMessage(ChatColor.AQUA + "Første position til Teleporter sat.");
					toolUsers.replace(player, new Integer[]{1, posx, posy, posz});
					
				}else if(toolUsers.get(player)[0]==1) {
					
					
					int posx = event.getClickedBlock().getX();
					int posy = event.getClickedBlock().getY();
					int posz = event.getClickedBlock().getZ();
					
					
					if(TeleporterListManager.isTeleporter(new Integer[] {posx, posy, posz})) {
						player.sendMessage(ChatColor.DARK_RED + "Denne block er allerede linket til en anden teleporter.");
						return;
					}
					
					Integer[] pos1 = toolUsers.get(player);
					
					if(posx == pos1[1] && posy == pos1[2] && posz == pos1[3]) {
						player.sendMessage(ChatColor.AQUA + "Du kan ikke linke en teleporter til sig selv.");
						return;
					}
					
					player.sendMessage(ChatColor.AQUA + "Anden position til Teleporter sat.");
					
					
					// Kan ikke bruges i nether og end pt. det vil break pluginet
					Block block1 = Bukkit.getServer().getWorld("world").getBlockAt(pos1[1], pos1[2]+1, pos1[3]);
					Block block2 = Bukkit.getServer().getWorld("world").getBlockAt(posx, posy+1, posz);
					block1.setType(Material.LIME_CARPET);
					block2.setType(Material.LIME_CARPET);
					
					 
					TeleporterListManager.addTeleporter(new Integer[]{pos1[1], pos1[2], pos1[3]}, new Integer[]{posx, posy, posz});
					TeleporterListManager.addTeleporter(new Integer[]{posx, posy, posz}, new Integer[]{pos1[1], pos1[2], pos1[3]});

					toolUsers.replace(player, new Integer[]{0, 0 ,0 ,0});
					
					player.sendMessage(ChatColor.AQUA + "Du har nu succesfuld linket to teleportere.");
				}
			}
		}
	}
	
	
	
    @EventHandler // Holder øje med om spillere ødelægger en teleporter block vis ja så fejl teleporters fra listen.
    public void onBlockBreak(BlockBreakEvent event) {
    	Player player = event.getPlayer();
    	Block block = event.getBlock();
    	World world = block.getWorld();
    	
    	if(block.getBlockData().getMaterial() == Material.LIME_CARPET && TeleporterListManager.isTeleporter(new Integer[] {block.getX(),block.getY()-1,block.getZ()})) {
    		player.sendMessage(ChatColor.AQUA + "Dette er del af en teleporter.");
    		event.setCancelled(true);
    		return;
    	}
    	
    	if(!TeleporterListManager.isTeleporter(new Integer[] {block.getX(),block.getY(),block.getZ()})) {
    		return;
    	}
    	
    	if(!player.hasPermission("teleporter.deleteTeleporter")) {
    		event.setCancelled(true);
    		player.sendMessage(ChatColor.RED + "Du har ikke lov til at ødelægge teleporters.");
			return;
		}
    	
    	if(block.getBlockData().getMaterial() == Material.GOLD_BLOCK) {
    		Integer[] loc = TeleporterListManager.getTeleporterCords(new Integer[] {block.getX(),block.getY(),block.getZ()});
    		TeleporterListManager.deleteTeleportres(new Integer[] {block.getX(),block.getY(),block.getZ()});
    		Bukkit.getServer().getWorld("world").getBlockAt(block.getX(),block.getY()+1,block.getZ()).setType(Material.AIR);
    		Bukkit.getServer().getWorld("world").getBlockAt(loc[0], loc[1]+1, loc[2]).setType(Material.AIR);
    		Bukkit.getServer().getWorld("world").getBlockAt(loc[0], loc[1], loc[2]).setType(Material.AIR);
    		
    		if(player.getGameMode().equals(GameMode.SURVIVAL)) {
    			world.dropItem(block.getLocation(), new ItemStack(Material.GOLD_BLOCK, 1));
    		}
    		
    		player.sendMessage(ChatColor.AQUA + "Teleporter i begge ender er nu ødelagt.");
    		return;
    	}
    }
}
