package io.github.mufca.libgdx.gui.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.mufca.libgdx.gui.core.bookevent.DataAwareBookEvent;
import io.github.mufca.libgdx.gui.core.widget.CoreTypingLabel;
import java.util.List;

public class ChooseRaceEvent extends DataAwareBookEvent {

    private String selectedRace = null;

    public ChooseRaceEvent(List<Actor> traverser) {
        super("Choose Race");

        CoreTypingLabel title = new CoreTypingLabel("Choose your race:");
        content().add(title).left().padBottom(10).row();

        addRaceOption(traverser, 'a', "Elf");
        addRaceOption(traverser, 'b', "Human");
        addRaceOption(traverser, 'c', "Orc");
        addRaceOption(traverser, 'd', "Dwarf");
    }

    private void addRaceOption(List<Actor> traverser, char key, String race) {
    }

    @Override
    public ObjectNode getData() {
        ObjectNode out = JsonNodeFactory.instance.objectNode();
        out.put("race", selectedRace);
        return out;
    }

    @Override
    public boolean canProceed() {
        return selectedRace != null;
    }
}