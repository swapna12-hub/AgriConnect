<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("UTF-8"); // Handle special characters

    // Database connection details
    String dbURL = "jdbc:mysql://localhost:3306/agriconnect";
    String dbUser = "root";
    String dbPass = "password";

    Connection conn = null;
    PreparedStatement stmt = null;

    try {
        // Ensure the form's enctype="multipart/form-data"
        if (!request.getContentType().startsWith("multipart/form-data")) {
            throw new Exception("Form must have enctype='multipart/form-data'");
        }

        // Get form parameters
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");
        String description = request.getParameter("description");

        // Handle image upload
        Part imagePart = request.getPart("image");
        String imageName = imagePart.getSubmittedFileName();
        if (imageName == null || imageName.isEmpty()) {
            throw new Exception("Image file is required.");
        }

        // Save image to a proper directory inside Tomcat
        String uploadDir = application.getRealPath("/") + "uploads";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdir(); // Create folder if not exists

        String imagePath = "uploads" + File.separator + imageName;
        File fileSaveDir = new File(uploadFolder, imageName);
        imagePart.write(fileSaveDir.getAbsolutePath());

        // Connect to database
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

        // Insert product details into the database
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
        out.println("<h2 style='color:red;'>Error: " + e.getMessage() + "</h2>");
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>
