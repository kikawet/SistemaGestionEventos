<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="custom"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!doctype html>
<html lang="es">
<head>

<title>Inicio</title>

<%@include file="/WEB-INF/jspf/enlaces.jspf"%>

<script type="text/javascript" defer>
	$(function() {
		$('[data-toggle="tooltip"]').tooltip()
	})
</script>

</head>
<body class="d-flex flex-column">

	<%@include file="/WEB-INF/jspf/cabecera.jspf"%>

	<main class="container" role="main">

		<custom:flashmessage />

		<div class="container my-3 mx-auto">
			<div class="row align-items-between row-cols-2">
				<form class="form-inline my-2 col-8" action="" method="POST">
					<sec:csrfInput/>
					<input name="limpiar" hidden type="checkbox"
						${empty filtroTitulo ? '' : 'checked'}> <input
						name="buscarNombre" value="${filtroTitulo}"
						class="form-control py-2 border-right-0 border w-75" type="search"
						placeholder="buscar ..." autocomplete="off"> <span
						class="input-group-append"> <input role="button"
						class="btn btn-outline-secondary border-left-0 border"
						type="submit" value="${empty filtroTitulo ? '🔍' : '❌'}" />
					</span>
				</form>

				<div class="input-group my-2 col-4">
					<div class="my-auto mx-2">Ordenar por</div>
					<select class="custom-select" id="inputGroupSelect01">
						<option selected>Choose...</option>
						<option value="1">One</option>
						<option value="2">Two</option>
						<option value="3">Three</option>
					</select>
				</div>
			</div>
		</div>

		<div class="container my-3 mx-auto">
			<custom:listaEventos eventos="${eventos}" />
		</div>
	</main>
	<%@include file="../jspf/footer.jspf"%>



	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
		integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
		integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
		integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
		crossorigin="anonymous"></script>
</body>
</html>