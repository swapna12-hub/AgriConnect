<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%
    // Database connection details
    String dbURL = "jdbc:mysql://localhost:3306/agriconnect";
    String dbUser = "root";
    String dbPass = "password";

    Connection conn = null;
    PreparedStatement stmt = null;

    try {
        // Get form parameters
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");
        String description = request.getParameter("description");
        Part imagePart = request.getPart("image");
        
        // Image upload processing
        String imageName = imagePart.getSubmittedFileName();
        String imagePath = "uploads/" + imageName;
        File fileSaveDir = new File(application.getRealPath("/") + imagePath);
        imagePart.write(fileSaveDir.getAbsolutePath());
        
        // Connect to database
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
        
        // Insert product details into database
        String sql = "INSERT INTO products (name, category, price, quantity, description, image) VALUES (?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setString(2, category);
        stmt.setString(3, price);
        stmt.setString(4, quantity);
        stmt.setString(5, description);
        stmt.setString(6, imagePath);
        stmt.executeUpdate();
        
        out.println("<h2>Product Uploaded Successfully!</h2>");
    } catch (Exception e) {
        out.println("<h2>Error: " + e.getMessage() + "</h2>");
    } finally {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }
%>
