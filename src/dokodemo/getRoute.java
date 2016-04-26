package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class getRoute {
	static String Hubpath = "Station_Hub.txt";
	static String Routepath = "RouteDis.txt";
	static HashMap<String,Integer> PairDis = new HashMap<String,Integer>();
	static HashMap<String,Integer> PairTime = new HashMap<String,Integer>();
	static HashMap<String,String> PairStation = new HashMap<String,String>();
	static Vector<String> Station_Hub = new Vector<String>();
	static HashMap<String,Vector<Integer>> Route_Dis = new HashMap<String,Vector<Integer>>();
	static HashMap<String,Vector<String>> Route_Station = new HashMap<String,Vector<String>>();
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
            	Station_Hub.addElement(tempString.split(" ")[0]);
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
		readFileByLines(Routepath);
		readHubFileByLines(Hubpath);
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
						PairTime.put(a+","+b, 0);
					}
			
		}
		for (int k = 0;k<Station_Hub.size();k++)
		for (int i = 0;i<Station_Hub.size();i++)
			for (int j = 0;j<Station_Hub.size();j++)
					if (i!=j&&j!=k&&k!=i)
					{
						String a = Station_Hub.elementAt(i);
						String b = Station_Hub.elementAt(j);
						String c= Station_Hub.elementAt(k);
						if (PairDis.containsKey(a+","+c)&&PairDis.containsKey(c+","+b))
						if (!PairDis.containsKey(a+","+b)||
								(PairDis.get(a+","+b)>PairDis.get(a+","+c)+PairDis.get(c+","+b))||
								((PairDis.get(a+","+b)==PairDis.get(a+","+c)+PairDis.get(c+","+b))
										&&
										PairTime.get(a+","+b)>PairTime.get(a+","+c)+PairTime.get(c+","+b)+1))
						{
							PairDis.put(a+","+b, PairDis.get(a+","+c)+PairDis.get(c+","+b));
							PairTime.put(a+","+b, PairTime.get(a+","+c)+PairTime.get(c+","+b)+1);
							PairStation.put(a+","+b, c);
						}
					}
		FileWriter fw = new FileWriter("TrainSchedule.txt");
		iter = PairDis.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			fw.write(entry.getKey()+" "+entry.getValue()+" "+PairTime.get(entry.getKey())+" ");
			routegenerate(((String)entry.getKey()).split(",")[0],((String)entry.getKey()).split(",")[1],fw);
			fw.write(((String)entry.getKey()).split(",")[1]+"\n");
		}
		fw.close();
	}
	static void routegenerate(String a, String b,FileWriter fw) throws IOException
	{
		if (PairStation.containsKey(a+","+b))
		{
			String c = PairStation.get(a+","+b);
			routegenerate(a,c,fw);
			routegenerate(c,b,fw);
		}
		else fw.write(a+" ");
	}
}