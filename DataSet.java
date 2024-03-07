import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

public class DataSet {
    public int n, m;
    public Point2D[] locations;
    public double[][] input;
    public int[] labels;

    public DataSet(String filename) {
        In datafile = new In(filename);

        n = datafile.readInt();
        m = datafile.readInt();

        locations = new Point2D[m];
        for (int i = 0; i < m; i++) {
            double x = datafile.readDouble();
            double y = datafile.readDouble();
            locations[i] = new Point2D(x, y);
        }

        labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        input = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                input[i][j] = datafile.readDouble();
            }
        }
    }

    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet test = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int iterations = Integer.parseInt(args[3]);


        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(training.input, training.labels,
                                                        training.locations, k);
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
    }
}
