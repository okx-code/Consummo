package sh.okx.consummo.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {
  private Gui gui;

  public GuiListener(Gui gui) {
    this.gui = gui;
  }

  @EventHandler
  public void on(InventoryClickEvent e) {
    HumanEntity entity = e.getWhoClicked();
    if (!e.getInventory().getHolder().equals(gui)) {
      return;
    }

    e.setCancelled(true);

    if (e.getClickedInventory() == null) {
      if (gui.isCloseIfClickOutsideOfWindow()) {
        Bukkit.getScheduler().runTask(gui.getPlugin(), entity::closeInventory);
      }
      return;
    } else if (!e.getInventory().equals(e.getClickedInventory())) {
      // if the inventory clicked is not the upper one
      return;
    }

    Bukkit.getScheduler().runTask(gui.getPlugin(), () -> gui.handle(e));
  }
}
