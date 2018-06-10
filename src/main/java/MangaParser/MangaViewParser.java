package MangaParser;

import MangaObject.ImageData;
import MangaObject.MangaData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class MangaViewParser {
    private String str;
    private String line,title="",img,classification,temp,tag;
    private int cnt=0,i=0;
    private MangaData[] manga;
    private ImageData thumnail=new ImageData();
    private long oldtime,nowtime;
    public MangaViewParser(String addr){
        // TODO Auto-generated method stub
        oldtime=System.currentTimeMillis();
        try {
            connectServer(addr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        nowtime=System.currentTimeMillis();
        System.out.println((float)(nowtime-oldtime)/(float)1000+"s"+"-MangaViewPaser");
    }
    public void connectServer(String addr) throws IOException{
        StringBuilder html = new StringBuilder();
        URL url = new URL(addr);
        URLConnection openconn=url.openConnection();
        openconn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        BufferedReader br = new BufferedReader(new InputStreamReader(openconn.getInputStream(), "UTF-8"));
        for (; ; ) {

            line=br.readLine();
            if(line==null) break;
            html.append(line);
            html.append('\n');

        }
        br.close();
        str=html.toString();

        parser(str);
    }
    public void parser(String html){
        img=html.substring(html.indexOf("<p class=\"crop\">"));
        img=img.substring(0,img.indexOf("</p>")).replaceAll("=\"", "=");
        img=img.substring(img.indexOf("src")).replace("px","");
        thumnail.setURL(img.substring(img.indexOf("=")+1,img.indexOf("\"")));

        img=img.substring(img.indexOf("width"));
        thumnail.setWidth(Integer.parseInt(img.substring(img.indexOf("=")+1,img.indexOf("\""))));

        img=img.substring(img.indexOf("height"));
        thumnail.setHeight(Integer.parseInt(img.substring(img.indexOf("=")+1,img.indexOf("\""))));

        tag=html.substring(html.indexOf("<meta name=\"keywords\"")+4);
        tag=tag.substring(0,tag.indexOf("<meta"));
        tag=tag.substring(tag.indexOf("content")).replaceAll("=\"", "=");
        tag=tag.substring(tag.indexOf("G"),tag.indexOf("\""));

        title=html.substring(html.indexOf("<meta name=\"subject\"")+4);
        title=title.substring(0,title.indexOf("<meta"));
        title=title.substring(title.indexOf("content")).replaceAll("=\"", "=");
        title=title.substring(title.indexOf("=")+1,title.indexOf("\""));

        classification=html.substring(html.indexOf("classification"));
        classification=classification.substring(0,classification.indexOf("<meta"));
        classification=classification.substring(classification.indexOf("content")).replaceAll("=\"", "=");
        classification=classification.substring(classification.indexOf("=")+1,classification.indexOf("\""));

        html=html.substring(html.indexOf("<div id=\"vContent\" class=\"content\">"));
        html=html.substring(0,html.indexOf("<div align=\"center\">"));

        img=html;
        while(html.indexOf("<")!=-1){
            html=html.replace(html.substring(html.indexOf("<"),html.indexOf(">")+1),",");
        }

        html=html.replace("&nbsp;","");
        StringTokenizer st=new StringTokenizer(html,",");

        while(st.hasMoreTokens()){
            temp=st.nextToken();
            if(temp.indexOf("ȭ")!=-1||temp.indexOf("��")!=-1||temp.indexOf("����")!=-1||temp.indexOf("��")!=-1){
                cnt++;
            }
        }

        manga=new MangaData[cnt];
        for(int i=0;i<cnt;i++) manga[i]=new MangaData();

        st=new StringTokenizer(html,",");
        while(st.hasMoreTokens()){
            temp=st.nextToken();
            if(temp.indexOf("ȭ")!=-1||temp.indexOf("��")!=-1||temp.indexOf("����")!=-1){
                manga[i].setTitle(temp);
                i++;
            }
        }
        html=img;
        html=html.replaceAll("=\"","=");
        i=0;
        while(html.indexOf("href")!=-1){
            html=html.substring(html.indexOf("href")+3);
            manga[i].setURL(html.substring(html.indexOf("=")+1,html.indexOf("\"")));
            if(manga[i].getURL().indexOf("imgur")==-1)i++;
        }

        for(i=0;i<cnt;i++) System.out.println("Name : "+manga[i].getTitle()+"     Link : "+manga[i].getURL());
        System.out.println("Image : "+thumnail.getURL());
        System.out.println("Width : "+thumnail.getWidth()+"     Height : "+thumnail.getHeight());
        System.out.println("Title : "+title);
        System.out.println("Classifition : "+classification);
        System.out.println(tag);
        System.out.println(cnt);

    }
}