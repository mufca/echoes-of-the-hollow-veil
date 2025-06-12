package io.github.mufca.libgdx.gui.screen.cinematic;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public record CinematicStep(TextureRegion background, String text, Sound narration, ShaderProgram shader,
                            boolean autoAdvance, float delaySeconds, Runnable onShow) {
}