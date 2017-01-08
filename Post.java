import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Post {
    HttpURLConnection connection;
    URL url;
    public static void main(String args[]) {
    Post at = new Post("192.168.0.8", "create");
        at.execute("1234567890");
    }
    Post(String ip, String src) {
        try {
            connection = null;
            url = new URL("http://" + ip + ":3000/card_for/" + src);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public void execute(String id){
    
        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                connection.getOutputStream(),
                StandardCharsets.UTF_8
            ));

            writer.write("id=" + id);
            writer.flush();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                try (
                    InputStreamReader isr = new InputStreamReader(
                    connection.getInputStream(),
                    StandardCharsets.UTF_8
                    );
                    BufferedReader reader = new BufferedReader(isr);
                ){
                    String line;
                    while((line = reader.readLine()) != null){
                        System.out.println(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}