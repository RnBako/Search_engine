package model;

import org.jsoup.nodes.Element;

import java.util.Comparator;

/**
 * SearchResult class
 * @author Roman Barsuchenko
 * @version 1.0
 */
public class SearchResult {
    /** Page id reference*/
    private final Page page;
    /** Absolute relevance of search result*/
    private final double absoluteRelevance;
    /** Relative relevance of search result*/
    private double relativeRelevance;
    /** Search result title*/
    private String title;
    /** Search result snippet*/
    private Element snippet;

    /**
     * Creating a new page object
     * @param page - Page on which the text was found
     * @param absoluteRelevance - Absolute relevance of search result
     * @param relativeRelevance - Relative relevance of search result
     */
    public SearchResult(Page page, double absoluteRelevance, double relativeRelevance) {
        this.page = page;
        this.absoluteRelevance = absoluteRelevance;
        this.relativeRelevance = relativeRelevance;
    }

    /**
     * Page get method
     * @return return page
     */
    public Page getPage() {
        return page;
    }

    /**
     * Absolute relevance get method
     * @return return absolute relevance
     */
    public double getAbsoluteRelevance() {
        return absoluteRelevance;
    }

    /**
     * Relative relevance get method
     * @return return relative relevance
     */
    public double getRelativeRelevance() {
        return relativeRelevance;
    }

    /**
     * Search result title get method
     * @return return search result title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Search result snippet get method
     * @return return search result snippet
     */
    public Element getSnippet() {
        return snippet;
    }

    /**
     * Relative relevance set method
     * @param relativeRelevance - Relative relevance for search result
     */
    public void setRelativeRelevance(double relativeRelevance) {
        this.relativeRelevance = relativeRelevance;
    }

    /**
     * Search result title set method
     * @param title - title for search result
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Search result snippet set method
     * @param snippet - snippet for search result
     */
    public void setSnippet(Element snippet) {
        this.snippet = snippet;
    }

    /**
     * Comparator for SearchResult objects
     */
    public static final Comparator<SearchResult> COMPARE_BY_RELATIVE_RELEVANCE = new Comparator<SearchResult>() {
        @Override
        public int compare(SearchResult o1, SearchResult o2) {
            return Double.compare(o1.getRelativeRelevance(), o2.getRelativeRelevance());
        }
    };
}
