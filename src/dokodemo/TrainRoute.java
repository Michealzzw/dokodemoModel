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

public class TrainRoute {
	static String path = "StationCity.txt";
	static String Hubpath = "MultiRouteStation.txt";
	static HashMap<String,Vector<String>> City2Station = new HashMap<String,Vector<String>>();
	static HashMap<String,Integer> Station_Hub = new HashMap<String,Integer>();
	static Vector<String> Station_ID = new Vector<String>();
	public static int readHubFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	Station_Hub.put(tempString.split(" ")[0],line);
            	Station_ID.addElement(tempString.split(" ")[0]);
            	line++;
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
	public static int readFileByLines(String fileName) {
		Pattern citypt = Pattern.compile("(香港特别行政区)|(.+[市|县|旗])");
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String StationName = null;
            //tempString = reader.readLine();
            line = 0;
            while ((StationName = reader.readLine()) != null) {
            	String CityName = reader.readLine();
            	if (!Station_Hub.containsKey(StationName)) continue;
            	Matcher mc = citypt.matcher(CityName);
            	if (mc.find())
            	{
            		CityName = mc.group();
            		if (!City2Station.containsKey(CityName))
            			City2Station.put(CityName, new Vector<String>());
            		City2Station.get(CityName).addElement(StationName);
            	}
            	else
            		System.out.println(CityName);
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
		readHubFileByLines(Hubpath);
		readFileByLines(path);
		FileWriter fw = new FileWriter("MulStationCity.txt");
		Iterator iter = City2Station.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			String CityName = (String)entry.getKey();
			Vector<String> StaList = (Vector<String>) entry.getValue();
			if (StaList.size()>1)
			{
				fw.write(CityName);
				for (int i = 0;i<StaList.size();i++)
				{
					fw.write("\t"+StaList.elementAt(i));
				}
				fw.write("\n");
			}
		}
		fw.close();
	}
}
