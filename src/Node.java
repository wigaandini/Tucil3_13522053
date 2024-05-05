class Node {
    String word;
    String parent;
    int cost;

    Node() {
        this.word = "";
        this.parent = "";
        this.cost = 0;
    }

    Node(String word, String parent, int cost) {
        this.word = word;
        this.parent = parent;
        this.cost = cost;
    }
}