package io.github.mufca.libgdx.gui.screen.cinematic;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.mufca.libgdx.shaders.ShaderProgramEnumerated;

public record CinematicStep(TextureRegion background, String text, Sound narration, ShaderProgramEnumerated shader,
                            boolean autoAdvance, float delaySeconds, Runnable onShow) {

}