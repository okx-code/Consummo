package sh.okx.consummo.scheduler;

import java.util.function.Supplier;

import org.bukkit.plugin.Plugin;

public abstract class Task implements Supplier<Boolean> {
  public abstract void schedule(SchedulerChain callback, Plugin plugin, long delay);

  public void schedule(SchedulerChain callback, Plugin plugin) {
    schedule(callback, plugin, 1);
  }
}
