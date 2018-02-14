package sh.okx.consummo.scheduler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import org.bukkit.plugin.Plugin;

public class SchedulerChain {
  private Queue<Task> tasks = new LinkedBlockingQueue<>();
  private Plugin plugin;

  /**
   * Start the execution of the scheduler chain.
   * @param plugin the plugin to schedule the tasks with
   */
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

  /**
   * Add a task to be run synchronously in the chain.
   */
  public SchedulerChain sync(Runnable runnable) {
    return sync(() -> {
      runnable.run();
      return true;
    });
  }

  /**
   * Add a task to be run synchronously in the chain, returning false to stop the chain.
   */
  public SchedulerChain sync(Supplier<Boolean> shouldContinue) {
    return addTask(new SyncTask() {
      @Override
      public Boolean get() {
        return shouldContinue.get();
      }
    });
  }

  /**
   * Add a task to be run synchronously in the chain.
   */
  public SchedulerChain async(Runnable runnable) {
    return async(() -> {
      runnable.run();
      return true;
    });
  }

  /**
   * Add a task to be run synchronously in the chain, returning false to stop the chain.
   */
  public SchedulerChain async(Supplier<Boolean> shouldContinue) {
    return addTask(new AsyncTask() {
      @Override
      public Boolean get() {
        return shouldContinue.get();
      }
    });
  }

  /**
   * Add a sync or async task to the chain.
   */
  public SchedulerChain addTask(Task task) {
    tasks.offer(task);
    return this;
  }
}
