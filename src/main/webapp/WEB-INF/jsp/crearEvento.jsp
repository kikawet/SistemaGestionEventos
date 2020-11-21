<%@page
	import="equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.TipoEvento"%>
<%@page
	import="equipo3.ujaen.backend.sistemagestioneventos.dtos.EventoDTO.CategoriaEvento"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>



<html>
<head>
<%@include file="/WEB-INF/jspf/enlaces.jspf"%>

<title>Crear Evento</title>
</head>

<body>
	<%@include file="../jspf/cabecera.jspf"%>

	<h2 class='m-5'>Crear nuevo evento</h2>
	<div class='container pb-5 w-50'>
		<form:form  action="crea" method="POST" modelAttribute="evento">
			<div class='form-group'>
				<label for='TextBox'>Nombre del Evento</label>
				<s:bind path="titulo">
					<form:input path="titulo" type="text" id="inputTitulo"
						class="form-control ${status.error ?'is-invalid': empty status.value ? '' :'is-valid'}"
						placeholder='' aria-describedby='shortDesc' required="required" />
					<small id='subEvento' class='form-text text-muted '>Nombre
						con el que se asocia el evento</small>
					<form:errors path="titulo" cssClass="invalid-feedback" />
				</s:bind>
			</div>

			<div>
				<label for='TextBox'>Descripción del evento</label>
				<s:bind path="descripcion">
					<form:input path="descripcion" type="text" id="inputDescripcion"
						class="form-control ${status.error ?'is-invalid': empty status.value ? '' :'is-valid'}"
						placeholder='' aria-describedby='shortDesc' />
					<small id='descripcionEvento' class='form-text text-muted'></small>
					<form:errors path="descripcion" cssClass="invalid-feedback" />
				</s:bind>
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
				<label for='TextBox'>Lugar</label>
				<s:bind path="lugar">
					<form:input path="lugar" id="inputLugar" type='text'
						class="form-control ${status.error ?'is-invalid': empty status.value ? '' :'is-valid'}"
						placeholder='ej: Calle Cervantes, 4, 23700 Linares, Jaén'
						aria-describedby='shortDesc' required="required" />
					<small id='lugar' class='form-text text-muted'>Lugar donde
						se celebraría</small>
					<form:errors path="lugar" cssClass="invalid-feedback" />
				</s:bind>
			</div>

			<div class='form-group'>
				<label for='TextBox'>Fecha</label>
				<s:bind path="fecha">
					<form:input path="fecha" type="datetime-local" id="inputFecha"
						class="form-control ${status.error ?'is-invalid': empty status.value ? '' :'is-valid'}"
						placeholder='' aria-describedby='shortDesc' required="required" />
					<small id='fecha' class='form-text text-muted'></small>
					<form:errors path="fecha" cssClass="invalid-feedback" />
				</s:bind>
			</div>



			<div class='form-group'>
				<label for='TextBox'>Aforo máximo</label>
				<s:bind path="aforoMaximo">
					<form:input id="inputAforoMaximo" path="aforoMaximo" type="number"
						class="form-control  ${status.error ?'is-invalid': empty status.value ? 'is-valid' : '' }"
						placeholder='ej:50' aria-describedby='shortDesc' required="required" />
					<small id='aforo' class='form-text text-muted' ></small>
					<form:errors path="aforoMaximo" cssClass="invalid-feedback" />
				</s:bind>
			</div>

			<div class='form-group'>
				<label for='TextBox'>Fotografía</label>
				<s:bind path="foto">
					<form:input id="inputFoto" path="foto" type="text" class='form-control'
						placeholder='ej: https://www.google.com/imagen/123)'
						aria-describedby='shortDesc' />
					<small id='pieFoto' class='form-text text-muted'>Aquí tiene
						que introducir una url de una foto</small>
				</s:bind>
			</div>

			<button type="submit" class='btn btn-primary'>Crear Evento</button>
		</form:form>
	</div>


</body>

<%@include file="../jspf/footer.jspf"%>
</html>