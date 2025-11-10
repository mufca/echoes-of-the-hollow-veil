package io.github.mufca.libgdx.utils.gdxextension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * E2E test extension that opens a real LibGDX window and provides a fully functional DockedViewportPanel with *
 * integrated batch/renderers.
 */
public final class GdxExtension implements BeforeAllCallback {

    private GdxTestThread gdxThread;
    private final TestApplicationListener listener = new TestApplicationListener();

    @Override
    public void beforeAll(ExtensionContext context) {
        gdxThread = new GdxTestThread(context.getDisplayName(), listener);
        gdxThread.start();
    }

    public void setRenderCallback(RenderCallback callback) {
        listener.renderCallback(callback);
    }
}