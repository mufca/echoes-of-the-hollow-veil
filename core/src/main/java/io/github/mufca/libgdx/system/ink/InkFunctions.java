package io.github.mufca.libgdx.system.ink;

import static io.github.mufca.libgdx.constant.PathConstants.INK_FUNCTIONS;

public class InkFunctions extends InkStory {

    public InkFunctions() {
        super(INK_FUNCTIONS);
    }

    public String getSunDescription(int sunPosition) {
        return evaluate("sunEvent", sunPosition);
    }

    public String getMovementMessage(String direction, boolean isPresent) {
        return evaluate("movementMessage", direction, isPresent);
    }

    public String getStatFlavorParts(String statCode) {
        return evaluate("statFlavors", statCode);
    }
}
