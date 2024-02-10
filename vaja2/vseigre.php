<?php
$DEBUG = true;							
include("orodja.php"); 	
include("../vendor/autoload.php");
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
include("../vaja2/database.php");				

$zbirka = dbConnect();					// Pridobitev povezave s podatkovno zbirko

header('Content-Type: application/json');	// Nastavimo MIME tip vsebine odgovora .... Da se držimo REST-a

switch ($_SERVER["REQUEST_METHOD"]) {
    case 'GET':
        if (isset($_GET['uporabniskoime'])) {
            pridobi_odigrane_igre($_GET['uporabniskoime']);
        } else {
            pridobi_vse_igre();
        }
        break;

    case 'POST':
        dodaj_igro();
        break;

    case 'PUT':

        break;

    default:
        http_response_code(405);		//Če naredimo zahtevo s katero koli drugo metodo je to 'Method Not Allowed'
        break;
}

mysqli_close($zbirka);


// AVTENTIKACIJA

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
    return isset($decoded_jwt->userId) && $decoded_jwt->userId === 'tim';
}

// Konec Avtentikacije 


function pridobi_vse_igre()
{
    global $zbirka;
    $odgovor = array();

    $poizvedba = "SELECT uporabniskoime, tezavnost, rezultat, casovni_zig FROM igra";

    $rezultat = mysqli_query($zbirka, $poizvedba);

    while ($vrstica = mysqli_fetch_assoc($rezultat)) {
        $odgovor[] = $vrstica;
    }

    http_response_code(200);		//OK
    echo json_encode($odgovor);
}

function pridobi_odigrane_igre($uporabniskoime)
{
    global $zbirka;
    $odgovor = array();
    $poizvedba = "SELECT tezavnost, rezultat, casovni_zig FROM igra WHERE uporabniskoime = ?";
    $stmt = mysqli_prepare($zbirka, $poizvedba);
    // Povežemo parameter s poizvedbo
    mysqli_stmt_bind_param($stmt, "s", $uporabniskoime);
    // Izvedemo pripravljeno poizvedbo
    mysqli_stmt_execute($stmt);
    // Pridobimo rezultat
    $rezultat = mysqli_stmt_get_result($stmt);

    while ($vrstica = mysqli_fetch_assoc($rezultat)) {
        $odgovor[] = $vrstica;
    }

    http_response_code(200);		//OK
    echo json_encode($odgovor);
}

function dodaj_igro(){
    global $zbirka, $DEBUG;

    $podatki = json_decode(file_get_contents("php://input"), true);

    $uporabniskoime = mysqli_escape_string($zbirka, $podatki['uporabniskoime']);
    $tezavnost = mysqli_escape_string($zbirka, $podatki['tezavnost']);
    $rezultat = mysqli_escape_string($zbirka, $podatki['rezultat']);

    $poizvedba = "INSERT INTO igra (uporabniskoime, tezavnost, rezultat) VALUES ('$uporabniskoime', '$tezavnost', '$rezultat')";

    if (mysqli_query($zbirka, $poizvedba)) {
        http_response_code(201); // Created
        $odgovor = URL_vira($uporabniskoime);
        echo json_encode($odgovor);
    } else {
        http_response_code(500); // Internal server error
        if ($DEBUG) {
            pripravi_odgovor_napaka("Napaka pri poizvedbi: " . mysqli_error($zbirka), 666);
        }
    }
}
?>
