package io.github.mufca.libgdx.datastructure.player;

import static com.badlogic.gdx.graphics.Color.PINK;
import static io.github.mufca.libgdx.datastructure.character.CharacterType.PLAYER;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.mufca.libgdx.datastructure.GameContext;
import io.github.mufca.libgdx.datastructure.character.AppearanceTraits;
import io.github.mufca.libgdx.datastructure.character.BaseCharacter;
import io.github.mufca.libgdx.datastructure.character.PrimaryStatistics;
import io.github.mufca.libgdx.datastructure.character.SecondaryStatistics;
import io.github.mufca.libgdx.gui.core.portrait.PortraitFile;
import io.github.mufca.libgdx.gui.core.portrait.PortraitHandler;
import io.github.mufca.libgdx.util.UIHelper;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import lombok.AccessLevel;
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

    private List<PortraitStorage> portraits = new ArrayList<>(3);

    @Getter(AccessLevel.NONE)
    private final GameContext context;

    public Player(GameContext context) {
        characterId = context.idProvider().generateUniqueId();
        baseCharacter = new BaseCharacter(characterId, PLAYER, "Player");
        appearanceTraits = new AppearanceTraits(characterId, "tall", "slender", "elf");
        primaryStatistics = new PrimaryStatistics(characterId,
            10f, 10f, 10f,
            10f, 10f, 10f);
        secondaryStatistics = new SecondaryStatistics(characterId,
            100f, 100f, 100f);
        this.context = context;
        var portraitHandler = new PortraitHandler(context, characterId, "mikki");
        EnumSet<PortraitFile> portraitFiles = EnumSet.allOf(PortraitFile.class);
        portraitHandler.loadAndRegister(portraitFiles);
        portraitFiles.forEach(portraitFile -> portraits.add(
            new PortraitStorage(false, portraitFile,
                context.portraitRepository().getPortrait(characterId, portraitFile))
        ));
    }

    public void updatePortraitsIfNeeded() {
        portraits.forEach(this::updatePortraitIfNeeded);
    }

    private void updatePortraitIfNeeded(PortraitStorage portrait) {
        TextureRegion region = context.portraitRepository().getPortrait(characterId, portrait.portraitFile);
        if (portrait.isReady()) {
            return;
        }
        if (region != null) {
            portrait.region(region);
            portrait.isReady(true);
        }
    }

    public TextureRegion getPortrait(PortraitFile portraitFile) {
        return portraits.stream()
            .filter(portrait -> portrait.portraitFile == portraitFile)
            .map(PortraitStorage::region)
            .findFirst().orElseThrow();
    }

    @Getter
    private class PortraitStorage {

        private final PortraitFile portraitFile;
        @Setter
        private boolean isReady;
        @Setter
        private TextureRegion region;

        private PortraitStorage(boolean ready, PortraitFile portraitFile, TextureRegion region) {
            this.portraitFile = portraitFile;
            this.isReady = ready;
            if (region != null) {
                this.region = region;
            } else {
                this.region = UIHelper.getFilledColor(PINK).getRegion();
                this.region.setRegionWidth(230);
                this.region.setRegionHeight(300);
            }
        }
    }
}
