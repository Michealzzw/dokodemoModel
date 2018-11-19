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
  
public class BaiduMap {  
      
    public static final String KEY_1 = "1QdeSF3EUBXWSXNzNfWBeWiTLBhQmv4W";  
    static Vector<String> airport = new Vector<String>();
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
            	airport.addElement(list[0]);
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
            URL tirc = new URL("http://api.map.baidu.com/geocoder/v2/?address="+ address +"&output=json&ak="+ KEY_1);  
              
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
    	//readFileByLines("MultiRouteStation.txt");
    	readFileByLines("UnHub2HubStation.txt");
//    	FileWriter fw = new FileWriter("trainStation_position.txt");
    	FileWriter fw = new FileWriter("UnHubtrainStation_position.txt");
    	for (int i = 0;i<airport.size();i++)
    	{    		
    		Map<String, String> json = BaiduMapTrain.getGeocoderLatitude(airport.elementAt(i)+"火车站");
    		Map<String, String> tmp = BaiduMapTrain.getGeocoderLatitude(airport.elementAt(i)+"火车站");
    		if (json==null)
    		{
    			System.out.println(airport.elementAt(i));
    			//System.out.println("null");
    		}
    		else
    		{
    			int num = 0;
    			while ((Double.parseDouble(tmp.get("lng"))-Double.parseDouble(json.get("lng")))*(Double.parseDouble(tmp.get("lng"))-Double.parseDouble(json.get("lng")))+
        				(Double.parseDouble(tmp.get("lat"))-Double.parseDouble(json.get("lat")))*(Double.parseDouble(tmp.get("lat"))-Double.parseDouble(json.get("lat")))>0.01)
        		{
        			json = tmp;
        			tmp = BaiduMapTrain.getGeocoderLatitude(airport.elementAt(i)+"火车站");
        			System.out.println(num++);
        			System.out.println(Double.parseDouble(tmp.get("lng"))-Double.parseDouble(json.get("lng")));
        			System.out.println(Double.parseDouble(tmp.get("lat"))-Double.parseDouble(json.get("lat")));
        		}
    			fw.write(airport.elementAt(i)+"\t"+json.get("lng")+"\t"+json.get("lat")+"\n");  
    		}
    	}
    	fw.close();
    }  
      
} 