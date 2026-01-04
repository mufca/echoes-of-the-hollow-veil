package io.github.mufca.libgdx.gui.core.interfaces.implementations;

import static io.github.mufca.libgdx.util.SafeCast.explicitCeiling;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import io.github.mufca.libgdx.datastructure.command.Command;
import io.github.mufca.libgdx.datastructure.shortcut.ShortcutBinding;
import io.github.mufca.libgdx.gui.core.icon.KeycapIcon;
import io.github.mufca.libgdx.gui.core.interfaces.WithShortcut;
import lombok.Setter;

public class ActorWithShortcut<T extends Actor> extends WidgetGroup implements WithShortcut {

    private final T targetActor;
    private final KeycapIcon icon;
    private final ShortcutBinding binding;
    private final int gap = 7;

    @Setter
    private boolean isActive;
    private boolean isFocused;

    public ActorWithShortcut(T targetActor, KeycapIcon icon, Command command) {
        this.targetActor = targetActor;
        this.icon = icon;
        this.binding = new ShortcutBinding(icon.key().keycode(), command);
        addActor(icon);
        addActor(targetActor);
    }

    @Override
    public void layout() {
        int iconW = explicitCeiling(icon.getWidth());
        int iconH = explicitCeiling(icon.getHeight());

        int targetPrefW = explicitCeiling(
            (targetActor instanceof Layout l) ? l.getPrefWidth() : targetActor.getWidth());
        int targetPrefH = explicitCeiling(
            (targetActor instanceof Layout l) ? l.getPrefHeight() : targetActor.getHeight());

        icon.setSize(iconW, iconH);
        targetActor.setSize(targetPrefW, targetPrefH);

        int neededHeight = Math.max(iconH, targetPrefH);

        int assignedHeight = explicitCeiling(getHeight());

        int contentY = explicitCeiling((assignedHeight - neededHeight) / 2f);
        int iconY = contentY + explicitCeiling((neededHeight - iconH) / 2f);
        int targetY = contentY + explicitCeiling((neededHeight - targetPrefH) / 2f);

        icon.setPosition(0, iconY);

        int targetX = iconW + gap;
        targetActor.setPosition(targetX, targetY);

        if (targetActor instanceof Layout l) {
            l.validate();
        }
    }

    @Override
    public void act(float delta) {
        targetActor.act(delta);
        icon.act(delta);
    }

    @Override
    public float getPrefWidth() {
        int targetW = explicitCeiling((targetActor instanceof Layout l) ? l.getPrefWidth() : targetActor.getWidth());
        return explicitCeiling(icon.getWidth()) + gap + targetW;
    }

    @Override
    public float getPrefHeight() {
        int targetH = explicitCeiling((targetActor instanceof Layout l) ? l.getPrefHeight() : targetActor.getHeight());
        return Math.max(explicitCeiling(icon.getHeight()), targetH);
    }

    @Override
    public T getTarget() {
        return targetActor;
    }

    @Override
    public KeycapIcon getIcon() {
        return icon;
    }

    @Override
    public ShortcutBinding getShortcutBinding() {
        return binding;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void focus() {
        isFocused = true;
    }

    @Override
    public void forget() {
        isFocused = false;
    }

    @Override
    public boolean isFocused() {
        return isFocused;
    }
}