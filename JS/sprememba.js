function prikaziPodatkeIgralca() {
    // Preveri, če je uporabnik prijavljen
    var token = localStorage.getItem('token');
    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html";
        return;
    }

    var uporabniskoime = document.getElementById("sprememba").elements["uporabniskoime"].value;
    if (!uporabniskoime) {
        return; // Ne izvajamo nadaljnjih korakov, če ni bilo vnešeno uporabniško ime
    }

    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            //console.log("Status odgovora strežnika:", this.status);
            if (this.status == 200) {
                try {
                    var odgovorJSON = JSON.parse(this.responseText);
                    prikaziPodatke(odgovorJSON);
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

    xhr.open("GET", "vaja2/igralci.php?uporabniskoime=" + uporabniskoime, true);
    xhr.setRequestHeader("Authorization", "Bearer " + token); // Dodajanje JWT žetona v zaglavje zahtevka
    xhr.send();
}

function prikaziPodatke(odgovorJSON) {
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
    var obrazec = document.getElementById("posodobitev");
    obrazec.style.display = "block";
    obrazec.ime.value = odgovorJSON['ime'];
    obrazec.priimek.value = odgovorJSON['priimek'];
    obrazec.email.value = odgovorJSON['email'];
}

function posodobiPodatke(uporabniskoime) {
    const data = formToJSON(document.getElementById("posodobitev").elements);
    var JSONdata = JSON.stringify(data, null, "  ");
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 204) {
                //document.getElementById("odgovor").innerHTML = "Podatki so bili uspešno posodobljeni!";
                prikaziSporocilo("Podatki so bili uspešno posodobljeni!", 3000);
                console.log("Posodobitev uspela!");

                // Po uspešni posodobitvi izvedi ponovno prikaz podatkov
                prikaziPodatkeIgralca();
            } else {
                // Dodaj preverjanje za status 409 (Conflict), kar pomeni, da email že obstaja
                if (this.status == 409) {
                    prikaziSporocilo("Email ki ga želite spremeniti žal že obstaja!", 3000);
                } else {
                    document.getElementById("odgovor").innerHTML = "Posodobitev ni uspela: " + this.status;
                }
            }
        }
    };

    var uporabniskoime = document.getElementById("sprememba").uporabniskoime.value;

    xhr.open("PUT", "vaja2/igralci.php?uporabniskoime=" + uporabniskoime, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSONdata);
}

function odstraniUporabnika(uporabniskoime) {
    var uporabniskoime = document.getElementById("sprememba").elements["uporabniskoime"].value;
    var token = localStorage.getItem('token');
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                prikaziSporocilo("Uporabnik uspešno odstranjen!", 3000);
                console.log("Odstranjevanje uporabnika uspelo!");
            } else {
                prikaziSporocilo("Napaka pri odstranjevanju uporabnika!", 3000);
                console.log("Napaka pri odstranjevanju uporabnika:", this.status);
            }
        }
    };

    xhr.open("DELETE", "vaja2/odstrani.php?uporabniskoime=" + uporabniskoime, true);
    xhr.setRequestHeader("Authorization", "Bearer " + token);
    xhr.send();
}


function prikaziSporocilo(sporocilo, cas) {
    var sporociloElement = document.getElementById("sporociloMenjava");
    sporociloElement.innerHTML = sporocilo;
    // Počakaj nekaj sekund (v milisekundah) in nato odstrani sporočilo
    setTimeout(function() {
        sporociloElement.innerHTML = "";
    }, cas);
}

function preveriAdmin2(){
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        return;
    }
    //konec
    // Admin preveri:
    var decodedToken = decodeJwt(token);
    var uporabniskoime = decodedToken.payload.userId;
    if(uporabniskoime == "sarci"){
        document.getElementById('obrazec2').classList.remove('hidden');
        document.getElementById('naslovObrazec2').classList.remove('hidden');
        document.getElementById('obrazec3').classList.remove('hidden');
        document.getElementById('sprememba').classList.remove('hidden');
    } else {
        alert('Nimate dovoljenj za vstop.');
    }
}
