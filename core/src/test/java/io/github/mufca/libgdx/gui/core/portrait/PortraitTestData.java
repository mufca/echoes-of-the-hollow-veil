package io.github.mufca.libgdx.gui.core.portrait;

import static io.github.mufca.libgdx.gui.core.portrait.PortraitFile.LARGE;
import static io.github.mufca.libgdx.gui.core.portrait.PortraitFile.MEDIUM;
import static io.github.mufca.libgdx.gui.core.portrait.PortraitFile.SMALL;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class PortraitTestData {

    private static final String DIRECTORY_PATTERN = "stress_character_%d";
    private final Set<PortraitGenerationParameter> stressTest = getStressTestSet();

    public PortraitTestData() {
    }

    public Stream<Arguments> getPortraits() {
        return Stream.of(
            Arguments.of(Set.of(
                new PortraitGenerationParameter(1L, "character_1", SMALL),
                new PortraitGenerationParameter(1L, "character_1", MEDIUM),
                new PortraitGenerationParameter(1L, "character_1", LARGE)
            )),
            Arguments.of(Set.of(
                new PortraitGenerationParameter(1L, "character_1", SMALL),
                new PortraitGenerationParameter(1L, "character_2", SMALL),
                new PortraitGenerationParameter(1L, "character_3", SMALL),
                new PortraitGenerationParameter(1L, "character_4", SMALL),
                new PortraitGenerationParameter(1L, "character_5", SMALL),
                new PortraitGenerationParameter(1L, "character_6", SMALL),
                new PortraitGenerationParameter(2L, "character_7", SMALL),
                new PortraitGenerationParameter(2L, "character_8", SMALL),
                new PortraitGenerationParameter(2L, "character_9", SMALL),
                new PortraitGenerationParameter(2L, "character_10", SMALL),
                new PortraitGenerationParameter(2L, "character_11", SMALL),
                new PortraitGenerationParameter(2L, "character_12", SMALL)
            ))
        );
    }

    public Stream<Arguments> getStressData() {
        return Stream.of(
            Arguments.of(stressTest)
        );
    }

    private Set<PortraitGenerationParameter> getStressTestSet() {
        Set<PortraitGenerationParameter> portraitGenerationParameters = new HashSet<>();
        IntStream.range(0, 300).forEach(i -> {
            long characterId = i;
            String directory = DIRECTORY_PATTERN.formatted(i);
            portraitGenerationParameters.add(new PortraitGenerationParameter(characterId, directory, LARGE));
        });
        return portraitGenerationParameters;
    }
}
