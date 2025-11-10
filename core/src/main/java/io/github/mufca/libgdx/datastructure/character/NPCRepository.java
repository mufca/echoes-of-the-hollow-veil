package io.github.mufca.libgdx.datastructure.character;

import java.io.IOException;

public class NPCRepository {

    private final CharacterDefinitionRepository definitionRepository;

    public NPCRepository() throws IOException {
        definitionRepository = new CharacterDefinitionRepository();
    }
}
