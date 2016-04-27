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
  
public class BaiduMapReverse {  
      
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
            	airport.addElement(tempString);
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
    public static String getGeocoderLatitude(String address){  
        BufferedReader in = null;  
        try {  
            //将地址转换成utf-8的16进制  
        	String[]list = address.split("\t");
            //address = URLEncoder.encode(address, "UTF-8");  
//       如果有代理，要设置代理，没代理可注释  
//      System.setProperty("http.proxyHost","192.168.1.188");  
//      System.setProperty("http.proxyPort","3128");
            
            URL tirc = new URL("http://api.map.baidu.com/geocoder/v2/?location="+ list[2]+","+list[1] +"&ak="+ KEY_1);  
            
            in = new BufferedReader(new InputStreamReader(tirc.openStream(),"UTF-8"));  
            String res;  
            StringBuilder sb = new StringBuilder("");  
            while((res = in.readLine())!=null){  
                sb.append(res.trim());  
            }  
            String str = sb.toString();  
            return str;  
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
    	readFileByLines("airport_position.txt");
    	FileWriter fw = new FileWriter("airport_position_reverse.txt");
    	for (int i = 0;i<airport.size();i++)
    	{    		
    		String json = BaiduMapReverse.getGeocoderLatitude(airport.elementAt(i));
    		fw.write(airport.elementAt(i)+"\n"+json+"\n");  
    		
    	}
    	fw.close();
    }  
      
} 