<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$host = "sql106.infinityfree.com";  // MySQL Hostname
$user = "if0_38622304";             // MySQL Username
$password = "1micro234";  // Password
$dbname = "if0_38622304_agriconnect_db";  // Database Name

$conn = new mysqli($host, $user, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Check if all required POST data is set
if (
    isset($_POST['product_name']) && 
    isset($_POST['category']) && 
    isset($_POST['price']) && 
    isset($_POST['quantity']) && 
    isset($_POST['description']) && 
    isset($_FILES['image']) // Handling file upload
) {
    $product_name = $_POST['product_name'];
    $category = $_POST['category'];
    $price = $_POST['price'];
    $quantity = $_POST['quantity'];
    $description = $_POST['description'];

    // Handle the image upload
    $image = $_FILES['image'];
    $image_name = $image['name'];
    $image_tmp_name = $image['tmp_name'];
    $image_error = $image['error'];
    $image_size = $image['size'];

    // Check if the image uploaded correctly
    if ($image_error === 0) {
        // Define the upload directory
        $upload_dir = 'uploads/';
        $image_path = $upload_dir . basename($image_name);

        // Move the uploaded file to the uploads folder
        if (move_uploaded_file($image_tmp_name, $image_path)) {
            // Use Prepared Statements for security
            $stmt = $conn->prepare("INSERT INTO products (product_name, category, price, quantity, description, image_path) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ssdiis", $product_name, $category, $price, $quantity, $description, $image_path);

            if ($stmt->execute()) {
                echo "Product added successfully!";
            } else {
                echo "Error: " . $stmt->error;
            }

            $stmt->close();
        } else {
            echo "Error: Image upload failed.";
        }
    } else {
        echo "Error: Invalid image upload.";
    }

} else {
    echo "Error: Missing required fields.";
}

$conn->close();
?>
