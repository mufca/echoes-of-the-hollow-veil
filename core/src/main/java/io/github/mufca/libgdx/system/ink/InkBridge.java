package io.github.mufca.libgdx.system.ink;

import com.badlogic.gdx.Gdx;
import com.bladecoder.ink.runtime.Story;
import java.io.IOException;

public class InkBridge {

    private final Story story;

    public InkBridge() throws IOException {
        try {
            String inkJson = Gdx.files.internal("ink/test.json").readString();
            this.story = new Story(inkJson);
        } catch (Exception e) {
            throw new IOException("Failed to load or parse Ink story: ink/test.json", e);
        }
    }

    /**
     * Evaluates a function defined in the Ink script and returns its result.
     *
     * @param functionName The name of the function to call in the Ink script (e.g., "sun_event").
     * @param args         The arguments to pass to the Ink function.
     * @return The string returned by the Ink function.
     */
    public String format(String functionName, Object... args) {
        try {
            Object result = story.evaluateFunction(functionName, args);
            return result != null ? result.toString() : "";
        } catch (Exception e) {
            throw new RuntimeException("Failed to evaluate Ink function: " + functionName, e);
        }
    }
}
