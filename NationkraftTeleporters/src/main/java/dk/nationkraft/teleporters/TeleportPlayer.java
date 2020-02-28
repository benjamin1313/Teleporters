package dk.nationkraft.teleporters;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportPlayer implements Listener{
	
	// skaffer spillerenes retning sådan de kan blive placeret en block
	// forskudt i den retning, For at påden måde undgå at de bliver 
	// teleporteret frem og tilbage mellem de to teleportere. 
	private String getDirection(Player player) {
	    double rot = (player.getLocation().getYaw() - 90) % 360;
	    if (rot < 0) {
	        rot += 360.0;
	    }
	    if (0 <= rot && rot < 67.5) {
	        return "W";
	    } else if (67.5 <= rot && rot < 157.5) {
	        return "N";
	    } else if (157.5 <= rot && rot <247.5) {
	        return "E";
	    } else if (247.5 <= rot && rot < 360.0) {
	        return "S";
	    } else {
	        return null;
	    }
	}
	
	@EventHandler // holder øje med om spiller går oven på en teleporter
	public void onPlayerMove(PlayerMoveEvent event){
		
		Player player = event.getPlayer();
		
		if(!player.hasPermission("nkt.useTeleporter")) { // hvis spilleren ikke har perm til at bruge en teleporter sker der intet.
			return;
		}
		
		Integer[] playerLoc = {player.getLocation().getBlockX(), player.getLocation().getBlockY()-1, player.getLocation().getBlockZ()};
		if(TeleporterListManager.isTeleporter(playerLoc)) {
			Integer[] des = TeleporterListManager.getTeleporterCords(playerLoc);
			String direction = getDirection(player);
			
			Location destenation = null;
			
			if(direction == "N") {
				destenation = new Location(player.getWorld(), des[0], des[1]+1, des[2]-1);
			}else if (direction == "S") {
				destenation = new Location(player.getWorld(), des[0], des[1]+1, des[2]+1);
			}else if (direction == "E") {
				destenation = new Location(player.getWorld(), des[0]+1, des[1]+1, des[2]);
			}else if (direction == "W") {
				destenation = new Location(player.getWorld(), des[0]-1, des[1]+1, des[2]);
			}
			destenation.setYaw(player.getLocation().getYaw());
            player.teleport(destenation);
		}
	}
}
