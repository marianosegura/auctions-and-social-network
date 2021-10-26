package commandsLib;

import java.util.HashMap;
import java.util.Map;

/**
 * Data consisting of an hash map of strings. Contains the logic to parse and encode the 
 * hash map.
 * @author Luis Mariano Ramírez Segura
 */
public class CmdData {
    private HashMap<String, String> data;  // hashmap of data

    /**
     * Default constructor. Initializes empty hash map.
     */
    public CmdData() {
        this.data = new HashMap<>();
    }
    
    /**
     * Constructor that receives the hash map.
     * @param data Hash map to directly set the data
     */
    public CmdData(HashMap<String, String> data) {
        this.data = data;
    }
    
    
    /**
     * Constructor that receives the hash map as string and parse it.
     * @param dataString Data as string
     */
    public CmdData(String dataString) {
        this.data = parse(dataString);
    }
    
    
    /**
     * Puts a value in the hash map.
     * @param key Key string
     * @param value Value string
     */
    public void put(String key, String value) {
        data.put(key, value);
    }
    
    
    /**
     * Gets a value from the hash map.
     * @param key Key string
     * @return Hash map value that corresponds to the key
     */
    public String get(String key) {
        return data.get(key);
    }
    
    
    /**
     * Parses a data hash map from a string
     * @param dataString Hash map as string
     * @return Hash map with values
     */
    public HashMap<String, String> parse(String dataString) {
        HashMap<String, String> parsedData = new HashMap<>();
        
        if (dataString.isEmpty()) return parsedData;

        String[] keyValuePairs = dataString.split(",,");  // ,, as key-value pairs separator
        
        for (String keyValuePair : keyValuePairs) {
            String[] keyValueStrings = keyValuePair.split("::");  // :: as key-value separator
            String key = keyValueStrings[0];
            String value = keyValueStrings[1];
            parsedData.put(key, value);
        }
        
        return parsedData;
    }
    

    /**
     * Encodes the data hash map to a string.
     * @return Data hash map as string
     */
    @Override
    public String toString() {
        String toString = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            toString += entry.getKey() + "::" + entry.getValue() + ",,";
        }
        if (data.size() > 0) {  // trim last ",,"
            toString = toString.substring(0, toString.length() - 2);
        }
        return toString;
    }
    
    
    public HashMap<String, String> getData() {
        return data;
    }
    

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
