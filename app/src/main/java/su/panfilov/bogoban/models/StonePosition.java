package su.panfilov.bogoban.models;

import java.util.Objects;

public class StonePosition {
    private final int left;
    private final int top;

    public StonePosition(int left, int top) {
        this.left = left;
        this.top = top;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StonePosition that = (StonePosition) o;
        return left == that.left &&
                top == that.top;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, top);
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }
}
