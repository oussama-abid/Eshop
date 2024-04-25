import java.util.ArrayList;
import java.util.List;

public class Warenkorb {
    private List<WarenkorbArticle> items = new ArrayList<>();


    public Warenkorb(List<WarenkorbArticle> items) {
        super();
        this.items = items;
    }

    public List<WarenkorbArticle> getItems() {
        return items;
    }

    public void setItems(List<WarenkorbArticle> items) {
        this.items = items;
    }











}
