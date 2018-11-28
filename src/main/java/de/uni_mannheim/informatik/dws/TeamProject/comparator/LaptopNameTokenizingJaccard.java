package de.uni_mannheim.informatik.dws.TeamProject.comparator;

import de.uni_mannheim.informatik.dws.TeamProject.model.Laptop;
import de.uni_mannheim.informatik.dws.TeamProject.util.CompareUtil;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;


public class LaptopNameTokenizingJaccard implements Comparator<Laptop, Attribute> {

    private TokenizingJaccardSimilarity sim = new TokenizingJaccardSimilarity();
    private ComparatorLogger compLogger;

    @Override
    public double compare (Laptop laptop1, Laptop laptop2, Correspondence<Attribute, Matchable> schemaCorrespondence){
        return CompareUtil.compareLaptopName(laptop1, laptop2, getClass().getName(),sim, this.compLogger);
    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.compLogger;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.compLogger = comparatorLog;
    }

}
