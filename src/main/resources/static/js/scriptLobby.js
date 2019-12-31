function createGame() {
    $.get({
        url: '/game/create'
    })
        .fail( alert("Nie udało się utworzyć gry-spróbuj ponownie!") );
}
function chooseGame(x) {
    var label = document.getElementById("labelChosenGame");
    label.innerText = x;
}
function startGame() {
    var gameID;
    var label = document.getElementById("labelChosenGame");
    gameID = label.textContent;
    joinGame(gameID);
}
function joinGame(gameID) {
    $.get({
        url: '/game/join/' + gameID
    })
        .fail("Nie udało się dołączyć do gry. Spróbuj odświeżyć stronę i wybrać aktywną grę");
}