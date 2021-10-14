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
                String root = node.getValue().substring(0, node.getValue().lastIndexOf(".ru") + 4);
                if (hrefUI.get(i).attr("href").isEmpty() ||
                        hrefUI.get(i).attr("href").contains("%") ||
                        hrefUI.get(i).attr("href").contains(".pdf") ||
                        hrefUI.get(i).attr("href").contains(".jpg")) {
                    continue;
                } else if (hrefUI.get(i).attr("href").charAt(0) == '/' && hrefUI.get(i).attr("href").length() > 1) {
                    String child = hrefUI.get(i).attr("href").substring(1);
                    String parent = node.getValue().substring(0, node.getValue().lastIndexOf("/")+1);
                    String childCheck = root + child.substring(0, child.lastIndexOf("/")+1);
                    if (!node.getValue().equals(root + child) &&
                            node.getChildren().stream().filter(n -> n.getValue().equals(root + child)).count() == 0 &&
                            siteList.get(root + child) == null &&
                            !parent.equals(childCheck)) {
                        SiteMapCreator task = new SiteMapCreator(root + child);
                        task.fork();
                        taskList.add(task);
                    }
                } else if (hrefUI.get(i).attr("href").contains(root)) {
                    if (siteList.get(hrefUI.get(i).attr("href")) == null) {
                        SiteMapCreator task = new SiteMapCreator(hrefUI.get(i).attr("href"));
                        task.fork();
                        taskList.add(task);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        for (SiteMapCreator task : taskList) {
            Node child = task.join();
            node.addChildren(child);
        }
        return node;
    }
}
