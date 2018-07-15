package com.minlia.cloud.ignored.list;

import com.minlia.cloud.ignored.list.properties.SystemIgnoredListProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author will
 */
@Configuration
//@ConditionalOnProperty(prefix = "system.ignored.", name = "urls", havingValue = "true")
@EnableConfigurationProperties(value = {SystemIgnoredListProperties.class})
public class SystemIgnoredListAutoConfiguration {

}
