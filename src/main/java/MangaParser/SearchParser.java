package MangaParser;

import MangaObject.ImageData;
import MangaObject.MangaData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SearchParser {
    private int RECNUM=25;
    private long oldtime,nowtime;
    private String line,title,page,temp;
    private ImageData[] thumnail;
    private MangaData[] manga;
    private String[] cat;
    private int cnt=0;
    public SearchParser(String code){
        oldtime=System.currentTimeMillis();
        try {
            connectServer(code);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        nowtime=System.currentTimeMillis();
        System.out.println((float)(nowtime-oldtime)/(float)1000+"s"+" -MangaPaser");
    }

    public void connectServer(String addr) throws IOException{
        StringBuilder html = new StringBuilder();

        URL url = new URL(addr);
        URLConnection openconn=url.openConnection();
        openconn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        InputStreamReader is=new InputStreamReader(openconn.getInputStream(), "UTF-8");
        BufferedReader br = new BufferedReader(new InputStreamReader(openconn.getInputStream(), "UTF-8"));
        for (; ; ) {
            line=br.readLine();
            if(line==null) break;
            html.append(line);
        }
        br.close();
        parser(html.toString());
    }
    public void parser(String html){
        page=html.substring(html.indexOf("<div class=\"article\">")+2);

        page=page.substring(page.indexOf(">")+1,page.indexOf("<"));
        html=html.substring(html.indexOf("<div class=\"gallery\""));
        html=html.substring(0,html.indexOf("<div class=\"clear\""));

        thumnail=new ImageData[RECNUM];
        for(int i=0;i<RECNUM;i++) thumnail[i]=new ImageData();

        manga=new MangaData[RECNUM];
        for(int i=0;i<RECNUM;i++) manga[i]=new MangaData();

        cat=new String[RECNUM];
        while(html.indexOf("picbox")!=-1){
            temp=html=html.substring(html.indexOf("picbox"));
            temp=temp.substring(temp.indexOf("src"),temp.indexOf("span class=\"comment\"")).replaceAll("=\"","=");

            thumnail[cnt].setURL(temp.substring(temp.indexOf("=")+1,temp.indexOf("\"")));

            temp=temp.substring(temp.indexOf("width")).replaceAll("px","");
            thumnail[cnt].setWidth(Integer.parseInt(temp.substring(temp.indexOf("=")+1,temp.indexOf("\""))));

            temp=temp.substring(temp.indexOf("height"));
            thumnail[cnt].setHeight(Integer.parseInt(temp.substring(temp.indexOf("=")+1,temp.indexOf("\""))));

            temp=temp.substring(temp.indexOf("href")).replaceAll("&amp;","&");
            manga[cnt].setURL("http://marumaru.in"+temp.substring(temp.indexOf("=")+1,temp.indexOf("\"")));

            temp=temp.substring(temp.indexOf("cat"));
            cat[cnt]=temp.substring(temp.indexOf("[")+1,temp.indexOf("]"));

            temp=temp.substring(temp.indexOf("span"));
            manga[cnt].setTitle(temp.substring(temp.indexOf(">")+1,temp.indexOf("<")).replace("[����] ",""));

            System.out.println("Cat : "+cat[cnt]+"  Name : "+manga[cnt].getTitle());
            System.out.println("Link : "+manga[cnt].getURL());
            System.out.println("Image : "+thumnail[cnt].getURL()+"   Width : "+thumnail[cnt].getWidth()+"   Height : "+thumnail[cnt].getHeight());
            System.out.println();

            cnt++;
            html=html.substring(html.indexOf("span class=\"comment\""));
        }
        System.out.println("Result : "+page);
        System.out.println(cnt);
    }
}
