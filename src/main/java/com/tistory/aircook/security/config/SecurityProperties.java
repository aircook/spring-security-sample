package com.tistory.aircook.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security.authenticated")
@Data
public class SecurityProperties {

    private List<String> exclude;

}
