package de.uni_mannheim.informatik.dws.TeamProject.blocking;

import de.uni_mannheim.informatik.dws.TeamProject.model.Laptop;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;


public class LaptopBlockingKeyByManufacturerGenerator extends RecordBlockingKeyGenerator {
    private TokenizingJaccardSimilarity sim = new TokenizingJaccardSimilarity();

    // after extension of the data set to be extended
    private String[] manufacturers = {
        "apple",
        "hp",
        "lenovo"
    };

    @Override
    public void generateBlockingKeys(
    Laptop laptop,
    Processable<Correspondence<Attribute, Matchable>> correspondences,
    DataIterator<Pair<String, Laptop>> resultCollector
    ) {
        int key = getIdForManufacturer(laptop.getManufacturer());
        if (key < 0) {
            // ignore manufacturers that are not whitelisted
            return;
        }
        resultCollector.next(new Pair<>(Integer.toString(key), laptop));
    }

    private int getIdForManufacturer(String manufacturer) {
        manufacturer = manufacturer.toLowerCase();
        for (int i = 0; i < manufacturers.length; i++) {
            if (manufacturers[i].equals("opel") && manufacturer.equals("vauxhall")) {
                return i;
            }
            double similarity = sim.calculate(manufacturers[i], manufacturer);
            if (similarity > 0.3) {
                return i;
            }
        }
        return -1;
    }
}
