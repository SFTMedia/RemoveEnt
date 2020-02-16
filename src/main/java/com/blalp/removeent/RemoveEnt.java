package com.blalp.removeent;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveEnt extends JavaPlugin {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("removeyEnt")&&sender.hasPermission("sftremoveent.remove")){
			if(args.length!=5){
				sender.sendMessage("/removeEnt [x] [y] [z] [radius] [mob (tab helps]");
				return true;
			}
			if(Integer.parseInt(args[3])>100){
				sender.sendMessage("Invalid raduis");
				return true;
			}
			if(sender instanceof Player){
				Player player = ((Player)sender);
				int raduis = Integer.parseInt(args[3]);
				int removeCount=0;
				Location loc = new Location(player.getWorld(), Integer.parseInt(args[0]),Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				ArrayList<Chunk> beLoaded = new ArrayList<>();
				for(int ix=Integer.parseInt(args[0])-raduis;ix<Integer.parseInt(args[0])+raduis;ix++){
					for(int iy=Integer.parseInt(args[0])-raduis;iy<Integer.parseInt(args[1])+raduis;iy++){
						for(int iz=Integer.parseInt(args[0])-raduis;iz<Integer.parseInt(args[2])+raduis;iz++){
							if(!new Location(player.getWorld(), ix, iy, iz).getChunk().isLoaded()){
								new Location(player.getWorld(), ix, iy, iz).getChunk().load();
								beLoaded.add(new Location(player.getWorld(), ix, iy, iz).getChunk());
							}
						}
					}
				}
				for(Entity ent:((Player)sender).getLocation().getWorld().getEntities()){
					if(loc.distance(ent.getLocation())<raduis){
						if(ent.getType().equals(EntityType.valueOf(args[4]))){
							ent.remove();
							removeCount++;
						} else {
							System.out.println(ent.getType().name()+" "+EntityType.valueOf(args[4]).name());
						}
					} else {
						System.out.println(ent.getLocation().distance(loc));
					}
				}
				for(Chunk chunk:beLoaded){
					chunk.unload();
				}
				sender.sendMessage("Removed "+removeCount+" "+args[4]);
			} else {
				sender.sendMessage("not from console u silly from console");
			}
			return true;
		}
		return false;
	}
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    	ArrayList<String> output = new ArrayList<>();
    	if(cmd.getName().equalsIgnoreCase("removeyEnt")){
    		if(args.length==5){
				for(EntityType entType:EntityType.values()){
					if(entType.name().toLowerCase().startsWith(args[4].toLowerCase())){
						output.add(entType.name());
					}
				}
				return output;
    		}
    	}
		return null;
    }
}