import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        try {
            Document lenta = Jsoup.parse(new URL("https://lenta.ru/"), 10000);
            Elements pictures = lenta.select("img.g-picture");
            for (Element picture : pictures) {
                String pic = picture.attr("abs:src");
                String picName = pic.substring(pic.lastIndexOf("/") + 1);
                URL urlPic = new URL(pic);
                InputStream inputStream = urlPic.openStream();
                byte[] buffer = new byte[4096];
                int n;
                OutputStream outputStream =new FileOutputStream("C:\\Users\\AVK\\Documents\\Java\\java_basics\\13_FilesAndNetwork\\homework_13.4\\src\\images\\" + picName);
                while ( (n = inputStream.read(buffer)) != -1 ){
                    outputStream.write(buffer, 0, n);
                }
                outputStream.close();
                System.out.println(picName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

