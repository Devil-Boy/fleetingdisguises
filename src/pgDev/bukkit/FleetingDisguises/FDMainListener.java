package pgDev.bukkit.FleetingDisguises;

import java.util.Date;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerQuitEvent;

import pgDev.bukkit.DisguiseCraft.Disguise.MobType;
import pgDev.bukkit.DisguiseCraft.api.PlayerDisguiseEvent;
import pgDev.bukkit.DisguiseCraft.api.PlayerUndisguiseEvent;

public class FDMainListener implements Listener {
	final FleetingDisguises plugin;
	
	public FDMainListener(final FleetingDisguises plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDisguise(PlayerDisguiseEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();
			if (plugin.undisguiseTimers.containsKey(player.getName())) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You are still disguised.");
			} else if (!plugin.hasPermissions(player, "fleetingdisguises.cooldown.exempt") &&
				plugin.coolDB.containsKey(player.getName()) &&
				(new Date()).getTime() <= plugin.coolDB.get(player.getName()) + plugin.pluginSettings.disguiseCool * 1000) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You must wait " + ((plugin.coolDB.get(player.getName()) + plugin.pluginSettings.disguiseCool * 1000 - (new Date()).getTime()) / 1000) + " seconds before disguising again.");
			} else {
				if (!plugin.hasPermissions(player, "fleetingdisguises.timelimit.exempt")) {
					Timer t = new Timer();
					if (event.getDisguise().isPlayer()) {
						t.schedule(new UndisguiseTimer(player, plugin), plugin.pluginSettings.disguiseLengths.get("player") * 1000);
					} else {
						t.schedule(new UndisguiseTimer(player, plugin), plugin.pluginSettings.disguiseLengths.get(event.getDisguise().mob.name().toLowerCase()) * 1000);
					}
					plugin.undisguiseTimers.put(player.getName(), t);
				}
			}
		}
	}
	
	@EventHandler
	public void onUndisguise(PlayerUndisguiseEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();
			if (plugin.undisguiseTimers.containsKey(player.getName())) {
				plugin.undisguiseTimers.get(player.getName()).cancel();
				plugin.undisguiseTimers.remove(player.getName());
			}
			
			plugin.coolDB.put(player.getName(), (new Date()).getTime());
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (plugin.undisguiseTimers.containsKey(player.getName())) {
			plugin.undisguiseTimers.get(player.getName()).cancel();
			plugin.undisguiseTimers.remove(player.getName());
		}
		
		if (plugin.dcAPI.isDisguised(player)) {
			plugin.coolDB.put(player.getName(), (new Date()).getTime());
		}
	}
}
