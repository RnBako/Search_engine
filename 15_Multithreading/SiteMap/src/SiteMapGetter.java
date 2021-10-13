
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SiteMapGetter extends RecursiveTask<StringBuilder> {

    private Node node;

    public SiteMapGetter(Node node) {
        this.node = node;
    }

    @Override
    protected StringBuilder compute() {
        StringBuilder siteMap = new StringBuilder();
        siteMap.append(node.getValue() + "\n");
        List<SiteMapGetter> taskList = new ArrayList<>();
        for (Node child : node.getChildren()) {
            SiteMapGetter task = new SiteMapGetter(child);
            task.fork();
            taskList.add(task);
        }

        for (SiteMapGetter task : taskList) {
            StringBuilder site = task.join();
            if (!site.toString().isEmpty()) {
                siteMap.append("    " + site + "\n");
            }
        }
        return siteMap;
    }
}
