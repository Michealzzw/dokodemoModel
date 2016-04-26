package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StationCityReGen {
	static String path = "train.txt";
	static HashMap<String,Integer> Station = new HashMap<String,Integer>();
	public static int readFileByLines(String fileName) {
		Pattern routeNo = Pattern.compile("[0-9A-Z/]+");
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
        	FileWriter fw = new FileWriter("SMtrainRoute.txt");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	String[] list = tempString.split(" ");
            	for (int i = 0;i<list.length;i++)
            	{
            		if (list[i].matches("[0-9A-Z/]+"))
            		{
            			if (i!=0)
            				fw.write("\n");            			
            		}
            		else
            		{
            			if (!Station.containsKey(list[i]))
            				Station.put(list[i], 1);
            			else
            				Station.put(list[i],Station.get(list[i])+1);
            		}
            		fw.write(list[i]+"\t");
            		
            	}
            }
            fw.close();
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
		FileWriter fw = new FileWriter("MultiRouteStation.txt");
		Iterator iter = Station.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			String Station = (String)entry.getKey();
			int times = (int) entry.getValue();
			if (times>20)
			{
				fw.write(Station+" "+times+"\n");
			}
		}
		fw.close();
	}
}
