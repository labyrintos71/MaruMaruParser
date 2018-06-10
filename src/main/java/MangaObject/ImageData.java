package MangaObject;

public class ImageData {
    private String url;
    private int width;
    private int height;
    public void setURL(String str){
        url=str;
    }
    public void setWidth(int num){
        width=num;
    }
    public void setHeight(int num){
        height=num;
    }
    public String getURL(){
        return url;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
}