var stompClient = null;
var gameID = null;
var username = null;

window.onload = function () {
    tableCreate();
    connect(gameID);
    var movesJson = $('#moves');
    var moves = JSON.parse(movesJson.val());
    moves.forEach(decodeMove);
};

function connect(ID) {
    gameID = ID;
    var socket = new SockJS('/game/stompEndpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/game/' + gameID, function (message) {
            var move = JSON.parse(message);
            if (message !== 'ERROR') {
                decodeMove(move);
            }
        });
    });
}

//MOVE, PASS, SURRENDER
function sendMove(x, y, type) {
    alert("POLECENIE WYSŁANE");
    if (stompClient != null && gameID != null) {
        stompClient.send('/game/' + gameID, {}, JSON.stringify({
            'x': x,
            'y': y,
            'commandType': type,
            'username': username
        }));
    }
}

function decodeMove(moveList) {
    for (var i = 0; i < moveList.length; i++) {
        var move = moveList[i];
        switch (move.commandType) {
            case 'MOVE': {
                if (move.color === 'WHITE') placePawn(move.x, move.y, true);
                else if (move.color === 'BLACK') placePawn(move.x, move.y, false);
                else clearGrid(move.x, move.y);
                break;
            }
            case 'PASS': {
                if (move.username === username) pass(true);
                else pass(false);
                break;
            }
            case 'SURRENDER': {
                if (move.username === username) surrender(true);
                else surrender(false);
                break;
            }
            case 'WIN' : {
                if (move.username === username) win(true);
                else win(false);
            }
            case 'DRAW': {
                draw();
                break;
            }
        }
    }
}

function pass(me) {
    alert("PASS");
    if (me) {

    }
}

function surrender(me) {
    alert("SURRENDER")
    if (me) {

    }
}

function draw() {

}

function win(me) {
alert("WIN");
}

function tableCreate() {
    var body = document.getElementById("gameBoard");
    var tbdy = document.createElement('tbody');
    for (var i = 0; i < 19; i++) {
        var tr = document.createElement('tr');
        for (var j = 0; j < 19; j++) {
            var td = document.createElement('td');
            var img = document.createElement('img');
            img.setAttribute('src', '/Images/emptyGrid.jpg');
            img.setAttribute('class', 'gameGrid');
            img.setAttribute('onclick', "sendMove("+ i +", "+ j +", 'MOVE')");
            //document.createTextNode('O')
            td.appendChild(img);
            tr.appendChild(td);
        }
        tbdy.appendChild(tr);
    }
    body.appendChild(tbdy);
}

function placePawn(x, y, white) {
    var myTable = document.getElementById('gameBoard');
    myTable.rows[x].cells[y].innerHTML = '';
    var img = document.createElement('img');
    if (white) {
        img.setAttribute('src', '/Images/gridWhitePawn.jpg');
        img.setAttribute('class', 'gameGrid');
    } else {
        img.setAttribute('src', '/Images/gridBlackPawn.jpg');
        img.setAttribute('class', 'gameGrid');
    }
    myTable.rows[x].cells[y].appendChild(img);
}

function clearGrid(x, y) {
    var myTable = document.getElementById('gameBoard');
    myTable.rows[x].cells[y].innerHTML = '';
    var img = document.createElement('img');
    img.setAttribute('src', '/Images/emptyGrid.jpg');
    img.setAttribute('class', 'gameGrid');
    myTable.rows[x].cells[y].appendChild(img);
}
