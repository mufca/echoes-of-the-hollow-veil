package io.github.mufca.libgdx.datastructure.character.logic.presets;

import java.util.List;
import lombok.Getter;

@Getter
public enum AppearancePreset {

    WOLF(
        List.of("black"),
        List.of("haunting")
    );

    private final List<String> firstTrait;
    private final List<String> secondTrait;

    AppearancePreset(List<String> firstTrait, List<String> secondTrait) {
        this.firstTrait = firstTrait;
        this.secondTrait = secondTrait;
    }
}
