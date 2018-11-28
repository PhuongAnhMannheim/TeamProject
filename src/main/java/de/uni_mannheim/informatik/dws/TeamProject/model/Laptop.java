package de.uni_mannheim.informatik.dws.TeamProject.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
//import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;


public class Laptop extends AbstractRecord<Attribute> {
//public class Laptop implements Matchable {

    public Laptop(String identifier, String provenance) {
        this.identifier = identifier;
        this.provenance = provenance;
    }

    //Mandatory properties of Matchable
    private String identifier;
    private String provenance;

    //Properties of laptop
    private String offerId;
    private String clusterId;
    private String gtin;
    private String mpn;
    private String sku;
    private String name;
    //more to come in the next phase

    public static final Attribute OFFERID = new Attribute ("OfferId");
    public static final Attribute CLUSTERID = new Attribute ("ClusterId");
    public static final Attribute GTIN = new Attribute ("Gtin");
    public static final Attribute MPN = new Attribute ("Mpn");
    public static final Attribute SKU = new Attribute ("Sku");
    public static final Attribute NAME = new Attribute ("Name");
    //more to come in the next phase

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getGtin(){
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku){
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean hasValue(Attribute attribute) {
        if (attribute == OFFERID)
            return getOfferId() != null && !getOfferId().isEmpty();
        if (attribute == CLUSTERID)
            return getClusterId() != null && !getClusterId().isEmpty();
        if (attribute == GTIN)
            return getGtin() != null && !getGtin().isEmpty();
        if (attribute == MPN)
            return getMpn() != null && !getMpn().isEmpty();
        if (attribute == SKU)
            return getSku() != null && !getSku().isEmpty();
        if (attribute == NAME)
            return getName() != null && !getName().isEmpty();
        return false;
    }

}
