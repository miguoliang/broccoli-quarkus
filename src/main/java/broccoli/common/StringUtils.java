package broccoli.common;

/**
 * The {@link StringUtils}.
 */
public class StringUtils {

  private StringUtils() {

  }

  /**
   * Default string.
   *
   * @param string string
   * @return string itself if not null, otherwise empty string
   */
  public static String defaultString(String string) {
    return string == null ? "" : string;
  }

  /**
   * Is blank.
   *
   * @param q q
   * @return true if q is null or empty
   */
  public static boolean isBlank(String q) {
    return q == null || q.trim().isEmpty();
  }

  /**
   * Join strings into one.
   *
   * @param items items
   * @return joined string
   */
  public static String join(String... items) {
    final var sb = new StringBuilder();
    for (var item : items) {
      sb.append(item);
    }
    return sb.toString();
  }
}
