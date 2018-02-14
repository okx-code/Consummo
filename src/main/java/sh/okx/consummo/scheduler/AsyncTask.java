package sh.okx.consummo.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class AsyncTask extends Task {
  @Override
  public void schedule(SchedulerChain callback, Plugin plugin, long delay) {
    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
      if(this.get()) {
        callback.next();
      }
    }, delay);
  }
}
