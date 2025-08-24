package cheonwooo.spring_client_config_client;

import jakarta.annotation.PostConstruct;
import java.io.Closeable;

import java.util.concurrent.atomic.AtomicBoolean;

import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import static org.springframework.util.StringUtils.hasText;

public class CustomConfigClientWatch implements Closeable, EnvironmentAware {

    private static Log log = LogFactory.getLog(ConfigServicePropertySourceLocator.class);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicReference<String> version = new AtomicReference<>();
    private final ContextRefresher refresher;
    private final ConfigServicePropertySourceLocator locator;

    private Environment environment;

    public CustomConfigClientWatch(ContextRefresher refresher, ConfigServicePropertySourceLocator locator) {
        this.refresher = refresher;
        this.locator = locator;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void start() {
        this.running.compareAndSet(false, true);
    }

    @Scheduled(initialDelayString = "${spring.cloud.config.watch.initialDelay:10000}",
            fixedDelayString = "${spring.cloud.config.watch.delay:5000}")
    public void watchConfigServer() {
        if (this.running.get()) {
            String newVersion = fetchNewVersion();
            if (newVersion == null) {
                return;
            }

            String oldVersion = version.get();
            if (versionChanged(oldVersion, newVersion)) {
                System.out.println("Find Cloud Config Git Changed !");
                version.set(newVersion);
                refresher.refresh();
            }
        }
    }

    private String fetchNewVersion() {
        try {
            CompositePropertySource propertySource = (CompositePropertySource) locator.locate(environment);
            return (String) propertySource.getProperty("config.client.version");
        } catch (NullPointerException e) {
            System.out.println("Cannot fetch from Cloud Config Server: " + e);
        }
        return null;
    }

    boolean versionChanged(String oldState, String newState) {
        return (!hasText(oldState) && hasText(newState))
                || (hasText(oldState) && !oldState.equals(newState));
    }

    @Override
    public void close() {
        this.running.compareAndSet(true, false);
    }

}