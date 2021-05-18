package de.kggaming.cryptowallet;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlayerJoinListener implements Listener{
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	Plugin plugin = main.getPlugin(main.class);
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		try {
				String fileName = "plugins/cryptowallet/players.json";
				User user = new User(event.getPlayer().getName(), (float)plugin.getConfig().getDouble("defaultWalletAmount"));
				JsonParser parser = new JsonParser();
	    	    JsonObject object = gson.fromJson(new FileReader(fileName), JsonObject.class);
	    	    JsonArray parsedArray = object.get("players").getAsJsonArray();
	    	    
	    	    boolean isInJson = false;
	    	    
	    	    for (int i = 0; i < parsedArray.size(); i++) {
	    	    	if (parsedArray.get(i).getAsJsonObject().get("name").getAsString().contains(event.getPlayer().getName())) {
	    	    		isInJson = true;
	    	    		break;
	    	    	} else {
	    	    		isInJson = false;
	    	    	}
	    	    }
	    	    
	    	    
	    	    System.out.println(isInJson);
	    	    if (!isInJson) {
		    		FileWriter write = new FileWriter(fileName);
		    		BufferedWriter writer = new BufferedWriter(write);
		    		
		    		parsedArray.add(parser.parse(gson.toJson(user)).getAsJsonObject());
		    		gson.toJson(object, writer);
		    		writer.flush();
		    		writer.close();
		    		System.out.println("Player: "+event.getPlayer().getName()+" is created.");
		    		event.getPlayer().sendMessage(plugin.getConfig().getString("welcomeText"));
	    	    }
	    	    
	    	    


		} catch (Exception err) {
			System.out.println(err.toString());
		}
	}
	

}
