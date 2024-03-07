Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
We first create an edge weighted graph with all locations and add edges between every location, computing the distance
between them for each iteration. Then, we use kruscals to create a minimum spanning tree from this graph.
Then we store all the edges in the MST in an array list. Then, we make a new graph with all the vertices but only the
m-k edges with the lowest weights (distances). This is the new graph that is then used to create the cluster graph,
with k connected components.

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
In the weaklearner constructor we iterate through all possible values of dp, sp, and vp and computed the weight of
correct predictions for each of these weak learners (by looping through all input points) and stored the champion
which is the weak learner with the best numCorrectPred value. We use the Dp, Sp, and Vp of the champion.


/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the training.txt and test.txt datasets instead, otherwise
 *  this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
    10           10            .7               .255
    18          70              .9              .374
    22          95              .867            .402
    25          74              .9333           .386


/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */

Optimal K: 25
Optimal T = 74
Test Accuracy: .9333
Used training.txt and test.txt

To find the optimal k and T we kept T the same and increased k until the accuracy stopped increasing. We repeated this
process to find T. We basically used trial and error to find the best K and T values.
Too small of a value for T leads to low test accuracy because it doesn't allow the model to
iteratively correct as many misclassifications which prevents the predictive model to be enhanced as much as it would
be with more iterations. A K value that is too small will lead to the clustering not
capturing all the complexity of the data which would result in a loss of information and an oversimplification of the
data. On the other hand, for a k value that's too big the model may start to memorize the training data instead of
generalizing well to unseen data which consequently could result in low test accuracy.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
