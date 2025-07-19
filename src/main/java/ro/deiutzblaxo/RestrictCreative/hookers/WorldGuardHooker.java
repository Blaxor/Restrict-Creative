package ro.deiutzblaxo.RestrictCreative.hookers;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class WorldGuardHooker {


    public static boolean isProtected(Player player, org.bukkit.Location location, World world) {

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        Location loc = new Location(BukkitAdapter.adapt(world), location.getBlockX(), location.getBlockY(),
                location.getBlockZ());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        RegionQuery query = container.createQuery();

        if (!query.testState(loc, localPlayer, Flags.BUILD)) {

            return true;
        }

        return false;
    }

}


