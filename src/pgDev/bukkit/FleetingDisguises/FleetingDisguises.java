package pgDev.bukkit.FleetingDisguises;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class FleetingDisguises extends JavaPlugin {
	// File Locations
    static String pluginMainDir = "./plugins/FleetingDisguises";
    
    // Permissions support
    static PermissionHandler Permissions;
    
    // DisguiseCraft API
    DisguiseCraftAPI dcAPI;
    
    // Listeners
    FDMainListener mainListener = new FDMainListener(this);
	
	public void onEnable() {
		// Check for the plugin directory (create if it does not exist)
    	File pluginDir = new File(pluginMainDir);
		if(!pluginDir.exists()) {
			boolean dirCreation = pluginDir.mkdirs();
			if (dirCreation) {
				System.out.println("New FleetingDisguises directory created!");
			}
		}
		
		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(mainListener, this);
		
		// Integrations!
        setupPermissions();
        setupDisguiseCraft();
        
        // Output to console
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
	
	public void onDisable() {
		System.out.println("FleetingDisguises disabled!");
	}
	
	// Permissions Methods
    private void setupPermissions() {
        Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (permissions != null) {
                Permissions = ((Permissions)permissions).getHandler();
            } else {
            }
        }
    }
    
    public boolean hasPermissions(Player player, String node) {
        if (Permissions != null) {
        	return Permissions.has(player, node);
        } else {
            return player.hasPermission(node);
        }
    }
    
    // DisguiseCraft API Setup
    private void setupDisguiseCraft() {
    	dcAPI = DisguiseCraft.getAPI();
    }
}