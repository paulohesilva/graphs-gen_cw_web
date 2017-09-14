var pointid = 0;
var listaDePontos = [];
var melhorRota = [];
var ctx;
var ctxbkp;
var pointList = [];
var point = new Object();
var configuracao;
var quadrosave;
var algoritmo = 1;
var desenhou = false;
var podeGerarPonto = true; 

//===========> especifico Clarke & Writhing
var selecionarDeposito = false;
var depositoAdicionado = false;

// ========================> configuracoes do canvas
var widthCanvas = 1100;
var heightCanvas = 560;
var quadro;

window.onload = function() {
	initiCanvas();
};

function initiCanvas() {
	quadro = document.getElementById("quadro");
	quadro.setAttribute("width", widthCanvas.toString());
	quadro.setAttribute("height", heightCanvas.toString());
	quadro.addEventListener('click', onCanvasClick, false);
	ctx = quadro.getContext("2d");
	ctx.save();
}

$(function() {
	inicializaAlgoritmo(1);
});

$(function() {
	$("#btnExecutar").click(function() {
		switch (algoritmo) {
			case 1:{
				executaAlgoritmoSelecionado();
				break;
			}
			//C&W
			case 2:{
				if(validaCW()){
					executaAlgoritmoSelecionado();
				}else{
					alert('Configuracoes incorretas!');
				}
				break;
			}
		}
		
		
	});

	$("#btnCW").click(function() {
		inicializaAlgoritmo(2);
		console.log("CW");
	});

	$("#btnGenetico").click(function() {
		inicializaAlgoritmo(1);
		console.log("Genetico");
	});
	$("#btnFormigas").click(function() {
		console.log("Formigas");
	});

	$("#btnDeposito").click(function() {
		selecionarDeposito = !selecionarDeposito;
	});

	$("#btnNovo").click(function() {
		novo();
	});

	$("#btnGo").bind("click", geraPontosAleatorios);

});

function executaAlgoritmoSelecionado(){
	reiniciaDados();
	saveCanvas();
	podeGerarPonto = false;
	$.ajax({
		url : "grafos",
		type : "POST",
		dataType : 'json',
		contentType : 'application/json',
		mimeType : 'application/json',
		data : JSON.stringify(montaConfiguracao())
	});
}

function inicializaAlgoritmo(opcao) {
	novo();
	switch (opcao) {
	case 1: {
		algoritmoGenetico(true);
		algoritmoCW(false);
		break;
	}
	case 2: {
		algoritmoGenetico(false);
		algoritmoCW(true);
		break;
	}
	}
}

function algoritmoGenetico(inicializar) {
	if (inicializar) {
		$(".gen").show("slow");
		algoritmo = 1;
	} else {
		$(".gen").hide("slow");
	}
}

function algoritmoCW(inicializar) {
	if (inicializar) {
		$(".cw").show("slow");
		algoritmo = 2;
	} else {
		$(".cw").hide("slow");
	}
}

setInterval(function vm() {
	desenhou = false;
	$.ajax({
		url : "grafos",
		method : "get",
		data : 'vm',
		success : function(data) {
			if (data != "") {
				melhorRota = [];
				teste = data[0];
				if (teste.p == undefined) {
					if (!desenhou) {
						for (var j = 0; j < data.length; j++) {
							var a = data[j];
							for (var i = 0; i < a.length; i++) {
								melhorRota.push(a[i]);
							}
						}
						desenhaRota();
						desenhou = true;
					}

				} else {
					melhorRota = [];
					for (var i = 0; i < data.length; i++) {
						melhorRota.push(data[i].p);
					}
					desenhaRota();
				}

			}
		}
	});
}, 500);

function montaConfiguracao() {
	configuracao = new Object();
	configuracao.execute = '1';
	configuracao.iteracoes = document.getElementById('iIteracoes').value;
	configuracao.pontos = pointList;
	configuracao.mutacao = document.getElementById('iMutacao').value;
	configuracao.reproducao = document.getElementById('iReproducao').value;
	configuracao.populacaoInicial = document
			.getElementById('iPopulacaoInicial').value;
	configuracao.restricaoPontos = document.getElementById('iRestricao').value;
	configuracao.algoritmo = algoritmo;

	return configuracao;
}

