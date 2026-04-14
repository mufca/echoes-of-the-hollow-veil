package io.github.mufca.libgdx.system.time;

import lombok.Getter;

@Getter
public enum Ticks {
    SECOND(5L),
    FIVE_SECONDS(SECOND.duration * 5),
    TEN_SECONDS(SECOND.duration * 10),
    FIFTEENTH_SECONDS(SECOND.duration * 15),
    THIRTY_SECONDS(SECOND.duration * 30),
    MINUTE(SECOND.duration * 60);

    private final long duration;

    Ticks(long duration) {
        this.duration = duration;
    }


}
