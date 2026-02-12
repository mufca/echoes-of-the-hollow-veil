package io.github.mufca.libgdx.datastructure.location;

import io.github.mufca.libgdx.datastructure.location.logic.BaseLocation;
import java.io.IOException;

public interface CurrentLocationProvider {

    BaseLocation currentLocation();

    void currentLocation(String locationId) throws IOException;
}