function onCanvasClick(evt) {
	if(podeGerarPonto){
		var xt = evt.clientX - quadro.offsetLeft;
		var yt = evt.clientY - quadro.offsetTop;
		desenhaPonto(xt, yt);
	}
	
}

function desenhaPonto(xt, yt) {
	var storeHouse = false;
	ctx.beginPath();
	ctx.fillStyle = "blue";
	if (selecionarDeposito && !depositoAdicionado) {
		//adicionar ponto como deposito propriedade storeHouse
		ctx.arc(xt, yt, 7, 0, 2 * Math.PI, true);
		ctx.fillStyle = "red";
		storeHouse = true;
		selecionarDeposito = false;
		depositoAdicionado = true;
	} else {
		ctx.arc(xt, yt, 5, 0, 2 * Math.PI, true);
	}
	ctx.fill();
	pointid++;
	listaDePontos.push({
		"point" : pointid,
		"x" : xt,
		"y" : yt,
		"storeHouse" : storeHouse
	});
	var p = new Object();
	p.id = pointid;
	p.x = xt;
	p.y = yt;
	p.storeHouse = storeHouse;
	pointList.push(p);
	saveCanvas();
}

function clearCanvas() {
	ctx.beginPath();
	ctx.setTransform(1, 0, 0, 1, 0, 0);
	ctx.clearRect(0, 0, widthCanvas.toString(), heightCanvas.toString());
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

function novo() {
	podeGerarPonto = true;
	pointid = 0;
	listaDePontos = [];
	melhorRota = [];
	pointList = [];
	point = new Object();
	configuracao;
	desenhou = false;
	selecionarDeposito = false;
	depositoAdicionado = false;
	initiCanvas();
	clearCanvas();
	$.ajax({
		url : "grafos",
		method : "post",
		data : "restart=1"
	});
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
	if(podeGerarPonto){
		pointList = [];
		listaDePontos = [];
		clearCanvas();
		var qtdPontos = document.getElementById('iPontos').value;
		for (var i = 0; i < qtdPontos; i++) {
			desenhaPonto(randX(), randY());
		}
	}
	
}

function randX() {
	return rand(0, widthCanvas.toString());
}

function randY() {
	return rand(0, heightCanvas.toString());
}

function rand(num_minimo, num_maximo) {
	return Math.floor((Math.random() * (num_maximo - num_minimo + 1))
			+ num_minimo);
}

function saveCanvas() {
	quadrosave = document.getElementById("quadrosave");
	quadrosave.setAttribute("width", widthCanvas.toString());
	quadrosave.setAttribute("height", heightCanvas.toString());
	quadrosave.addEventListener('click', onCanvasClick, false);
	ctxbkp = quadrosave.getContext("2d");
	ctxbkp.drawImage(quadro, 0, 0);
}

function restoreCanvas() {
	q = document.getElementById("quadro");
	q.setAttribute("width", widthCanvas.toString());
	q.setAttribute("height", heightCanvas.toString());
	q.addEventListener('click', onCanvasClick, false);
	q.getContext("2d").drawImage(quadrosave, 0, 0);
}

//===============================> validadores
$(function(){
		$(".icampo").blur(function(event) {
			var valor = event.currentTarget.value; 
			if(isNaN(valor) || valor < 0){
				event.currentTarget.value = "";
			}
		});
});

function validaCW(){
	if(depositoAdicionado && listaDePontos.length > 1 && pointList.length > 1){
		return true;
	}
	return false;
}

//function validaGeneticos(){
//	if(listaDePontos.length > 1 && pointList.length > 1){
//		return true;
//	}
//	return false;
//}
//
//function validaCamposGeneticos(){
//
//}



