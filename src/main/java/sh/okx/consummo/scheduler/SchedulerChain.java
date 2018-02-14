package sh.okx.consummo.scheduler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
    return addTask(new SyncTask() {
      @Override
      public void run() {
        runnable.run();
      }
    });
  }

  public SchedulerChain async(Runnable runnable) {
    return addTask(new AsyncTask() {
      @Override
      public void run() {
        runnable.run();
      }
    });
  }

  public SchedulerChain addTask(Task task) {
    tasks.offer(task);
    return this;
  }
}
