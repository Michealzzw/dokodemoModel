package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class getStation {
	static String path = "RouteDis.txt";
	static HashMap<String, Integer> Station_Dis = new HashMap<String, Integer>();
	static HashMap<String, Integer> Route_Number = new HashMap<String, Integer>();
	static HashMap<String, Integer> Station_Hub = new HashMap<String, Integer>();

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
            	String[] list = tempString.split(",");
            	if (Route_Number.containsKey(list[0]))
            		Route_Number.put(list[0], Route_Number.get(list[0])+1);
            	else Route_Number.put(list[0],1);
            	if (Station_Hub.containsKey(list[1]))
            		Station_Hub.put(list[1], Station_Hub.get(list[1])+1);
            	else
            	{
            		if (Station_Dis.containsKey(list[1]))
            		{
            			Station_Dis.remove(list[1]);
            			Station_Hub.put(list[1], 2);
            		}
            		else
            			Station_Dis.put(list[1], Integer.parseInt(list[2]));
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
	public static void main(String[] args) throws IOException
	{
		readFileByLines(path);
		FileWriter fw = new FileWriter("Station_Hub.txt");
		Iterator iter = Station_Hub.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			fw.write(entry.getKey()+" "+entry.getValue()+"\n");
		}
		fw.close();
		
	}
}
