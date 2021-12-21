public class SearchResult {


    private final Page page;
    private final double absoluteRelevance;
    private double relativeRelevance;

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

    public void setRelativeRelevance(double relativeRelevance) {
        this.relativeRelevance = relativeRelevance;
    }
}
