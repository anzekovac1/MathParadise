function vseIgre() {
    // Preveri, če je uporabnik prijavljen
    var token = localStorage.getItem('token');

    if (!token) {
        console.log("Ni veljavnega JWT žetona. Uporabnik ni prijavljen.");
        window.location.href = "vstopnastran.html"; // Preusmeritev na vstopno stran
        return;
    }
    
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (this.readyState == 4) {
            if (this.status == 200) {
                try {
                    var odgovorJSON = JSON.parse(this.responseText);
                    prikaziIgre(odgovorJSON, 'tabelaIgre');
                } catch (e) {
                    console.log("Napaka pri razčlenjevanju podatkov");
                }
            } else if (this.status == 401) {
                console.log("JWT žeton ni veljaven. Uporabnik ni avtenticiran.");
            } else {
                console.log("Napaka pri pridobivanju podatkov. Status: " + this.status);
            }
        }
    };

    xhr.open("GET", "vaja2/vseigre.php", true);
    xhr.setRequestHeader("Authorization", "Bearer " + token); // Dodajanje JWT žetona v zaglavje zahtevka
    xhr.send();
}

function prikaziIgre(odgovorJSON, tabelaId) {
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
    
    var tabela = document.getElementById(tabelaId);
    tabela.innerHTML = "<tr><th>Uporabniško ime</th><th>Težavnostna stopnja</th><th>Rezultat</th><th>Datum in ura</th></tr>";
    tabela.appendChild(fragment);
    tabela.innerHTML += "</tbody>";
    tabela.classList.add('styled-table');
}
