import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(maxFileSize = 16177215) // Limit image size
public class UploadProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            // Get form data
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String description = request.getParameter("description");
            Part filePart = request.getPart("image");

            // Save image to a folder
            String uploadPath = "C:/uploads/"; // Change this based on hosting setup
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
            String filePath = uploadPath + fileName;
            FileOutputStream fos = new FileOutputStream(filePath);
            InputStream is = filePart.getInputStream();
            byte[] data = new byte[is.available()];
            is.read(data);
            fos.write(data);
            fos.close();
            is.close();

            // Store product details in the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://your_host/your_db", "your_user", "your_password");
            String sql = "INSERT INTO products (name, category, price, quantity, description, image_path) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setString(5, description);
            stmt.setString(6, filePath);
            stmt.executeUpdate();

            response.sendRedirect("success.html"); // Redirect after success
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
