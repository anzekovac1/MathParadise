function prijavise() {
    var uporabniskoime = document.getElementById('uporabniskoime').value;
    var geslo = document.getElementById('geslo').value;

    // Create the AJAX request
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost/mpgame/vaja2/login.php', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status === 200) {
            try {
                var response = JSON.parse(xhr.responseText);
                console.log(response);
                // Shrani token v lokalni pomnilnik
                localStorage.setItem('token', response.token);
                // Preveri, ali je token shranjen
                var storedToken = localStorage.getItem('token');
                console.log(storedToken);
                
                // Preusmeri uporabnika na meni.html
                window.location.href = '/mpgame/meni.html';
            } catch (e) {
                console.error('Error parsing JSON:', e);
            }
        } else {
            // Prikaži sporočilo o neuspešni prijavi
            prikazSporociloPrij('Neuspešna prijava. Poskusite ponovno!.', 3000);
        }
    };

    xhr.send(JSON.stringify({ uporabniskoime: uporabniskoime, geslo: geslo }));
}

function prikazSporociloPrij(sporocilo, cas) {
    var sporociloElement1 = document.getElementById("sporociloprijavi");

    // Preveri, ali je element na voljo
    if (sporociloElement1) {
        sporociloElement1.innerHTML = sporocilo;

        // Počakaj nekaj sekund (v milisekundah) in nato odstrani sporočilo
        setTimeout(function () {
            sporociloElement1.innerHTML = "";
        }, cas);
    } else {
        console.error("Element s ID-jem 'sporociloregister' ni bil najden.");
    }
}

