<?php
$DEBUG = true;							
include("orodja.php"); 	
include("../vendor/autoload.php");
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
include("../vaja2/database.php");						// Vključitev 'orodij' za delo z bazo
$zbirka = dbConnect();					// Pridobitev povezave s podatkovno zbirko

header('Content-Type: application/json');	// Nastavimo MIME tip vsebine odgovora .... Da se držimo REST-a

switch ($_SERVER["REQUEST_METHOD"]) {
    case 'GET':
        if (isset($_GET['uporabniskoime'])) {
            pridobi_mnenje($_GET['uporabniskoime']);
        } else {
            pridobi_vsa_mnenja();
        }
        break;

    case 'POST':
        dodaj_mnenje();
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
        $key = "MySuperSecretKey12345!@#$%"; // Replace with your secret key
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

function pridobi_vsa_mnenja()
{
    global $zbirka;
    $odgovor = array();
    $poizvedba = "SELECT uporabniskoime, ocena, komentar, cas_ocene FROM mnenje";
    $rezultat = mysqli_query($zbirka, $poizvedba);

    while ($vrstica = mysqli_fetch_assoc($rezultat)) {
        $odgovor[] = $vrstica;
    }

    http_response_code(200);		//OK
    echo json_encode($odgovor);
}

function pridobi_mnenje($uporabniskoime)
{
    global $zbirka;
    $odgovor = array();
    $poizvedba = "SELECT uporabniskoime, ocena, komentar, cas_ocene FROM mnenje WHERE uporabniskoime = ?";
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


function dodaj_mnenje(){
    global $zbirka, $DEBUG;

    // Pridobimo iz JWT uporabniško ime
    $decoded_jwt = authenticate();
    $uporabniskoime = $decoded_jwt->userId;
    $podatki = json_decode(file_get_contents("php://input"), true);
    $ocena = mysqli_escape_string($zbirka, $podatki['ocena']);
    $komentar = mysqli_escape_string($zbirka, $podatki['komentar']);

    $poizvedba = "INSERT INTO mnenje (uporabniskoime, ocena, komentar) VALUES ('$uporabniskoime', '$ocena', '$komentar')";

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
