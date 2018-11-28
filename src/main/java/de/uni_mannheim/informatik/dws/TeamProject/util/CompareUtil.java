package de.uni_mannheim.informatik.dws.TeamProject.util;

import de.uni_mannheim.informatik.dws.TeamProject.model.Laptop;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;

public class CompareUtil {

    public static double compareLaptopName(Laptop laptop1,
                                         Laptop laptop2,
                                         String compName,
                                         SimilarityMeasure<String> sim,
                                         ComparatorLogger compLogger) {

        String sName1 = laptop1.getName();
        String sName2 = laptop2.getName();

        if (compLogger != null) {
            compLogger.setComparatorName(compName);
            compLogger.setRecord1Value(sName1);
            compLogger.setRecord2Value(sName2);
        }

        // preprocessing
        sName1 = (sName1 != null) ? sName1.toLowerCase() : "";
        sName2 = (sName2 != null) ? sName2.toLowerCase() : "";


        // calculate similarity
        double similarity = sim.calculate(sName1, sName2);

        if(compLogger != null){
            compLogger.setRecord1PreprocessedValue(sName1);
            compLogger.setRecord2PreprocessedValue(sName2);
            compLogger.setSimilarity(Double.toString(similarity));
        }

        return similarity;
    }
}
