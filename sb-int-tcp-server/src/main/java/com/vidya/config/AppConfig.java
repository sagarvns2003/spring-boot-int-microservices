package com.vidya.config;

import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author Vidya Sagar Gupta
 * @since v1.0.0
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AppConfig {

  private final ServerProperties serverProperties;
  private final BuildProperties buildProperties;

  @Getter
  @Value("${tcp.server.port}")
  private int tcpPort;

  public int httpPort() {
    return this.serverProperties.getPort();
  }

  public String appName() {
    return this.buildProperties.getName();
  }

  public String appVersion() {
    return this.buildProperties.getVersion();
  }

  public Instant appBuildTime() {
    return this.buildProperties.getTime();
  }

  public String appJvmVersion() {
    return this.buildProperties.get("java.version");
  }
}
