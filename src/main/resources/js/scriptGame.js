function tableCreate() {
    var body = document.getElementById("gameBoard");
    var tbdy = document.createElement('tbody');
    for (var i = 0; i < 19; i++) {
        var tr = document.createElement('tr');
        for (var j = 0; j < 19; j++) {
                var td = document.createElement('td');
                var img = document.createElement('img');
                img.setAttribute('src','../Images/emptyGrid.jpg');
                img.setAttribute('class','gameGrid')
                //document.createTextNode('O')
                td.appendChild(img);
                tr.appendChild(td);
        }
        tbdy.appendChild(tr);
    }
    body.appendChild(tbdy);
}
function placePawn(x,y,white) {
    var myTable = document.getElementById('gameBoard');
    myTable.rows[x].cells[y].innerHTML='';
    var img = document.createElement('img');
    if(white)
    {
        img.setAttribute('src','../Images/gridWhitePawn.jpg');
        img.setAttribute('class','gameGrid');
    }
    else
    {
        img.setAttribute('src','../Images/gridBlackPawn.jpg');
        img.setAttribute('class','gameGrid');
    }
    myTable.rows[x].cells[y].appendChild(img);
}
function clearGrid(x,y)
{
    var myTable = document.getElementById('gameBoard');
    myTable.rows[x].cells[y].innerHTML='';
    var img = document.createElement('img');
    img.setAttribute('src','../Images/emptyGrid.jpg');
    img.setAttribute('class','gameGrid');
    myTable.rows[x].cells[y].appendChild(img);
}
tableCreate();
placePawn(3,3,1);
placePawn(4,4,0);
placePawn(5,5,0);
clearGrid(4,4);