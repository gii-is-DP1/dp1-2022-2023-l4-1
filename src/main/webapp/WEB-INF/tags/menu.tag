<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</petclinic:menuItem>

				<petclinic:menuItem active="${name eq 'info'}" url="/info"
					title="Reglas">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Reglas</span>
				</petclinic:menuItem>

				<sec:authorize access="isAuthenticated()">
					<petclinic:menuItem active="${name eq 'usuarios'}" url="/users/"
						title="usuarios">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Usuarios</span>
					</petclinic:menuItem>
				</sec:authorize>

				<sec:authorize access="isAuthenticated()">
					<petclinic:menuItem active="${name eq 'find users'}" url="/users/find"
						title="Buscar Usuario">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Buscar Usuario</span>
					</petclinic:menuItem>
				</sec:authorize>


				<petclinic:menuItem active="${name eq 'partidas'}" url="/partida/partidas"
						title="Partidas">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Partidas</span>
				</petclinic:menuItem>	
				
				<sec:authorize access="hasAuthority('player')">
					<petclinic:menuItem active="${name eq 'mis partidas'}" url="/partida/misPartidas"
						title="Mis partidas">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Mis Partidas</span>
					</petclinic:menuItem>
				</sec:authorize>

				<sec:authorize access="isAuthenticated()">
					<petclinic:menuItem active="${name eq 'achievements'}" url="/statistics/achievements"
						title="Achievements" dropdown="${true}">										
							<ul class="dropdown-menu">
								<li>
									<a href="<c:url value="/statistics/achievements/" />">Achievements listing</a>		
								</li>
								<li class="divider"></li>
								<li>								
									<a href="<c:url value="/statistics/achievements/myAchievements" />">My Achievements <span class="glyphicon glyphicon-certificate" aria-hidden="true"></span></a>		

								</li>
							</ul>					
					</petclinic:menuItem>
				</sec:authorize>	

			</ul>
			
			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/users/new" />">Register</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>�
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/users/perfil" />"
													class="btn btn-primary btn-block btn-sm">Mi perfil</a>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
