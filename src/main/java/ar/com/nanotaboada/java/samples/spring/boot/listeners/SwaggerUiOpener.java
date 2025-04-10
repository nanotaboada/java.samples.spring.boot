package ar.com.nanotaboada.java.samples.spring.boot.listeners;

import java.awt.Desktop;
import java.net.URI;
import java.util.Arrays;
import java.awt.GraphicsEnvironment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")


public class SwaggerUiOpener implements ApplicationListener<ApplicationReadyEvent>{
    private static final Logger log = LoggerFactory.getLogger(SwaggerUiOpener.class);
    
  @Value("${server.port}")
    private int port;

    @Value("${springdoc.swagger-ui.path:/swagger/index.html}")
    private String swaggerPath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String url = "http://localhost:" + port + swaggerPath;
        log.info("✅ Swagger UI will open at: {}", url);

        try {
            if (Desktop.isDesktopSupported() && !GraphicsEnvironment.isHeadless()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                launchBrowserByOS(url);
            }
        } catch (Exception e) {
            log.error("❌ Failed to open Swagger UI", e);
        }
    }

    private void launchBrowserByOS(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else if (os.contains("nix") || os.contains("nux")) {
                boolean launched = false;
                for (String browser : Arrays.asList("xdg-open", "google-chrome", "firefox")) {
                  Process process = new ProcessBuilder("which", browser).start();
                    try {
                        int exitCode = process.waitFor();
                        if (exitCode == 0) {
                        new ProcessBuilder(browser, url).start();
                        launched = true;
                        break;
                        }
                    } catch (InterruptedException ie) {
                          Thread.currentThread().interrupt();
                          log.warn("Browser detection was interrupted", ie);
                    }
                }
                if (!launched) {
                    log.warn("⚠️ No supported browser found to open Swagger.");
                }
            } else {
                log.warn("⚠️ Unsupported OS for automatic browser launch.");
            }
        } catch (Exception e) {
            log.error("❌ Error while launching browser", e);
        }
    }
}
