<?php
/**
 * Datoteka orodja.php vsebuje funkcije, ki se uporabljajo v več različnih datotekah.
 */
function dbConnect()
{
	$servername = "localhost";
	$username = "root";
	$password = "";
	$dbname = "mpgame";

	// Ustvarimo povezavo do podatkovne zbirke
	$conn = mysqli_connect($servername, $username, $password, $dbname);
	mysqli_set_charset($conn,"utf8");
	
	// Preverimo uspeh povezave
	if (mysqli_connect_errno())
	{
		printf("Povezovanje s podatkovnim strežnikom ni uspelo: %s\n", mysqli_connect_error());
		exit();
	} 	
	return $conn;
}

function pripravi_odgovor_napaka($vsebina, $koda)
{
	$odgovor=array(
		'status' => $koda,
		'error_message'=>$vsebina
	);
	echo json_encode($odgovor);
}

function igralec_obstaja($uporabniskoime)
{	
	global $zbirka;
	$uporabniskoime=mysqli_escape_string($zbirka, $uporabniskoime);
	
	$poizvedba="SELECT * FROM igralci WHERE uporabniskoime='$uporabniskoime'";
	
	if(mysqli_num_rows(mysqli_query($zbirka, $poizvedba)) > 0)
	{
		return true;
	}
	else
	{
		return false;
	}	
}

function email_obstaja($email) {
    global $zbirka;
    $poizvedba = "SELECT * FROM igralci WHERE email = '$email'";
    $rezultat = mysqli_query($zbirka, $poizvedba);
    return mysqli_num_rows($rezultat) > 0;
}

function URL_vira($vir)
{
	if(isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on')
	{
		$url = "https"; 
	}
	else
	{
		$url = "http"; 
	}
	$url .= "://" . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'] . $vir;
	
	return $url; 
}
?>