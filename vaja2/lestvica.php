<?php
$DEBUG = true; 
include("orodja.php"); 	
include("../vendor/autoload.php");
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
include("../vaja2/database.php");

$zbirka = dbConnect(); // Pridobitev povezave s podatkovno zbirko

header('Content-Type: application/json'); // Nastavimo MIME tip vsebine odgovora .... Da se držimo REST-a

switch ($_SERVER["REQUEST_METHOD"]) {
    case 'GET':
        if (isset($_GET['tezavnost'])) {
            pridobi_igre_po_tezavnosti($_GET['tezavnost']);
        } else {
            http_response_code(400); // Bad Request, če ni podane težavnosti
        }
        break;

    default:
        http_response_code(405); // Če naredimo zahtevo s katero koli drugo metodo je to 'Method Not Allowed'
        break;
}

mysqli_close($zbirka);

function pridobi_igre_po_tezavnosti($tezavnost)
{
    global $zbirka;
    $odgovor = array();
    $poizvedba = "SELECT uporabniskoime, tezavnost, rezultat, casovni_zig FROM igra WHERE tezavnost = ? ORDER BY rezultat DESC";
    $stmt = mysqli_prepare($zbirka, $poizvedba);
    // Povežemo parameter s poizvedbo
    mysqli_stmt_bind_param($stmt, "i", $tezavnost);
    // Izvedemo pripravljeno poizvedbo
    mysqli_stmt_execute($stmt);
    // Pridobimo rezultat
    $rezultat = mysqli_stmt_get_result($stmt);

    while ($vrstica = mysqli_fetch_assoc($rezultat)) {
        $odgovor[] = $vrstica;
    }

    http_response_code(200); // OK
    echo json_encode($odgovor);
}

