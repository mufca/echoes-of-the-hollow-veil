package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

public class BoundFileHandle extends FileHandle {

    private final int expectedWidth;
    private final int expectedHeight;

    public BoundFileHandle(String fileName, int expectedWidth, int expectedHeight) {
        super(fileName);
        this.expectedWidth = expectedWidth;
        this.expectedHeight = expectedHeight;
    }

    public void check() {
        if (!this.exists()) {
            throw new RuntimeException("Portrait image not found: %s".formatted(this.name()));
        }
        var pixmap = new Pixmap(this);
        var pixmapWidth = pixmap.getWidth();
        var pixmapHeight = pixmap.getHeight();
        pixmap.dispose();
        if ((pixmapWidth != this.expectedWidth) || (pixmapHeight != this.expectedHeight)) {
            throw new RuntimeException("Given portrait image has wrong size: %d x %d. Expected %d x %d"
                .formatted(pixmapWidth, pixmapHeight, expectedWidth, expectedHeight));
        }
    }
}
