package de.kggaming.cryptowallet;


import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;


public class main extends JavaPlugin{
	
	CreateFileStructure structure = new CreateFileStructure();
	public FileConfiguration config = getConfig();
    private static final Logger log = Logger.getLogger("CryptoWallet");
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;
	
	
    @Override
    public void onEnable() {
    	System.out.println("Enabling CryptoWallet Plugin by KGGaming");
    	structure.createDirStructure();
    	// Register Listeners
    	getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
    	
        // Config creation
        
        config.addDefault("NoPerm", "You don't have permissions to do that!");
        config.addDefault("defaultWalletAmount", 0);
        config.addDefault("welcomeText", "Welcome to the Server");
        config.options().copyDefaults(true);
        saveConfig();
        
        
        
        
    	
    	//Register Commands
    	this.getCommand("wallet").setExecutor(new CommandWallet());
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    	System.out.println("Thank you for using CryptoWallet Plugin by KGGaming");
    	System.out.println("Disabling CryptoWallet Plugin by KGGaming");

    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    public static Economy getEconomy() {
        return econ;
    }
    
    public static Permission getPermissions() {
        return perms;
    }
    
    public static Chat getChat() {
        return chat;
    }

    
}
