var pointid = 0;
var listaDePontos = [];
var melhorRota = [];
var ctx;
var ctxbkp;
var pointList = [];
var point = new Object();
var configuracao;
var quadrosave;

function executar() {
	$.ajax({
		url : "grafos",
		method : "get",
		data : montaGet()
	});
}

function vm() {
	$.ajax({
		url : "grafos",
		method : "get",
		data : 'vm',
		success : function(data) {
			if (data != "") {
				melhorRota = [];
				for (var i = 0; i < data.length; i++) {
					melhorRota.push(data[i].p);
				}
				desenhaRota();
			}
		}
	});
}

function montaConfiguracao() {
	configuracao = new Object();
	configuracao.execute = '1';
	configuracao.iteracoes = document.getElementById('iIteracoes').value;
	configuracao.pontos = pointList;
	configuracao.mutacao = document.getElementById('iMutacao').value;
	configuracao.reproducao = document.getElementById('iReproducao').value;
	configuracao.populacaoInicial = document.getElementById('iPopulacaoInicial').value;
	
	return configuracao;
}
function montaGet() {
	return 'execute=1&iteracoes=' + document.getElementById('iIteracoes')
			+ '&pontos=' + document.getElementById('iPontos') + '&mutacao='
			+ document.getElementById('iMutacao') + '&reproducao='
			+ document.getElementById('iReproducao');
}

function verificaMelhorRota() {
	$.ajax({
		url : "grafos",
		type : "POST",
		dataType : 'json',
		contentType : 'application/json',
		mimeType : 'application/json',
		data : JSON.stringify(pointList)
	});
}

function exealgo() {
	saveCanvas();
	$.ajax({
		url : "grafos",
		type : "POST",
		dataType : 'json',
		contentType : 'application/json',
		mimeType : 'application/json',
		data : JSON.stringify(montaConfiguracao()),
	});
}

setInterval('vm()', 600);

window.onload = function() {
	quadro = document.getElementById("quadro");
	quadro.setAttribute("width", 1150);
	quadro.setAttribute("height", 550);
	quadro.addEventListener('click', onCanvasClick, false);
	ctx = quadro.getContext("2d");
	ctx.save();

};

function onCanvasClick(evt) {
	var xt = evt.clientX - quadro.offsetLeft;
	var yt = evt.clientY - quadro.offsetTop;
	desenhaPonto(xt, yt);
}

function desenhaPonto(xt, yt) {
	ctx.beginPath();
	ctx.arc(xt, yt, 5, 0, 2 * Math.PI, true);
	ctx.fill();
	pointid++;
	listaDePontos.push({
		"point" : pointid,
		"x" : xt,
		"y" : yt
	});
	var p = new Object();
	p.id = pointid;
	p.x = xt;
	p.y = yt;
	pointList.push(p);
	saveCanvas();
}

function clearCanvas() {
	ctx.beginPath();
	ctx.setTransform(1, 0, 0, 1, 0, 0);
	ctx.clearRect(0, 0, 1150, 550);
	ctx.restore();
}

function desenhaRota() {
	clearCanvas();
	restoreCanvas();
	for (var i = 0; i < melhorRota.length; i++) {
		var origem = retornaOIndiceDaListaDePontos(melhorRota[i]);
		ctx.moveTo(listaDePontos[origem].x, listaDePontos[origem].y);
		var destino = 0;
		if (i === (melhorRota.length - 1)) {
			destino = retornaOIndiceDaListaDePontos(melhorRota[0]);
		} else {
			destino = retornaOIndiceDaListaDePontos(melhorRota[i + 1]);
		}
		ctx.lineTo(listaDePontos[destino].x, listaDePontos[destino].y);
		ctx.stroke();
	}
}

function retornaOIndiceDaListaDePontos(p) {
	for (var i = 0; i < listaDePontos.length; i++) {
		if (listaDePontos[i].point == p) {
			return i;
		}
	}
}

function reiniciaDados() {
	clearCanvas();
	restoreCanvas();
	$.ajax({
		url : "grafos",
		method : "post",
		data : "restart=1"
	});
	
}

function geraPontosAleatorios() {
	clearCanvas();
	// TODO tem que validar a entrada
	var qtdPontos = document.getElementById('iPontos').value;
	for (var i = 0; i < qtdPontos; i++) {
		desenhaPonto(randX(), randY());
	}
}

function randX() {
	return rand(0, 1150);
}

function randY() {
	return rand(0, 550);
}

function rand(num_minimo, num_maximo) {
	return Math.floor((Math.random() * (num_maximo - num_minimo + 1))
			+ num_minimo);
}

function saveCanvas() {
	quadrosave = document.getElementById("quadrosave");
	quadrosave.setAttribute("width", 1150);
	quadrosave.setAttribute("height", 550);
	quadrosave.addEventListener('click', onCanvasClick, false);
	ctxbkp = quadrosave.getContext("2d");
	ctxbkp.drawImage(quadro, 0, 0);
}

function restoreCanvas() {
	q = document.getElementById("quadro");
	q.setAttribute("width", 1150);
	q.setAttribute("height", 550);
	q.addEventListener('click', onCanvasClick, false);
	q.getContext("2d").drawImage(quadrosave, 0, 0);
}
