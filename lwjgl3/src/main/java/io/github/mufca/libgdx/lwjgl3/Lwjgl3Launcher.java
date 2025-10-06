package io.github.mufca.libgdx.lwjgl3;

import static com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration.GLEmulation.GL32;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.mufca.libgdx.Main;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher {

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) {
            return; // This handles macOS support and helps on Windows.
        }
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Echoes of the Hollow Veil");
        configuration.setOpenGLEmulation(GL32, 3, 2);
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        configuration.setDecorated(false);
        Graphics.Monitor primaryMonitor = Lwjgl3ApplicationConfiguration.getPrimaryMonitor();
        Graphics.DisplayMode displayMode = Arrays.stream(Lwjgl3ApplicationConfiguration.getDisplayModes(primaryMonitor))
            .max(Comparator.comparingInt(aDisplayMode -> aDisplayMode.width * aDisplayMode.height))
            .orElseThrow(() -> new IllegalStateException("No display modes"));
        configuration.setWindowedMode(displayMode.width, displayMode.height);
        configuration.setWindowPosition(primaryMonitor.virtualX, primaryMonitor.virtualY);
        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}