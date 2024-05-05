class AStarNode {
    String word;
    int g;
    int h;
    AStarNode parent;

    AStarNode() {
        this.word = "";
        this.g = 0;
        this.h = 0;
        this.parent = null ;
    } 

    AStarNode(String word) {
        this.word = word;
        this.g = 0;
        this.h = 0;
        this.parent = new AStarNode();
    }

    AStarNode(String word, int g, int h, AStarNode parent) {
        this.word = word;
        this.g = g;
        this.h = h;
        this.parent = parent;
    }

    int getF() {
        return g + h;
    }
}