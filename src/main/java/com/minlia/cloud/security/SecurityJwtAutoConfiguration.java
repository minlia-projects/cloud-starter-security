package com.minlia.cloud.security;

import com.minlia.cloud.security.internal.InternalSecurityConfiguration;
import com.minlia.cloud.security.security.DummyUserDetailsService;
import com.minlia.cloud.security.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**
 * 当启用 security 的jwt功能时进行如下配置
 * @author will
 */
@Configuration
@ConditionalOnProperty(prefix = "system.security.jwt.", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {JwtSettings.class})
public class SecurityJwtAutoConfiguration {

  @ConditionalOnMissingBean
  @Bean
  public DummyUserDetailsService userDetailsService() {
    return new DummyUserDetailsService();
  }


//  @Bean
//  @ConditionalOnMissingBean
//  public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean(){
//    FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
////    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    filterRegistrationBean.setFilter(corsFilter());
//    return filterRegistrationBean;
//  }


  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    CorsConfiguration config = jHipsterProperties.getCors();
//    if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
//      log.debug("Registering CORS filter");
//      source.registerCorsConfiguration("/api/**", config);
//      source.registerCorsConfiguration("/management/**", config);
//      source.registerCorsConfiguration("/v2/api-docs", config);
//    }
    return new CorsFilter(source);
  }

  @Bean
  @ConditionalOnMissingBean
  protected BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//
//  @ConditionalOnMissingBean
//  @Bean
//  public CorsConfig corsConfig() {
//    return new CorsConfig();
//  }


  @Autowired
  private JwtSettings jwtSettings;


  @Bean
  @ConditionalOnMissingBean
  public TokenProvider tokenProvider() {
    return new TokenProvider(jwtSettings);
  }

  @Configuration
  @Import(value = {InternalSecurityConfiguration.class})
  public static class TheInternalSecurityConfiguration {

  }

}
