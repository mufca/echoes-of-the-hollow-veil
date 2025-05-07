package io.github.mufca.libgdx.gui.core.widget;

public enum Layer {
    LOWER_MODAL(121),
    UPPER_MODAL(211);

    private final int index;
    Layer(int index) {
        this.index = index;
    }

    public int getLayerIndex() {
        return this.index;
    }
}
