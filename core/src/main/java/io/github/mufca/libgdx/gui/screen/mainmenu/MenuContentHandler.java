package io.github.mufca.libgdx.gui.screen.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;

public class MenuContentHandler {
    public static Actor getContent(CoreTypingLabel selected) {
        if (selected.getUnformattedText().equals("Licences")) {
            return LicenseReport.getReport();
        } else {
            return new CoreTypingLabel(selected.getUnformattedText() + " content");
        }
    }
}
