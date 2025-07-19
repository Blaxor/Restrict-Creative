package ro.deiutzblaxo.RestrictCreative.menu.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ro.deiutzblaxo.RestrictCreative.menu.MenuManager;
import ro.deiutzblaxo.RestrictCreative.menu.objects.Menu;

import java.util.logging.Level;

public class PlayerOpenMenuEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;

    public PlayerOpenMenuEvent(MenuManager manager, Player player, String id) {
        if (isCancelled())
            return;
        player.closeInventory();
        player.openInventory(manager.getMenu(id).getInterface());
        Bukkit.getLogger().log(Level.INFO, "MenuManager: " + player.getName() + " open the menu " + id);
    }

    public PlayerOpenMenuEvent(Menu menu, Player player) {
        if (isCancelled())
            return;
        player.closeInventory();
        player.openInventory(menu.getInterface());
        Bukkit.getLogger().log(Level.INFO, "MenuManager: " + player.getName() + " open the menu " + menu.getID());
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
