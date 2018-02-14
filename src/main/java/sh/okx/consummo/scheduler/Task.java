package sh.okx.consummo.scheduler;

import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

public abstract class Task implements Supplier<Boolean> {
  public abstract void schedule(SchedulerChain callback, Plugin plugin, long delay);

  public void schedule(SchedulerChain callback, Plugin plugin) {
    schedule(callback, plugin, 1);
  }
}
