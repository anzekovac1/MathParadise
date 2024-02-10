<?php
$DEBUG = true;						
include("orodja.php"); 	
$zbirka = dbConnect();	
				
header('Content-Type: application/json');	// Nastavimo MIME tip vsebine odgovora .... Da se držimo REST-a

switch ($_SERVER["REQUEST_METHOD"])		{
	case 'POST':
		dodajanje();
		break;

	default:
		http_response_code(405);		//Če naredimo zahtevo s katero koli drugo metodo je to 'Method Not Allowed'
		break;
}

mysqli_close($zbirka);					// Sprostimo povezavo z zbirko


function dodajanje(){

    global $zbirka, $DEBUG;
    $podatki = json_decode(file_get_contents('php://input'), true);   // branje podatkov
    if(isset($podatki['uporabniskoime'], $podatki['geslo'], $podatki['ime'], $podatki['priimek'], $podatki['email']))
    {
        $uporabniskoime = mysqli_escape_string($zbirka, $podatki['uporabniskoime']);
        $geslo = mysqli_escape_string($zbirka, $podatki['geslo']);
        $ime = mysqli_escape_string($zbirka, $podatki['ime']);
        $priimek = mysqli_escape_string($zbirka, $podatki['priimek']);
        $email = mysqli_escape_string($zbirka, $podatki['email']);

        $geslo = password_hash($geslo, PASSWORD_DEFAULT);

        if(!igralec_obstaja($uporabniskoime) && !email_obstaja($email)) # preverimo -> torej igralci ne obstaja !=
        {
            $poizvedba = "INSERT INTO igralci (uporabniskoime, geslo, ime, priimek, email) VALUES ('$uporabniskoime', '$geslo', '$ime', '$priimek', '$email')";
            if(mysqli_query($zbirka, $poizvedba))
            {
                http_response_code(201);        //Created
                $odgovor = URL_vira($uporabniskoime);
                echo json_encode($odgovor);
            }
            else
            {
                http_response_code(500);        //Internal server error - ni nujno vedno strežnik kriv
                if($DEBUG)
                {
                    pripravi_odgovor_napaka("Napaka pri poizvedbi: " . mysqli_error($zbirka), 666);
                }
            }
        }
        else
        {
            http_response_code(409);        //Conflict
            if (igralec_obstaja($uporabniskoime) && email_obstaja($email)) {
				pripravi_odgovor_napaka("Uporabniško ime in e-poštni naslov že obstajata", 125);
			} elseif (igralec_obstaja($uporabniskoime)) {
				pripravi_odgovor_napaka("Uporabniško ime že obstaja", 123);
			} elseif (email_obstaja($email)) {
				pripravi_odgovor_napaka("E-poštni naslov že obstaja", 124);
			}
        }
    }
    else
    {
        http_response_code(400);        //Bad request
    }
}

?>
