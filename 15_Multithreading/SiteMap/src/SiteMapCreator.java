import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

public class SiteMapCreator extends RecursiveTask<Node> {

    private final Node node;
    private static Map<String, Node> siteList = new ConcurrentHashMap<>();

    public SiteMapCreator (String url) {
        this.node = new Node(url);
        siteList.put(url, this.node);
    }

    @Override
    protected Node compute() {
        List<SiteMapCreator> taskList = new ArrayList<>();
        try {
            Thread.sleep(120);
            Document document = Jsoup.connect(node.getValue()).maxBodySize(0).get();
            Elements hrefUI = document.select("a");
            for (int i = 0; i < hrefUI.size(); i++) {
                if (hrefUI.get(i).attr("href").isEmpty()) {
                    continue;
                } else if (hrefUI.get(i).attr("href").charAt(0) == '/' && hrefUI.get(i).attr("href").length() > 1) {
                    String root = node.getValue().substring(0, node.getValue().lastIndexOf(".ru") + 4);
                    String child = hrefUI.get(i).attr("href").substring(1);
                    if (!node.getValue().equals(root + child) &&
                            node.getChildren().stream().filter(n -> n.getValue().equals(root + child)).count() == 0 &&
                            siteList.get(root + child) == null) {
                        SiteMapCreator task = new SiteMapCreator(root + child);
                        task.fork();
                        taskList.add(task);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (SiteMapCreator task : taskList) {
            Node child = task.join();
            Node parent = siteList.get(child.getValue().substring(0, child.getValue().lastIndexOf("/")+1));
            if (parent.getValue().equals(child.getValue())) {
                node.addChildren(child);
            }
            else {
                parent.addChildren(child);
            }
        }
        return node;
    }
}
