function dodajMnenjeIgralca() {
    // Preveri, ali je uporabnik prijavljen
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html";
        return;
    }

    const obrazec = document.getElementById("dodajmnenje");
    const data = formToJSON(obrazec.elements);

    const JSONdata = JSON.stringify(data, null, "  ");
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 201) {
                document.getElementById("odgovormnenje").innerHTML = "Dodajanje uspelo!";
                obrazec.reset();
                prikazSporoc("Hvala da ste oddali svoje mnenje!", 3000);
            } else if (this.status === 409) {
                const odgovorJSON = JSON.parse(this.responseText);
                document.getElementById("odgovorMnenje").innerHTML = "Oddaja mnenja ni uspela: " + this.status;
            }
        }
    };

    xhr.open("POST", "vaja2/mnenja.php", true);
    xhr.setRequestHeader("Authorization", "Bearer " + token); // Dodajanje JWT žetona v zaglavje zahtevka
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSONdata);
}

function prikazSporoc(sporocilo, cas) {
    var sporociloElement = document.getElementById("sporociloDodajMnenje");

    // Preveri, ali je element na voljo
    if (sporociloElement) {
        sporociloElement.innerHTML = sporocilo;

        // Počakaj nekaj sekund (v milisekundah) in nato odstrani sporočilo
        setTimeout(function () {
            sporociloElement.innerHTML = "";
        }, cas);
    } 
}
