package broccoli.common;

/**
 * The {@link StringUtils}.
 */
public class StringUtils {

  public static String defaultString(String type) {
    return type == null ? "" : type;
  }

  public static boolean isBlank(String q) {
    return q == null || q.trim().isEmpty();
  }

  public static String join(String... items) {
    final var sb = new StringBuilder();
    for (var item : items) {
      sb.append(item);
    }
    return sb.toString();
  }
}
