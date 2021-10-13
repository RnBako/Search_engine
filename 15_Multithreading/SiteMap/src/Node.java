
import java.util.ArrayList;
import java.util.Collection;

public class Node {

    private String url;
    private Collection<Node> children = new ArrayList<>();

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