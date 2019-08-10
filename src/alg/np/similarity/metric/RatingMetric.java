/**
 * Compute the similarity between two items based on the Cosine between item ratings
 */

package alg.np.similarity.metric;

import profile.Profile;
import util.reader.DatasetReader;

import java.util.Map;
import java.util.Set;

public class RatingMetric implements SimilarityMetric {
    private DatasetReader reader; // dataset reader

    /**
     * constructor - creates a new RatingMetric object
     *
     * @param reader - dataset reader
     */
    public RatingMetric(final DatasetReader reader) {
        this.reader = reader;
    }

    /**
     * computes the similarity between items
     *
     * @param X - the id of the first item
     * @param Y - the id of the second item
     */
    public double getItemSimilarity(final Integer X, final Integer Y) {
        // calculate similarity using Cosine
        reader.getUserProfiles();
        //itemProfileMap cashed the itemId and Profile corresponding to itemId
        //The profile stores all the user IDs and corresponding scores that have been scored for the item.
        Profile profileX = reader.getItemProfiles().get(X);
        Profile profileY = reader.getItemProfiles().get(Y);
        //get the user ID that has been commented on both movies
        Set<Integer> sharedUserIds = profileX.getCommonIds(profileY);
        //compute the numerator
        /**Because users who have not scored are treated as 0 points,
         we can select the item that has scored both items when calculating.
         If only one movie has been scored, the result of calculating the vector product sum will also be 0,
         so it is invalid and does not need to be included in the calculation of the numerator.
         */
        Double numerator = 0.0d;
        for (Integer userId : sharedUserIds) {
            numerator += profileX.getValue(userId) * profileY.getValue(userId);
        }
        return numerator.compareTo(0.0) == 0 ? 0 : numerator / (profileX.getNorm() * profileY.getNorm());
    }
}
