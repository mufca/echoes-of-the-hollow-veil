package io.github.mufca.libgdx.gui.screen.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;

public class LicenseReport {
    private static ScrollPane report;

    private LicenseReport() {
    }

    public static ScrollPane getReport() {
        if (report == null) {
            String licenseText = Gdx.files.internal("licenses/THIRD-PARTY-NOTICES.txt").readString("UTF-8");

            CoreTypingLabel label = new CoreTypingLabel(licenseText);
            label.skipToTheEnd();
            label.setWrap(true);

            report = new ScrollPane(label);
            report.setFillParent(true);
            report.setFadeScrollBars(false);
            report.setScrollingDisabled(true, false);
        }

        return report;
    }

}
