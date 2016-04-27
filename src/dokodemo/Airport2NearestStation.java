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


public class Airport2NearestStation {
	static Vector<position> Airport = new Vector<position>();
	static Vector<position> Station = new Vector<position>();
	public static int readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            FileWriter fw = new FileWriter("airport2station.txt");
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	String[] list = tempString.split("\t");
            	Airport.addElement(new position(list[0],Double.parseDouble(list[1]),Double.parseDouble(list[2])));
            	int i = Airport.size()-1;
            	//double min = 10000; int mark = -1;
            	fw.write(tempString+" ");
            	int num = 0;
        		for (int j = 0;j<gate;j++)
        		{
        			double tmpx =  (Station.elementAt(j).x-Airport.elementAt(i).x);
        			double tmpy =  (Station.elementAt(j).y-Airport.elementAt(i).y);
        			double tmp = tmpx*tmpx+tmpy*tmpy;
        			if (Math.sqrt(tmp)*111<50)  {
        			fw.write("\t"+Station.elementAt(j).name);
        			num++;
        			}
        		}
        		if (num==0)
        		{
        			for (int j = gate;j<Station.size();j++)
            		{
            			double tmpx =  (Station.elementAt(j).x-Airport.elementAt(i).x);
            			double tmpy =  (Station.elementAt(j).y-Airport.elementAt(i).y);
            			double tmp = tmpx*tmpx+tmpy*tmpy;
            			if (Math.sqrt(tmp)*111<50)  {
            			fw.write("\t"+Station.elementAt(j).name);
            			num++;
            			}
            		}
        		}
        		fw.write("\n");
        		
            }
            reader.close();
            fw.close();
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
	public static int readStationFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	String[] list = tempString.split("\t");
            	Station.addElement(new position(list[0],Double.parseDouble(list[1]),Double.parseDouble(list[2])));
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
	static int gate;
	static public void main(String[] args) throws IOException
	{
		readStationFileByLines("trainStation_position.txt");
		gate = Station.size();
		readStationFileByLines("UnHubtrainStation_position.txt");
		readFileByLines("airport_position.txt");
	}
}
