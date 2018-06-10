package MangaParser;

import MangaObject.ImageData;
import MangaObject.MangaData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateParser {
    private int RECNUM=25;
    private long oldtime,nowtime;
    private String line,title,str,page,temp;
    private ImageData[] thumnail;
    private String[] time;
    private MangaData[] hotManga,manga;
    private int cnt=0,hcnt=0;
    public UpdateParser(String code){
        oldtime=System.currentTimeMillis();
        try {
            connectServer(code);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        nowtime=System.currentTimeMillis();
        System.out.println((float)(nowtime-oldtime)/(float)1000+"s"+" -UpdateParser");
    }

    public void connectServer(String addr) throws IOException{
        StringBuilder html = new StringBuilder();

        URL url = new URL(addr);
        URLConnection openconn=url.openConnection();
        openconn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
        InputStreamReader is=new InputStreamReader(openconn.getInputStream(), "UTF-8");
        BufferedReader br = new BufferedReader(new InputStreamReader(openconn.getInputStream(), "UTF-8"));
        for (; ; ) {
            line=br.readLine();
            if(line==null) break;
            html.append(line);
        }
        br.close();
        str=html.toString();
        parser(str);
        parserHot(str);
    }
    public void parser(String html){

        manga=new MangaData[RECNUM];
        for(int i=0;i<RECNUM;i++) manga[i]=new MangaData();

        thumnail=new ImageData[RECNUM];
        for(int i=0;i<RECNUM;i++) thumnail[i]=new ImageData();

        html=html.substring(html.indexOf("<tr cid"));
        html=html.substring(0,html.indexOf("/tbody>")).replace("=\"","=");

        while(html.indexOf("<tr cid")!=-1){
            html=html.substring(html.indexOf("href"));
            hotManga[cnt].setURL("http://marumaru.in"+html.substring(html.indexOf("=")+1,html.indexOf("\"")));
            html=html.substring(html.indexOf("url"));
            thumnail[cnt].setURL("http://marumaru.in"+html.substring(html.indexOf("(")+1,html.indexOf(")")));
            html=html.substring(html.indexOf("cid"));
            hotManga[cnt].setTitle(html.substring(html.indexOf(">")+1,html.indexOf("<")).trim());
            html=html.substring(html.indexOf("regis"));
/*
		System.out.println("name : "+hotManga[cnt].getTitle );
		System.out.println("link : "+hotManga[cnt].getURL );
		System.out.println("thum : "+thumnail[cnt].getURL());
	*/	cnt++;
        }
    }
    public void parserHot(String html){

        html=html.substring(html.indexOf("<div class=\"hotlink-box\">"));
        html=html.substring(0,html.indexOf("</div>")).replace("=\"","=");
        temp=html;
        while(html.indexOf("href")!=-1){
            html=html.substring(html.indexOf("href"));
            hcnt++;
            html=html.substring(html.indexOf("</a>"));
        }
        hotManga=new MangaData[hcnt];
        for(int i=0;i<hcnt;i++) hotManga[i]=new MangaData();

        html=temp;
        hcnt=0;
        while(html.indexOf("href")!=-1){
            html=html.substring(html.indexOf("href"));
            hotManga[hcnt].setURL("http://marumaru.in"+html.substring(html.indexOf("=")+1,html.indexOf("\"")));
            hotManga[hcnt].setTitle(html.substring(html.indexOf(">")+1,html.indexOf("<")));
            System.out.println("Name : "+hotManga[hcnt].getTitle()+"  Link : "+hotManga[hcnt].getURL());
            html=html.substring(html.indexOf("</a>"));
            hcnt++;
        }
    }
}