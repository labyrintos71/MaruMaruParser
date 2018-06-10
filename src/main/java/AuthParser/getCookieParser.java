package AuthParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.StringTokenizer;

public class getCookieParser {
    private long oldtime,nowtime;
    private String line,temp,str;
    private int cnt=0;
    public getCookieParser(){
        oldtime=System.currentTimeMillis();
        try {
            connectServer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        nowtime=System.currentTimeMillis();
        System.out.println((float)(nowtime-oldtime)/(float)1000+"s"+" -getCookie");
    }

    public void connectServer() throws IOException{
        StringBuilder html = new StringBuilder();

        URL url = new URL("http://wasabisyrup.com/archives/1858776");
        URLConnection openconn=url.openConnection();
        openconn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        openconn.setRequestProperty("Cookie","PHPSESSID=76894a0ff7475c0db6a16abfce68fcf3;");
        BufferedReader br = new BufferedReader(new InputStreamReader(openconn.getInputStream(), "UTF-8"));

        for (; ; ) {
            line=br.readLine();
            if(line==null) break;
            html.append(line);
        }
        br.close();
        str=html.toString();
        System.out.println(str);
    }

    public String cookieDecoder(String str){
        String sValue,fValue,cookieValue;
        String decodedString="";
        sValue=str.substring(str.indexOf("S=")+3);
        sValue=sValue.substring(0,sValue.indexOf("'"));

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(sValue);
            decodedString = new String(decodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        decodedString=decodedString.replace("\"", "");
        decodedString=decodedString.replace("'", "");
        decodedString=decodedString.replace(" ", "");

        fValue=decodedString.substring(decodedString.indexOf("=")+1, decodedString.indexOf("document"));
        decodedString=decodedString.substring(decodedString.indexOf("document"));
        cookieValue=decodedString.substring(decodedString.indexOf("=")+1, decodedString.indexOf(";"));
        decodedString=decodedString.substring(decodedString.indexOf(";")+1);
        decodedString=decodedString.substring(0,decodedString.indexOf("location"));

        StringTokenizer st=new StringTokenizer(fValue,"+");
        fValue=plusDecode(st);
        st=new StringTokenizer(cookieValue,"+");
        cookieValue=plusDecode(st);
        cookieValue=cookieValue.substring(0, cookieValue.indexOf("=")+1)+fValue;
        cookieValue=cookieValue.replace("\n", "");
        //cookieValue+=decodedString;
        return cookieValue;
    }

    public String plusDecode(StringTokenizer st){
        String fValueFix="";
        int start=0,end=0;
        while(st.hasMoreTokens()){
            fValueFix=fValueFix.replaceAll("\n","");
            String temp=st.nextToken().replaceAll("\n","");
            if(temp.equals("")|temp==null){
                temp="";
            }else if(temp.indexOf("slice")!=-1){
                start=Integer.parseInt(temp.substring(temp.indexOf("(")+1,temp.indexOf(",")));
                end=Integer.parseInt(temp.substring(temp.indexOf(",")+1,temp.indexOf(")")));
                if(start>end)end+=start;
                fValueFix+=temp.substring(start,end);
            }else if(temp.indexOf("fromCharCode")!=-1){
                if(temp.indexOf("0x")==-1) fValueFix+=(char)Integer.parseInt(temp.substring(temp.indexOf("(")+1,temp.indexOf(")")));
                else fValueFix+=(char)Integer.parseInt(temp.substring(temp.indexOf("(0x")+3,temp.indexOf(")")),16);
            }else if(temp.indexOf("charAt")!=-1){
                fValueFix+=temp.charAt(Integer.parseInt(temp.substring(temp.indexOf("(")+1,temp.indexOf(")"))));
            }else if(temp.indexOf("substr")!=-1){
                start=Integer.parseInt(temp.substring(temp.indexOf("(")+1,temp.indexOf(",")));
                end=Integer.parseInt(temp.substring(temp.indexOf(",")+1,temp.indexOf(")")));
                if(start>end)end+=start;
                fValueFix+=temp.substring(start,end);
            }else{
                fValueFix+=temp;
            }
        }
        return fValueFix;
    }

}