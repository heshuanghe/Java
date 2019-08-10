/**
 * Compute the similarity between two items based on the Cosine between item genome scores
 */

package alg.np.similarity.metric;

import profile.Profile;
import util.reader.DatasetReader;

import java.util.Set;

public class GenomeMetric implements SimilarityMetric {
    private DatasetReader reader; // dataset reader

    /**
     * constructor - creates a new GenomeMetric object
     *
     * @param reader - dataset reader
     */
    public GenomeMetric(final DatasetReader reader) {
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
        //Get genomic vector
        reader.getItemGenomeScores();
        //DatasetReader's itemGenomeScoresMap Caching the relevance of each item to all tags
        //Get the corresponding profile based on the item ID
        //Each profile stores all tagID and the relevance score to which the item is associated with the tagId.
        Profile pX = reader.getItemGenomeScores().get(X);
        Profile pY = reader.getItemGenomeScores().get(Y);
        //Computing genomic vector's Cosine
        //Calculate the product of the vectors of two items
        Double numerator = 0.0d;
        for (Integer i : pX.getCommonIds(pY)) {
            numerator += pX.getValue(i) * pY.getValue(i);
        }
        //Cosine
        //getNorm() fuction return the length of vector
        return ((Double)(pX.getNorm() + pY.getNorm())).compareTo(0.0) == 0 ? 0 : numerator / (pX.getNorm() * pY.getNorm());
    }

}
