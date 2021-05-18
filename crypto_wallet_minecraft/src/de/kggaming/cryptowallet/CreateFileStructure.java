package de.kggaming.cryptowallet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CreateFileStructure {
	
	
	public boolean createDirStructure() {
		
		File f = new File("plugins/cryptowallet");
		
		if (f.mkdir() ) {
			System.out.println("Initial FileStructure created.");
			createJsonFile(f);
			return true;
		} else {
			if (!f.exists()) {
				System.out.println("Could not create FileStructure.");
				return false;
			}
		return true;
		}
	}
	
	public boolean createJsonFile(File f) {
		try {
			File j = new File(f.getPath()+"/players.json");
			if (j.createNewFile()) {
				System.out.println("Initial JSON Player file created.");
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				
	    		String fileName = "plugins/cryptowallet/players.json";
	    		FileWriter write = new FileWriter(fileName);
	    		BufferedWriter writer = new BufferedWriter(write);
	    		
	    		JsonObject players = new JsonObject();
				JsonArray playerArray = new JsonArray();
				
				players.add("players", playerArray);
				
				gson.toJson(players, writer);
	    		writer.flush();
	    		writer.close();
	    		
	    		System.out.println("Initial JSON Structure created.");
			
				return true;
			} else {
				if (!j.exists()) {
					System.out.println("Could not create JSON file");
					return false;
				}
			return true;
			}
		} catch (Exception err) {
			System.out.println(err.toString());
			return false;
		}
	}
}
