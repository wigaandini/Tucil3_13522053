import java.util.List;

public class SearchResult {
    private final List<String> path;
    private final int nodesVisited;

    public SearchResult(List<String> path, int nodesVisited) {
        this.path = path;
        this.nodesVisited = nodesVisited;
    }

    public List<String> getPath() {
        return path;
    }

    public int getNodesVisited() {
        return nodesVisited;
    }
}
