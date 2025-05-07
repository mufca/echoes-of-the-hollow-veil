package io.github.mufca.libgdx.gui.screen.mainmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LeafActor extends Actor {

    private final TextureRegion texture;

    private final float viewportWidth;
    private final float viewportHeight;

    private float velocityY;
    private float velocityX;
    private float rotationSpeed;
    private float scale;

    private float elapsedTime = 0f;
    private float fadeOutTime;
    private float windDelay;
    private float windStrength;
    private float windDuration;
    private float currentSpeed;
    private boolean isWindBlowing = false;

    public LeafActor(TextureRegion texture, float viewportWidth, float viewportHeight) {
        this.texture = texture;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;

        // Random spawn position (upper-right quadrant)
        float startX = MathUtils.random(this.viewportWidth * 0.6f, this.viewportWidth);
        float startY = MathUtils.random(this.viewportHeight * 0.6f, this.viewportHeight);
        setPosition(startX, startY);

        // Movement speed
        this.velocityY = this.viewportHeight * MathUtils.random(0.015f, 0.03f);
        this.velocityX = this.viewportWidth * MathUtils.random(0.015f, 0.03f);

        this.currentSpeed = velocityX;

        // Rotation speed in degrees per second
        this.rotationSpeed = MathUtils.randomSign() * MathUtils.random(10f, 30f);

        // Scale of the leaf
        this.scale = MathUtils.random(0.3f, 1f);
        setScale(scale);

        // Timing
        this.fadeOutTime = MathUtils.random(9f, 20f);
        this.windDelay = MathUtils.random(0.2f, 1f);

        // Start fully transparent
        getColor().a = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;

        // Fade in
        if (getColor().a < 1f) {
            getColor().a = Math.min(1f, getColor().a + delta / 3f);
        }

        if (!isWindBlowing) {
            normalMovement(delta);
        } else if (windDelay > 0) {
            windDelay -= delta;
            normalMovement(delta);
        } else {
            windMovement(delta);
        }

        if (elapsedTime > fadeOutTime) {
            fadeOut(delta);
        }

        // Off-screen cleanup (safe bounds)
        if (getY() + getHeight() * getScaleY() < -texture.getRegionHeight() ||
            getX() + getWidth() * getScaleX() < -texture.getRegionWidth()) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        float originX = texture.getRegionWidth() / 2f;
        float originY = texture.getRegionHeight() / 2f;

        batch.draw(
            texture,
            getX(), getY(),
            originX, originY,
            texture.getRegionWidth(), texture.getRegionHeight(),
            getScaleX(), getScaleY(),
            getRotation()
        );

        batch.setColor(Color.WHITE); // Reset color
    }

    private void normalMovement(float delta) {
        if (currentSpeed > velocityX) {
            currentSpeed = Math.max(velocityX, currentSpeed - windStrength * 0.5f);
        }
        rotateBy(rotationSpeed * delta);
        moveBy(-currentSpeed * delta, -velocityY * delta);
    }

    private void windMovement(float delta) {
        if (windDuration > 0) {
            windDuration -= delta;
            currentSpeed += windStrength;
            rotateBy(rotationSpeed * delta);
            moveBy(-currentSpeed * delta, -velocityY * delta);
        } else {
            windDuration = 0;
            isWindBlowing = false;
        }
    }

    public void blowWind(float duration) {
        this.isWindBlowing = true;
        this.windDuration = duration;
        this.windStrength = viewportWidth * MathUtils.random(0.001f, 0.003f);
    }

    private void fadeOut(float delta) {
        getColor().a = Math.max(0f, getColor().a - delta / 3f);
    }
}