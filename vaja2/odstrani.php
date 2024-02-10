<?php
$DEBUG = true;							        
include("orodja.php"); 	
include("../vendor/autoload.php");
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
include("../vaja2/database.php");	

$zbirka = dbConnect();					

header('Content-Type: application/json');	

switch ($_SERVER["REQUEST_METHOD"])		{
	case 'DELETE':
        $decoded_jwt = authenticate(); // Authentikacija
        if (!isAdmin($decoded_jwt)) {
            http_response_code(403); // Forbidden
            exit();
        }
		if(!empty($_GET["uporabniskoime"]))
		{
			odstrani_igralca($_GET["uporabniskoime"]);	
		}
		break;
	default:
		http_response_code(405);		
		break;
}

mysqli_close($zbirka);					

// Authentikacija
function authenticate() {
    if (!isset($_SERVER['HTTP_AUTHORIZATION'])) {
        sendResponse(401, ["message" => "Authentication token not found."]);
        exit();
    }
    $authHeader = $_SERVER['HTTP_AUTHORIZATION'];
    list($jwt) = sscanf($authHeader, 'Bearer %s');
    if (!$jwt) {
        sendResponse(401, ["message" => "Access token required."]);
        exit();
    }
    try {
        $key = "MySuperSecretKey12345!@#$%"; 
        $decoded_jwt = JWT::decode($jwt, new Key($key, 'HS256'));
        return $decoded_jwt;
    } catch (Exception $e) {
        sendResponse(401, ["message" => "Access token invalid: " . $e->getMessage()]);
        exit();
    }
}

function isAdmin($decoded_jwt) {
    return isset($decoded_jwt->userId) && $decoded_jwt->userId === 'sarci';
}
// Konec authentikacije

function odstrani_igralca($uporabniskoime)
{
	global $zbirka;
	$uporabniskoime=mysqli_escape_string($zbirka, $uporabniskoime);
	$poizvedba="DELETE FROM igralci WHERE uporabniskoime='$uporabniskoime'";
	$rezultat=mysqli_query($zbirka, $poizvedba);

	if($rezultat)	
	{
		http_response_code(200);	
		echo json_encode(["message" => "Igralec uspešno odstranjen."]);
	}
	else							
	{
		http_response_code(404);	
        echo json_encode(["message" => "Igralec ne obstaja ali je prišlo do napake pri brisanju."]);
	}
}

?>
