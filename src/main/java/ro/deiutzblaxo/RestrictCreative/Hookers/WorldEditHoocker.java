package ro.deiutzblaxo.RestrictCreative.Hookers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;

import ro.deiutzblaxo.RestrictCreative.Main;
import ro.deiutzblaxo.RestrictCreative.config.enums.MessageEnum;

public class WorldEditHoocker {


	public static void changeMark(Main plugin, Player p, boolean set) {
		LocalSession player = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(p));

		try {



			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable(){
				@Override
				public void run() {
					try {

					} catch (Error e) {
						plugin.getServer().getConsoleSender().sendMessage(
								"[RestrictCreative] [ERROR] You need to have WorldEdit version 7.1+ to work properly. May result in crash of the server or not working.");
					}

					World world = player.getSelectionWorld();

					Region region = null;
					try {
						region = player.getSelection(player.getSelectionWorld());
					} catch (Exception e) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								plugin.getConfigManager().getStringValue(MessageEnum.WORLDEDIT_AREA_NOT_SELECTED)));
					} finally {
						if (region == null) {
							return;
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									plugin.getConfigManager().getStringValue(MessageEnum.WORLDEDIT_AREA_NOT_SELECTED)));
						}
					}
					int i = 0;
					for (BlockVector3 block : region) {



						Location l = BukkitAdapter.adapt(BukkitAdapter.adapt(world), block);
						if (!set) {
							if (plugin.getMark().isMarked(l)) {
								if (!world.getBlock(block).getBlockType().getMaterial().isAir()) {
									plugin.getMark().removeMark(l);
									i++;

								}
							}
						} else {
							if (!plugin.getMark().isMarked(l)) {
								if (!world.getBlock(block).getBlockType().getMaterial().isAir()) {
									plugin.getMark().setMark(l);
									i++;

								}
							}
						}
					}
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							plugin.getConfigManager().getStringValue(MessageEnum.BULK_MARK_FINISH)
							.replaceAll("%blocks%", "" + i)));

				}


			});


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
