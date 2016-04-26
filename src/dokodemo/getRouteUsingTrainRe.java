package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

public class getRouteUsingTrainRe {
	static String Hubpath = "MultiRouteStation.txt";
	static String Routepath = "trainRe.txt";
	static int Gate = 10;
	static int ChangeTime = 150;
	static boolean HighSpeed = false;
	static HashMap<String,Integer> PairDis = new HashMap<String,Integer>();
	static HashMap<String,Integer> PairDense = new HashMap<String,Integer>();
	static HashMap<String,String> PairStation = new HashMap<String,String>();
	static Vector<String> Station_Hub = new Vector<String>();
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
            	String list[] = tempString.split("\t");
            	if (HighSpeed&&!list[0].contains("G")&&!list[0].contains("D")) continue;
            	Vector<String> sta = new Vector<String>();
            	Vector<Integer> dis = new Vector<Integer>();
            	if (list.length>3)
            	{
            		dis.addElement(0);
            		int sum = 0;
            		for (int i = 1;i<list.length;i+=2)
            		{
            			sta.addElement(list[i]);
            			if (i+1<list.length)
            				{
            					sum = sum + Integer.parseInt(list[i+1]);
            					if (HighSpeed) dis.addElement(sum);
            					else dis.addElement(sum*3);
            				}
            		}
            		for (int i = 0;i<sta.size()-1;i++)
            			for (int j = i+1;j<sta.size();j++)
	            		{
	            			if (PairDense.containsKey((sta.elementAt(i)+" "+sta.elementAt(j))))
	        					PairDense.put(sta.elementAt(i)+" "+sta.elementAt(j), PairDense.get((sta.elementAt(i)+" "+sta.elementAt(j)))+1);
	        				else PairDense.put(sta.elementAt(i)+" "+sta.elementAt(j), 1);
	            			if (!PairDis.containsKey(sta.elementAt(i)+" "+sta.elementAt(j))||dis.elementAt(j)-dis.elementAt(i)<PairDis.get(sta.elementAt(i)+" "+sta.elementAt(j))) 
	            				PairDis.put(sta.elementAt(i)+" "+sta.elementAt(j), dis.elementAt(j)-dis.elementAt(i));
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
	public static void main(String[] args) throws IOException
	{
		PairDis = new HashMap<String,Integer>();
		PairDense = new HashMap<String,Integer>();
		PairStation = new HashMap<String,String>();
		Station_Hub = new Vector<String>();
		if (args.length>0)
		{
			Gate = Integer.parseInt(args[0]);
			if (Integer.parseInt(args[1])==1)
			HighSpeed = true; else HighSpeed = false;
			ChangeTime = 10/Gate*150;
		}
		readFileByLines(Routepath);
		readHubFileByLines(Hubpath);
		for (int k = 0;k<Station_Hub.size();k++)
		for (int i = 0;i<Station_Hub.size();i++)
			for (int j = 0;j<Station_Hub.size();j++)				
					if (i!=j&&j!=k&&k!=i)
					{
						if (k%100==0&&j==1&&i==2) System.out.println(k);
						String a = Station_Hub.elementAt(i);
						String b = Station_Hub.elementAt(j);
						String c= Station_Hub.elementAt(k);
						if (PairDis.containsKey(a+" "+c)&&PairDis. containsKey(c+" "+b))
						if (PairDense.get(a+" "+c)>Gate&&PairDense.get(c+" "+b)>Gate)
						if (!PairDis.containsKey(a+" "+b)||(PairDense.get(a+" "+b)<=Gate)||
								(PairDis.get(a+" "+b)>PairDis.get(a+" "+c)+PairDis.get(c+" "+b)+ChangeTime))
						{
							PairDis.put(a+" "+b, PairDis.get(a+" "+c)+PairDis.get(c+" "+b)+ChangeTime);
							PairDense.put(a+" "+b, min(PairDense.get(a+" "+c),PairDense.get(c+" "+b)));
							PairStation.put(a+" "+b, c);
						}
					}
		FileWriter fw = new FileWriter("TrainScheduleGate"+Gate+"HighSpeed"+HighSpeed+".txt");
		Iterator<Entry<String, Integer>> iter = PairDis.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			if (PairDense.get(entry.getKey())>Gate)
			{
			fw.write(entry.getKey()+" "+entry.getValue()+" "+PairDense.get(entry.getKey())+" ");
			routegenerate(((String)entry.getKey()).split(" ")[0],((String)entry.getKey()).split(" ")[1],fw);
			fw.write(((String)entry.getKey()).split(" ")[1]+"\n");
			}
		}
		fw.close();
	}
	private static Integer min(Integer a, Integer b) {
		if (a<b) return a; else return b;
	}
	static void routegenerate(String a, String b,FileWriter fw) throws IOException
	{
		if (PairStation.containsKey(a+" "+b))
		{
			String c = PairStation.get(a+" "+b);
			routegenerate(a,c,fw);
			routegenerate(c,b,fw);
		}
		else fw.write(a+" ");
	}
}