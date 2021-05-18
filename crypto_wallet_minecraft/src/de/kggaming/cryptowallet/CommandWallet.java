package de.kggaming.cryptowallet;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.milkbowl.vault.economy.Economy;

public class CommandWallet implements CommandExecutor {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	CryptoCurrency cur = new CryptoCurrency();
	Economy econ = main.getEconomy();
	Plugin plugin = main.getPlugin(main.class);
	
	String defaultNoPerm = plugin.getConfig().getString("NoPerm");
	
	
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
        if (sender instanceof Player) {
        	Player player = (Player) sender;
        	try {
        		if (args.length == 0) {
        			if (player.hasPermission("wallet")) {
            			String fileName = "plugins/cryptowallet/players.json";
            			JsonParser parser = new JsonParser();
                	    JsonObject object = gson.fromJson(new FileReader(fileName), JsonObject.class);
                	    JsonArray parsedArray = object.get("players").getAsJsonArray();
                	    
        	    	    for (int i = 0; i < parsedArray.size(); i++) {
        	    	    	if (parsedArray.get(i).getAsJsonObject().get("name").getAsString().contains(player.getName())) {
        	    	    		double btcamount = parsedArray.get(i).getAsJsonObject().get("bitcoin_amount").getAsDouble();
        	    	    		player.sendMessage("You have "+btcamount+" Bitcoin in your Wallet!");
        	    	    		
        	    	    	}
        	    	    }
        			} else {
        				player.sendMessage(defaultNoPerm);
        			}
        		}
        	} catch (Exception err) {
        		System.out.println(err.toString());
        	}
        	
        	if (args.length > 0) {
        		if (args[0].contains("buy")) {
        			if (player.hasPermission("wallet.buy")) {
            			
            			if (args.length < 2) {
            				player.sendMessage("/wallet buy [amount of money] - you need to add the amount of money!");
            			}
            			
            			if (args.length == 2) {
            				try {
            					float buyAmount = Float.parseFloat(args[1]);
            					
            	    			String fileName = "plugins/cryptowallet/players.json";
            	        	    JsonObject object = gson.fromJson(new FileReader(fileName), JsonObject.class);
            	        	    JsonArray parsedArray = object.get("players").getAsJsonArray();
            		    		FileWriter write = new FileWriter(fileName);
            		    		BufferedWriter writer = new BufferedWriter(write);
            		    		float curValueBTC = cur.getCurrency();
            		    		if (buyAmount > (float) econ.getBalance(player)) {
            		    			player.sendMessage("You cannot afford this! Your current Balance: "+econ.getBalance(player));
        				    		gson.toJson(object, writer);
        				    		writer.flush();
    		    	    			writer.close();
    		    	    			return true;
            		    		}
            		    		
            		    		
            		    		float amount = buyAmount / cur.getCurrency();
            		    		
            		    		
            		    		
            	        	    
            		    	    for (int i = 0; i < parsedArray.size(); i++) {
            		    	    	if (parsedArray.get(i).getAsJsonObject().get("name").getAsString().contains(player.getName())) {
            		    	    		Float btcamount = parsedArray.get(i).getAsJsonObject().get("bitcoin_amount").getAsFloat();
            		    	    		parsedArray.get(i).getAsJsonObject().remove("bitcoin_amount");
            		    	    		parsedArray.get(i).getAsJsonObject().addProperty("bitcoin_amount", btcamount+amount);
            		    	    		
            				    		gson.toJson(object, writer);
            				    		writer.flush();
            				    		writer.close();
            				    		econ.withdrawPlayer(player, buyAmount);
            				    		player.sendMessage("Your new Bitcoin Amount: "+(amount+btcamount));
            				    		return true;
            		    	    	}
            		    	    }
            					
            				} catch (Exception e) {
            					System.out.println(e.toString());
            				}
            			}
        			} else {
        				player.sendMessage(defaultNoPerm);
        			}
        		}
        	}
        	
        	if (args.length > 0) {
        		if (args[0].contains("sell")) {
        			if (player.hasPermission("wallet.sell")) {
            			if (args.length < 2) {
            				player.sendMessage("/wallet sell [amount of bitcoin] - you need to add the amount of bitcoin!");
            			}
            			
            			if (args.length == 2) {
            				try {
            					float sellamount = Float.parseFloat(args[1]);
            					
            	    			String fileName = "plugins/cryptowallet/players.json";
            	        	    JsonObject object = gson.fromJson(new FileReader(fileName), JsonObject.class);
            	        	    JsonArray parsedArray = object.get("players").getAsJsonArray();
            		    		FileWriter write = new FileWriter(fileName);
            		    		BufferedWriter writer = new BufferedWriter(write);
            	        	    
            		    	    for (int i = 0; i < parsedArray.size(); i++) {
            		    	    	if (parsedArray.get(i).getAsJsonObject().get("name").getAsString().contains(player.getName())) {
            		    	    		Float btcamount = parsedArray.get(i).getAsJsonObject().get("bitcoin_amount").getAsFloat();
            		    	    		if (btcamount-sellamount < 0) {
            		    	    			player.sendMessage("You cannot sell more then you have!");
                				    		gson.toJson(object, writer);
                				    		writer.flush();
            		    	    			writer.close();
            		    	    			return true;
            		    	    		} else {
                		    	    		parsedArray.get(i).getAsJsonObject().remove("bitcoin_amount");
                		    	    		parsedArray.get(i).getAsJsonObject().addProperty("bitcoin_amount", btcamount-sellamount);
                		    	    		
                				    		gson.toJson(object, writer);
                				    		writer.flush();
                				    		writer.close();
                				    		econ.depositPlayer(player, sellamount*cur.getCurrency());
                				    		
                				    		player.sendMessage("Your new Bitcoin Amount: "+(btcamount-sellamount));
                				    		return true;
            		    	    		}

            		    	    	}
            		    	    }
            					
            				} catch (Exception e) {
            					System.out.println(e.toString());
            				}
            			}
        			} else {
        				player.sendMessage(defaultNoPerm);
        			}
        		}
        	} 
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
	}

}
