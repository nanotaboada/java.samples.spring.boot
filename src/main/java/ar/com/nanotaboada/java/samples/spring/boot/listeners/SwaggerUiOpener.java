package ar.com.nanotaboada.java.samples.spring.boot.listeners;

import java.awt.Desktop;
import java.net.URI;
import java.awt.GraphicsEnvironment;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")


public class SwaggerUiOpener implements ApplicationListener<ApplicationReadyEvent>{

    @Value("${server.port}")
    private int port;

    @Value("${springdoc.swagger-ui.path:/swagger/index.html}")
    private String swaggerPath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            String url = "http://localhost:" + port + swaggerPath;
            System.out.println("✅ Swagger UI will open at: " + url);
    
            if (Desktop.isDesktopSupported() && !GraphicsEnvironment.isHeadless()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                // Fallback: open Swagger UI in browser depending on the OS (Windows, macOS, Linux)
                String os = System.getProperty("os.name").toLowerCase();
    
                if (os.contains("win")) {
                    new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url).start();
                } else if (os.contains("mac")) {
                    new ProcessBuilder("open", url).start();
                } else if (os.contains("nix") || os.contains("nux")) {
                    String[] browsers = { "xdg-open", "google-chrome", "firefox" };
                    boolean launched = false;
    
                    for (String browser : browsers) {
                        Process process = new ProcessBuilder("which", browser).start();
                        if (process.getInputStream().read() != -1) {
                            new ProcessBuilder(browser, url).start();
                            launched = true;
                            break;
                        }
                    }
    
                    if (!launched) {
                        System.err.println("⚠️ No supported browser found to open Swagger.");
                    }
                } else {
                    System.err.println("⚠️ Unsupported OS for automatic browser launch.");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to open Swagger UI: " + e.getMessage());
        }
    }
    
}
