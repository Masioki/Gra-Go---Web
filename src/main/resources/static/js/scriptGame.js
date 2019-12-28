var stompClient = null;
var gameID = null;
var username = null;

$(document).ready(function () {
    var movesJson = $('#moves');
    var moves = JSON.parse(movesJson.val());
    moves.forEach(placePawn);
});

function connect(ID) {
    gameID = ID;
    var socket = new SockJS('/game/stompEndpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/game/' + gameID, function (message) {
            var move = JSON.parse(message);
            if (message !== 'ERROR') {
                if (move.commmandType === 'MOVE') {
                    placePawn(move.x, move.y, move.white)
                } else if (move.commmandType === 'PASS') {
                    if (move.username === username) pass(true);
                    else pass(false);
                } else if (move.commmandType === 'SURRENDER') {
                    if (move.username === username) surrender(true);
                    else surrender(false);
                }
            }
        });
    });
}

//MOVE, PASS, SURRENDER
function sendMove(x, y, type) {
    if (stompClient != null && gameID != null) {
        stompClient.send('/game/' + gameID, {}, JSON.stringify({
            'x': x,
            'y': y,
            'commandType': type,
            'username': username
        }));
    }
}

function pass(me) {
    if (me) {

    }
}

function surrender(me) {
    if (me) {

    }
}

function tableCreate() {
    var body = document.getElementById("gameBoard");
    var tbdy = document.createElement('tbody');
    for (var i = 0; i < 19; i++) {
        var tr = document.createElement('tr');
        for (var j = 0; j < 19; j++) {
            var td = document.createElement('td');
            var img = document.createElement('img');
            img.setAttribute('src', '../Images/emptyGrid.jpg');
            img.setAttribute('class', 'gameGrid');
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
        img.setAttribute('src', '../Images/gridWhitePawn.jpg');
        img.setAttribute('class', 'gameGrid');
    } else {
        img.setAttribute('src', '../Images/gridBlackPawn.jpg');
        img.setAttribute('class', 'gameGrid');
    }
    myTable.rows[x].cells[y].appendChild(img);
}

function clearGrid(x, y) {
    var myTable = document.getElementById('gameBoard');
    myTable.rows[x].cells[y].innerHTML = '';
    var img = document.createElement('img');
    img.setAttribute('src', '../Images/emptyGrid.jpg');
    img.setAttribute('class', 'gameGrid');
    myTable.rows[x].cells[y].appendChild(img);
}

tableCreate();
placePawn(3, 3, 1);
placePawn(4, 4, 0);
placePawn(5, 5, 0);
clearGrid(4, 4);