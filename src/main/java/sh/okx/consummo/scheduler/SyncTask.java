package sh.okx.consummo.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class SyncTask extends Task {
  @Override
  public void schedule(SchedulerChain callback, Plugin plugin, long delay) {
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      if(this.get()) {
        callback.next();
      }
    }, delay);
  }
}
