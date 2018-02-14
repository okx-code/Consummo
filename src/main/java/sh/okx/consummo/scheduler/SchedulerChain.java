package sh.okx.consummo.scheduler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import org.bukkit.plugin.Plugin;

public class SchedulerChain {
  private Queue<Task> tasks = new LinkedBlockingQueue<>();
  private Plugin plugin;

  public void start(Plugin plugin) {
    this.plugin = plugin;
    next();
  }

  protected void next() {
    Task next = tasks.poll();
    if (next == null) {
      return;
    }

    next.schedule(this, plugin);
  }

  public SchedulerChain sync(Runnable runnable) {
    return sync(() -> { runnable.run(); return true; });
  }

  public SchedulerChain sync(Supplier<Boolean> shouldContinue) {
    return addTask(new SyncTask() {
      @Override
      public Boolean get() {
        return shouldContinue.get();
      }
    });
  }

  public SchedulerChain async(Runnable runnable) {
    return async(() -> { runnable.run(); return true; });
  }

  public SchedulerChain async(Supplier<Boolean> shouldContinue) {
    return addTask(new AsyncTask() {
      @Override
      public Boolean get() {
        return shouldContinue.get();
      }
    });
  }

  public SchedulerChain addTask(Task task) {
    tasks.offer(task);
    return this;
  }
}
