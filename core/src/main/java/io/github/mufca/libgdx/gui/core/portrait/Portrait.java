package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import java.util.Objects;

public record Portrait(TextureRegion small, TextureRegion medium, TextureRegion large) implements Disposable {

    private static final String TEXTURE_REGION_CANNOT_BE_NULL = "%s portrait TextureRegion cannot be null";
    private static final String LARGE = "Large";
    private static final String MEDIUM = "Medium";
    private static final String SMALL = "Small";

    public Portrait {
        small = Objects.requireNonNull(small, TEXTURE_REGION_CANNOT_BE_NULL.formatted(SMALL));
        medium = Objects.requireNonNull(medium, TEXTURE_REGION_CANNOT_BE_NULL.formatted(MEDIUM));
        large = Objects.requireNonNull(large, TEXTURE_REGION_CANNOT_BE_NULL.formatted(LARGE));
    }

    @Override
    public void dispose() {
        small.getTexture().dispose();
        medium.getTexture().dispose();
        large.getTexture().dispose();
    }
}
