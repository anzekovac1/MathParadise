const formToJSON = elements => [].reduce.call(elements, (data, element) => {
    if (element.name !== "") {
        data[element.name] = element.value;
    }
    return data;
}, {});

function registracija() {
    const obrazec = document.getElementById("registracijaobrazec");
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
                //document.getElementById("odgovor21").innerHTML = "Dodajanje uspelo! Sedaj se lahko prijavite!";
                prikazSporociloReg("Dodajanje uspelo! Sedaj se lahko prijavite!", 3000);
                obrazec.reset();
            } else if (this.status === 409) {
                const odgovorJSON = JSON.parse(this.responseText);
                if (odgovorJSON.status === 123) {
                    prikazSporociloReg("Uporabniško ime že obstaja!", 3000);
                } else if (odgovorJSON.status === 124) {
                    prikazSporociloReg("E-poštni naslov že obstaja!", 3000);
                } else if (odgovorJSON.status === 125) {
                    prikazSporociloReg("Uporabniško ime in e-poštni naslov že obstajata!", 3000);
                } else {
                    prikazSporociloReg("Dodajanje ni uspelo: " + this.status, 5000);
                }
            } else {
                //prikazSporociloReg("Dodajanje ni uspelo: " + this.status, 5000);
            }
        }
    };
    xhr.onerror = function () {
        document.getElementById("odgovor21").innerHTML = "Napaka pri pošiljanju zahteve.";
    };

    xhr.open("POST", "vaja2/registracija.php", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSONdata);
}

function prikazSporociloReg(sporocilo, cas) {
    var sporociloElement = document.getElementById("sporociloregister");

    // Preveri, ali je element na voljo
    if (sporociloElement) {
        sporociloElement.innerHTML = sporocilo;

        // Počakaj nekaj sekund (v milisekundah) in nato odstrani sporočilo
        setTimeout(function () {
            sporociloElement.innerHTML = "";
        }, cas);
    } else {
        console.error("Element s ID-jem 'sporociloregister' ni bil najden.");
    }
}
