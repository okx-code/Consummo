package sh.okx.consummo.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoUpdater {
  private JavaPlugin plugin;
  private int resourceId;

  public AutoUpdater(JavaPlugin plugin, int resourceId) {
    this.plugin = plugin;
    this.resourceId = resourceId;
  }

  /**
   * Checks the latest version of this resource.
   * This makes a web request and should not be called on the Bukkit main thread!
   * @return the latest version of this resource
   */
  public String getLatestVersion() throws IOException {
    byte[] responseBytes = new byte[1024];

    URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
    InputStream stream = url.openStream();
    stream.read(responseBytes);

    return new String(responseBytes);
  }

  /**
   * Gets the latest update. Includes the version, name, and description of the update.
   * @return the latest update.
   */
  public Update getLatestUpdate() throws IOException {
    URL url = new URL("https://api.spiget.org/v2/resources/" + resourceId + "/updates?size=1&sort=-date");
    InputStream stream = url.openStream();

    JsonObject json = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
    return new Update(getLatestVersion(),
        json.get("title").getAsString(),
        json.get("description").getAsString());
  }

  /**
   * Checks if you are running an old version of the plugin.
   * This should not be run on the Bukkit main thread.
   * @return true if you should update, false otherwise
   * @see AutoUpdater#isOldVersion(String)
   */
  public boolean isOldVersion() throws IOException {
    return isOldVersion(getLatestVersion());
  }

  /**
   * Like {@link AutoUpdater#isOldVersion()} but uses <code>version</code> as the latest version.
   * This should not be run on the Bukkit main thread.
   * @return true if you should update, false otherwise
   * @see AutoUpdater#isOldVersion()
   */
  public boolean isOldVersion(String version) {
    int[] latestVersionParts = versionToIntArray(version);
    int[] currentVersionParts = versionToIntArray(plugin.getDescription().getVersion());

    return compareVersions(latestVersionParts, currentVersionParts);
  }

  /**
   * Download the latest version of the plugin and place it in.
   * the <code>/plugins/update</code> directory.
   * This should not be run on the Bukkit main thread.
   * @return the update downloaded if any was downloaded, or <code>null</code>
   *         otherwise or if the file already exists.
   */
  public Update updateIfOld() throws IOException {
    Update latest = getLatestUpdate();
    if (!isOldVersion(latest.getVersion())) {
      return null;
    }

    URL url = new URL("https://api.spiget.org/v2/resources/" + resourceId + "/download");
    URLConnection connection = url.openConnection();
    connection.setRequestProperty("User-Agent", plugin.getDescription().getFullName());

    String fileName = connection.getHeaderField("Content-Disposition").split("=")[1]
        .replaceAll("#\\d+(?=\\.jar)", ""); // remove version ID from file name

    File update = plugin.getServer().getUpdateFolderFile();
    update.mkdir();

    Path download = update.toPath().resolve(fileName);
    if (download.toFile().exists()) {
      return null;
    }


    Files.copy(connection.getInputStream(),
        download);
    return latest;
  }

  /**
   * Run {@link AutoUpdater#updateIfOld()} every <code>delay</code> ticks asynchronously.
   * @param delay    How often to schedule an update
   * @param callback This will be run asynchronously if a new version is downloaded.
   */
  public void schedule(long delay, Consumer<Update> callback, boolean stopWhenUpdateFound) {
    new BukkitRunnable() {
      @Override
      public void run() {
        try {
          Update downloaded = updateIfOld();
          if (downloaded != null) {
            callback.accept(downloaded);
            if (stopWhenUpdateFound) {
              cancel();
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }.runTaskTimerAsynchronously(plugin, delay, delay);
  }

  /**
   * Executes {@link AutoUpdater#schedule(long, Consumer, boolean)}
   * and does nothing when an update is downloaded.
   * @see AutoUpdater#schedule(long, Consumer, boolean)
   */
  public void schedule(long delay, boolean stopWhenUpdateFound) {
    schedule(delay, a -> { }, stopWhenUpdateFound);
  }

  private int[] versionToIntArray(String version) {
    return Arrays.stream(version.split("\\."))
        .mapToInt(a -> a.charAt(0)).toArray();
  }

  /**
   * Compares two versions as an int array.
   *
   * @return true if <code>arrA</code> is later or equal to <code>arrB</code>
   */
  protected boolean compareVersions(int[] arrA, int[] arrB) {
    for (int i = 0; i < Math.max(arrA.length, arrB.length); i++) {
      int a = i < arrA.length ? arrA[i] : 0;
      int b = i < arrB.length ? arrB[i] : 0;

      if (a < b) {
        return false;
      } else if (a > b) {
        return true;
      }
    }

    return true;
  }
}
