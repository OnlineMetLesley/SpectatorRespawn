package me.OnlineMetlesley.com.SpectatorRespawn;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
implements Listener
{
	FileConfiguration config = getConfig();
	Server getserver = getServer();
	public static Plugin instance;

	public Plugin getInstance()
	{
		return instance;
	}
	

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		config.options().copyDefaults(true);
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new DeathEvent(), this);

		MetricsLite metrics = new MetricsLite(this);
	}

	

	public static void registerEvents(Plugin plugin, Listener[] listeners) {
		for (Listener listener : listeners)
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
	}
}