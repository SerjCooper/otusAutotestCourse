package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ServerConfig extends Config{

    @Key("url")
    String url();

    @Key("browser")
    @DefaultValue("chrome")
    String browser();
}
