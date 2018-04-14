package com.minlia.cloud.security;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "system.security.jwt", ignoreUnknownFields = false)
public class JwtSettings {

  private Boolean enabled;
  private String secret;
  private long tokenValidityInSeconds;
  private long tokenValidityInSecondsForRememberMe;

  public JwtSettings() {
    this.secret = null;
    this.enabled=true;
    this.tokenValidityInSeconds = 1800L;
    this.tokenValidityInSecondsForRememberMe = 2592000L;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getSecret() {
    return this.secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public long getTokenValidityInSeconds() {
    return this.tokenValidityInSeconds;
  }

  public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
    this.tokenValidityInSeconds = tokenValidityInSeconds;
  }

  public long getTokenValidityInSecondsForRememberMe() {
    return this.tokenValidityInSecondsForRememberMe;
  }

  public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
  }
}