package io.github.mufca.libgdx.util;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonHelper {

    /**
     * Merges updates into the base JSON object without modifying either. Returns a new ObjectNode.
     */
    public static ObjectNode merge(ObjectNode base, ObjectNode updates) {
        if (base == null && updates == null) {
            return null;
        }
        if (base == null) {
            return updates.deepCopy();
        }
        if (updates == null) {
            return base.deepCopy();
        }

        ObjectNode merged = base.deepCopy();
        merged.setAll(updates);
        return merged;
    }
}
