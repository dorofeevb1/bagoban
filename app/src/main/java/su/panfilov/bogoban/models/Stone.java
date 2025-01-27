package su.panfilov.bogoban.models;

public class Stone {
    private final StoneSize size;
    private final StoneColor color;
    private final StonePosition position;

    public Stone(StoneSize size, StoneColor color, StonePosition position) {
        this.size = size;
        this.color = color;
        this.position = position;
    }

    public StoneSize getSize() {
        return size;
    }

    public StoneColor getColor() {
        return color;
    }

    public StonePosition getPosition() {
        return position;
    }
}
