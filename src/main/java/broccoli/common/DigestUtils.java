package broccoli.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The {@link DigestUtils} class.
 */
public class DigestUtils {

  private DigestUtils() {
  }

  public static String sha512Hex(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    byte[] bytes = md.digest(input.getBytes());
    return bytesToHex(bytes);
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }
}
