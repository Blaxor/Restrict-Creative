package ro.deiutzblaxo.RestrictCreative;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.RestrictCreative.Hookers.WorldGuardHooker;

public class InteractionsListener implements Listener {
    protected Main plugin;

    public InteractionsListener(Main main) {
        plugin = main;

    }

    private String BypassDropPermission = "restrictcreative.bypass.drop";
    private String BypassBrakePermission = "restrictcreative.bypass.brake";

    private String BypassChestPermission = "restrictcreative.bypass.chest";
    private String BypassDisabledItemsPermission = "restrictcreative.bypass.disableditems";
    private String BypassPvPPermission = "restrictcreative.bypass.pvp";
    private String BypassPvEPermission = "restrictcreative.bypass.pve";


    // ============= org.bukkit.event.inventory START
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryCreative(InventoryCreativeEvent event) {

        event.setCurrentItem(plugin.getMark().setCreativeItem((Player) event.getWhoClicked(), event.getCurrentItem()));
        event.setCursor(plugin.getMark().setCreativeItem((Player) event.getWhoClicked(), event.getCursor()));
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!plugin.getConfigManager().getConfig().getString("Disabled-Items").isEmpty()) {
            if (!event.getWhoClicked().hasPermission(BypassDisabledItemsPermission)) {
                if (plugin.getConfigManager().getBannedItems().contains(event.getCursor().getType())) {

                    event.getCursor().setType(Material.AIR);
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (plugin.getConfigManager().getConfig().getBoolean("RestrictInventoryPut")) {
            if (!event.getWhoClicked().hasPermission(BypassChestPermission)) {

                for (InventoryType c : InventoryType.values()) {
                    if (event.getInventory().getType() == c) {

                        Inventory inv = event.getClickedInventory();
                        if (event.getClick().isShiftClick()) {
                            if (inv == event.getWhoClicked().getInventory()) {
                                ItemStack item = event.getCurrentItem();
                                if (item != null && (plugin.getMark().isCreativeItem(item))) {
                                    event.setCancelled(true);
                                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            plugin.getConfigManager().getMessage().getString("ErrorInventoryPut")));
                                }
                            }
                        }
                        if (inv != event.getWhoClicked().getInventory()) {
                            ItemStack item = event.getCursor();

                            if (item != null && (plugin.getMark().isCreativeItem(item))) {
                                event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        plugin.getConfigManager().getMessage().getString("ErrorInventoryPut")));
                                event.setCancelled(true);
                            }
                        }
                    }
                }
                if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
                    ItemStack item = event.getCursor();
                    plugin.getMark().setCreativeItem((Player) event.getWhoClicked(), item);
                }
            }
        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            if (e.getEntity() instanceof Player) {

                if (plugin.getConfigManager().getConfig().getBoolean("PvP")) {
                    if (!damager.hasPermission(BypassPvPPermission)) {
                        if (plugin.getMark().isCreativeItem(damager.getInventory().getItemInMainHand())) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            } else {
                if (!damager.hasPermission(BypassPvEPermission)) {
                    if (plugin.getConfigManager().getConfig().getBoolean("PVE")) {
                        if (plugin.getMark().isCreativeItem(damager.getInventory().getItemInMainHand())) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {

        if (plugin.getConfigManager().getConfig().getBoolean("RestrictInventoryPut")) {
            if (!event.getWhoClicked().hasPermission(BypassChestPermission)) {
                ItemStack item = event.getOldCursor();
                for (InventoryType c : InventoryType.values()) {

                    if (event.getInventory().getType() == c) {
                        if (plugin.getMark().isCreativeItem(item)) {
                            int invSize = event.getInventory().getSize();
                            for (int i : event.getRawSlots()) {
                                if (i < invSize) {
                                    event.setCancelled(true);
                                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            plugin.getConfigManager().getMessage().getString("ErrorInventoryPut")));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBrewEvent(BrewEvent e) {

        if (plugin.getMark().isCreativeItem(e.getContents().getIngredient())) {

        }
        if (plugin.getMark().isMarked(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBrewingStandFuel(BrewingStandFuelEvent e) {

        if (plugin.getMark().isCreativeItem(e.getFuel())) {
            e.setCancelled(true);
        }
        if (plugin.getMark().isMarked(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {

        if (plugin.getMark().isCreativeItem(e.getCursor())) {
            e.getWhoClicked().closeInventory();
        }
        if (plugin.getMark().isMarked(e.getClickedInventory().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent e) {

        if (plugin.getMark().isCreativeItem(e.getFuel())) {
            e.setCancelled(true);
        }
        if (plugin.getMark().isMarked(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent e) {

        if (plugin.getMark().isCreativeItem(e.getSource())) {
            e.setCancelled(true);
        }
        if (plugin.getMark().isCreativeItem(e.getResult())) {
            e.setCancelled(true);
        }
        if (plugin.getMark().isMarked(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {

        for (ItemStack i : e.getInventory().getContents()) {
            if (plugin.getMark().isCreativeItem(i)) {
                e.getResult().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent e) {
        for (ItemStack i : e.getInventory().getContents()) {
            if (plugin.getMark().isCreativeItem(i)) {
                e.getView().getPlayer().closeInventory();
            }
        }
    }

    /*
     * without : FurnaceExtractEvent , InventoryClickEvent , InventoryCloseEvent,
     * InventoryDragEvent, InventoryEvent InventoryIteractEvent ,
     * InventoryMoveItemEvent , InventoryOpenEvent, InventoryPickupItemEvent,
     * ============= org.bukkit.event.inventory FINISH
     *
     * https://hub.spigotmc.org/javadocs/spigot/index.html?overview-summary.html
     *
     * Player Drop item event
     */

    @EventHandler
    public void onEnchantItem(EnchantItemEvent e) {
        if (plugin.getMark().isCreativeItem(e.getItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void ExpBottle(ExpBottleEvent e) {
        if (e.getEntityType().equals(EntityType.PLAYER)) {
            Player p = (Player) e.getEntity();
            if (plugin.getMark().isCreativeItem(p.getInventory().getItemInMainHand())) {
                e.setExperience(0);
            }
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        if (plugin.getMark().isCreativeItem(e.getItemInHand()) || e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (e.getItemInHand().getType().equals(Material.TNT)) {
                if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {
                    plugin.getServer().getConsoleSender().sendMessage("is tnt , in creative");
                }
                e.setCancelled(true);
            } else {
                plugin.getMark().setMark(e.getBlockPlaced().getLocation());
                if (plugin.getConfigManager().getConfig().getBoolean("Debug")) {
                    plugin.getServer().getConsoleSender().sendMessage("not tnt , in creative");
                }
            }

        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if (!e.getPlayer().hasPermission(BypassBrakePermission)) {
            if (!plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
                if (plugin.getMark().isMarked(e.getBlock()
                        .getLocation())
                        || plugin.getMark().isCreativeItem(e.getPlayer().getInventory().getItemInMainHand())) {
                    if (plugin.getMark().isMarked(e.getBlock().getLocation())) {

                        plugin.getMark().removeMark(e.getBlock().getLocation());
                        e.setDropItems(false);
                    }
                    e.getBlock().setType(Material.AIR);
                }
                if (e.getBlock().getType() == Material.CHEST

                        || e.getBlock().getType() == Material.DROPPER || e.getBlock().getType() == Material.DISPENSER) {
                    Chest chest = (Chest) e.getBlock().getState();
                    for (ItemStack t : chest.getBlockInventory().getContents()) {
                        if (plugin.getMark().isCreativeItem(t)) {
                            t.setType(Material.AIR);
                        }
                    }
                }
            } else {
                if (plugin.getMark().isMarked(e.getBlock()
                        .getLocation())
                        || plugin.getMark().isCreativeItem(e.getPlayer().getInventory().getItemInMainHand())) {
                    if (WorldGuardHooker.isProtected(e.getPlayer(), e.getBlock().getLocation(), e.getBlock().getWorld())) {
                        e.setCancelled(true);
                        return;
                    } else {

                        if (plugin.getMark().isMarked(e.getBlock().getLocation())) {

                            plugin.getMark().removeMark(e.getBlock().getLocation());
                            e.setDropItems(false);
                        }
                        e.getBlock().setType(Material.AIR);
                    }
                    if (e.getBlock().getType() == Material.CHEST
                            || e.getBlock().getType() == Material.DROPPER
                            || e.getBlock().getType() == Material.DISPENSER) {
                        Chest chest = (Chest) e.getBlock().getState();
                        for (ItemStack t : chest.getBlockInventory().getContents()) {
                            if (plugin.getMark().isCreativeItem(t)) {
                                t.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockSpreadEvent(BlockSpreadEvent e) {
        if (plugin.getConfigManager().getConfig().getBoolean("DisableBlockSpreadEvent")) {

            if (plugin.getMark().isMarked(e.getSource().getLocation())) {

                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onStructureGrowEvent(StructureGrowEvent e) {
        if (plugin.getConfigManager().getConfig().getBoolean("DisableStructureGrowEvent")) {
            if (plugin.getMark().isMarked(e.getLocation())) {
                e.setCancelled(true);
            }

        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockGrowEvent(BlockGrowEvent e) {
        if (!plugin.getConfigManager().getConfig().getBoolean("DisableBlockGrowEvent")) {
            return;
        }
        Location loc = e.getBlock().getLocation();

        Location checkLoc = loc.clone();
        checkLoc.setY(checkLoc.getY() - 1);
        if (getBlock(checkLoc)
                .getType() == Material.matchMaterial("SUGAR_CANE")
                || getBlock(checkLoc).getType() == Material.matchMaterial("CACTUS")
                || getBlock(checkLoc).getType() == Material.matchMaterial("BAMBOO")) {
            if (plugin.getMark().isMarked(checkLoc)) {
                e.setCancelled(true);
                return;
            } else {
                e.setCancelled(false);
            }

        }
        e.setCancelled(plugin.getMark().isMarked(loc));
    }

    private Block getBlock(Location loc) {
        return loc.getWorld().getBlockAt(loc);
    }

    @EventHandler
    public void onDropCreative(PlayerDropItemEvent e) {
        if (!e.getPlayer().hasPermission(BypassDropPermission)) {

            if (plugin.getMark().isCreativeItem(e.getItemDrop().getItemStack())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfigManager().getMessage().getString("ErrorDropItem")));
            } else {
                e.setCancelled(false);
            }
        }

    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        if (plugin.getConfigManager().getConfig().getBoolean("BlockPistonPush")) {
            for (Block block : e.getBlocks()) {
                if (plugin.getMark().isMarked(block.getLocation())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPistonRetreat(BlockPistonRetractEvent e) {
        if (plugin.getConfigManager().getConfig().getBoolean("BlockPistonPush")) {
            for (Block block : e.getBlocks()) {
                if (plugin.getMark().isMarked(block.getLocation())) {
                    e.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onFallingBlocks(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            if (plugin.getMark().isMarked(event.getBlock().getLocation())) {
                event.setCancelled(true);
                event.getBlock().getState().update(true, true);
            }
        }
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
        if (plugin.getMark().isMarked(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void InteractInventory(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.getClickedBlock().getType() == Material.matchMaterial("Stonecutter")
                    || e.getClickedBlock().getType() == Material.matchMaterial("GRINDSTONE")) {
                if (plugin.getMark().isMarked(e.getClickedBlock().getLocation())) {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfigManager().getMessage().getString("StoneCutter_GrindStone_Restrict")));
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onTabCompleter(TabCompleteEvent e) {

        String[] args = e.getBuffer().toLowerCase().split(" ");
        if (args.length >= 1) {

            if (args[0].startsWith("/")) {

                switch (args[0]) {
                    case "/restrictcreative":
                        e.getCompletions().clear();
                        break;
                    case "/bulk":
                        e.getCompletions().clear();
                        if (e.getSender().hasPermission("restrictcreative.bulk.add")
                                || e.getSender().hasPermission("restrictcreative.bulk.*"))
                            e.getCompletions().add("add");
                        if (e.getSender().hasPermission("restrictcreative.bulk.remove")
                                || e.getSender().hasPermission("restrictcreative.bulk.*"))
                            e.getCompletions().add("remove");
                        if (args.length > 1)
                            e.getCompletions().clear();
                        break;

                    case "/import":
                        e.getCompletions().clear();
                        e.getCompletions().add("local");
                        e.getCompletions().add("online");
                        if (args.length == 2)
                            switch (args[1]) {
                                case "local":
                                    e.getCompletions().clear();
                                    e.getCompletions().add("online");
                                    break;
                                case "online":
                                    e.getCompletions().clear();
                                    e.getCompletions().add("local");
                            }
                        else if (args.length > 2)
                            e.getCompletions().clear();

                }


            }
        }
    }
}