package sh.okx.consummo.update;

import java.util.Base64;

/**
 * Represents an immutable update containing the version, title and description of it.
 */
public class Update implements Cloneable {
  private String version;
  private String title;
  private String description;

  /**
   * Constructs an Update with the version, title and description encoded in base 64.
   */
  public Update(String version, String title, String base64description) {
    this.version = version;
    this.title = title;
    this.description = new String(Base64.getDecoder().decode(base64description));
  }

  public String getVersion() {
    return version;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return version;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Update)) {
      return false;
    }

    Update update = (Update) o;
    return update.version.equals(version)
        && update.title.equals(title)
        && update.description.equals(description);
  }

  @Override
  public int hashCode() {
    int result = 0;

    result = 31 * result + version.hashCode();
    result = 31 * result + title.hashCode();
    result = 31 * result + description.hashCode();

    return result;
  }

  @Override
  public Object clone() {
    return new Update(version, title, description);
  }
}
