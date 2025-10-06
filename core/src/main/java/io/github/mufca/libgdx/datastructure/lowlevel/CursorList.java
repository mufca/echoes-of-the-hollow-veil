package io.github.mufca.libgdx.datastructure.lowlevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class CursorList<T> {

    private final List<T> list = new ArrayList<>();
    private T current;

    public void add(T item) {
        Objects.requireNonNull(item);
        list.add(item);
        if (current == null) {
            current = item;
        }
    }

    public Optional<T> current() {
        if (current != null) {
            return Optional.of(current);
        }
        return Optional.empty();
    }

    public boolean moveToNext() {
        int cursor = list.indexOf(current);
        if (cursor + 1 < list.size()) {
            current = list.get(cursor + 1);
            return true;
        }
        return false;
    }

    public boolean moveToPrevious() {
        int cursor = list.indexOf(current);
        if (cursor - 1 >= 0) {
            current = list.get(cursor - 1);
            return true;
        }
        return false;
    }

    public void reset() {
        current = list.isEmpty() ? null : list.getFirst();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean setCurrent(T newCurrent) {
        for (T element : list) {
            if (element == newCurrent) {
                current = newCurrent;
                return true;
            }
        }
        return false;
    }

    public Stream<T> stream() {
        return list.stream();
    }

    public boolean contains(T target) {
        return list.contains(target);
    }
}