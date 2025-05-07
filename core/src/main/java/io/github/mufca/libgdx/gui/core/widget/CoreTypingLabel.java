package io.github.mufca.libgdx.gui.core.widget;

import com.github.tommyettinger.textra.TypingLabel;
import io.github.mufca.libgdx.util.UIHelper;

public class CoreTypingLabel extends TypingLabel {
    private String displayedText;
    public CoreTypingLabel(String displayedText) {
        super(displayedText, UIHelper.getDefaultFont());
        this.displayedText = displayedText;
    }

    public void underlineWholeText() {
        this.setText("[_]"+displayedText+"[/_]",true);
    }

    public void removeWholeTextUnderline() {
        this.setText(displayedText,true);
    }

    public String getUnformattedText() {
        return this.displayedText;
    }
}
