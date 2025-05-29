package io.github.mufca.libgdx.datastructure.keyprovider;

import com.badlogic.gdx.Input;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum KeyboardLayout {
    QWERTY_ENGLISH(
        // PREFERED_NUMERICS
        List.of(
            new KeyboardKey(Input.Keys.NUM_1, '1'),
            new KeyboardKey(Input.Keys.NUM_2, '2'),
            new KeyboardKey(Input.Keys.NUM_3, '3'),
            new KeyboardKey(Input.Keys.NUM_4, '4'),
            new KeyboardKey(Input.Keys.NUM_5, '5'),
            new KeyboardKey(Input.Keys.NUM_6, '6')
        ),
        // PREFERED_LETTERS
        List.of(
            new KeyboardKey(Input.Keys.Q, 'Q'),
            new KeyboardKey(Input.Keys.W, 'W'),
            new KeyboardKey(Input.Keys.E, 'E'),
            new KeyboardKey(Input.Keys.R, 'R'),
            new KeyboardKey(Input.Keys.T, 'T'),
            new KeyboardKey(Input.Keys.A, 'A'),
            new KeyboardKey(Input.Keys.S, 'S'),
            new KeyboardKey(Input.Keys.D, 'D'),
            new KeyboardKey(Input.Keys.F, 'F'),
            new KeyboardKey(Input.Keys.Z, 'Z'),
            new KeyboardKey(Input.Keys.X, 'X'),
            new KeyboardKey(Input.Keys.C, 'C')
        ),
        // THE_REST
        List.of(
            new KeyboardKey(Input.Keys.NUM_7, '7'),
            new KeyboardKey(Input.Keys.NUM_8, '8'),
            new KeyboardKey(Input.Keys.NUM_9, '9'),
            new KeyboardKey(Input.Keys.NUM_0, '0'),

            new KeyboardKey(Input.Keys.Y, 'Y'),
            new KeyboardKey(Input.Keys.U, 'U'),
            new KeyboardKey(Input.Keys.I, 'I'),
            new KeyboardKey(Input.Keys.O, 'O'),
            new KeyboardKey(Input.Keys.P, 'P'),

            new KeyboardKey(Input.Keys.G, 'G'),
            new KeyboardKey(Input.Keys.H, 'H'),
            new KeyboardKey(Input.Keys.J, 'J'),
            new KeyboardKey(Input.Keys.K, 'K'),
            new KeyboardKey(Input.Keys.L, 'L'),

            new KeyboardKey(Input.Keys.V, 'V'),
            new KeyboardKey(Input.Keys.B, 'B'),
            new KeyboardKey(Input.Keys.N, 'N'),
            new KeyboardKey(Input.Keys.M, 'M')
        )
    ),
    QWERTZ_GERMAN(
        // PREFERED_NUMERICS
        copyWithOverrides(QWERTY_ENGLISH.preferredNumerics()),
        // PREFERED_LETTERS (Z ↔ Y)
        copyWithOverrides(
            QWERTY_ENGLISH.preferredLetters(),
            new KeyboardKey(Input.Keys.Z, 'Y')
        ),
        // THE_REST
        copyWithOverrides(
            QWERTY_ENGLISH.theRest(),
            new KeyboardKey(Input.Keys.Y, 'Z')
        )
    ),
    AZERTY_FRENCH(
        // PREFERED_NUMERICS
        copyWithOverrides(QWERTY_ENGLISH.preferredNumerics(),
            new KeyboardKey(Input.Keys.NUM_1, '&'),
            new KeyboardKey(Input.Keys.NUM_2, 'é'),
            new KeyboardKey(Input.Keys.NUM_3, '"'),
            new KeyboardKey(Input.Keys.NUM_4, '\''),
            new KeyboardKey(Input.Keys.NUM_5, '('),
            new KeyboardKey(Input.Keys.NUM_6, '-')
        ),
        // PREFERED_LETTERS (A ↔ Q, Z ↔ W)
        copyWithOverrides(
            QWERTY_ENGLISH.preferredLetters(),
            new KeyboardKey(Input.Keys.Q, 'A'),
            new KeyboardKey(Input.Keys.A, 'Q'),
            new KeyboardKey(Input.Keys.W, 'Z'),
            new KeyboardKey(Input.Keys.Z, 'W')
        ),
        // THE_REST
        copyWithOverrides(
            QWERTY_ENGLISH.theRest(),
            new KeyboardKey(Input.Keys.NUM_7, 'è'),
            new KeyboardKey(Input.Keys.NUM_8, '_'),
            new KeyboardKey(Input.Keys.NUM_9, 'ç'),
            new KeyboardKey(Input.Keys.NUM_0, 'à')
        )
    );

    private final List<KeyboardKey> preferredNumerics;
    private final List<KeyboardKey> preferredLetters;
    private final List<KeyboardKey> theRest;

    KeyboardLayout(
        List<KeyboardKey> preferredNumerics,
        List<KeyboardKey> preferredLetters,
        List<KeyboardKey> theRest
    ) {
        this.preferredNumerics = preferredNumerics;
        this.preferredLetters = preferredLetters;
        this.theRest = theRest;
    }

    private static List<KeyboardKey> copyWithOverrides(
        List<KeyboardKey> base, KeyboardKey... overrides
    ) {
        Map<Integer, KeyboardKey> map = new LinkedHashMap<>();
        for (KeyboardKey key : base) {
            map.put(key.keycode(), key);
        }
        for (KeyboardKey override : overrides) {
            map.put(override.keycode(), override);
        }
        return List.copyOf(map.values());
    }

    public List<KeyboardKey> preferredNumerics() {
        return preferredNumerics;
    }

    public List<KeyboardKey> preferredLetters() {
        return preferredLetters;
    }

    public List<KeyboardKey> theRest() {
        return theRest;
    }

    public List<List<KeyboardKey>> getAll() {
        return List.of(preferredNumerics,preferredLetters,theRest);
    }
}
