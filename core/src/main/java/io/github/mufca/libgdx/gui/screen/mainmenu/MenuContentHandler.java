package io.github.mufca.libgdx.gui.screen.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;

public class MenuContentHandler {

    public static Actor getContent(CoreTypingLabel selected) {
        return switch (selected.getUnformattedText()) {
            case "Licences" -> LicenseReport.getReport();
            default -> new CoreTypingLabel(selected.getUnformattedText() + " content");
        };
    }
}
