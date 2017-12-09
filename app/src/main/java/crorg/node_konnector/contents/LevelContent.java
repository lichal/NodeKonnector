package crorg.node_konnector.contents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class LevelContent implements Serializable {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<LevelItem> ITEMS = new ArrayList<LevelItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, LevelItem> ITEM_MAP = new HashMap<String, LevelItem>();

    public static int COUNT = 5;

    private static void numItems(){

    }

    public static void createList(int count){
        int size = ITEMS.size();
        while (size < count){
            addItem(createLevelItem(size+1));
            size++;
        }
    }

    private static void addItem(LevelItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static LevelItem createLevelItem(int position) {
        return new LevelItem(String.valueOf(position), (position + 1) + " Nodes", makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class LevelItem {
        public final String id;
        public final String content;
        public final String details;

        public LevelItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
