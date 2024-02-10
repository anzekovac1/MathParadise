// Dekodiranje JWT žetona-------------------
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


function prikaziPodatkeIgralcaSpremembe() {
    // Pridobitev JWT žetona iz lokalnega pomnilnika
    var token = localStorage.getItem('token');
    
    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html";
        return;
    }
    var decodedToken = decodeJwt(token);
    var uporabniskoime = decodedToken.payload.userId;
    
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                try {
                    var odgovorJSON = JSON.parse(this.responseText);
                    prikaziPodatkeSpremembe(odgovorJSON);
                    document.getElementById("uporabniskoime").innerHTML = uporabniskoime;
                } catch (e) {
                    console.log("Napaka pri razčlenjevanju podatkov");
                    prikaziNapako("Napaka pri razčlenjevanju podatkov");
                }
            } else {
                console.log("Napaka pri zahtevi:", this.statusText);
                prikaziNapako("Napaka pri pridobivanju podatkov o igralcu. Prosim vpišite veljavno uporabniško ime");
            }
        }
    };

    xhr.open("GET", "vaja2/spremeniPodatke.php?uporabniskoime=" + uporabniskoime, true);
    xhr.setRequestHeader("Authorization", "Bearer " + token); // Dodajanje JWT žetona v zaglavje zahtevka
    xhr.send();
}

function prikaziPodatkeSpremembe(odgovorJSON) {
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html";
        return;
    }

    var fragment = document.createDocumentFragment();
    var tr = document.createElement("tr");

    for (var stolpec in odgovorJSON) {
        if (odgovorJSON.hasOwnProperty(stolpec)) {
            var td = document.createElement("td");
            td.innerHTML = odgovorJSON[stolpec];
            tr.appendChild(td);
        }
    }
    fragment.appendChild(tr);


    // Izpolni polja obrazca za posodobitev z dobljenimi podatki
    var obrazec = document.getElementById("posodobitevUser");
    obrazec.style.display = "block";
    obrazec.ime.value = odgovorJSON['ime'];
    obrazec.priimek.value = odgovorJSON['priimek'];
    obrazec.email.value = odgovorJSON['email'];
}

function posodobiSvojePodatke(uporabniskoime) {
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html";
        return;
    }
    var decodedToken = decodeJwt(token);
    var uporabniskoime = decodedToken.payload.userId;

    const data = formToJSON(document.getElementById("posodobitevUser").elements);
    var JSONdata = JSON.stringify(data, null, "  ");
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 204) {
                prikaziSporocilo("Podatki so bili uspešno posodobljeni!", 3000);

                // Po uspešni posodobitvi izvedi ponovno prikaz podatkov
                prikaziPodatkeIgralcaSpremembe();
            } else {
                // Dodaj preverjanje za status 409 (Conflict), kar pomeni, da email že obstaja
                if (this.status == 409) {
                    prikaziSporocilo("Email ki ga želite spremeniti žal že obstaja!", 3000);
                } else {
                    document.getElementById("sporocilo").innerHTML = "Posodobitev ni uspela: " + this.status;
                }
            }
        }
    };

    xhr.open("PUT", "vaja2/spremeniPodatke.php?uporabniskoime=" + uporabniskoime, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", "Bearer " + token); // Dodajanje JWT žetona v zaglavje zahtevka
    xhr.send(JSONdata);
}

function prikaziSporocilo(sporocilo, cas) {
    var sporociloElement = document.getElementById("sporocispremembo");
    sporociloElement.innerHTML = sporocilo;
    // Počakaj nekaj sekund (v milisekundah) in nato odstrani sporočilo
    setTimeout(function() {
        sporociloElement.innerHTML = "";
    }, cas);
}
