const formToJSON = elements => [].reduce.call(elements, (data, element) => {
    if (element.name !== "") {
        data[element.name] = element.value;
    }
    return data;
}, {});

function dodajIgralca() {
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html";
        return;
    }

    const obrazec = document.getElementById("obrazec");
    const data = formToJSON(obrazec.elements);

    // Preveri, ali so vsi zahtevani podatki prisotni
    if (!data.uporabniskoime || !data.geslo || !data.ime || !data.priimek || !data.email) {
        alert("Vsa polja morajo biti izpolnjena.");
        return;
    }

    const JSONdata = JSON.stringify(data, null, "  ");
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 201) {
                document.getElementById("odgovor1").innerHTML = "Dodajanje uspelo!";
                obrazec.reset();
                prikazSporocilo11("Dodajanje uporabnika je bilo uspešno!", 3000);
            } else if (this.status === 409) {
                const odgovorJSON = JSON.parse(this.responseText);
                if (odgovorJSON.status === 123) {
                    prikazSporocilo1("Uporabniško ime že obstaja!", 3000);
                } else if (odgovorJSON.status === 124) {
                    prikazSporocilo1("E-poštni naslov že obstaja!", 3000);
                } else if (odgovorJSON.status === 125) {
                    prikazSporocilo1("Uporabniško ime in e-poštni naslov že obstajata!", 3000);
                } else {
                    document.getElementById("odgovor1").innerHTML = "Dodajanje ni uspelo: " + this.status;
                }
            } else {
                document.getElementById("odgovor1").innerHTML = "Dodajanje ni uspelo: " + this.status;
            }
        }
    };
    xhr.onerror = function () {
        document.getElementById("odgovor1").innerHTML = "Napaka pri pošiljanju zahteve.";
    };

    xhr.open("POST", "vaja2/igralci.php", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", "Bearer " + token); // Dodajanje JWT žetona v zaglavje zahtevka
    xhr.send(JSONdata);
}

function prikazSporocilo1(sporocilo, cas) {
    var sporociloElement = document.getElementById("sporociloDodaj");

    if (sporociloElement) {
        sporociloElement.innerHTML = sporocilo;

        // Počakaj nekaj sekund (v milisekundah) in nato odstrani sporočilo
        setTimeout(function () {
            sporociloElement.innerHTML = "";
        }, cas);
    } else {
        console.error("Element s ID-jem 'sporociloDodaj' ni bil najden.");
    }
}


// Dekodiranje JWT žetona
function base64urlDecode(str) {
    str = str.replace(/-/g, '+').replace(/_/g, '/');
    while (str.length % 4) {
        str += '=';
    }
    return atob(str);
}

function decodeJwt(token) {
    const parts = token.split('.');
    if (parts.length !== 3) {
        throw new Error('Neveljaven JWT žeton: pričakovane tri dele.');
    }

    const [header, payload, signature] = parts.map(base64urlDecode);

    const decodedToken = {
        header: JSON.parse(header),
        payload: JSON.parse(payload),
        signature: signature
    };

    return decodedToken;
}

//Konec dekodiranja JWT žetona

function preveriAdmin(){
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        //window.location.href = "vstopnastran.html";
        return;
    }
    //konec
    // Admin preveri:
    var decodedToken = decodeJwt(token);
    var uporabniskoime = decodedToken.payload.userId;
    if(uporabniskoime == "sarci"){
        document.getElementById('obrazec').classList.remove('hidden');
        document.getElementById('naslovObrazec').classList.remove('hidden');
    } else {
        alert('Nimate dovoljenj za vstop.');
    }
}
