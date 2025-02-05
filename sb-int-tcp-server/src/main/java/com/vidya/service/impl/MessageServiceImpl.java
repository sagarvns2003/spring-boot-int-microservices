package com.vidya.service.impl;

import com.vidya.service.MessageService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author Vidya Sagar Gupta
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MessageServiceImpl implements MessageService {

  @Override
  public String handle(String messageBody, Map<String, Object> messageHeaders) {
    log.info("Message from client: {}", messageBody);
    log.info("All message headers: {}", messageHeaders);
    // Handle message here and provide the response

    return "Hi from TCP Server";
  }
}
