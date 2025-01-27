package su.panfilov.bogoban.models;

public class HelpItem {
    private final String title;
    private final String text;

    public HelpItem(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
