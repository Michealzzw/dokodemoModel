package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ReadAirport {
	static HashMap<String,Integer> Airport = new HashMap<String,Integer>();
	public static int readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	if (!tempString.matches("([0-9:a-zA-Z]+)"))
            	{
            		if (tempString.contains("经停"))
            		{
            			reader.readLine();
            		}
            		else
            		{
            			if(Airport.containsKey(tempString))
            			Airport.put(tempString, Airport.get(tempString)+1);
            			else
            				Airport.put(tempString, 1);
            		}
            			
            	}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return line; 
    }
	static public void main(String[] args) throws IOException
	{
		readFileByLines("航班时刻.txt");
		FileWriter fw = new FileWriter("airport.txt");
		int num = 0;
		Iterator<Entry<String, Integer>> iter = Airport.entrySet().iterator();
		while (iter.hasNext()) {
			num++;
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			fw.write((String)entry.getKey()+"\n");
		}
		fw.close();
	}
}
