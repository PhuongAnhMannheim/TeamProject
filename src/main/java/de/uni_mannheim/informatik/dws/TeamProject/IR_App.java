package de.uni_mannheim.informatik.dws.TeamProject;

import de.uni_mannheim.informatik.dws.TeamProject.comparator.LaptopNameTokenizingJaccard;
import de.uni_mannheim.informatik.dws.TeamProject.model.Laptop;
import de.uni_mannheim.informatik.dws.TeamProject.model.LaptopXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Attr;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class IR_App {
    public static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main (String[] args) throws Exception {

        // Load the datasets
        logger.info("Loading datasets...");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        HashedDataSet<Laptop, Attribute> offerInt = IR_App.loadData("laptops_offers");

        logger.info("Sucessfully loaded data sets");

        // Load goldstandard
        MatchingGoldStandard goldStandardTest = new MatchingGoldStandard();
        goldStandardTest.loadFromCSVFile(new File(classloader.getResource("goldstandard/test_laptop_final_300.csv").getFile()));
        logger.info("Successfully loaded the goldstandard");

        //Prepare reusable datasets and parameters
        int blockSize = 900;
        int iterations = 2;
        logger.info("matching " + blockSize * iterations + "random offers with laptops");
        Laptop[] laptopOffers = offerInt.get().toArray(new Laptop[]{});
        Processable<Correspondence<Laptop, Attribute>> laptopLaptopCorrespondences = null;
        for (int i = 0; i < iterations; i++) {

            System.gc();

            //Prepare sampled offers dataset
            HashedDataSet<Laptop, Attribute> offers = new HashedDataSet<>();
            for (int j = 0; j < blockSize; j++) {
                offers.add(getRandom(laptopOffers));
            }

            //Add all GS elements to the sample
            List<Pair<String, String>> fullGS = goldStandardTest.getPositiveExamples();
            fullGS.addAll(goldStandardTest.getNegativeExamples());
            for(Pair<String, String> corr : fullGS) {
                String first = corr.getFirst();
                String second = corr.getSecond();
                if(first.contains("offer") && offerInt.getRecord(first) != null) {
                    offers.add(offerInt.getRecord(first));
                }
                if(second.contains("offer") && offerInt.getRecord(second) !=null){
                    offers.add(offerInt.getRecord(second));
                }
            }

            logger.info("Start the matching for iteration " + i + "/" + iterations);

            Processable<Correspondence<Laptop, Attribute>> corr;

            corr = getlaptopLaptopCorrespondences(offers, offers);
            if (laptopLaptopCorrespondences == null){
                laptopLaptopCorrespondences = corr;
            } else {
                for (Correspondence<Laptop, Attribute> correspondence : corr.get()){
                    laptopLaptopCorrespondences.add(correspondence);
                }
            }

            logger.info("Successfully completed the matching for iteration " + (i + 1) + "/" + iterations);

            MatchingGoldStandard gs = new MatchingGoldStandard();
            gs.loadFromCSVFile(new File(classloader.getResource("goldstandard/CSV_laptop_final_300.csv").getFile()));
            logger.info("Successfully loaded the goldstandard");

            evaluateDataset("laptops-laptops", laptopLaptopCorrespondences, gs);

        }
    }

    private static void evaluateDataset(String name, Processable<Correspondence<Laptop, Attribute>> corr, MatchingGoldStandard gs) throws Exception {
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/" + name + ".csv"), corr);
        logger.info("Successfully wrote " + name + " to data/output/...");

        logger.info("Starting the result evaluation...");
        MatchingEvaluator<Laptop, Attribute> evaluator = new MatchingEvaluator<>();
        Performance performance = evaluator.evaluateMatching(corr, gs);

        logger.info("Results for " + name);
        logger.info(String.format("Precision: %.4f", performance.getPrecision()));
        logger.info(String.format("Recall: %.4f", performance.getRecall()));
        logger.info(String.format("F1: %.4f", performance.getF1()));
    }

    private static Laptop getRandom(Laptop[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


    private static Processable<Correspondence<Laptop, Attribute>> getlaptopLaptopCorrespondences(
    HashedDataSet<Laptop, Attribute> d1,
    HashedDataSet<Laptop, Attribute> d2
    ) throws Exception {
        // Add comparators
        logger.info("Add matching rules");
        LinearCombinationMatchingRule<Laptop, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.65);
        matchingRule.addComparator(new LaptopNameTokenizingJaccard(), 1);

        // TODO blocking in IR
        // Add blocking strategy
        //logger.info("Initialize the blocker");
        //StandardRecordBlocker<Laptop, Attribute> blocker = new StandardRecordBlocker<>(new LaptopBlockingKeyByManufacturerGenerator());
        //blocker.setMeasureBlockSizes(true);

        // Add matching engine
        MatchingEngine<Laptop, Attribute> engine = new MatchingEngine<>();
        //return engine.runIdentityResolution(d1, d2, null, matchingRule, blocker);
        return engine.runIdentityResolution(d1, d2,null, matchingRule, null);

    }

    private static HashedDataSet<Laptop, Attribute> loadData(String fileName) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        // create a new data set
        HashedDataSet<Laptop, Attribute> ds = new HashedDataSet<>();
        logger.info("Loading " + fileName + "...");

        //load an XML file
        new LaptopXMLReader().loadFromXML(new File(classloader.getResource("data/" + fileName + ".xml").getFile()), "/target/laptop", ds);
        return ds;
    }
}


