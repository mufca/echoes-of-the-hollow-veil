package io.github.mufca.libgdx.system.ink;

import com.badlogic.gdx.Gdx;
import com.bladecoder.ink.runtime.Story;

public abstract class InkStory {

    private static final String INK_LOAD_ERROR = "Failed to load or parse Ink story: %s";
    private static final String EVALUATION_ERROR = "Failed to evaluate Ink function: %s";

    protected final Story story;

    public InkStory(String internalPath) {
        try {
            String inkJson = Gdx.files.internal(internalPath).readString();
            this.story = new Story(inkJson);
        } catch (Exception e) {
            throw new RuntimeException(INK_LOAD_ERROR.formatted(internalPath), e);
        }
    }

    /**
     * Evaluates a function defined in the Ink script and returns its result.
     *
     * @param functionName The name of the function to call in the Ink script (e.g., "sunEvent").
     * @param args         The arguments to pass to the Ink function.
     * @return The string returned by the Ink function.
     */
    protected String evaluate(String functionName, Object... args) {
        try {
            Object result = story.evaluateFunction(functionName, args);
            return result != null ? result.toString() : "";
        } catch (Exception e) {
            throw new RuntimeException(EVALUATION_ERROR.formatted(functionName), e);
        }
    }
}