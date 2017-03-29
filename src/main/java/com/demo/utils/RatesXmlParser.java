package com.demo.utils;

import com.demo.repository.IExchangeRatesRepository;
import com.demo.repository.impl.InMemoryExchangeRatesRepository;
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

/**
 * Utility class that provides the
 * parsing methods to extract exchange rates
 * from XML inputs.
 */
public class RatesXmlParser {

    private static final Logger logger = LoggerFactory.getLogger(RatesXmlParser.class);

    // Retrieve instance of InMemoryExchangeRatesRepository
    private static final IExchangeRatesRepository exchangeRatesRepository = InMemoryExchangeRatesRepository.getInstance();

    private RatesXmlParser() {}

    /**
     * Method used to extract daily rates
     * from XML input retrieved from European
     * central Bank.
     *
     * Note: (Void) null reference is returned to allow
     * the use of CompletableFuture<T> feature
     * @param data
     */
    public static Void extractDailyRates(String data) {
        
        try {
            Document doc = loadXMLFromString(data);
            LocalDate ratesDate = null;
            String currency = null;
            Double rate;
            Map<String, Double> rates = new HashMap<>();

            // retrieve all 'Cube' nodes
            NodeList cubeNodeList = doc.getElementsByTagName(CUBE_NODE);
            logger.info("Cube Node List Length :: {}", cubeNodeList.getLength());
            for (int i = 0; i < cubeNodeList.getLength(); i++) {
                Node child = cubeNodeList.item(i);
                // if node has attributes - time, currency or rate
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
                logger.info("Adding rates for date :: {}", ratesDate.toString());
                // initialize Exchange Rates
                exchangeRatesRepository.addDailyRates(ratesDate, rates);
            } else {
                logger.error("Rates date could not be determined");
            }

        } catch(Exception e) {
            logger.error("Exception caught while processing daily rates data. Exception Message :: {}", e.getMessage());
        }

        // Note: (Void) null reference is returned to allow
        // the use of CompletableFuture<T> feature
        return null;
    }

    /**
     * Method used to extract historical rates
     * from XML input retrieved from European
     * central Bank.
     *
     * Note: (Void) null reference is returned to allow
     * the use of CompletableFuture<T> feature
     * @param data
     */
    public static Void extractHistoricalRates(String data) {

        try {
            Document doc = loadXMLFromString(data);
            LocalDate ratesDate = null;
            String currency = null;
            Double rate;
            Map<String, Double> rates = new HashMap<>();

            // retrieve all 'Cube' nodes
            NodeList cubeNodeList = doc.getElementsByTagName(CUBE_NODE);
            logger.info("Cube Node List Length :: {}", cubeNodeList.getLength());
            for (int i = 0; i < cubeNodeList.getLength(); i++) {
                Node child = cubeNodeList.item(i);
                // if node has attributes - time, currency or rate
                if (child.hasAttributes()) {
                    // Get Cube attributes
                    NamedNodeMap attributes = child.getAttributes();

                    for (int j = 0; j < attributes.getLength(); j++) {
                        Node attribute = attributes.item(j);
                        if (TIME_ATTRIBUTE.equalsIgnoreCase(attribute.getNodeName())) {
                            if (ratesDate == null) {
                                // initialize Date time for the first time
                                ratesDate = LocalDate.parse(attribute.getNodeValue(), DateTimeFormatter.ISO_LOCAL_DATE);
                                logger.info("Rate Date :: {}", ratesDate.toString());
                            } else {
                                logger.info("Adding rates for date :: {}", ratesDate.toString());
                                // Add Exchange Rates to Map
                                exchangeRatesRepository.addDailyRates(ratesDate, rates);
                                // Initialize Date time
                                ratesDate = LocalDate.parse(attribute.getNodeValue(), DateTimeFormatter.ISO_LOCAL_DATE);
                                // Re-init rates Map
                                rates = new HashMap<>();
                            }
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

            logger.info("Adding last historical rates for date :: {}", ratesDate.toString());
            exchangeRatesRepository.addDailyRates(ratesDate, rates);
        } catch (Exception e) {
            logger.error("Exception caught while processing historical rates data. Exception Message :: {}", e.getMessage());
        }

        // Note: (Void) null reference is returned to allow
        // the use of CompletableFuture<T> feature
        return null;
    }

    /**
     * Method that allows the wrapping of the
     * XML input, as String, into a Document object
     * used by the DOMParser.
     * @param xml
     * @return
     * @throws Exception
     */
    private static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

}
