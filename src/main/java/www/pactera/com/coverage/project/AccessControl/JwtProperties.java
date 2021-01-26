package www.pactera.com.coverage.project.AccessControl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "config.jwt")
public class JwtProperties {

    /**
     *  密钥
     */
    public String secret;
    /**
     *  过期时间
     */
    public int expire;
    /**
     *  token 头部名称
     */
    public String header;

}
