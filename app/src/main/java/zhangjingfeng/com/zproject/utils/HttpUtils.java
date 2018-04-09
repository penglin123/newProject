package zhangjingfeng.com.zproject.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class HttpUtils
{

	public static JSONObject httpUrlConnection(Context context, String pathUrl, JSONObject param){
	    try{
	     //建立连接
	     URL url=new URL(pathUrl);
	     HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
	     
	     ////设置连接属性
	     httpConn.setDoOutput(true);//使用 URL 连接进行输出
	     httpConn.setDoInput(true);//使用 URL 连接进行输入
	     httpConn.setUseCaches(false);//忽略缓存
	     httpConn.setRequestMethod("POST");//设置URL请求方法
	     //设置请求属性
	    //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
	          byte[] requestStringBytes = param.toString().getBytes("UTF-8");
	          httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
	          httpConn.setRequestProperty("Content-Type", "application/json");
	          httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
	          httpConn.setRequestProperty("Charset", "UTF-8");
//	          //
//	          String name=URLEncoder.encode("13595618663","utf-8");
//	          httpConn.setRequestProperty("account", name);
//	          
//	          String pwd=URLEncoder.encode("123456","utf-8");
//	          httpConn.setRequestProperty("pasword", pwd);
//	          
//	          String mac=URLEncoder.encode("f8:a4:5f:19:d5:ed","utf-8");
//	          httpConn.setRequestProperty("mac", mac);
//	          //建立输出流，并写入数据
	          OutputStream outputStream = httpConn.getOutputStream();
	          outputStream.write(requestStringBytes);
	          outputStream.close();
	         //获得响应状态
	          int responseCode = httpConn.getResponseCode();
	          if(HttpURLConnection.HTTP_OK == responseCode){//连接成功
	           
	           //当正确响应时处理数据
	           StringBuffer sb = new StringBuffer();
	              String readLine;
	              BufferedReader responseReader;
	             //处理响应流，必须与服务器响应流输出的编码一致
	              responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
	              while ((readLine = responseReader.readLine()) != null) {
	            	  sb.append(readLine).append("\n");
	              }
	             
	              responseReader.close();
	              String s=sb.toString().substring(1,sb.length()-2);
	              JSONObject json=new JSONObject( StringEscapeUtils.unescapeJava(s));
	              return json;
	          }else{
	        	  return null;
	          }
	    }catch(Exception ex){
	     return null;
	    }
	   }
	
	public static JSONArray httpUrlConnection4JsonArray(Context context, String pathUrl, JSONObject param){
	    try{
	     //建立连接
	     URL url=new URL(pathUrl);
	     HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
	     
	     ////设置连接属性
	     httpConn.setDoOutput(true);//使用 URL 连接进行输出
	     httpConn.setDoInput(true);//使用 URL 连接进行输入
	     httpConn.setUseCaches(false);//忽略缓存
	     httpConn.setRequestMethod("POST");//设置URL请求方法
	     //设置请求属性
	    //获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
	          byte[] requestStringBytes = param.toString().getBytes("UTF-8");
	          httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
	          httpConn.setRequestProperty("Content-Type", "application/json");
	          httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
	          httpConn.setRequestProperty("Charset", "UTF-8");
//	          //
//	          String name=URLEncoder.encode("13595618663","utf-8");
//	          httpConn.setRequestProperty("account", name);
//	          
//	          String pwd=URLEncoder.encode("123456","utf-8");
//	          httpConn.setRequestProperty("pasword", pwd);
//	          
//	          String mac=URLEncoder.encode("f8:a4:5f:19:d5:ed","utf-8");
//	          httpConn.setRequestProperty("mac", mac);
//	          //建立输出流，并写入数据
	          OutputStream outputStream = httpConn.getOutputStream();
	          outputStream.write(requestStringBytes);
	          outputStream.close();
	         //获得响应状态
	          int responseCode = httpConn.getResponseCode();
	          if(HttpURLConnection.HTTP_OK == responseCode){//连接成功
	           
	           //当正确响应时处理数据
	           StringBuffer sb = new StringBuffer();
	              String readLine;
	              BufferedReader responseReader;
	             //处理响应流，必须与服务器响应流输出的编码一致
	              responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
	              while ((readLine = responseReader.readLine()) != null) {
	            	  sb.append(readLine).append("\n");
	              }
	             
	              responseReader.close();
	              JSONArray json=new JSONArray( StringEscapeUtils.unescapeJava(sb.toString()));
	              return json;
	          }else{
	        	  return null;
	          }
	    }catch(Exception ex){
	     return null;
	    }
	   }
	public static String getLocalMacAddress(Context con) {

		final TelephonyManager tm = (TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE);
		 
	    final String tmDevice, tmSerial, tmPhone, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(con.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	 
	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String uniqueId = deviceUuid.toString();
	    
	    Log.d("sort", uniqueId);
		return uniqueId;
	}
}
