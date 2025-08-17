package cheonwooo.spring_client_config_client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Getter
@Setter
@ConfigurationProperties("cheonwooo")
@RefreshScope
@ToString
public class MyConfig {

    private String profile;
    private String content;
}
