<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <!--bootstrap css-->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!--dolaczam plik css z opisem graficznym strony-->
    <link href="/css/basicStyleSheet.css" rel="stylesheet" type="text/css"/>
    <title>Game</title>
</head>
<body>
<header>GAME</header>
<br/>

<input type="hidden" id="moves" value="${moves}">

<div class="row">
    <div class="col-sm-3">
        <section class="sekcjaGame">
            <h2>
                TOOLS
            </h2>
            <button class="przyciskMenu">
                RETURN
            </button>
            <br/>
            <label>
                PLAYER:
            </label>
            <br/>
            <label>
                nickname...
            </label>
            <br/>
            <label>
                POINTS:
            </label>
            <label>
                0
            </label>
            <br>
            <label>
                ENEMY POINTS
            </label>
            <br>
            <label>
                0
            </label>
            <br/>
            <button class="przyciskMenu">
                PAUSE
            </button>
            <button class="przyciskMenu">
                SURRENDER
            </button>

        </section>
    </div>
    <div class="col-sm-9">
        <section class="sekcjaGame" id="sekcjaGameBoard">
            <h2>
                GAME BOARD
            </h2>
            <table id="gameBoard">

            </table>
        </section>
    </div>
</div>
<!--dolaczam java script-->
<script src="/js/scriptGame.js"></script>
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