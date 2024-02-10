function lestvica() {
    // Pridobitev JWT žetona iz lokalnega pomnilnika
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html";
        return;
    }

    var tezavnost = document.getElementById("obrazecLestvica")['tezavnost'].value;

    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                try {
                    var odgovorJSON = JSON.parse(this.responseText);
                    prikaziLestvico(odgovorJSON);
                } catch (e) {
                    console.log("Napaka pri razčlenjevanju podatkov");
                }
            } else {
                document.getElementById("odgovor").innerHTML = "Ni uspelo: " + this.status;
            }
        }
    };

    xhr.open("GET", "vaja2/lestvica.php?tezavnost=" + tezavnost, true);
    xhr.setRequestHeader("Authorization", "Bearer " + token); // Dodajanje JWT žetona v zaglavje zahtevka
    xhr.send();
}

function prikaziLestvico(odgovorJSON) {
    // Pridobitev JWT žetona iz lokalnega pomnilnika
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        return;
    }
    
    var tabela = document.getElementById("tabelaLestvica");
    tabela.innerHTML = "<tr><th>Uporabniško ime</th><th>Težavnostna stopnja</th><th>Rezultat</th><th>Datum in ura</th></tr>";

    var fragment = document.createDocumentFragment();

    for (var i = 0; i < odgovorJSON.length; i++) {
        var tr = document.createElement("tr");

        for (var stolpec in odgovorJSON[i]) {
            var td = document.createElement("td");
            td.innerHTML = odgovorJSON[i][stolpec];
            tr.appendChild(td);
        }

        fragment.appendChild(tr);
    }
    tabela.appendChild(fragment);
    tabela.innerHTML += "</tbody>";
    tabela.classList.add('styled-table');
}
