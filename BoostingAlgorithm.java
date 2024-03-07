import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class BoostingAlgorithm {

    // Number of locations
    private int m;

    // Number of transactions
    private int n;

    // Dimension-Reduced Input
    private double[][] reducedInput;

    // Array of labels
    private int[] labels;

    // Array of weights
    private double[] weights;

    // List of Learners
    private LinkedList<WeakLearner> listOfLearners;

    // Clustered Dimensions
    private Clustering cluster;


    // create the clusters and initialize your data structures
    public BoostingAlgorithm(double[][] input, int[] labels,
                             Point2D[] locations, int k) {
        // Corner Cases
        if (input == null || labels == null || locations == null)
            throw new IllegalArgumentException("Null arguments");

        for (double[] arr : input) {
            if (arr == null)
                throw new IllegalArgumentException("Null argument");
        }

        if (input[0].length != locations.length || input.length != labels.length)
            throw new IllegalArgumentException("Null");

        // this.k = k;
        listOfLearners = new LinkedList<>();
        // Number of summaries
        n = input.length;
        // Number of map locations
        m = locations.length;

        // Defensive copy of labels
        int[] labCopy = new int[n];
        for (int i = 0; i < n; i++) {
            labCopy[i] = labels[i];
        }
        this.labels = labCopy;

        // the values of labels are not all either 0 or 1
        for (int i = 0; i < n; i++) {
            if (labels[i] != 0 && labels[i] != 1)
                throw new IllegalArgumentException("Null");
        }

        // checks for null locations
        for (Point2D point : locations) {
            if (point == null)
                throw new IllegalArgumentException("Null argument");
        }

        if (k < 1 || k > m)
            throw new IllegalArgumentException("Invalid K");

        // Initializes Cluster Object
        cluster = new Clustering(locations, k);

        // Initialize weight
        weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 1.0 / n;
        }

        // Initializes reduced dimension input
        reducedInput = new double[n][k];
        for (int i = 0; i < n; i++) {
            reducedInput[i] = cluster.reduceDimensions(input[i]);
        }

    }

    // return the current weights
    public double[] weights() {
        return weights;
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        // Create a weak learner using the current weights and the input.
        WeakLearner weakLeaner = new WeakLearner(reducedInput, weights, labels);
        listOfLearners.add(weakLeaner);

        // For each input point, double its weight if
        // the weak learner you created mislabels it.
        for (int i = 0; i < n; i++) {
            int prediction =
                    weakLeaner.predict(reducedInput[i]);
            if (prediction != labels[i]) {
                weights[i] *= 2;
            }
        }


        // Normalize the weight array, so that it sums to 1 again
        double sum = 0;
        for (double weight : weights) {
            sum += weight;
        }
        for (int i = 0; i < n; i++) {
            weights[i] = weights[i] / sum;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(double[] sample) {
        if (sample == null || sample.length != m)
            throw new IllegalArgumentException("Null");
        int numOnes = 0;
        int numZeros = 0;
        double[] reducedSample = cluster.reduceDimensions(sample);

        for (WeakLearner weakL : listOfLearners) {
            int result = weakL.predict(reducedSample);
            if (result == 1)
                numOnes++;
            else
                numZeros++;
        }
        if (numZeros >= numOnes)
            return 0;
        else
            return 1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet test = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int iterations = Integer.parseInt(args[3]);

        // train the model
        BoostingAlgorithm model = new
                BoostingAlgorithm(training.input,
                                  training.labels, training.locations, k);
        for (int t = 0; t < iterations; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.n; i++)
            if (model.predict(training.input[i]) == training.labels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.n;

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < test.n; i++)
            if (model.predict(test.input[i]) == test.labels[i])
                testAccuracy += 1;
        testAccuracy /= test.n;

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model:     " + testAccuracy);

        double[] arr = model.weights();

        for (int i = 0; i < 5; i++) {
            StdOut.println(arr[i]);
        }

    }
}

