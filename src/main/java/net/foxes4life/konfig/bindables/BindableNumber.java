package net.foxes4life.konfig.bindables;

public class BindableNumber<T extends Number> extends Bindable<T> {
    private T min;
    private T max;

    public BindableNumber(T defaultValue) {
        super(defaultValue);
    }

    @Override
    public void setValue(T value) {
        if (min != null && value.doubleValue() < min.doubleValue()) {
            value = min;
        } else if (max != null && value.doubleValue() > max.doubleValue()) {
            value = max;
        }

        super.setValue(value);
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }
}
