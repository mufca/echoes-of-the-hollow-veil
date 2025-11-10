package io.github.mufca.libgdx.datastructure.character;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mufca.libgdx.datastructure.character.jsondata.CharacterData;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class CharacterDefinitionRepository {

    private static final String BASE_DIR = "characters";
    private static final String INDEX_PATH = BASE_DIR + "/index.txt";

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, CharacterData> characterCache = new HashMap<>();
    private final Set<String> resourcePaths;

    public CharacterDefinitionRepository() throws IOException {

        FileHandle index = Gdx.files.internal(INDEX_PATH);
        this.resourcePaths = index.readString()
            .lines()
            .map(String::trim)
            .filter(Predicate.not(String::isEmpty))
            .collect(Collectors.toUnmodifiableSet());

        readAllCharacters();
    }

    private void readAllCharacters() throws IOException {
        for (String relativePath : resourcePaths) {
            FileHandle file = Gdx.files.internal(BASE_DIR + "/" + relativePath);
            CharacterData data = mapper.readValue(file.read(), CharacterData.class);
            characterCache.putIfAbsent(data.id(), data);
        }
    }

    public Optional<CharacterData> get(String npcId) {
        return Optional.ofNullable(characterCache.get(npcId));
    }
}