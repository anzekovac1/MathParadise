function odjava() {
    // Izbris tokena in preusmeritev na vstopno stran
    localStorage.removeItem('token');
    window.location.href = '/mpgame/vstopnastran.html';
}