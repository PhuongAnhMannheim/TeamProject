package de.uni_mannheim.informatik.dws.TeamProject.model;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LaptopXMLReader extends XMLMatchableReader<Laptop, Attribute>{

        @Override
        public Laptop createModelFromElement(Node node, String provenance) {
            Element elem = (Element) node;

            // get the ID value
            String id = elem.getAttribute("id");

            // create a new object with id and provenance information
            Laptop laptop = new Laptop(id, provenance);

            // fill the attributes
            laptop.setOfferId(getValueFromChildElement(node, "offerId"));
            laptop.setClusterId(getValueFromChildElement(node, "clusterId"));
            laptop.setGtin(getValueFromChildElement(node, "gtin"));
            laptop.setMpn(getValueFromChildElement(node, "mpn"));
            laptop.setSku(getValueFromChildElement(node, "sku"));
            laptop.setName(getValueFromChildElement(node, "name"));

            // return the new object
            return laptop;
        }

}
