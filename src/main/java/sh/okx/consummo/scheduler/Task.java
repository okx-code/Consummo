package sh.okx.consummo.scheduler;

import org.bukkit.plugin.Plugin;

public abstract class Task implements Runnable {
  public abstract void schedule(SchedulerChain callback, Plugin plugin, long delay);

  public void schedule(SchedulerChain callback, Plugin plugin) {
    schedule(callback, plugin, 1);
  }
}
