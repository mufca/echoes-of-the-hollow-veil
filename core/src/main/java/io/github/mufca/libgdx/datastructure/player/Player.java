package io.github.mufca.libgdx.datastructure.player;

import static io.github.mufca.libgdx.datastructure.character.CharacterType.PLAYER;
import static io.github.mufca.libgdx.gui.core.portrait.PortraitFile.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.mufca.libgdx.datastructure.GameContext;
import io.github.mufca.libgdx.datastructure.character.AppearanceTraits;
import io.github.mufca.libgdx.datastructure.character.BaseCharacter;
import io.github.mufca.libgdx.datastructure.character.PrimaryStatistics;
import io.github.mufca.libgdx.datastructure.character.SecondaryStatistics;
import io.github.mufca.libgdx.gui.core.portrait.PortraitFile;
import io.github.mufca.libgdx.gui.core.portrait.PortraitHandler;
import java.util.EnumSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Player {

    private final BaseCharacter baseCharacter;
    private final AppearanceTraits appearanceTraits;
    private final PrimaryStatistics primaryStatistics;
    private final SecondaryStatistics secondaryStatistics;
    private final Long characterId;

    private TextureRegion small;
    private TextureRegion medium;
    private TextureRegion large;

    public Player(GameContext context) {
        characterId = context.idProvider().generateUniqueId();
        baseCharacter = new BaseCharacter(characterId, PLAYER, "Player");
        appearanceTraits = new AppearanceTraits(characterId, "tall", "slender", "elf");
        primaryStatistics = new PrimaryStatistics(characterId,
            10f, 10f, 10f,
            10f, 10f, 10f);
        secondaryStatistics = new SecondaryStatistics(characterId,
            100f, 100f, 100f);
        var portraitHandler = new PortraitHandler(context, characterId, "mikki");
        portraitHandler.loadAndRegister(EnumSet.allOf(PortraitFile.class));
        small = context.portraitRepository().getPortrait(characterId, SMALL);
        medium = context.portraitRepository().getPortrait(characterId, MEDIUM);
        large = context.portraitRepository().getPortrait(characterId, LARGE);
    }
}
