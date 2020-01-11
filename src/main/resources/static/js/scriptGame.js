var stompClient = null;
var gameID = null;
var username = null;

window.onload = function () {
    tableCreate();
    gameID = $('#ID').val();
    getMoves(gameID);
    connect(gameID);
};


function getMoves(ID) {
    $.get({
        url: '/game/moves/' + gameID,
        dataType: "json",
        success: [function (data) {
            decodeMove(data);
        }]
    })
}

function connect(ID) {
    gameID = ID;
    var socket = new SockJS('/gameStompEndpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/stomp/' + gameID, function (move) {
            if (move.body !== "ERROR") decodeMove(JSON.parse(move.body));
        });
    });
}

//MOVE, PASS, SURRENDER
function sendMove(x, y, type) {
    if (stompClient != null && gameID != null) {
        stompClient.send('/stomp/' + gameID, {}, JSON.stringify({
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
                if (move.color.localeCompare("WHITE")) placePawn(move.x, move.y, true);
                else if (move.color.localeCompare("BLACK")) placePawn(move.x, move.y, false);
                else clearGrid(move.x, move.y);
                break;
            }
            case 'PASS': {
                if (move.username.localeCompare(username)) pass(true);
                else pass(false);
                break;
            }
            case 'SURRENDER': {
                if (move.username.localeCompare(username)) surrender(true);
                else surrender(false);
                break;
            }
            case 'WIN' : {
                if (move.username.localeCompare(username)) win(true);
                else win(false);
                break;
            }
            case 'DRAW': {
                draw();
                break;
            }
            case 'MOVE_AUTO': {
                clearGrid(move.x, move.y);
                break;
            }
        }
    }
    refreshScore();
}

function pass(me) {
    if (me) {
        alert("PASS");
    }
    else
    {
        alert("Opponent passed")
    }
}

function surrender(me) {
    if (me) {
        alert("You lost. Sadly ... :(")
    }
}

function draw() {
    alert("Its draw.... :/")
}

function win(me) {
    if(me)
    {
        alert("You Won :)");
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
            img.setAttribute('src', '/Images/emptyGrid.jpg');
            img.setAttribute('class', 'gameGrid');
            img.setAttribute('onclick', "sendMove(" + i + ", " + j + ", 'MOVE')");
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
    if (white === true) {
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

function refreshScore() {
    $.get({
        url: "/game/score",
        dataType: 'json',
        success: [function (data) {
            setScore(data.own, true);
            setScore(data.opponent, false);
        }]
    });
}

function setScore(score, me) {
    /*jeśli prawda ustawiamy punkty gracza w przeciwnym razie jego przeciwnika*/
    if (me) {
        var label = document.getElementById("labelPlayerScore");
        label.innerText = score;
    } else {
        var label = document.getElementById("labelEnemyScore");
        label.innerText = score;
    }
}

function setPlayerName(name) {
    var label = document.getElementById("labelPlayerName");
    label.innerText = score;
}