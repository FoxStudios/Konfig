package net.foxes4life.konfig.bindables;

import java.util.ArrayList;
import java.util.List;

public class Bindable<T> {
    private T value;
    private T defaultValue;
    public List<OnChange<T>> valueChanged = new ArrayList<>();
    public List<OnChange<T>> defaultChanged = new ArrayList<>();

    public Bindable(T defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public boolean isDefault() {
        return value.equals(defaultValue);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;

        for (OnChange<T> listener : valueChanged) listener.change(value);
    }

    public T getDefault() {
        return defaultValue;
    }

    public void setDefault(T defaultValue) {
        this.defaultValue = defaultValue;

        for (OnChange<T> listener : defaultChanged) listener.change(defaultValue);
    }

    public void reset() {
        setValue(defaultValue);
    }
}
