package io.github.mufca.libgdx.datastructure.player;

import static io.github.mufca.libgdx.datastructure.character.logic.components.CharacterType.PLAYER;

import io.github.mufca.libgdx.datastructure.character.logic.components.AppearanceTraits;
import io.github.mufca.libgdx.datastructure.character.logic.components.BaseCharacter;
import io.github.mufca.libgdx.datastructure.character.logic.components.PrimaryStatistics;
import io.github.mufca.libgdx.datastructure.character.logic.components.SecondaryStatistics;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.gui.core.portrait.PortraitContainer;
import io.github.mufca.libgdx.gui.core.portrait.PortraitFile;
import io.github.mufca.libgdx.gui.core.portrait.PortraitHandler;
import io.github.mufca.libgdx.gui.core.portrait.PortraitRepository;
import java.util.EnumSet;
import lombok.Getter;

@Getter
public final class Player {

    private final BaseCharacter baseCharacter;
    private final AppearanceTraits appearanceTraits;
    private final PrimaryStatistics primaryStatistics;
    private final SecondaryStatistics secondaryStatistics;
    private final Long characterId;

    private final PortraitContainer portraits;

    public Player(IdProvider idProvider, PortraitRepository portraitRepository) {
        characterId = idProvider.generateUniqueId();
        baseCharacter = new BaseCharacter(characterId, PLAYER, "Player");
        appearanceTraits = new AppearanceTraits(characterId, "tall", "slender", "elf");
        primaryStatistics = new PrimaryStatistics(characterId,
            10f, 10f, 10f,
            10f, 10f, 10f);
        secondaryStatistics = new SecondaryStatistics(characterId,
            100f, 100f, 100f);
        var portraitHandler = new PortraitHandler(characterId, "mikki", portraitRepository);
        EnumSet<PortraitFile> portraitFiles = EnumSet.allOf(PortraitFile.class);
        portraitHandler.loadAndRegister(portraitFiles);
        portraits = new PortraitContainer(characterId, portraitRepository);
        portraitFiles.forEach(portraits::add);
    }
}
