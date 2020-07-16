<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
	integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
	integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
	integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
	crossorigin="anonymous"></script>

<style type="text/css">
html, body {
	height: 100%;
}

main {
	flex: 1 0 auto;
}

#sticky-footer {
	position: relative;
	bottom: 0;
	width: 100%;
}

#logo {
	max-height: 5em;
	max-width: 5em;
}
</style>

<%@ taglib tagdir="/WEB-INF/tags" prefix="custom"%>

</head>
<body>
	<%@include file="/WEB-INF/jspf/cabecera.jspf"%>

	<div class="container-fuild">
		<div class="panel">
			<div class="bg-secondary text-white text-center "
				style="height: 100px">
				<h1 style="position: relative; top: 25px">Perfil de usuario</h1>
			</div>
			<br>
			<div class="panel-body">

				<div class="col">
					<div class="container text-center">
						<h2>${usuario.login}</h2>
					</div>
					<hr>
					<p align="center"><font size="5"><b>Email: </b></font>
						<span class="glyphicon glyphicon-envelope one"
							style="width: 50px; font-size: 22px">${usuario.email}</span>
					</p>
					<hr>
					<p align="center"><font size="5"><b>Eventos creados: </b></font>
						<span class="glyphicon glyphicon-envelope one"
							style="width: 50px; font-size: 22px">${creados.size()}</span>
							
						<custom:listaEventos eventos="${creados}"/>
					</p>
					<hr>
					<p align="center"><font size="5"><b>Eventos inscritos: </b></font>
						<span class="glyphicon glyphicon-envelope one"
							style="width: 50px; font-size: 22px">${inscritos.size()}</span>
							<custom:listaEventos eventos="${inscritos}"/>
					</p>
					<hr>
				</div>
			</div>
		</div>
	</div>

	<%@include file="/WEB-INF/jspf/footer.jspf"%>
</body>
</html>