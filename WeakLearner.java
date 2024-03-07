import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WeakLearner {
    // Prediction sample length
    private int k;
    // Dimension Predictor
    private int dp;
    // Value Predictor
    private double vp;
    // Sign Predictor
    private int sp;


    private static class Point implements Comparable<Point> {
        double value;
        int index;

        public Point(double value, int index) {
            this.value = value;
            this.index = index;
        }

        public int compareTo(Point p) {
            return Double.compare(this.value, p.value);
        }
    }

    // train the weak learner
    public WeakLearner(double[][] input, double[] weights, int[] labels) {

        // Corner cases
        if (input == null || weights == null || labels == null)
            throw new IllegalArgumentException("Null");

        if (input.length != weights.length || input.length != labels.length)
            throw new IllegalArgumentException("Null");

        for (double[] arr : input) {
            if (arr == null)
                throw new IllegalArgumentException("Null");
        }

        // the values of weights are not all non-negative.
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < 0.0)
                throw new IllegalArgumentException("Null");
        }
        // the values of labels are not all either 0 or 1
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != 0 && labels[i] != 1)
                throw new IllegalArgumentException("Null");
        }


        // Number of dimensions
        k = input[0].length;
        // Number of inputs
        int n = input.length;

        // Keeps track of the number of correct predictions of the best learner
        double champNumCor = 0.0;
        // Initialize variables
        dp = 0;
        sp = 0;
        vp = 0;

        for (int s = 0; s < 2; s++) {
            for (int d = 0; d < k; d++) {
                // Create and sort points in current dimension
                Point[] points = new Point[n];
                for (int i = 0; i < n; i++) {
                    points[i] = new Point(input[i][d], i);
                }
                Arrays.sort(points);
                // Initialize weight of correct predictions
                double numCorrectPred = 0.0;

                for (int i = 0; i < n; i++) {
                    int curIndex = points[i].index;

                    // Equation to determine prediction
                    int predictedLabel;
                    if (s == 0 && input[curIndex][d] <= points[i].value ||
                            s == 1 && input[curIndex][d] > points[i].value) {
                        predictedLabel = 0;
                    }
                    else {
                        predictedLabel = 1;
                    }

                    // Check if the prediction is correct
                    if (predictedLabel == labels[curIndex]) {
                        numCorrectPred += weights[curIndex];
                    }

                    // Update the weight based on the change in prediction (except for the first iteration)
                    if (i > 0) {
                        int prevIndex = points[i - 1].index;
                        if (s == 0 && input[curIndex][d] > input[prevIndex][d] ||
                                s == 1 && input[curIndex][d] <= input[prevIndex][d]) {
                            if (labels[curIndex] == 0) {
                                numCorrectPred -= weights[curIndex];
                            }
                            else {
                                numCorrectPred += weights[curIndex];
                            }
                        }
                    }

                    // Check if the current predictor is better
                    if (numCorrectPred > champNumCor) {
                        dp = d;
                        sp = s;
                        vp = points[i].value;
                        champNumCor = numCorrectPred;
                    }
                }

                // // Compute weight of correct predictions in sorted order
                // for (int i = 0; i < n; i++) {
                //     int curIndex = points[i].index;
                //     double v = points[i].value;
                //     double numCorrectPred = 0.0;
                //
                //     // Equation to determine prediction
                //     if ((s == 0 && input[curIndex][d] <= v) ||
                //             (s == 1 && input[curIndex][d] > v)) {
                //         if (labels[curIndex] == 0) {
                //             numCorrectPred += weights[curIndex];
                //         }
                //     }
                //     else if (labels[curIndex] == 1) {
                //         numCorrectPred += weights[curIndex];
                //     }
                //
                //     // Check if current predictor is better
                //     if (numCorrectPred > champNumCor) {
                //         // Updates the correct predictors
                //         dp = d;
                //         sp = s;
                //         vp = v;
                //         // Updates champion
                //         champNumCor = numCorrectPred;
                //     }
                // }
            }
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(double[] sample) {
        if (sample == null || sample.length != k)
            throw new IllegalArgumentException("No valid input");

        if (sp == 0 && sample[dp] <= vp || (sp == 1 && sample[dp] > vp)) {
            return 0;
        }
        else
            return 1;
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dp;
    }

    // return the value the learner uses to separate the data
    public double valuePredictor() {
        return vp;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return sp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet test = new DataSet(args[1]);
        double[] weights = new double[training.n];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1;
        }

        // train the model
        WeakLearner model = new
                WeakLearner(training.input, weights, training.labels);

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

        StdOut.println(model.dimensionPredictor());
        StdOut.println(model.valuePredictor());
        StdOut.println(model.signPredictor());
    }
}

