
import java.util.ArrayList;
import java.util.Collection;

public class Node {

    private final String url;
    private final Collection<Node> children = new ArrayList<>();

    public Node (String url) {
        this.url = url;
    }

    public void addChildren(Node child) {
        children.add(child);
    }

    public Collection<Node> getChildren(){
        return children;
    }

    public String getValue() {
        return url;
    }
}