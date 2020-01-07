
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
    window.location.replace('/game/join/' + gameID);
}