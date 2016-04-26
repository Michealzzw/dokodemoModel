package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class NonHub2Hub {
	static String Routepath = "SMtrainRoute.txt";
	static String Hubpath = "MultiRouteStation.txt";
	static String Railwaypath = "RouteDis.txt";
	static HashMap<String,Integer> Station_Hub = new HashMap<String,Integer>();
	static HashMap<String,HashMap<String,Integer>> Station2Station = new HashMap<String,HashMap<String,Integer>>();
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
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	String list[] = tempString.split("\t");
            	int pre = 1;
            	for (int i = 1;i<list.length;i++)
            	if (Station_Hub.containsKey(list[i]))
            	{            		
            		for (int j = 1;j<i;j++)
            			if (!Station_Hub.containsKey(list[j]))
            			{
            				if (!Station2Station.containsKey(list[j]))
            					{
            						Station2Station.put(list[j], new HashMap<String,Integer>());
            					}
            				if (Station2Station.get(list[j]).containsKey(list[i]))
            					Station2Station.get(list[j]).put(list[i],Station2Station.get(list[j]).get(list[i])+1);
            				else
            					Station2Station.get(list[j]).put(list[i],1);
            			}
            		//pre = i;
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
	static HashMap<String,Integer> PairDis = new HashMap<String,Integer>();
	static HashMap<String,String> PairStation = new HashMap<String,String>();
	static HashMap<String,Vector<Integer>> Route_Dis = new HashMap<String,Vector<Integer>>();
	static HashMap<String,Vector<String>> Route_Station = new HashMap<String,Vector<String>>();
	public static int readWayFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	String list[] = tempString.split(",");
            	if (!Route_Station.containsKey(list[0]))
            	{
            		Route_Station.put(list[0], new Vector<String>());
            		Route_Dis.put(list[0], new Vector<Integer>());
            	}
            	Route_Station.get(list[0]).addElement(list[1]);
        		Route_Dis.get(list[0]).addElement(Integer.parseInt(list[2]));
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
		readWayFileByLines(Railwaypath);
		Iterator iter = Route_Station.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			String route = (String)entry.getKey();
			Vector<String> sta = Route_Station.get(route);
			Vector<Integer> dis= Route_Dis.get(route);
			for (int i = 0;i<sta.size();i++)
				for (int j = 0;j<sta.size();j++)
					if (i!=j)
					{
						String a = sta.elementAt(i);
						String b = sta.elementAt(j);
						int da = dis.elementAt(i);
						int db = dis.elementAt(j);
						int dist;
						if (da>db) dist = da-db;else dist = db-da; 
						PairDis.put(a+","+b, dist);
					}
			
		}
		readHubFileByLines(Hubpath);
		readFileByLines(Routepath);
		FileWriter fw = new FileWriter("UnHub2HubStation.txt");
		iter = Station2Station.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			fw.write((String)entry.getKey());
			HashMap<String,Integer> sta = (HashMap)entry.getValue();
			Iterator iter2 = sta.entrySet().iterator();
			while (iter2.hasNext()) {
				HashMap.Entry entry2 = (HashMap.Entry) iter2.next();
				if (PairDis.containsKey(((String)entry2.getKey())+","+entry.getKey()))
						fw.write("\t"+(String)entry2.getKey()+"\t"+(Integer)entry2.getValue()+"\t"+PairDis.get(((String)entry2.getKey())+","+entry.getKey()));
			}
			fw.write("\n");
		}
		fw.close();
	}
	private static Integer min(Integer a, Integer b) {
		if (a<b) return a; else return b;
	}
}