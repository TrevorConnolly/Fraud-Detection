import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Clustering {
    // Number of clusters
    private int numClust;

    // Number of locations
    private int m;

    // Cluster Graph
    private CC cluster;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        // Corner cases
        if (locations == null || k < 1 || k > locations.length) {
            throw new IllegalArgumentException("Null argument");
        }

        numClust = k;
        m = locations.length;

        // check if each point2D is null
        for (Point2D point : locations) {
            if (point == null) {
                throw new IllegalArgumentException("Null argument");
            }
        }
        // Edge weighted graph
        EdgeWeightedGraph graph = new EdgeWeightedGraph(m);


        // Connecting each point to each other with an edge
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if (i != j) {
                    // Weight of edge is euclidean  distance between points
                    double weight = locations[i].distanceTo(locations[j]);
                    Edge e = new Edge(i, j, weight);
                    graph.addEdge(e);
                }
            }
        }
        // Minimum Spanning tree
        KruskalMST span = new KruskalMST(graph);

        // Add edges
        ArrayList<Edge> edges = new ArrayList<>();
        for (Edge e : span.edges()) {
            edges.add(e);
        }


        // Graph that will be clustered
        EdgeWeightedGraph toBeClust = new EdgeWeightedGraph(m);

        for (int i = 0; i < m - k; i++) {
            toBeClust.addEdge(edges.get(i));
        }
        // Make k clusters
        cluster = new CC(toBeClust);
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        if (i < 0 || i > m) {
            throw new IllegalArgumentException("Out of bounds");
        }
        return cluster.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public double[] reduceDimensions(double[] input) {
        if (input == null || input.length != m) {
            throw new IllegalArgumentException("Null arguement");
        }
        double[] reduced = new double[numClust];

        for (int i = 0; i < m; i++) {
            reduced[clusterOf(i)] += input[i];
        }
        return reduced;

    }

    // unit testing (required)
    public static void main(String[] args) {
        int k = StdIn.readInt();
        Point2D[] loc = new Point2D[k];
        int index = 0;
        while (!StdIn.isEmpty()) {
            double v = StdIn.readDouble();
            double w = StdIn.readDouble();
            loc[index] = new Point2D(v, w);
            index++;
        }
        Clustering clust = new Clustering(loc, 5);
        StdOut.println("Id of index 1 " + clust.clusterOf(1));
        StdOut.println("Id of index 2 " + clust.clusterOf(2));
        StdOut.println("Id of index 3 " + clust.clusterOf(3));
        StdOut.println("Id of index 0 " + clust.clusterOf(0));
        StdOut.println("Id of index 4 " + clust.clusterOf(4));
        StdOut.println("Id of index 12 " + clust.clusterOf(12));
        StdOut.println("Id of index 20 " + clust.clusterOf(20));
        // Input array
        double[] input = {
                5, 6, 7, 0, 6, 7, 5, 6, 7, 0, 6,
                7, 0, 6, 7, 0, 6, 7, 0, 6, 7
        };
        // Reduced input
        double[] arr = clust.reduceDimensions(input);

        for (double a : arr) {
            StdOut.println(a);
        }

    }
}
