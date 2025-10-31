package io.github.mufca.libgdx.gui.core.portrait;

import lombok.Getter;

@Getter
public enum PortraitFile {
    SMALL("small.png", 230, 300),
    MEDIUM("medium.png", 400, 535),
    LARGE("large.png", 960, 1320);

    private final String filename;
    private final int width, height;

    PortraitFile(String filename, int width, int height) {
        this.filename = filename;
        this.width = width;
        this.height = height;
    }
}
