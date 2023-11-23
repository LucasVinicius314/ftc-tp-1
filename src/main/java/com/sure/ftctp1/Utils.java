package com.sure.ftctp1;

public class Utils {

  private Utils() {
  }

  public static String padEnd(String input, int length, char padChar) {

    final var paddedText = new StringBuilder(input);

    while (paddedText.length() < length) {
      paddedText.append(padChar);
    }

    return paddedText.toString();
  }
}
