package dokodemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class GenerateSeederModel {
	static HashMap<String,Integer> Airport = new HashMap<String,Integer>();
	static FileWriter fw;
	static HashMap<String,Integer> Station_Hub = new HashMap<String,Integer>();
	static HashMap<String,Integer> Station_ID = new HashMap<String,Integer>();
	static HashMap<String,Integer> Airport_ID = new HashMap<String,Integer>();
	public static String string2Unicode(String string) {
		 return string;
//	    StringBuffer unicode = new StringBuffer();
//	 
//	    for (int i = 0; i < string.length(); i++) {
//	 
//	        // 取出每一个字符
//	        char c = string.charAt(i);
//	 
//	        // 转换为unicode
//	        unicode.append("\\u" + Integer.toHexString(c));
//	    }
//	 
//	    return unicode.toString();
	}
	public static int read3lineFileByLines(String fileName,int No) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	generate(tempString+"\n"+reader.readLine()+"\n"+reader.readLine()+"\n"+reader.readLine(),No);
            	
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
	public static int readFileByLines(String fileName,int No) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            while ((tempString = reader.readLine()) != null) {
            	generate(tempString,No);
            	
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
	static int maxair = 0;
	static int maxtrain = 0;
	static int maxstat = 0;
	static int StatNum = 0;
	static int AirpNum = 0;
	static String generate(String tmp,int No) throws IOException
	{
		if (No == 0)
		{
			String[] list = tmp.split("\t");
			Station_Hub.put(list[0], 0);
			StatNum++;
			Station_ID.put(list[0], StatNum);
			fw.write("Station::create([\'stat_id\' => "+StatNum+
					",\'stat_name\' => \'"+string2Unicode(list[0])+
					"\',\'stat_type\' => 1,\'stat_lng\' => "+list[1]+",\'stat_lat\' => "+list[2]+"]);\n");
		}
		if (No == 1)
		{
			String[] list = tmp.split("\t");
			StatNum++;
			Station_ID.put(list[0], StatNum);
			fw.write("Station::create([\'stat_id\' => "+StatNum+
					",\'stat_name\' => \'"+string2Unicode(list[0])+
					"\',\'stat_type\' => 0,\'stat_lng\' => "+list[1]+",\'stat_lat\' => "+list[2]+"]);\n");
		}
		if (No == 2)
		{
			String[] list = tmp.split("\t");
			AirpNum++;
			Airport_ID.put(list[0], AirpNum);
			String now = "";
			for (int i = 3;i<list.length-1;i++)
				now+=Station_ID.get(list[i])+"\t";
			if (list.length>3)now+=Station_ID.get(list[list.length-1]);
			fw.write("Airport::create([\'airp_id\' => "+AirpNum+
					",\'airp_name\' => \'"+string2Unicode(list[0])+
					"\',\'airp_lng\' => "+list[1]+",\'airp_lat\' => "+list[2]+","
							+ "\'airp_nearstat\' => \'"
							+ now
							+ "\']);\n");
			if (now.length()>maxair) maxair = now.length();
		}
		if (No == 3)
		{
			String[] list = tmp.split("\n");
			String[] list2 = list[0].split("\t");
			for (int i = 0;i<3;i++)
			{
				String[] l = list[i+1].split("\t");
				for (int j = 0;j<l.length;j++)
					if (Station_ID.containsKey(l[j]))
					list[i+1] = list[i+1].replaceFirst(l[j], Station_ID.get(l[j]).toString());
			}
			fw.write("Stat2StatSchedule::create([\'from_stat_id\' => "+Station_ID.get(list2[0])+
					",\'to_stat_id\' => "+Station_ID.get(list2[1])+",\'schedule\' => \'"
							+ list[1]+"\\n"+list[2]+"\\n"+list[3]
							+ "\']);\n");
			if ((list[1]+"\\n"+list[2]+"\\n"+list[3]).length()>maxstat) maxstat = (list[1]+"\\n"+list[2]+"\\n"+list[3]).length();
		}
		if (No == 4)
		{
			String[] list = tmp.split(" ");
			String schedule="";
			String[] stat = new String[list.length/4];
			for (int i = 1;i<list.length;i+=4)
			if (Station_ID.containsKey(list[i]))
			{
				schedule += Station_ID.get(list[i])+"\t"+list[i+1]+"\t"+list[i+2]+"\\n";
				stat[i/4] = list[i];
			}
			else System.out.println(list[i]);
			if (schedule.length()>maxtrain) maxtrain =schedule.length(); 
			fw.write("Train::create([\'train_no\' => \'"+list[0]+
					"\',\'train_schedule\' => \'"+schedule+ "\']);\n");
			for (int i = 0;i<stat.length-1;i++)
				for (int j = i+1;j<stat.length;j++)
				if (Station_Hub.containsKey(stat[i])&&Station_Hub.containsKey(stat[j]))
				{
					fw.write("Hub2HubTrain::create([\'train_no\' => \'"+list[0]+
							"\',\'from_stat_id\' => "+Station_ID.get(stat[i])+ ","
									+ "\'to_stat_id\' => "+Station_ID.get(stat[j])+ "]);\n");
				}
			for (int i = 0;i<stat.length;i++)
			if (!Station_Hub.containsKey(stat[i]))
				{
				if (Station_ID.containsKey(stat[i]))
					fw.write("UnHubTrain::create([\'train_no\' => \'"+list[0]+
							"\',\'stat_id\' => \'"+Station_ID.get(stat[i])+ "\']);\n");
				else
				{
					//System.out.println(stat[i]);
				}
				}
			
		}
		return null;
	}
	static public void main(String[] args) throws IOException
	{
		fw = new FileWriter("ModelSeeder.php");
		fw.write("<?php\nuse Illuminate\\Database\\Seeder;\nuse App\\Station;"
				+ "\nuse App\\Airport;\nuse App\\Train;\nuse App\\Stat2StatSchedule;"
				+ "\nuse App\\Hub2HubTrain;\nuse App\\UnHubTrain;\nuse Illuminate\\Support\\Facades\\Hash;"
				+ "\nclass ModelSeeder extends Seeder\n{\npublic function run()\n{");
		fw.write("DB::table('Stations')->delete();\n");
		fw.write("DB::table('Airports')->delete();\n");
		fw.write("DB::table('Stat2StatSchedules')->delete();\n");
		
		fw.write("DB::table('Trains')->delete();\n");
		fw.write("DB::table('UnHubTrains')->delete();\n");
		fw.write("DB::table('Hub2HubTrains')->delete();\n");
		
		readFileByLines("trainStation_position.txt",0);
		readFileByLines("UnHubtrainStation_position.txt",1);
		readFileByLines("airport2station.txt",2);
		read3lineFileByLines("TrainAllSchedule.txt",3);
		readFileByLines("traininfo.txt",4);
		fw.write("\n}\n}");
		fw.close();
		System.out.println(maxair);
		System.out.println(maxtrain);
		System.out.println(maxstat);
	}
}
