package ru.kpfu.itis.translation.utils;

import lombok.experimental.UtilityClass;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

@UtilityClass
public class SanitizationUtils {
  private static final PolicyFactory SANITIZATION = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

  public static String getSanitizedString(String toSanitized) {
    return SANITIZATION.sanitize(toSanitized);
  }
}
