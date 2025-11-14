package io.github.mufca.libgdx.gui.core.portrait;

import static com.badlogic.gdx.graphics.Color.PINK;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.mufca.libgdx.util.UIHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

public final class PortraitContainer {

    private final List<Entry> entries = new ArrayList<>();
    private final Long characterId;

    private final PortraitRepository portraitRepository;

    public PortraitContainer(Long characterId, PortraitRepository portraitRepository) {
        this.characterId = characterId;
        this.portraitRepository = portraitRepository;
    }

    public void add(PortraitFile file) {
        var region = portraitRepository.getPortrait(characterId, file);
        entries.add(new Entry(file, region.isPresent(), region.orElse(null)));
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
    private static class Entry {

        private final PortraitFile portraitFile;

        @Setter
        private boolean isReady;

        @Setter
        private TextureRegion region;

        Entry(PortraitFile portraitFile, boolean ready, TextureRegion region) {
            this.portraitFile = portraitFile;
            this.isReady = ready;
            if (region != null) {
                this.region = region;
            } else {
                this.region = UIHelper.getFilledColor(PINK).getRegion();
                this.region.setRegionWidth(portraitFile().width());
                this.region.setRegionHeight(portraitFile().height());
            }
        }
    }
}
