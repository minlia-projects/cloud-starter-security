package com.minlia.cloud.security.internal;

import com.minlia.cloud.security.security.jwt.JwtConfigurer;
import com.minlia.cloud.security.security.jwt.TokenProvider;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;


@Import(SecurityProblemSupport.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
public class InternalSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  private final UserDetailsService userDetailsService;

  private final TokenProvider tokenProvider;

  @Autowired
    private  CorsFilter corsFilter;

  @Autowired
  protected BCryptPasswordEncoder passwordEncoder;

  private final SecurityProblemSupport problemSupport;

//    @Autowired
//    private JHipsterProperties jHipsterProperties;
////

  public InternalSecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder,
      UserDetailsService userDetailsService, TokenProvider tokenProvider,
//      CorsFilter corsFilter,
      SecurityProblemSupport problemSupport) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.userDetailsService = userDetailsService;
    this.tokenProvider = tokenProvider;
//        this.corsFilter = corsFilter;
    this.problemSupport = problemSupport;
  }

  @PostConstruct
  public void init() {
    try {
      authenticationManagerBuilder
          .userDetailsService(userDetailsService)
          .passwordEncoder(passwordEncoder);
    } catch (Exception e) {
      throw new BeanInitializationException("Security configuration failed", e);
    }
  }

//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = jHipsterProperties.getCors();
//        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
//            log.debug("Registering CORS filter");
//            source.registerCorsConfiguration("/api/**", config);
//            source.registerCorsConfiguration("/management/**", config);
//            source.registerCorsConfiguration("/v2/api-docs", config);
//        }
//        return new CorsFilter(source);
//    }


//  @Override
//  @ConditionalOnMissingBean
//  @Bean
//  public DummyUserDetailsService userDetailsService() {
//    return new DummyUserDetailsService();
//  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .antMatchers("/app/**/*.{js,html}")
        .antMatchers("/i18n/**")
        .antMatchers("/content/**")
        .antMatchers("/swagger-ui/index.html")
        .antMatchers("/test/**")
        .antMatchers("/h2-console/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(problemSupport)
        .accessDeniedHandler(problemSupport)
        .and()
        .csrf()
        .disable()
        .headers()
        .frameOptions()
        .disable()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/api/register").permitAll()
        .antMatchers("/api/activate").permitAll()
        .antMatchers("/api/authenticate").permitAll()
        .antMatchers("/api/test").permitAll()
        .antMatchers("/api/status").permitAll()
        .antMatchers("/api/notActivated").permitAll()
        .antMatchers("/api/account/reset-password/init").permitAll()
        .antMatchers("/api/account/reset-password/finish").permitAll()
        .antMatchers("/api/profile-info").permitAll()
        .antMatchers("/api/**").authenticated()
        .antMatchers("/management/health").permitAll()
//            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/v2/api-docs/**").permitAll()
        .antMatchers("/swagger-resources/configuration/ui").permitAll()
//            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
        .apply(securityConfigurerAdapter());

  }

  private JwtConfigurer securityConfigurerAdapter() {
    return new JwtConfigurer(tokenProvider);
  }


  /**
   * For security 2.0.1.RELEASE fix only
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
