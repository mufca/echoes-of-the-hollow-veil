package io.github.mufca.libgdx.gui.core.portrait;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import io.github.mufca.libgdx.datastructure.GameContext;
import io.github.mufca.libgdx.util.LogHelper;
import java.util.EnumSet;

public final class PortraitHandler {

    private static final String WRONG_SIZE = "Given portrait image %s has wrong size: %d x %d. Expected %d x %d";
    private static final String PORTRAIT_NOT_FOUND = "Portrait image not found: %s";
    private static final String DIRECTORY_PATTERN = "portraits/%s";
    private final Long characterId;
    private final FileHandle portraitDirectory;
    private final GameContext context;

    public PortraitHandler(GameContext context, Long characterId, String portraitName) {
        String path = DIRECTORY_PATTERN.formatted(portraitName);
        this.portraitDirectory = Gdx.files.internal(path);
        this.characterId = characterId;
        this.context = context;
    }

    public void loadAndRegister(EnumSet<PortraitFile> portraitFiles) {
        for (PortraitFile portraitFile : portraitFiles) {
            checkSize(portraitFile);
            FileHandle handle = portraitDirectory.child(portraitFile.filename());
            context.portraitRepository().loadPortraitAsync(characterId, handle, portraitFile);
        }
    }

    private void checkSize(PortraitFile portraitFile) {
        FileHandle portraitHandle = portraitDirectory.child(portraitFile.filename());
        if (!portraitHandle.exists()) {
            var message = PORTRAIT_NOT_FOUND.formatted(portraitHandle.name());
            LogHelper.error(this, message);
            throw new RuntimeException(message);
        }
        var pixmap = new Pixmap(portraitHandle);
        var pixmapWidth = pixmap.getWidth();
        var pixmapHeight = pixmap.getHeight();
        pixmap.dispose();
        if ((pixmapWidth != portraitFile.width()) || (pixmapHeight != portraitFile.height())) {
            var message = WRONG_SIZE.formatted(portraitHandle.name(),
                pixmapWidth, pixmapHeight,
                portraitFile.width(), portraitFile.height());
            LogHelper.error(this, message);
            throw new RuntimeException(message);
        }
    }
}