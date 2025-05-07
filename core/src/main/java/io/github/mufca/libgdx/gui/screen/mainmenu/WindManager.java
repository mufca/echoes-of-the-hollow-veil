package io.github.mufca.libgdx.gui.screen.mainmenu;

import com.badlogic.gdx.math.MathUtils;
import lombok.Getter;

public class WindManager {
    private float elapsedTime = 0f;
    private final float timeToBlowIn = MathUtils.random(9f, 45f);
    @Getter
    private final float duration = MathUtils.random(1f, 1.3f);


    public boolean isTimeToStart() {
        return elapsedTime > timeToBlowIn;
    }

    public void elapsed(float delta) {
        elapsedTime += delta;
    }

}
