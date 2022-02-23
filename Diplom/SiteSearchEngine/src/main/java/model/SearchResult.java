package model;

import org.jsoup.nodes.Element;

import java.util.Comparator;

public class SearchResult {


    private final Page page;
    private final double absoluteRelevance;
    private double relativeRelevance;
    private String title;
    private Element snippet;

    public SearchResult(Page page, double absoluteRelevance, double relativeRelevance) {
        this.page = page;
        this.absoluteRelevance = absoluteRelevance;
        this.relativeRelevance = relativeRelevance;
    }

    public Page getPage() {
        return page;
    }

    public double getAbsoluteRelevance() {
        return absoluteRelevance;
    }

    public double getRelativeRelevance() {
        return relativeRelevance;
    }

    public String getTitle() {
        return title;
    }

    public Element getSnippet() {
        return snippet;
    }

    public void setRelativeRelevance(double relativeRelevance) {
        this.relativeRelevance = relativeRelevance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(Element snippet) {
        this.snippet = snippet;
    }

    public static final Comparator<SearchResult> COMPARE_BY_RELATIVE_RELEVANCE = new Comparator<SearchResult>() {
        @Override
        public int compare(SearchResult o1, SearchResult o2) {
            return Double.compare(o1.getRelativeRelevance(), o2.getRelativeRelevance());
        }
    };
}
