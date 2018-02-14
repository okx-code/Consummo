package sh.okx.consummo.update;

import org.apache.commons.lang.Validate;
import org.junit.Test;

// [2, 2] 2.2
// [2, 3] 2.3
// [2, 3, 1] 2.3.1
// [2, 4] 2.4
// [3, 4] 3.4
public class AutoUpdaterTest {
  @Test
  public void autoUpdater_compareVersions() {
    AutoUpdater updater = new AutoUpdater(null, 0);

    Validate.isTrue(!updater.compareVersions(new int[] {2, 2}, new int[] {2, 3}));

    // the latest version is 2.3 and you are on 2.2
    // so it should return true meaning you should update.
    Validate.isTrue(updater.compareVersions(new int[] {2, 3}, new int[] {2, 2}));
    Validate.isTrue(updater.compareVersions(new int[] {2, 3, 1}, new int[] {2, 3}));
    Validate.isTrue(updater.compareVersions(new int[] {2, 4}, new int[] {2, 3, 1}));
    Validate.isTrue(updater.compareVersions(new int[] {3, 4}, new int[] {2, 4}));
    Validate.isTrue(updater.compareVersions(new int[] {3, 4}, new int[] {3, 4}));
  }
}
