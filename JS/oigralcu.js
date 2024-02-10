function podatkiIgralca() {
    // Preveri, če je uporabnik prijavljen
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html";
        return;
    }

    var uporabniskoime = document.getElementById("infobrazec").elements["uporabniskoime"].value;
    if (!uporabniskoime) {
        return; // Ne izvajamo nadaljnjih korakov, če ni bilo vnešeno uporabniško ime
    }

    document.getElementById('naslovInfo').style.display = 'block'; // Prikaži naslov "Informacije o izbranem uporabniku"
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            //console.log("Status odgovora strežnika:", this.status);

            if (this.status == 200) {
                try {
                    var odgovorJSON = JSON.parse(this.responseText);
                    prikaziIgralca(odgovorJSON);
                    pridobiOdigraneIgre(uporabniskoime);
                } catch (e) {
                    console.log("Napaka pri razčlenjevanju podatkov");
                    // Dodaj spodnjo vrstico za izpis napake v tabeloInfo
                    prikaziNapako("Napaka pri razčlenjevanju podatkov");
                }
            } else {
                console.log("Napaka pri zahtevi:", this.statusText);
                // Dodaj spodnjo vrstico za izpis napake v tabeloInfo
                prikaziNapako("Napaka pri pridobivanju podatkov o igralcu. Prosim vpišite veljavno uporabniško ime");
            }
        }
    };

    xhr.open("GET", "vaja2/igralci.php?uporabniskoime=" + uporabniskoime, true);
    xhr.setRequestHeader("Authorization", "Bearer " + token); // Dodajanje JWT žetona v zaglavje zahtevka
    xhr.send();
}

//---------------------------------------------------------------
//---                 ODIGRANE IGRE                          ----   
//---------------------------------------------------------------

function pridobiOdigraneIgre(uporabniskoime) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            try {
                var odigraneIgre = JSON.parse(this.responseText);
                prikaziOdigraneIgre(odigraneIgre);
            } catch (e) {
                console.log("Napaka pri razčlenjevanju podatkov o odigranih igrah");
                prikaziNapako("Napaka pri pridobivanju podatkov o odigranih igrah");
            }
        }
    };

    xhr.open("GET", "vaja2/vseigre.php?uporabniskoime=" + uporabniskoime, true);
    xhr.send();
}

// Funkcije za prikaz podatkov o igralcu
function prikaziIgralca(odgovorJSON) {
    var tabela = document.getElementById("tabelaInfo");
    tabela.innerHTML = "<tr><th>Ime</th><th>Priimek</th><th>Email</th></tr>";
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
    tabela.appendChild(fragment);
    tabela.innerHTML += "</tbody>";
    tabela.classList.add('styled-table');
}

function prikaziOdigraneIgre(odigraneIgre) {
    var tabelaIgre = document.getElementById("tabelaOdigraneIgre");
    
    if (odigraneIgre.length === 0) {
        tabelaIgre.innerHTML = "<tr><td colspan='3'>Uporabnik še ni odigral igre.</td></tr>";
        return;
    }

    tabelaIgre.innerHTML = "<tr><th>Težavnost</th><th>Rezultat</th><th>Datum in ura</th></tr>";
    var fragment = document.createDocumentFragment();

    for (var i = 0; i < odigraneIgre.length; i++) {
        var tr = document.createElement("tr");

        for (var stolpec in odigraneIgre[i]) {
            var td = document.createElement("td");
            td.innerHTML = odigraneIgre[i][stolpec];
            tr.appendChild(td);
        }

        fragment.appendChild(tr);
    }

    tabelaIgre.appendChild(fragment);
    tabelaIgre.innerHTML += "</tbody>";
    tabelaIgre.classList.add('styled-table');
}

// Funkcija za prikaz napake
function prikaziNapako(napaka) {
    var tabela = document.getElementById("tabelaInfo");
    var tabelaIgre = document.getElementById("tabelaOdigraneIgre");
    // Ustvari element za prikaz napake
    var errorContainer = document.createElement("div");
    errorContainer.classList.add("error-container"); 
     // Skrij naslov "Informacije o izbranem uporabniku"
     document.getElementById('naslovInfo').style.display = 'none';
    // Postavi napako v ustvarjeni element
    errorContainer.innerHTML = napaka;
    // Pobriši vsebino tabel
    tabela.innerHTML = "";
    tabelaIgre.innerHTML = "";
    // Dodaj ustvarjeni element v obe tabeli
    tabela.appendChild(errorContainer);
    tabelaIgre.appendChild(errorContainer);
}

