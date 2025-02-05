package com.vidya.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.messaging.MessageHeaders;

/**
 * @author Vidya Sagar Gupta
 * @since v1.0.0
 */
@UtilityClass
public class CommonUtil {

  public static Map<String, Object> extractAllHeaders(MessageHeaders headers) {
    if (null != headers && !headers.isEmpty()) {
      return headers.entrySet().parallelStream()
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
    return Map.of();
  }
}
