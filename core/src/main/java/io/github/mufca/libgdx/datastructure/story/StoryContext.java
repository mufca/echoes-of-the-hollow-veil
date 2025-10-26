package io.github.mufca.libgdx.datastructure.story;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public final class StoryContext {

    private final Map<Class<? extends Enum<?>>, EnumSet<?>> factSets = new HashMap<>();

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>> EnumSet<E> getSet(Class<E> type) {
        return (EnumSet<E>) factSets
            .computeIfAbsent(type, t -> EnumSet.noneOf(type));
    }

    public <E extends Enum<E>> void add(E fact) {
        getSet(fact.getDeclaringClass()).add(fact);
    }

    public <E extends Enum<E>> boolean has(E fact) {
        return getSet(fact.getDeclaringClass()).contains(fact);
    }

    public <E extends Enum<E>> void remove(E fact) {
        getSet(fact.getDeclaringClass()).remove(fact);
    }

}