package io.github.mufca.libgdx.datastructure.location;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LazyLocationLoader {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseDir;
    @Getter
    private final Map<String, BaseLocation> cache = new HashMap<>();

    public LazyLocationLoader(String baseDir) {
        this.baseDir = baseDir;
    }

    public BaseLocation getLocation(String id) throws IOException {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        FileHandle file = Gdx.files.internal(baseDir + "/" + id + ".json");
        if (!file.exists()) {
            throw new FileNotFoundException("Location file not found: " + file.path());
        }

        BaseLocation loc = mapper.readValue(file.read(), BaseLocation.class);
        cache.put(id, loc);
        return loc;
    }
}
