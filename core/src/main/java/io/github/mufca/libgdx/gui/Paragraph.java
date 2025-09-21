package io.github.mufca.libgdx.gui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import io.github.mufca.libgdx.constant.AssetConstants;
import io.github.mufca.libgdx.temporarytrash.ParagraphParameters;
import io.github.mufca.libgdx.temporarytrash.TextProcessor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static io.github.mufca.libgdx.constant.AssetConstants.ORC_FEMALE_PORTRAIT;

public class Paragraph extends Table {
    private final CoreTypingLabel typingLabel;
    private final Image image;

    public Paragraph(ParagraphParameters paragraphParameters) {
        super();
        NinePatch ninePatch = new NinePatch(AssetConstants.NINE_PATCH_FRAME, 96, 95, 105, 96);
        NinePatchDrawable drawable = new NinePatchDrawable(ninePatch);
        this.background(drawable);
        String processedText = TextProcessor.processText(paragraphParameters.text());
        typingLabel = new CoreTypingLabel(processedText);
        typingLabel.setWrap(true);
        typingLabel.skipToTheEnd();
        image = new Image(ORC_FEMALE_PORTRAIT);
    }

    public void addComponents() {
        this.add(image).left().top().padRight(10f);
        this.add(typingLabel).left().top().expandX().fillX();
    }

    @Override
    public boolean remove() {
        typingLabel.remove();
        image.remove();
        return super.remove();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
