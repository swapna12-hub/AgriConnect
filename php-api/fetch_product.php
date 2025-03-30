<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Allow cross-origin requests (if needed)
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

$servername = "sql106.infinityfree.com"; 
$username = "if0_38622304";  
$password = "1micro234";  
$dbname = "if0_38622304_agriconnect_db";  

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM products";
$result = $conn->query($sql);

if ($result === false) {
    die("Error: " . $conn->error);
}

$products = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $products[] = $row;
    }
}

echo json_encode($products);

$conn->close();  // Close connection right after done fetching
?>
