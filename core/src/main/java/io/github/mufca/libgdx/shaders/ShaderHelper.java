package io.github.mufca.libgdx.shaders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Map;

public class ShaderHelper {
    /**
     * Creates a map of shader uniforms required for highlighting an actor.
     *
     * @param actor the actor to be highlighted
     * @return a map of uniforms with two entries: "u_boundsMin" and "u_boundsMax"
     * containing the bottom-right and top-left corners of the actor in screen coordinates respectively
     */
    public static Map<String, Object> getHighlightUniforms(Actor actor) {
        Vector2 screenPos = actor.localToStageCoordinates(new Vector2(0, 0));
        float minX = screenPos.x;
        float minY = screenPos.y;
        float maxX = minX + actor.getWidth();
        float maxY = minY + actor.getHeight();
        return Map.of(
            "u_boundsMin", new Vector2(minX, minY),
            "u_boundsMax", new Vector2(maxX, maxY));
    }

    /**
     * Creates a map of shader uniforms required for stroking an actor.
     *
     * @param actor the actor to be stroked
     * @return a map of uniforms with four entries: "strokeTopStart", "strokeBottomStart", "strokeLength", and "strokeWidth"
     * containing the top-left and bottom-left corners of the actor in screen coordinates respectively,
     * the length of the stroke in pixels, and the stroke width in pixels
     */
    public static Map<String, Object> getStrokeUniforms(Actor actor) {
        Vector2 screenPos = adjustCoordinates(actor.localToScreenCoordinates(new Vector2(0, 0)), actor.getStage());
        float minX = screenPos.x;
        float minY = screenPos.y + actor.getHeight();
        float strokeLength = actor.getWidth();
        float strokeWidth = 3f;
        return Map.of(
            "strokeTopStart", new Vector2(minX, minY),
            "strokeBottomStart", new Vector2(minX, screenPos.y),
            "strokeLength", strokeLength,
            "strokeWidth", strokeWidth);
    }

    /**
     * Adjusts the given vector2's y-coordinate to be relative to the origin at the top of the screen.
     * <p>
     * This is needed because libGDX's coordinate system has the origin at the bottom left of the screen,
     * but shaders expect the origin to be at the top left of the screen.
     *
     * @param vector2 the vector to be adjusted
     * @param stage   the stage that the actor is in
     * @return the adjusted vector
     */
    private static Vector2 adjustCoordinates(Vector2 vector2, Stage stage) {
        return vector2.set(vector2.x, stage.getHeight() - vector2.y);
    }
}
