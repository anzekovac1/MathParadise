<?php
$DEBUG = true;							
include("orodja.php"); 	
include("../vendor/autoload.php");
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
include("../vaja2/database.php");	

$zbirka = dbConnect();					// Pridobitev povezave s podatkovno zbirko

header('Content-Type: application/json');	// Nastavimo MIME tip vsebine odgovora .... Da se držimo REST-a

switch ($_SERVER["REQUEST_METHOD"])		{
	case 'GET':
		if(!empty($_GET["uporabniskoime"]))
		{
			pridobi_igralcasprememba($_GET["uporabniskoime"]);		// Če odjemalec posreduje uporabniskoime, mu vrnemo podatke izbranega igralca
		}
		break;

	case 'PUT':
		if(!empty($_GET["uporabniskoime"]))
		{
			posodobi_igralcasprememba($_GET["uporabniskoime"]);
		}
		else
		{
			http_response_code(400);		// Bad request
		}
		break;

	default:
		http_response_code(405);		//Če naredimo zahtevo s katero koli drugo metodo je to 'Method Not Allowed'
		break;
}

mysqli_close($zbirka);					// Sprostimo povezavo z zbirko

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
//Konec authentikacije

function pridobi_igralcasprememba($uporabniskoime)
{
	global $zbirka;
    $decoded_jwt = authenticate();
    $uporabniskoime = $decoded_jwt->userId;
	
	$poizvedba="SELECT ime, priimek, email FROM igralci WHERE uporabniskoime='$uporabniskoime'";
	
	$rezultat=mysqli_query($zbirka, $poizvedba);

	if(mysqli_num_rows($rezultat)>0)	//igralci obstaja
	{
		$odgovor=mysqli_fetch_assoc($rezultat);
		
		http_response_code(200);		//OK
		echo json_encode($odgovor);
	}
	else							// igralci ne obstaja
	{
		http_response_code(404);		//Not found
	}
}

function posodobi_igralcasprememba($uporabniskoime)
{
	global $zbirka, $DEBUG; 
    $decoded_jwt = authenticate();
    $uporabniskoime = $decoded_jwt->userId;
	$podatki = json_decode(file_get_contents('php://input'), true);

	// Ali igralec obstaja?
	if (isset($podatki['email'])) {
		$novi_email = mysqli_escape_string($zbirka, $podatki['email']);

		if (email_obstaja($novi_email)) {
			http_response_code(409);  // Konflikt - e-poštni naslov že obstaja
			echo "E-poštni naslov že obstaja";
			return;
		}

		if (isset($podatki['ime'], $podatki['priimek'], $podatki['email'])) {
			$ime = mysqli_escape_string($zbirka, $podatki['ime']);
			$priimek = mysqli_escape_string($zbirka, $podatki['priimek']);

			$poizvedba = "UPDATE igralci SET ime='$ime', priimek='$priimek', email='$novi_email' WHERE uporabniskoime='$uporabniskoime'";
			if (mysqli_query($zbirka, $poizvedba)) {
				http_response_code(204); // OK with no content
				$odgovor = URL_vira($uporabniskoime);
				echo json_encode($odgovor);
			} else {
				http_response_code(500); // Internal server error - ni nujno vedno strežnik kriv
				if ($DEBUG) {
					pripravi_odgovor_napaka("Napaka pri poizvedbi: " . mysqli_error($zbirka), 666);
				}
			}
		} else {
			http_response_code(400); // Bad request
		}
	} else {
		http_response_code(400); // Bad request - manjkajo podatki
	}
}

?>
