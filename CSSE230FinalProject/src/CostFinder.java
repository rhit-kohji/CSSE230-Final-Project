
public interface CostFinder <T extends GraphNode> {
    double computeDistanceCost(T from, T to);
    double computeTimeCost(T from, T to);
}
