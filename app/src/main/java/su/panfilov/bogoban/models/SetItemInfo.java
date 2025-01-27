package su.panfilov.bogoban.models;

public class SetItemInfo {
    public String background;
    public String title;
    public String id;

    public SetItemInfo() {

    }

    public SetItemInfo(String title, String background) {
        this.title = title;
        this.background = background;
    }

    public SetItemInfo(String id, String title, String background) {
        this.id = id;
        this.title = title;
        this.background = background;
    }
}
