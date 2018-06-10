package AuthParser;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class qndxkrCookieParser{

    public qndxkrCookieParser(String num){
        try {
            connectServer(num);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void connectServer(String code) throws IOException{
        StringBuilder packet = new StringBuilder();
        String cookie="";
        String line;
        InetAddress addr = InetAddress.getByName("wasabisyrup.com");
        Socket socket = new Socket(addr, 80);
        String data="pass=qndxkr";
        String path = "http://wasabisyrup.com/archives/"+code;
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));

        wr.write("POST /archives/"+code+" HTTP/1.1\r\n");
        wr.write("Host: wasabisyrup.com\r\n");
        wr.write("Connection: keep-alive\r\n");
        wr.write("Content-Length: 11\r\n");
        wr.write("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36\r\n");
        wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
        wr.write("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n");
        wr.write("Referer: http://wasabisyrup.com/archives/"+code+"\r\n");
        wr.write("Accept-Encoding: gzip, deflate\r\n");
        wr.write("Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4\r\n");
        wr.write("\r\n");
        wr.write("pass=qndxkr");
        wr.flush();

        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while ((line = rd.readLine()) !="null") {
            packet.append(line);
            if(line.indexOf("PHP")!=-1) break;
        }
        cookie=line.substring(line.indexOf("PHP"),line.indexOf(";")+1);
        wr.close();
        rd.close();
        System.out.println(cookie);
    }
}