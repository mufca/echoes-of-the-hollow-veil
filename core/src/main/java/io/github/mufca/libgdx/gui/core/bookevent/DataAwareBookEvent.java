package io.github.mufca.libgdx.gui.core.bookevent;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class DataAwareBookEvent extends BaseBookEvent {
    private ObjectNode data;

    protected DataAwareBookEvent(String label) {
        super(label);
    }

    @Override
    public void setData(ObjectNode data) {
        this.data = data;
    }

    /**
     * Called by CoreModal to collect this step's output.
     */
    @Override
    public ObjectNode getData() {
        return data;
    }

    /**
     * Returns the current accumulated data it should be used by
     * extending class to access data
     */
    protected ObjectNode data() {
        return data;
    }
}
