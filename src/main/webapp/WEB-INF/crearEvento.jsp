<%@page
	import="equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.TipoEvento"%>
<%@page
	import="equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>



<html>
<head>
<%@include file="/WEB-INF/jspf/enlaces.jspf"%>

<title>Crear Evento</title>
</head>

<body>
	<%@include file="jspf/cabecera.jspf"%>

	<h2 class='m-5'>Crear nuevo evento</h2>
	<div class='container pb-5 w-50'>
		<form method="post">
			<div class='form-group'>
				<label for='TextBox'>Nombre del Evento</label> <input
					value="${evento.titulo}" name="nombreEvento" type='text'
					class="form-control ${not empty errors['titulo'] ?'is-invalid': empty errors ? '' : 'is-valid'}"
					id='TextBox' placeholder='' aria-describedby='shortDesc'> <small
					id='subEvento' class='form-text text-muted '>Nombre con el
					que se asocia el evento</small> <span class='invalid-feedback'>${errors['titulo']}</span>
			</div>

			<div>
				<label for='TextBox'>Descripción del evento</label> <input
					value="${evento.descripcion}" name='descripcionEvento' type='text'
					class="form-control ${not empty errors['descripcion'] ?'is-invalid': empty errors ? '' : 'is-valid'}"
					id='TextBox' placeholder='' aria-describedby='shortDesc'> <small
					id='descripcionEvento' class='form-text text-muted'></small> <span
					class='invalid-feedback'>${errors['descripcion']}</span>
			</div>
			<div class='form-group'>
				<label for='TextBox'>Tipo de Evento</label> <select
					name="tipoEvento" class="custom-select">
					<c:forEach items="<%=TipoEvento.values()%>" var="tipo">
						<option>${tipo}</option>
					</c:forEach>
				</select> <small id='tipoEvento' class='form-text text-muted'></small>
			</div>

			<div class='form-group'>
				<label for='TextBox'>Catalogación</label> <select
					name="categoriaEvento" class="custom-select">
					<c:forEach items="<%=CategoriaEvento.values()%>" var="categoria">
						<option>${categoria}</option>
					</c:forEach>
				</select> <small id='tipoEvento' class='form-text text-muted'></small>
			</div>

			<div class='form-group'>
				<label for='TextBox'>Lugar</label> <input value="${evento.lugar}"
					name="lugar" type='text'
					class="form-control ${not empty errors['lugar'] ?'is-invalid': empty errors ? '' : 'is-valid'}"
					id='TextBox'
					placeholder='ej: Calle Cervantes, 4, 23700 Linares, Jaén'
					aria-describedby='shortDesc'> <small id='lugar'
					class='form-text text-muted'>Lugar donde se celebraría</small> <span
					class='invalid-feedback'>${errors['lugar']}</span>
			</div>

			<div class='form-group'>
				<label for='TextBox'>Fecha</label> <input 
					value="${evento.fecha}" name="fecha" type="datetime-local" 
					class="form-control ${not empty errors['fecha'] ?'is-invalid': empty errors ? '' : 'is-valid'}" id='TextBox'
					placeholder='' aria-describedby='shortDesc' required> <small
					id='fecha' class='form-text text-muted'></small><span
					class='invalid-feedback'>${errors['fecha']}</span>
			</div>



			<div class='form-group'>
				<label for='TextBox'>Aforo máximo</label> <input
					value="${evento.aforoMaximo}" name='aforo' type="number"
					class="form-control  ${not empty errors['aforoMaximo'] ?'is-invalid': empty errors ? '' : 'is-valid'}"
					id='TextBox' placeholder='ej:50' aria-describedby='shortDesc' required>
				<small id='aforo' class='form-text text-muted'></small> <span
					class='invalid-feedback'>${errors['aforoMaximo']}</span>

			</div>

			<div class='form-group'>
				<label for='TextBox'>Fotografía</label> <input name='foto'
					type="text" class='form-control' id='TextBox'
					placeholder='ej: https://www.google.com/imagen/123)'
					aria-describedby='shortDesc'> <small id='pieFoto'
					class='form-text text-muted'>Aquí tiene que introducir una
					url de una foto</small>
			</div>

			<button type='submit' class='btn btn-primary'>Submit</button>
		</form>
	</div>


</body>

<%@include file="jspf/footer.jspf"%>
</html>