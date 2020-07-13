<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>



<%@ attribute name="eventos" required="true" type="java.util.List"%>

<c:forEach var="evento" items="${eventos}">
	<div class="card mb-3">
		<div class="row no-gutters">
			<div class="col-md-4">
				<img src="${evento.foto}" class="card-img" alt="foto del evento">
			</div>
			<div class="col-md-8">
				<div class="card-body">
					<div class="d-flex justify-content-between">
						<h2 class="card-title">${evento.titulo}</h2>
						<c:if test="${not empty session.usuario}">
							<c:choose>
								<c:when test="${evento.estado == 'ACEPTADO'}">
									<form action="${pageContext.request.contextPath}/evento/inscribir" method="POST">
										<input name="idEvento" type="number"
											value="${evento.idEvento}" hidden /> <input name="cancelar"
											type="checkbox" checked hidden />
										<button type="submit" class="btn btn-success"
											data-toggle="tooltip" data-placement="bottom"
											title="Cancelar inscripción">Aceptado</button>
									</form>
								</c:when>
								<c:when test="${evento.estado == 'LISTA_DE_ESPERA'}">
									<form action="${pageContext.request.contextPath}/evento/inscribir" method="POST">
										<input name="idEvento" type="number"
											value="${evento.idEvento}" hidden /> <input name="cancelar"
											type="checkbox" checked hidden />
										<button type="submit" class="btn btn-warning"
											data-toggle="tooltip" data-placement="bottom"
											title="Cancelar inscripción">Lista de espera</button>
									</form>
								</c:when>
								<c:otherwise>
									<form action="${pageContext.request.contextPath}/evento/inscribir" method="POST">
										<input name="idEvento" type="number"
											value="${evento.idEvento}" hidden /> <input name="cancelar"
											type="checkbox" hidden />
										<button type="submit" class="btn btn-danger"
											data-toggle="tooltip" data-placement="bottom"
											title="Inscribirse">No inscrito</button>
									</form>
								</c:otherwise>
							</c:choose>
						</c:if>

					</div>
					<p class="card-text">
						<small class="text-muted">Fecha <fmt:parseDate
 								value="${evento.fecha}" pattern="yyyy-MM-dd" var="parsedDate" 
								type="date" /> <fmt:formatDate value="${parsedDate}" 
								type="date" pattern="dd/MM/yyyy" /> 
						</small>
					</p>
					<p class="card-text">
						${evento.descripcion } ... <strong><a class="text-dark"
							href="#"> <u>Leer más </u>
						</a> </strong>
					</p>

				</div>
			</div>
		</div>
	</div>
</c:forEach>