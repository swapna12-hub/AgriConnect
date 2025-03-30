import java.io.*;
import java.net.*;

public class InsertProduct {
    public static void main(String[] args) throws Exception {
        String urlParameters = "product_name=Tomato&category=Vegetables&price=50&quantity=10&description=Fresh+Tomatoes&image_path=tomato.jpg";
        byte[] postData = urlParameters.getBytes();

        URL url = new URL("https://your-infinityfree-site.com/insert.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        OutputStream os = conn.getOutputStream();
        os.write(postData);
        os.flush();
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();
        System.out.println("Server Response: " + response);
        in.close();
    }
}
