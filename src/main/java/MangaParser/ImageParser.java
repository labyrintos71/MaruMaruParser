package MangaParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ImageParser {
    public ImageParser(String addr,String cookie){
        long oldtime,nowtime;
        String data = "";
        oldtime=System.currentTimeMillis();
        try {
            data = connectServer(addr,cookie);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(data.indexOf("Protected")!=-1){
            System.out.println("��Ű�� �������ּ���");
        }
        else{
            System.out.println("proxy : by-pass");
            parsingImage(data);
            parsingList(data);
        }

        nowtime=System.currentTimeMillis();

        System.out.println((float)(nowtime-oldtime)/(float)1000+"s"+"-ImageParser");
    }
    public String connectServer(String addr,String cookie) throws IOException{
        StringBuilder html = new StringBuilder();
        String line;

        URL url = new URL(addr);
        URLConnection openconn=url.openConnection();
        openconn.setRequestProperty("Cookie",cookie);
        openconn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");

        BufferedReader br = new BufferedReader(new InputStreamReader(openconn.getInputStream(), "UTF-8"));


        for (; ; ) {
            line=br.readLine();
            if(line==null) break;
            html.append(line);
        }
        br.close();
        return html.toString();

    }
    public void parsingImage(String str){
        String[] url;
        String tmp,html=str;
        int cnt=0,i=0;

        html=html.substring(html.indexOf("gallery-template"));
        html=html.substring(0,html.indexOf("</div>"));

        html=html.substring(html.indexOf("data-src"));

        tmp=html;
        while(tmp.indexOf("data-src=")!=-1){
            cnt++;
            tmp=tmp.substring(tmp.indexOf(">")+1);
        }

        url=new String[cnt];
        while(html.indexOf("data-src=")!=-1){
            tmp=html.substring(html.indexOf("data-src="), html.indexOf(">")).replaceAll("=\"","=");
            url[i]="http://wasabisyrup.com"+tmp.substring(tmp.indexOf("=")+1,tmp.indexOf("\""));
            html=html.substring(html.indexOf(">")+1);

            System.out.println(url[i]);
            i++;
        }
        System.out.println(cnt);
    }

    public void parsingList(String html){
        String temp;
        int pointer=0,cnt=0,i=0;
        String[] title;
        int[] code;
        html=html.substring(html.indexOf("gallery-section"));
        html=html.substring(html.indexOf("<option"));
        html=html.substring(0,html.indexOf("</select>"));
        temp=html;

        while(temp.indexOf("<option")!=-1){
            cnt++;
            temp=temp.substring(temp.indexOf("</option>")+3);
        }
        title=new String[cnt];
        code=new int[cnt];
        while(html.indexOf("<option")!=-1){
            temp=html.substring(html.indexOf("<option"),html.indexOf("</option>")).replaceAll("=\"", "=");

            if(temp.indexOf("selected")!=-1){ pointer=i;
                System.out.println(pointer);}
            code[i]=Integer.parseInt(temp.substring(temp.indexOf("=")+1,temp.indexOf("\"")));
            title[i]=temp.substring(temp.indexOf(">")+1).trim();
            html=html.substring(html.indexOf("</option>")+3);
            System.out.println("title : "+title[i]+"        code : "+code[i]+" "+pointer);
            i++;
        }

        if(pointer==0){
            //	System.out.println("title : "+title[pointer+1]+"        code : "+code[pointer+1]+" "+pointer);
            //cnt0
        }else if(pointer==cnt-1){
            //System.out.println("title : "+title[pointer-1]+"        code : "+code[pointer-1]+" "+pointer);
            //cnt-1
        }else{

            //System.out.println("title : "+title[pointer-1]+"        code : "+code[pointer-1]+" "+pointer);
            //	System.out.println("title : "+title[pointer+1]+"        code : "+code[pointer+1]+" "+pointer);

            //pointer+1 pointer-1
        }
    }
}

