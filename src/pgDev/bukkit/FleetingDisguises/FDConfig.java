package pgDev.bukkit.FleetingDisguises;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Properties;

import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

public class FDConfig {
	private Properties properties;
	private final FleetingDisguises plugin;
	public boolean upToDate = true;
	
	// List of Config Options
	HashMap<String, Integer> disguiseLengths = new HashMap<String, Integer>();
	int disguiseCool;
	String undisNotif;
	
	public FDConfig(Properties p, final FleetingDisguises plugin) {
		properties = p;
		this.plugin = plugin;
		
		// Grab values here
		for (DisguiseType disguise : DisguiseType.values()) {
			String mob = disguise.name().toLowerCase();
			disguiseLengths.put(mob, getInt(mob, 60));
		}
		disguiseCool = getInt("disguiseCool", 60);
		undisNotif = getString("undisNotif", "Time's up! You've been undisguised.");
	}
	
	// Value obtaining functions down below
	public int getInt(String label, int thedefault) {
		String value;
        try {
        	value = getString(label);
        	return Integer.parseInt(value);
        } catch (NoSuchElementException e) {
        	return thedefault;
        }
    }
    
    public double getDouble(String label) throws NoSuchElementException {
        String value = getString(label);
        return Double.parseDouble(value);
    }
    
    public File getFile(String label) throws NoSuchElementException {
        String value = getString(label);
        return new File(value);
    }

    public boolean getBoolean(String label, boolean thedefault) {
    	String values;
        try {
        	values = getString(label);
        	return Boolean.valueOf(values).booleanValue();
        } catch (NoSuchElementException e) {
        	return thedefault;
        }
    }
    
    public Color getColor(String label) {
        String value = getString(label);
        Color color = Color.decode(value);
        return color;
    }
    
    public HashSet<String> getSet(String label, String thedefault) {
        String values;
        try {
        	values = getString(label);
        } catch (NoSuchElementException e) {
        	values = thedefault;
        }
        String[] tokens = values.split(",");
        HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < tokens.length; i++) {
            set.add(tokens[i].trim().toLowerCase());
        }
        return set;
    }
    
    public LinkedList<String> getList(String label, String thedefault) {
    	String values;
        try {
        	values = getString(label);
        } catch (NoSuchElementException e) {
        	values = thedefault;
        }
        if(!values.equals("")) {
            String[] tokens = values.split(",");
            LinkedList<String> set = new LinkedList<String>();
            for (int i = 0; i < tokens.length; i++) {
                set.add(tokens[i].trim().toLowerCase());
            }
            return set;
        }else {
        	return new LinkedList<String>();
        }
    }
    
    public String getString(String label) throws NoSuchElementException {
        String value = properties.getProperty(label);
        if (value == null) {
        	upToDate = false;
            throw new NoSuchElementException("Config did not contain: " + label);
        }
        return value;
    }
    
    public String getString(String label, String thedefault) {
    	String value;
    	try {
        	value = getString(label);
        } catch (NoSuchElementException e) {
        	value = thedefault;
        }
        return value;
    }
    
    public String linkedListToString(LinkedList<String> list) {
    	if(list.size() > 0) {
    		String compounded = "";
    		boolean first = true;
        	for (String value : list) {
        		if (first) {
        			compounded = value;
        			first = false;
        		} else {
        			compounded = compounded + "," + value;
        		}
        	}
        	return compounded;
    	}
    	return "";
    }
    
    
    // Config creation method
    public void createConfig() {
    	try {
    		@SuppressWarnings("static-access")
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(plugin.pluginConfigLocation)));
    		out.write("#\r\n");
    		out.write("# FleetingDisguises Configuration\r\n");
    		out.write("#\r\n");
    		out.write("\r\n");
    		out.write("# Disguise Time Limit Length\r\n");
    		out.write("#	Here's the time (in seconds) that each disguise\r\n");
    		out.write("#	lasts.\r\n");
    		for (DisguiseType disguise : DisguiseType.values()) {
    			String mob = disguise.name().toLowerCase();
    			out.write(mob + "=" + disguiseLengths.get(mob) + "\r\n");
    		}
    		out.write("\r\n");
    		out.write("# Disguise Cooldown\r\n");
    		out.write("#	The time (in seconds), after being undisguised,\r\n");
    		out.write("#	that a player must wait before disguising again.\r\n");
    		out.write("disguiseCool=" + disguiseCool + "\r\n");
    		out.write("\r\n");
    		out.write("# Undisguise Notification\r\n");
    		out.write("#	This is the message shown to the player when\r\n");
    		out.write("#	he is undisguised after reaching the time limit.\r\n");
    		out.write("undisNotif=" + undisNotif + "\r\n");
    		out.close();
    	} catch (Exception e) {
    		System.out.println(e);
    		// Not sure what to do? O.o
    	}
    }
}
