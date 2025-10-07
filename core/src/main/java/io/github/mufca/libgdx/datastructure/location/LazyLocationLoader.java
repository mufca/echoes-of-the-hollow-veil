package io.github.mufca.libgdx.datastructure.location;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;

public class LazyLocationLoader {

    private static final String BASE_DIR = "locations";
    private static final String PATH_PATTERN = "%s/%s";
    private static final String JSON_EXTENSION = ".json";
    private static final String NO_ENTRY = "No index entry for resource with id: %s";
    private static final String LOCATION_FILE_NOT_FOUND = "Location file not found: %s.json";

    private final ObjectMapper mapper = new ObjectMapper();
    @Getter
    private final Map<String, BaseLocation> cache = new HashMap<>();
    @Getter
    private final Map<String, MapLocation> mapCache = new HashMap<>();
    private final Set<String> resourcePaths;

    public LazyLocationLoader() throws IOException {
        FileHandle index = Gdx.files.internal(BASE_DIR + "/index.txt");
        resourcePaths = index.readString().lines().map(String::trim).filter(Predicate.not(String::isEmpty)).collect(
            Collectors.toUnmodifiableSet());
        readEntireMap();
    }

    public BaseLocation getLocation(String id) throws IOException {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        String resourcePath = getLocationPath(id);
        FileHandle file = Gdx.files.internal(PATH_PATTERN.formatted(BASE_DIR, resourcePath));
        if (!file.exists()) {
            throw new FileNotFoundException(LOCATION_FILE_NOT_FOUND.formatted(file.path()));
        }

        BaseLocation loc = mapper.readValue(file.read(), BaseLocation.class);
        cache.put(id, loc);
        return loc;
    }

    public MapLocation getMapLocation(BaseLocation location) {
        return mapCache.get(location.getTargetId());
    }

    private String getLocationPath(String targetId) throws FileNotFoundException {
        return resourcePaths.stream()
            .filter(path -> path.endsWith(targetId + JSON_EXTENSION))
            .findFirst()
            .orElseThrow(() -> new FileNotFoundException(NO_ENTRY.formatted(targetId)));
    }

    private void readEntireMap() throws IOException {
        for (String resourcePath : resourcePaths) {
            FileHandle file = Gdx.files.internal(PATH_PATTERN.formatted(BASE_DIR, resourcePath));
            MapLocation mapLocation = mapper.readValue(file.read(), MapLocation.class);
            mapCache.put(mapLocation.targetId(), mapLocation);
        }
    }
}
