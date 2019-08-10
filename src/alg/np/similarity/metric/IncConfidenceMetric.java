/**
 * Compute the similarity between two items based on increase in confidence
 */

package alg.np.similarity.metric;

import profile.Profile;
import util.reader.DatasetReader;

public class IncConfidenceMetric implements SimilarityMetric {
    private static double RATING_THRESHOLD = 4.0; // the threshold rating for liked items
    private DatasetReader reader; // dataset reader

    /**
     * constructor - creates a new IncConfidenceMetric object
     *
     * @param reader - dataset reader
     */
    public IncConfidenceMetric(final DatasetReader reader) {
        this.reader = reader;
    }

    /**
     * computes the similarity between items
     *
     * @param X - the id of the first item
     * @param Y - the id of the second item
     */
    public double getItemSimilarity(final Integer X, final Integer Y) {
        // calculate similarity using conf(X => Y) / conf(!X => Y)
        //Get the total number of users
        int N = reader.getUserProfiles().size();
        //Take Remove the required profile from the cache based on the given movie ID
        // ItemProfile cache each user ID and rating that was voted on item
        Profile profileX = reader.getItemProfiles().get(X);
        Profile profileY = reader.getItemProfiles().get(Y);
        //calculate supp(X) and supp(!X)
        int countSuppX = 0, countSuppNX = 0;
        //Select all users who have rated movie X
        //Calculate the number of users who like the movie and hate the movie
        for (Integer userId : profileX.getIds()) {
            if (profileX.getValue(userId).compareTo(RATING_THRESHOLD) >= 0) {
                countSuppX++;
            } else {
                countSuppNX++;
            }
        }
        //calculate supp(X and Y) and supp(X! and Y)
        int countSuppXAndY = 0, countSuppNxAndY = 0;
        //use profileX.getCommonIds(profileY) select the user ID that has scored both item
        for (Integer sharedUserId : profileX.getCommonIds(profileY)) {
            //Select users who like both movies in the two movies
            if (profileX.getValue(sharedUserId).compareTo(RATING_THRESHOLD) >= 0 && profileY.getValue(sharedUserId).compareTo(RATING_THRESHOLD) >= 0) {
                countSuppXAndY++;
            }
            //In the users who scored both movies, select the user who does not like X and who likes Y.
            if (profileX.getValue(sharedUserId).compareTo(RATING_THRESHOLD) < 0 && profileY.getValue(sharedUserId).compareTo(RATING_THRESHOLD) >= 0) {
                countSuppNxAndY++;
            }
        }
        //Determine if the denominator is zero
        Double confXY = (countSuppX == 0 ? 0.0d : (countSuppXAndY * 1.0 / N) / (countSuppX * 1.0 / N));
        Double confNXY = (countSuppNxAndY == 0 ? 0.0d : (countSuppNxAndY * 1.0 / N) / (countSuppNX * 1.0) / N);
       // calculate   conf(X => Y) / conf(!X => Y)
        return confNXY.compareTo(0.0) == 0 ? 0.0d : (confXY / confNXY);
    }
}
