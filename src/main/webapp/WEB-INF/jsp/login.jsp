<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="custom"%>
<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}" />

<!doctype html>
<html lang="es">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<base
	href="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}/" />
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">

<title>LOGIN</title>

<style type="text/css">
:root { -
	-input-padding-x: 1.5rem; -
	-input-padding-y: .75rem;
}

body {
	background: #343a40;
	background: linear-gradient(to right, #343a40, #6c757d);
}

.colorBoton {
	background: #dc3545;
	color: white;
	font-style: blond;
}

.form-control:focus {
	border-color: #dc3545;
	box-shadow: 0 1px 1px #dc3545;
	outline: 0 none;
}

.card-signin {
	border: 0;
	border-radius: 1rem;
	box-shadow: 0 0.5rem 1rem 0 black;
}

.card-signin .card-title {
	margin-bottom: 2rem;
	font-weight: 300;
	font-size: 1.5rem;
}

.card-signin .card-body {
	padding: 2rem;
}

.form-signin {
	width: 100%;
}

.form-signin .btn {
	font-size: 80%;
	border-radius: 5rem;
	letter-spacing: .1rem;
	font-weight: bold;
	padding: 1rem;
	transition: all 0.2s;
}

.form-label-group {
	position: relative;
	margin-bottom: 1rem;
}

.form-label-group input {
	height: auto;
	border-radius: 2rem;
}

.form-label-group>input, .form-label-group>label {
	padding: var(- -input-padding-y) var(- -input-padding-x);
}

.form-label-group>label {
	position: absolute;
	top: 0;
	left: 0;
	display: block;
	width: 100%;
	margin-bottom: 0;
	/* Override default `<label>` margin */
	line-height: 1.5;
	color: #495057;
	border: 1px solid transparent;
	border-radius: .25rem;
	transition: all .1s ease-in-out;
}

.form-label-group input::-webkit-input-placeholder {
	color: #343a40;
}

.form-label-group input:-ms-input-placeholder {
	color: #343a40;
}

.form-label-group input::-ms-input-placeholder {
	color: #343a40;
}

.form-label-group input::-moz-placeholder {
	color: #343a40;
}

.form-label-group input::placeholder {
	color: #343a40;
}

.form-label-group




 




input








:not




 




(
:placeholder-shown




 




)
{
padding-top








:




 




calc








(
var






(-
-input-padding-y








)
+
var








(-
-input-padding-y








)
*




 




(2/3));
padding-bottom








:




 




calc








(
var






(-
-input-padding-y








)/3);
}
.form-label-group input:not (:placeholder-shown )~label {
	padding-top: calc(var(- -input-padding-y)/3);
	padding-bottom: calc(var(- -input-padding-y)/3);
	font-size: 12px;
	color: black;
}

/* Fallback for Edge
-------------------------------------------------- */
@
supports (-ms-ime-align: auto ) { .form-label-group>label {
	display: none;
}

.form-label-group input::-ms-input-placeholder {
	color: black;
}

}

/* Fallback for IE
-------------------------------------------------- */
@media all and (-ms-high-contrast: none) , ( -ms-high-contrast : active)
	{
	.form-label-group>label {
		display: none;
	}
	.form-label-group input:-ms-input-placeholder {
		color: #black;
	}
}
</style>


</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
				<div class="card card-signin my-5">
					<div class="card-body">
						<custom:return/>
						<h5 class="card-title text-center">WebApp</h5>
						<form class="form-signin" action="usuario/login" method="POST">
							<div class="form-label-group">
								<input name="login" type="text" value="${login}" id="inputLogin"
									class="form-control " placeholder="Login" required autofocus>
							</div>
							<div class="form-label-group">
								<input name="password" type="password" value="${password}"
									id="inputPassword" class="form-control "
									placeholder="Contraseña" required>

							</div>

							<div class="custom-control custom-checkbox mb-3">
								<input type="checkbox" name="checkbox"
									class="custom-control-input" id="customCheck1"> <label
									class="custom-control-label" for="customCheck1">Recordar
									Contraseña</label>
							</div>
							<button class="btn btn-lg btn-block colorBoton text-uppercase"
								type="submit">Iniciar Sesión</button>
							<hr class="my-4">
						</form>
						<c:if test="${not empty error}">
							<section class="alert alert-danger my-auto" role="alert">${error}</section>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>