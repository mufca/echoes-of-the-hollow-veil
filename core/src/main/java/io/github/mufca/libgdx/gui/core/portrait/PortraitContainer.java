package io.github.mufca.libgdx.gui.core.portrait;

import static io.github.mufca.libgdx.util.UIHelper.DEBUG_TEXTURE;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.mufca.libgdx.util.LogHelper;
import java.util.EnumMap;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

public final class PortraitContainer {

    private static final String EVICTED_FROM_CACHE = "Portrait evicted from cache for: %d";
    private final EnumMap<PortraitFile, Entry> entries = new EnumMap<>(PortraitFile.class);
    private final Long characterId;

    private final PortraitRepository portraitRepository;

    public PortraitContainer(Long characterId, PortraitRepository portraitRepository) {
        this.characterId = characterId;
        this.portraitRepository = portraitRepository;
    }

    public void add(PortraitFile file) {
        var region = portraitRepository.getPortrait(characterId, file);
        entries.put(file, new Entry(characterId, file, region.isPresent(), region.orElse(null)));
    }

    public boolean updateIfNeeded() {
        boolean anyChangedInThisTick = false;

        for (Entry entry : entries.values()) {
            Optional<TextureRegion> currentFromCache = portraitRepository.getPortrait(characterId, entry.portraitFile);

            if (currentFromCache.isPresent()) {
                TextureRegion newRegion = currentFromCache.get();
                if (!entry.isReady || entry.region != newRegion) {
                    entry.region(newRegion);
                    entry.isReady(true);
                    anyChangedInThisTick = true;
                }
            } else {
                if (entry.isReady) {
                    entry.region(DEBUG_TEXTURE.getRegion());
                    entry.isReady(false);
                    anyChangedInThisTick = true;
                    LogHelper.debug(this, EVICTED_FROM_CACHE.formatted(characterId));
                }
            }
        }
        return anyChangedInThisTick;
    }

    public TextureRegion get(PortraitFile file) {
        return entries.get(file).region;
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
            }
        }
    }
}
