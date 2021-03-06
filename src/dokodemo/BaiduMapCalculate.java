package dokodemo;  
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.URL;  
import java.net.URLEncoder;  
import java.util.HashMap;  
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;  

class position{
	String name;
	double x,y;
	public position(String _n,double _x,double _y)
	{
		name = _n;
		x = _x;
		y = _y;
	}
}

public class BaiduMapCalculate {  
      
    public static final String KEY_1 = "7d9fbeb43e975cd1e9477a7e5d5e192a";  
    static Vector<position> airport = new Vector<position>();
    static Vector<position> station = new Vector<position>();
	public static int readAirFileByLines(String fileName) {
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
            	airport.addElement(new position(list[0],Double.parseDouble(list[1]),Double.parseDouble(list[2])));
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
	public static int readTrainFileByLines(String fileName) {
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
            	station.addElement(new position(list[0],Double.parseDouble(list[1]),Double.parseDouble(list[2])));
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
    /** 
     * 返回输入地址的经纬度坐标 
     * key lng(经度),lat(纬度) 
     */  
    public static Map<String,String> getGeocoderLatitude(String address){  
        BufferedReader in = null;  
        try {  
            //将地址转换成utf-8的16进制  
            address = URLEncoder.encode(address, "UTF-8");  
//       如果有代理，要设置代理，没代理可注释  
//      System.setProperty("http.proxyHost","192.168.1.188");  
//      System.setProperty("http.proxyPort","3128");  
            URL tirc = new URL("http://api.map.baidu.com/geocoder?address="+ address +"&output=json&key="+ KEY_1);  
              
            in = new BufferedReader(new InputStreamReader(tirc.openStream(),"UTF-8"));  
            String res;  
            StringBuilder sb = new StringBuilder("");  
            while((res = in.readLine())!=null){  
                sb.append(res.trim());  
            }  
            String str = sb.toString();  
            Map<String,String> map = null;  
            if(StringUtils.isNotEmpty(str)){  
                int lngStart = str.indexOf("lng\":");  
                int lngEnd = str.indexOf(",\"lat");  
                int latEnd = str.indexOf("},\"precise");  
                if(lngStart > 0 && lngEnd > 0 && latEnd > 0){  
                    String lng = str.substring(lngStart+5, lngEnd);  
                    String lat = str.substring(lngEnd+7, latEnd);  
                    map = new HashMap<String,String>();     
                    map.put("lng", lng);  
                    map.put("lat", lat);  
                    return map;  
                }  
            }  
        }catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return null;  
    }  
    public static void main(String args[]) throws IOException{
    	readAirFileByLines("airport_position.txt");
    	readTrainFileByLines("trainStation_position.txt");
    	//FileWriter fw = new FileWriter("trainStation_position.txt");
    	for (int i = 0;i<airport.size();i++)
    	{    		
    		double min = 10000; int mark = -1;
    		for (int j = 0;j<station.size();j++)
    		{
    			double tmpx =  (station.elementAt(j).x-airport.elementAt(i).x);
    			double tmpy =  (station.elementAt(j).y-airport.elementAt(i).y);
    			double tmp = tmpx*tmpx+tmpy*tmpy;
    			if (tmp<min)  {mark = j;min = tmp;}    			
    		}
    		System.out.println(airport.elementAt(i).name+" "+station.elementAt(mark).name);
    	}
    	//fw.close();
    }  
      
} 