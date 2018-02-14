package sh.okx.consummo;

import org.bukkit.plugin.java.JavaPlugin;
import sh.okx.consummo.update.AutoUpdater;

public class Consummo extends JavaPlugin {
  @Override
  public void onEnable() {
    new AutoUpdater(this, 17933).schedule(200,
        a -> System.out.println("Updated to " + a), true);
  }
}
