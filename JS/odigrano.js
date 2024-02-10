function pridobiOdigraneIgre() {
    var uporabniskoime = document.getElementsByName('uporabniskoime')[0].value;

    // Uporabi AJAX zahtevko za pridobitev odigranih iger
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "vaja2/vseigre.php?uporabniskoime=" + uporabniskoime, true);

    xhr.onload = function () {
        if (xhr.status === 200) {
            var odigraneIgre = JSON.parse(xhr.responseText);

            // Prikazi odigrane igre v tabeli
            var tabelaOdigrano = document.getElementById('tabelaOdigrano');
            tabelaOdigrano.innerHTML = "<tr><th>Uporabniško ime</th><th>Časovni žig</th><th>Težavnost</th><th>Rezultat</th></tr>";

            for (var i = 0; i < odigraneIgre.length; i++) {
                tabelaOdigrano.innerHTML += "<tr><td>" + odigraneIgre[i].uporabniskoime + "</td><td>" + odigraneIgre[i].casovni_zig + "</td><td>" + odigraneIgre[i].tezavnost + "</td><td>" + odigraneIgre[i].rezultat + "</td></tr>";
            }
        } else {
            var odgovorOdigrano = document.getElementById('odgovorOdigrano');
            odgovorOdigrano.innerHTML = "Napaka pri pridobivanju informacij o odigranih igrah.";
        }
    };

    xhr.send();
}
