package com.vidya.service;

import java.util.Map;

/**
 * @author Vidya Sagar Gupta
 * @since v1.0.0
 */
public interface MessageService {
  String handle(String messageBody, Map<String, Object> messageHeaders);
}
