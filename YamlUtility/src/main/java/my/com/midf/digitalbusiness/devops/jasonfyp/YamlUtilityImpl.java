/**
 *
 */
package my.com.midf.digitalbusiness.devops.jasonfyp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * This class implements the interface created in YamlUtility, there are
 * overriding functions taken from the interface that are required to be here.
 * The program won't even compile without the overriding class, so do not remove
 * them but instead write your program as part of it and include more functions
 * where you feel like. Make them private functions. I don't see any reason why
 * there should be additional public functions here.
 *
 * @author fazreil
 *
 */
public class YamlUtilityImpl implements YamlUtility {

	private Map<String, Object> content;

	/**
	 * read a file and parse all of the data into content field above
	 */
	@Override
	public void read(File file) {
		InputStream is = null;
		try {
			// TODO Auto-generated method stub
			is = new FileInputStream(file);
			Yaml yaml = new Yaml();
			content = yaml.load(is);

		} catch (FileNotFoundException ex) {
			Logger.getLogger(YamlUtilityImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				is.close();
			} catch (IOException ex) {
				Logger.getLogger(YamlUtilityImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * extract an object from content field above by mentioning the key
	 */
	@Override
	public String extract(String key) {
		String[] keyArray = key.split("\\.");
		String extractedValue = recursiveExtract(0, keyArray, content);
		return extractedValue;
	}

	private String recursiveExtract(int index, String[] keyArray, Map map) {
		try {
			String stringToExtract = "";
			String key = keyArray[index];
			if (doesThisMapHas(key, map)) {
				if (isThisAYaml("" + map.get(key))) {
					Yaml yaml = new Yaml();

					Map<String, Object> newMap = yaml.load(yaml.dump(map.get(key)));
					index++;
					stringToExtract = recursiveExtract(index, keyArray, newMap);
				} else {
					return "" + map.get(key);
				}
			} else {
				System.out.println("key " + key + " does not exist");
			}
			return stringToExtract;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	private boolean isThisAYaml(String possibleYamlString) {
		if (possibleYamlString.startsWith("{")) {
			return true;
		} else
			return false;
	}

	private boolean doesThisMapHas(String key, Map map) {
		return map.containsKey(key);
	}

	/**
	 * write the content object into a new yaml file
	 */
	@Override
	public File write(File newFile) {
		// I don't think we need these because we are writing the value of content from line 31 into newFile
//            Map<String, Object> dataMap = new HashMap<>();
//            dataMap.put("id", 19);
//            dataMap.put("name", "John");
//            dataMap.put("address", "Star City");
//            dataMap.put("department", "Medical");
		
            try 
            {
                FileWriter myWriter = new FileWriter(newFile);
                Yaml yaml = new Yaml();
                String dumpedContent = yaml.dump(content);
                myWriter.write(dumpedContent);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
                return newFile;
            } catch (Exception ex) {
                System.out.println("An error occurred.");
                ex.printStackTrace();
                return newFile;
            }
            
        }
          

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
