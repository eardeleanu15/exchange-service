package com.demo.utils;

import com.demo.model.ExchangeRates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.demo.utils.Constants.CUBE_NODE;
import static com.demo.utils.Constants.TIME_ATTRIBUTE;
import static com.demo.utils.Constants.CURRENCY_ATTRIBUTE;
import static com.demo.utils.Constants.RATE_ATTRIBUTE;

public class RatesXmlParser {

    private static final Logger logger = LoggerFactory.getLogger(RatesXmlParser.class);

    private RatesXmlParser() {}

    public static Void extractDailyRates(String data) {
        
        try {
            Document doc = loadXMLFromString(data);
            LocalDate ratesDate = null;
            String currency = null;
            Double rate;
            Map<String, Double> rates = new HashMap<>();

            // make a node list
            NodeList cubeNodeList = doc.getElementsByTagName(CUBE_NODE);
            logger.info("Cube Node List Length :: {}", cubeNodeList.getLength());
            for (int i = 0; i < cubeNodeList.getLength(); i++) {
                Node child = cubeNodeList.item(i);
                if (child.hasAttributes()) {
                    // Get Cube attributes
                    NamedNodeMap attributes = child.getAttributes();

                    for (int j = 0; j < attributes.getLength(); j++) {
                        Node attribute = attributes.item(j);
                        if (TIME_ATTRIBUTE.equalsIgnoreCase(attribute.getNodeName())) {
                            // initialize Date time
                            ratesDate = LocalDate.parse(attribute.getNodeValue(), DateTimeFormatter.ISO_LOCAL_DATE);
                            logger.info("Rate Date :: {}", ratesDate.toString());
                        } else if (CURRENCY_ATTRIBUTE.equalsIgnoreCase(attribute.getNodeName())) {
                            // initialize currency
                            currency = attribute.getNodeValue();
                        } else if (RATE_ATTRIBUTE.equalsIgnoreCase(attribute.getNodeName())) {
                            // initialize rate
                            rate = Double.parseDouble(attribute.getNodeValue());
                            logger.info("Adding values to map, currency :: {}, rate :: {}", currency, rate);
                            rates.put(currency, rate);
                        }
                    }
                }
            }

            if (ratesDate != null) {
                // initialize Exchange Rates
                ExchangeRates.getInstance().addDailyRates(ratesDate, rates);
            } else {
                logger.error("Rates date could not be determined");
            }

        } catch(Exception e) {
            logger.error("Exception caught while processing rates data. Exception Message :: {}", e.getMessage());
        }

        return null;
    }

    private static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

}
