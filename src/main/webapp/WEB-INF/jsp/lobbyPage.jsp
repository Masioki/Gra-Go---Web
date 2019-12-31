<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <!--bootstrap css-->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!--dolaczam plik css z opisem graficznym strony-->
    <link href="/css/basicStyleSheet.css" rel="stylesheet" type="text/css"/>
    <title>Lobby</title>
</head>
<body>
<header>LOBBY</header>
<br/>
<div class="row">
    <div class="col-sm-4">
        <section class="sekcjaLobby">
            <h2>
                ACTIVE GAMES
            </h2>
            <button class="przyciskMenu" onclick="createGame()">
                CREATE GAME
            </button>
            <br/>
            <label>
                WYBRANA GRA:
            </label>
            <br/>
            <label id="labelChosenGame" onclick="startGame()">
                0
            </label>
            <br/>
            <button class="przyciskMenu">
                JOIN GAME
            </button>
            <br/>
            <button class="przyciskMenu">
                PLAY AGAINST CP
            </button>
            <br/>
        </section>
    </div>
    <div class="col-sm-8">
        <section class="sekcjaLobby">
            <h2>
                LOBBY LIST
            </h2>
            <button class="przyciskLobby" onclick="chooseGame(this.getText())">
                GAME IS DEAD...
            </button>
            <button class="przyciskLobby" onclick="chooseGame(this.getText())">
                GAME IS DEAD...
            </button>
            <button class="przyciskLobby" onclick="chooseGame(this.getText())">
                GAME IS DEAD...
            </button>
            <button class="przyciskLobby" onclick="chooseGame(this.getText())">
                GAME IS DEAD...
            </button>
            <button class="przyciskLobby" onclick="chooseGame(this.getText())">
                GAME IS DEAD...
            </button>
        </section>
    </div>
</div>
<!--dolaczam java script-->
<script src="/js/scriptLobby.js"></script>
<!--bootstrap js-->
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>

</body>
</html>