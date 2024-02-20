package broccoli.common;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * The {@link StringUtils}.
 */
public class StringUtils {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

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

  /**
   * Generate random string.
   *
   * @param length length
   * @return random string
   */
  public static String generateRandomString(int length) {
    byte[] randomBytes = new byte[length];
    SECURE_RANDOM.nextBytes(randomBytes);
    // Base64 encoding to ensure the generated string is text-friendly
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0, length);
  }
}
