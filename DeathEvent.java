package me.OnlineMetlesley.com.SpectatorRespawn;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import com.connorlinfoot.titleapi.TitleAPI;

import de.inventivegames.particle.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

public class DeathEvent implements Listener{
	public HashMap<String, Long> cooldown = new HashMap<String, Long>();
	public Plugin instance;

	public DeathEvent() {
		// TODO Auto-generated constructor stub
	}


	public void function() {
		Main plugin = (Main) Main.instance;
		plugin.getServer();
	}
	List<String> command = Main.instance.getConfig().getStringList("message");

	String deathmessage = Main.instance.getConfig().getString("DeathMessage");
	String AutoRespawnMessage = Main.instance.getConfig().getString("AutoRespawn.AutoRespawnMessage");
	String respawnMessage = Main.instance.getConfig().getString("respawnMessage");
	int Respawncooldown = Main.instance.getConfig().getInt("AutoRespawn.time");
	String respawnedtitle = Main.instance.getConfig().getString("respawnedtitle");
	String respawnedsubtitle = Main.instance.getConfig().getString("respawnedsubtitle");
	String BroadcastMessage = Main.instance.getConfig().getString("BroadcastMessage");
	@EventHandler
	public void damage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			Location Loc = event.getEntity().getLocation();
			BroadcastMessage.replace("%player%", player.getName());
			if (!Main.instance.getConfig().getBoolean("autorespawn")) {
				if (((player.getHealth() - event.getFinalDamage()) <= 0) && event.getEntity() instanceof Player) {
					event.setCancelled(true);
					player.setGameMode(GameMode.SPECTATOR);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', deathmessage.replace("%player%", player.getName())));
					player.spigot().playEffect(player.getLocation(), Effect.HEART, 0, 0, 0, 0, 0, (float) 0, 5, 30);
				}
				if (Main.instance.getConfig().getBoolean("Broadcast")) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.instance.getConfig().getString("BroadcastMessage")));
				}
			}

			if (Main.instance.getConfig().getBoolean("autorespawn")) {
				if (((player.getHealth() - event.getFinalDamage()) <= 0) && event.getEntity() instanceof Player) {
					if (Main.instance.getConfig().getBoolean("HeartEffect")) {
					player.spigot().playEffect(player.getLocation(), Effect.HEART, 0, 0, 0, 0, 0, (float) 0, 5, 30);
					}
					cooldown.put(player.getName(), System.currentTimeMillis());
					player.setGameMode(GameMode.SPECTATOR);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', AutoRespawnMessage.replace("%player%", player.getName())));
					event.setCancelled(true);
					if (Main.instance.getConfig().getBoolean("Broadcast")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.instance.getConfig().getString("BroadcastMessage")));
					}
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {

						public void run() {
							if (Main.instance.getConfig().getBoolean("deathcommand") == true) {
								player.setGameMode(GameMode.SURVIVAL);
								player.setHealth(20);
							} else {
								player.setGameMode(GameMode.SURVIVAL);
								player.setHealth(20);
								player.teleport(Loc);
							}
							TitleAPI.sendTitle(player,20,40,20,respawnedtitle,respawnedsubtitle);
						}
					}, Respawncooldown * 20);// 60 L == 3 sec, 20 ticks == 1 sec
				}
			}

			if (Main.instance.getConfig().getBoolean("playsound")) {
				player.playSound(player.getLocation(), Sound.valueOf(Main.instance.getConfig().getString("sound").toUpperCase()), 10F, 100F);
			}
			if (Main.instance.getConfig().getBoolean("deathcommand")) {
				if (((player.getHealth() - event.getFinalDamage()) <= 0) && event.getEntity() instanceof Player) {
					for(String cmd : Main.instance.getConfig().getStringList("deathcommands")) {
						Main.instance.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
					}
				}
			}
		}
	}
}
