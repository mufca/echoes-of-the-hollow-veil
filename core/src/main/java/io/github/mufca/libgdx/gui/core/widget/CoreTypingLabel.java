package io.github.mufca.libgdx.gui.core.widget;

import com.github.tommyettinger.textra.TypingLabel;
import io.github.mufca.libgdx.util.UIHelper;
import lombok.Setter;

public class CoreTypingLabel extends TypingLabel {

    private final String displayedText;
    @Setter
    private String keyIndicator = "";

    public CoreTypingLabel(String displayedText) {
        super(displayedText, UIHelper.getDefaultFont());
        this.displayedText = displayedText;
    }

    public void underlineWholeText() {
        this.setText("[_]" + displayedText + "[/_]", true);
    }

    public void setPrefix(String prefix) {
        this.setText(prefix + displayedText, true);
    }

    public void regenerate() {
        this.setText(keyIndicator + displayedText, true);
    }

    public String getUnformattedText() {
        return this.displayedText;
    }
}
