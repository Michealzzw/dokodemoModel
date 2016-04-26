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

public class StationCity {
	static String path = "SMtrainRoute.txt";
	static HashMap<String,Integer> Station = new HashMap<String,Integer>();
	static HashMap<String,Integer> Pair_Dis = new HashMap<String,Integer>();
	public static int readStationByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	String[] list = tempString.split(" ");
	            Station.put(list[0], Integer.parseInt(list[1]));
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
	public static int readDisByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	String[] list = tempString.split(" ");
            	Pair_Dis.put(list[0]+" "+list[1], Integer.parseInt(list[2]));
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
        	FileWriter fw = new FileWriter("trainRe.txt");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;            
            while ((tempString = reader.readLine()) != null) {
            	String[] list = tempString.split("\t");
            	fw.write(list[0]);
            	String pre = null;
            	int count = 0;
            	for (int i = 1;i<list.length;i++)
            	{
            		if (Station.containsKey(list[i]))
            		{
            			count++;
            			if (pre==null)
            			{
            				pre = list[i];
            				fw.write("\t"+list[i]);
            			}
            			else
            			{
            				int tmp = 0;
            				if (Pair_Dis.containsKey(pre+" "+list[i])) tmp = Pair_Dis.get(pre+" "+list[i]);
            				else tmp = Pair_Dis.get(list[i]+" "+pre);
            				fw.write("\t"+tmp+"\t"+list[i]);
            				pre = list[i];
            			}
            		}
            	}
            	fw.write("\n");
            	if (pre==null||count==1) {System.out.println(list[0]);line++;}
            }
            System.out.println(line);
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
		readDisByLines("trainPairDis.txt");
		readStationByLines("MultiRouteStation.txt");
		readFileByLines(path);
	}
}
