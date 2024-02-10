<?php
include("../vendor/autoload.php");
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
include("../vaja2/database.php");

// Allow from any origin
if (isset($_SERVER['HTTP_ORIGIN'])) {
    header("Access-Control-Allow-Origin: {$_SERVER['HTTP_ORIGIN']}");
    header('Access-Control-Allow-Credentials: true');
    header('Access-Control-Max-Age: 86400');    // cache for 1 day
}

// Access-Control headers are received during OPTIONS requests
if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
    
    if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_METHOD']))
        // may also be using PUT, PATCH, HEAD etc
        header("Access-Control-Allow-Methods: GET, POST, OPTIONS");         

    if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']))
        header("Access-Control-Allow-Headers: {$_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']}");

    exit(0);
}

// Set headers for JSON response
header('Content-Type: application/json');

// Database connection
$database = new Database();
$db = $database->getConnection();

// Retrieve data from request
$data = json_decode(file_get_contents("php://input"));
$username = $data->uporabniskoime ?? '';
$password = $data->geslo ?? '';

// User authentication
$query = "SELECT uporabniskoime, geslo FROM igralci WHERE uporabniskoime = :uporabniskoime";
$stmt = $db->prepare($query);
$stmt->bindParam(':uporabniskoime', $username);
$stmt->execute();

if ($stmt->rowCount() == 1) {
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    if (password_verify($password, $user['geslo'])) {
        // Generate JWT token
        $payload = [
            'iss' => 'YourIssuer', // Issuer
            'aud' => 'YourAudience', // Audience
            'iat' => time(), // Issued At
            'exp' => time() + 3600, // Expiration Time
            'userId' => $user['uporabniskoime']
        ];
    
        $jwt = JWT::encode($payload, 'MySuperSecretKey12345!@#$%', 'HS256');
        
        // Return the token in JSON response
        echo json_encode(['token' => $jwt, 'userId' => $user['uporabniskoime']]);
    } else {
        http_response_code(401);
        echo json_encode(['message' => 'Invalid password.']);
    }
} else {
    http_response_code(401);
    echo json_encode(['message' => 'User not found.']);
}
?>