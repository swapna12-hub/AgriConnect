import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/getProducts") // Servlet mapped to "/getProducts"
public class GetProductsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json"); // Set response type to JSON
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        JSONArray productList = new JSONArray();

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://sqlXXX.infinityfree.com:3306/yourDB", // Replace with your actual host & DB
                "your-username", 
                "your-password"
            );

            // Fetch products from the database
            String query = "SELECT id, name, price, image FROM products"; // Adjust table & columns as needed
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JSONObject product = new JSONObject();
                product.put("id", rs.getInt("id"));
                product.put("name", rs.getString("name"));
                product.put("price", rs.getDouble("price"));
                product.put("image", rs.getString("image")); // Ensure you store image URLs
                productList.put(product);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"Database connection failed!\"}");
            return;
        }

        out.write(productList.toString()); // Send JSON response
    }
}
