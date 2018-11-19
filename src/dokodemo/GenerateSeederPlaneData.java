package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class GenerateSeederPlaneData {
	static FileWriter fw;

	static HashMap<String,Integer> Airport_ID = new HashMap<String,Integer>();
	static HashMap<String,Integer> Station_ID = new HashMap<String,Integer>();
	static HashMap<String,Integer> PlaneId = new HashMap<String,Integer>();
	public static int read3lineFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {

            	String sche = reader.readLine();
            	String tmp = reader.readLine();
            	//System.out.println(tmp);
            	int idf = -1;
            	if (Airport_ID.containsKey(tmp)) idf = Airport_ID.get(tmp);
            	tmp =reader.readLine();
            	if (tmp.equals("经停")){
            		tmp =reader.readLine();
            		tmp =reader.readLine();
            	}
            	sche +="\t"+tmp;
            	tmp = reader.readLine();
            	int idt = -1;
            	if (Airport_ID.containsKey(tmp)) idt = Airport_ID.get(tmp);
            	if (idf==-1||idt==-1) continue;
            	if (!PlaneId.containsKey(tempString))
            	{
            	fw.write("Plane::create([\'plane_no\' => \'"+tempString+
    					"\',\'plane_schedule\' => \'"+sche+ "\',\'from_airp_id\' => "+idf+ ","
    									+ "\'to_airp_id\' => "+idt+ "]);\n");
            	PlaneId.put(tempString, 0);
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
	public static int readSFileByLines(String fileName) {
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
    			StatNum++;
    			Station_ID.put(list[0], StatNum);
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
            	String[] list = tempString.split("\t");
    			AirpNum++;
    			Airport_ID.put(list[0], AirpNum);
    			String now = "";
    			for (int i = 3;i<list.length-1;i++)
    				now+=Station_ID.get(list[i])+"\t";
    			if (list.length>3)now+=Station_ID.get(list[list.length-1]);
    			fw.write("Airport::create([\'airp_id\' => "+AirpNum+
    					",\'airp_name\' => \'"+(list[0])+
    					"\',\'airp_lng\' => "+list[1]+",\'airp_lat\' => "+list[2]+","
    							+ "\'airp_nearstat\' => \'"
    							+ now
    							+ "\']);\n");
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
	//		\'\' => \'\',
	static int AirpNum = 0;
	static int StatNum = 0;
	static public void main(String[] args) throws IOException
	{
		fw = new FileWriter("PlaneSeeder.php");
		fw.write("<?php\nuse Illuminate\\Database\\Seeder;\nuse App\\Station;"
				+ "\nuse App\\Plane;\nuse App\\Airport;\nuse App\\Stat2StatSchedule;"
				+ "\nuse App\\Hub2HubTrain;\nuse App\\UnHubTrain;\nuse Illuminate\\Support\\Facades\\Hash;"
				+ "\nclass PlaneSeeder extends Seeder\n{\npublic function run()\n{");
		fw.write("DB::table('Planes')->delete();\n");
		fw.write("DB::table('Airports')->delete();\n");
		readSFileByLines("trainStation_position.txt");
		readSFileByLines("UnHubtrainStation_position.txt");
		readFileByLines("airport2station.txt");
		read3lineFileByLines("planeinfo.txt");
		
		fw.write("\n}\n}");
		fw.close();
	}
}
