package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class PortraitLoader {

    private static final int SMALL_WIDTH = 260;
    private static final int SMALL_HEIGHT = 336;
    private static final int MEDIUM_WIDTH = 448;
    private static final int MEDIUM_HEIGHT = 600;
    private static final int LARGE_WIDTH = 1080;
    private static final int LARGE_HEIGHT = 1480;

    public static Portrait loadPortrait(String directory) {
        // Define file paths
        String smallPath = directory + "/small.png";
        String mediumPath = directory + "/medium.png";
        String largePath = directory + "/large.png";

        // Get file handles
        List<BoundFileHandle> portraitFiles = List.of(
            new BoundFileHandle(smallPath, SMALL_WIDTH, SMALL_HEIGHT),
            new BoundFileHandle(mediumPath, MEDIUM_WIDTH, MEDIUM_HEIGHT),
            new BoundFileHandle(largePath, LARGE_WIDTH, LARGE_HEIGHT));

        // Check if they exists
        portraitFiles.stream().forEach(BoundFileHandle::check);


        // Create texture regions
        Texture smallTexture = new Texture(smallPath);
        Texture mediumTexture = new Texture(mediumPath);
        Texture largeTexture = new Texture(largePath);

        TextureRegion smallRegion = new TextureRegion(smallTexture);
        TextureRegion mediumRegion = new TextureRegion(mediumTexture);
        TextureRegion largeRegion = new TextureRegion(largeTexture);

        return new Portrait(smallRegion, mediumRegion, largeRegion);
    }
}