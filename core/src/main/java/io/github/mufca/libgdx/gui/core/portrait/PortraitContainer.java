package io.github.mufca.libgdx.gui.core.portrait;

import static io.github.mufca.libgdx.util.UIHelper.DEBUG_TEXTURE;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

public final class PortraitContainer {

    private final Set<Entry> entries = new HashSet<>();
    private final Long characterId;

    private final PortraitRepository portraitRepository;

    public PortraitContainer(Long characterId, PortraitRepository portraitRepository) {
        this.characterId = characterId;
        this.portraitRepository = portraitRepository;
    }

    public void add(PortraitFile file) {
        var region = portraitRepository.getPortrait(characterId, file);
        entries.add(new Entry(characterId, file, region.isPresent(), region.orElse(null)));
    }

    public void updateIfNeeded() {
        for (Entry entry : entries) {
            if (!entry.isReady) {
                Optional<TextureRegion> region = portraitRepository.getPortrait(characterId, entry.portraitFile);
                region.ifPresent(presentRegion -> {
                    entry.region = presentRegion;
                    entry.isReady = true;
                });
            }
        }
    }

    public TextureRegion get(PortraitFile file) {
        return entries.stream()
            .filter(e -> e.portraitFile == file)
            .map(e -> e.region)
            .findFirst()
            .orElseThrow();
    }

    @Getter
    @EqualsAndHashCode(of = {"characterId", "portraitFile"})
    private static class Entry {

        private final PortraitFile portraitFile;
        private final Long characterId;

        @Setter
        private boolean isReady;

        @Setter
        private TextureRegion region;

        Entry(Long characterId, PortraitFile portraitFile, boolean ready, TextureRegion region) {
            this.characterId = characterId;
            this.portraitFile = portraitFile;
            this.isReady = ready;
            if (region != null) {
                this.region = region;
            } else {
                this.region = DEBUG_TEXTURE.getRegion();
                this.region.setRegionWidth(portraitFile().width());
                this.region.setRegionHeight(portraitFile().height());
            }
        }
    }
}
