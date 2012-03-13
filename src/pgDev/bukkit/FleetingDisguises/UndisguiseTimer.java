package pgDev.bukkit.FleetingDisguises;

import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UndisguiseTimer extends TimerTask {
	final FleetingDisguises plugin;
	Player player;
	
	public UndisguiseTimer(Player player, final FleetingDisguises plugin) {
		this.plugin = plugin;
		this.player = player;
	}
	
	public void run() {
		plugin.dcAPI.undisguisePlayer(player);
		plugin.undisguiseTimers.remove(player.getName());
		player.sendMessage(ChatColor.RED + plugin.pluginSettings.undisNotif);
	}
}
