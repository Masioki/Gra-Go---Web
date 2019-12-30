function createGame() {
    $.get({
        url: '/game/create'
    })
        .fail( //TODO );
}

function joinGame(gameID) {
    $.get({
        url: '/game/join/' + gameID
    })
        .fail(//TODO);
}