package io.github.mufca.libgdx.constant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static io.github.mufca.libgdx.constant.PathConstants.BACKGROUND_MAIN_MENU_PNG;
import static io.github.mufca.libgdx.constant.PathConstants.BACKGROUND_MENU_CANVAS_PNG;
import static io.github.mufca.libgdx.constant.PathConstants.FRAME_PNG;
import static io.github.mufca.libgdx.constant.PathConstants.LEAF_PNG;
import static io.github.mufca.libgdx.constant.PathConstants.ORC_PNG;

public class TextureConstants {
    public static Texture ORC_FEMALE_PORTRAIT;
    public static Texture NINE_PATCH_FRAME;
    public static Texture BACKGROUND_MAIN_MENU;
    public static Texture BACKGROUND_MENU_CANVAS;
    public static Texture LEAF;


    public static void init() {
        ORC_FEMALE_PORTRAIT = new Texture(Gdx.files.internal(ORC_PNG));
        NINE_PATCH_FRAME = new Texture(Gdx.files.internal(FRAME_PNG));
        BACKGROUND_MAIN_MENU = new Texture(Gdx.files.internal(BACKGROUND_MAIN_MENU_PNG));
        BACKGROUND_MENU_CANVAS = new Texture(Gdx.files.internal(BACKGROUND_MENU_CANVAS_PNG));
        LEAF = new Texture(Gdx.files.internal(LEAF_PNG));
    }
}
