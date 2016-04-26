package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class getRouteMergeHighSpeed {

	static String Routepath = "TrainScheduleGateNumHighSpeedBool.txt";
	static String Hubpath = "MultiRouteStation.txt";
	static HashMap<String,Integer> PairDis = new HashMap<String,Integer>();
	static HashMap<String,Integer> PairTime = new HashMap<String,Integer>();
	static HashMap<String,String> PairStation = new HashMap<String,String>();
	static HashMap<String,Integer> Station_Hub = new HashMap<String,Integer>();
	static Vector<String> Station_ID = new Vector<String>();
	static Integer[][][] Distance;
	static String[][][] Schedule;
	static Integer[][][] Times;
	static Integer[] GateNum = {1,5,10};
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
	public static int readFileByLines(String fileName,int gate) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	String list[] = tempString.split(" ");
            	int id1 = Station_Hub.get(list[0]);
            	int id2 = Station_Hub.get(list[1]);
            	int dis = Integer.parseInt(list[2]);
            	if (Distance[gate][id1][id2]==null||Distance[gate][id1][id2]>dis)
            	{
            		tempString = tempString.replaceFirst(list[0], "none");
            		tempString = tempString.replaceFirst(list[1], "none");
            		Distance[gate][id1][id2] = dis;
            		Schedule[gate][id1][id2] = tempString.substring(tempString.indexOf(list[4])).replace(" ", "\t");
            		Times[gate][id1][id2] = Integer.parseInt(list[3]);
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
		readHubFileByLines(Hubpath);
		Distance = new Integer[3][Station_ID.size()][Station_ID.size()];
		Schedule = new String[3][Station_ID.size()][Station_ID.size()];
		Times = new Integer[3][Station_ID.size()][Station_ID.size()];
		readFileByLines(Routepath.replace("Num", "1").replace("Bool", "false"),0);
		readFileByLines(Routepath.replace("Num", "1").replace("Bool", "true"),0);
		readFileByLines(Routepath.replace("Num", "5").replace("Bool", "false"),1);
		readFileByLines(Routepath.replace("Num", "5").replace("Bool", "true"),1);
		readFileByLines(Routepath.replace("Num", "10").replace("Bool", "false"),2);
		readFileByLines(Routepath.replace("Num", "10").replace("Bool", "true"),2);
		System.out.println("start");
		for (int gate = 0;gate<3;gate++)
		{
			System.out.println("start");
			for (int k = 0;k<Station_ID.size();k++)
				for (int i = 0;i<Station_ID.size();i++)
					for (int j = 0;j<Station_ID.size();j++)
						if (i!=j&&j!=k&&k!=i)
						if (Distance[gate][i][k]!=null&&Distance[gate][k][j]!=null)
						if (Distance[gate][i][j]==null||Distance[gate][i][k]+Distance[gate][k][j]+(10/GateNum[gate]*150)<Distance[gate][i][j])
						{
							Distance[gate][i][j] = Distance[gate][i][k]+Distance[gate][k][j]+(10/GateNum[gate]*150);
							Times[gate][i][j] = min(Times[gate][i][k],Times[gate][k][j]);
							Schedule[gate][i][j] = Schedule[gate][i][k]+"\t"+Schedule[gate][k][j];
						}
		}
		FileWriter fw = new FileWriter("TrainAllSchedule.txt");
		for (int i = 0;i<Station_ID.size();i++)
			for (int j = 0;j<Station_ID.size();j++)
			{
				fw.write(Station_ID.elementAt(i)+"\t"+Station_ID.elementAt(j)+"\n");
				for (int gate = 0;gate<3;gate++)
				if (Distance[gate][i][j]!=null)
				{
					fw.write(Distance[gate][i][j]+"\t"+Times[gate][i][j]+"\t");
					String[] list = Schedule[gate][i][j].split("\t");
					for (int k = 0;k<list.length-1;k++)
					if (!list[k].equals(list[k+1]))
					{
						fw.write(list[k]+"\t");
					}
					fw.write(list[list.length-1]+"\n");
				}
				else fw.write("null\n");
			}
		fw.close();
	}
	private static Integer min(Integer a, Integer b) {
		if (a<b) return a; else return b;
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