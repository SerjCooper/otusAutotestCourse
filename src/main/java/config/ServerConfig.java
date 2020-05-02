package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ServerConfig extends Config{

    @Key("url")
    String url();

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("URL_YA_MARKET")
    String urlYaMarket();

    @Key("TIMEOUT_IMPLICITLY")
    int timeout_implicitly();
}
