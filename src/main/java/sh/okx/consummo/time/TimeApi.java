package sh.okx.consummo.time;

import java.util.concurrent.TimeUnit;

/**
 * Converts user inputted time into a computer-readable format.
 */
public class TimeApi {
  private static long DAYS_IN_WEEK = 7;
  private static long DAYS_IN_MONTH = 30;
  private static long DAYS_IN_YEAR = 365;

  private long seconds;

  /**
   * @see TimeApi#reparse(String)
   */
  public TimeApi(String time) {
    reparse(time);
  }

  public TimeApi(long seconds) {
    this.seconds = seconds;
  }

  /**
   * Turn the inputted time into what can be turned into most time units.
   * @return the <code>TimeApi</code> instance which you can convert the time to various time units.
   */
  public TimeApi reparse(String time) {
    seconds = 0;

    TimeScanner scanner = new TimeScanner(time
        .replace(" ", "")
        .replace("and", "")
        .replace(",", "")
        .toLowerCase());

    long next;
    while (scanner.hasNext()) {
      next = scanner.nextLong();
      switch (scanner.nextString()) {
        case "s":
        case "sec":
        case "secs":
        case "second":
        case "seconds":
          seconds += next;
          break;
        case "m":
        case "min":
        case "mins":
        case "minute":
        case "minutes":
          seconds += TimeUnit.MINUTES.toSeconds(next);
          break;
        case "h":
        case "hr":
        case "hrs":
        case "hour":
        case "hours":
          seconds += TimeUnit.HOURS.toSeconds(next);
          break;
        case "d":
        case "dy":
        case "dys":
        case "day":
        case "days":
          seconds += TimeUnit.DAYS.toSeconds(next);
          break;
        case "w":
        case "week":
        case "weeks":
          seconds += TimeUnit.DAYS.toSeconds(next * DAYS_IN_WEEK);
          break;
        case "mo":
        case "mon":
        case "mnth":
        case "month":
        case "months":
          seconds += TimeUnit.DAYS.toSeconds(next * DAYS_IN_MONTH);
          break;
        case "y":
        case "yr":
        case "yrs":
        case "year":
        case "years":
          seconds += TimeUnit.DAYS.toSeconds(next * DAYS_IN_YEAR);
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
    return this;
  }


  public double getNanoseconds() {
    return TimeUnit.SECONDS.toNanos(seconds);
  }

  public double getMicroseconds() {
    return TimeUnit.SECONDS.toMicros(seconds);
  }

  public double getMilliseconds() {
    return TimeUnit.SECONDS.toMillis(seconds);
  }

  public double getSeconds() {
    return seconds;
  }

  public double getMinutes() {
    return seconds / 60D;
  }

  public double getHours() {
    return seconds / 3600D;
  }

  public double getDays() {
    return seconds / 86400D;
  }

  public double getWeeks() {
    return getDays() / DAYS_IN_WEEK;
  }

  public double getMonths() {
    return getDays() / DAYS_IN_MONTH;
  }

  public double getYears() {
    return getDays() / DAYS_IN_YEAR;
  }
}
