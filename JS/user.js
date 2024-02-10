// Funkcija za prikaz uporabniškega imena

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
//Konec dekodiranja JWT žetona-------------------

function prikaziUsername() {
    var token = localStorage.getItem('token');
    if (token) { // Preveri, ali je token na voljo
        var decodedToken = decodeJwt(token);
        var uporabniskoime = decodedToken.payload.userId;
        document.getElementById("Prikazuporabniskoime").innerHTML = uporabniskoime;
    }
}
