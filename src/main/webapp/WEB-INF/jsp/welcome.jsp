<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<style>

    .colorletter{
    color:"white";
    }
	.background{
	background-image: url("/resources/images/dwarf.jpg");
	background-repeat:no-repeat;
	background-size: 100%;
    }
    </style>

<petclinic:layout pageName="home">
    <body class="background">
        <center><h1 style = "color: white; text-shadow: 3px 3px 6px #000000;"><FONT FACE="verdana" SIZE=7><fmt:message key="welcome"/></FONT></h1></center>
        <div class="row">
        <center><h2 style="color: white; text-shadow: 3px 3px 6px #000000;"><font face="verdana" SIZE="5">Project ${title}</font></h2></center>
        <p><h2 style="color: white; text-shadow: 3px 3px 6px #000000;"> Group ${group}</h2></p>
        <p><ul>
        <c:forEach items="${persons}" var="person">
            <li style="color: white; text-shadow: 3px 3px 6px #000000;">${person.firstName}&nbsp;${person.lastName}</li>
        </c:forEach>
        </ul></p>
        </div>
        <div class="row">
            <div class="col-md-12">
                <spring:url value="/resources/images/pets.png" htmlEscape="true" var="petsImage"/>
                <spring:url value="/resources/images/dwarf.jpg" htmlEscape="true" var="dwarfImage"/>
                <spring:url value="/resources/images/DwarfGif.gif" htmlEscape="true" var="dwarfGif"/>
                <marquee><img class="img-responsive" src="${dwarfGif}"/></marquee>

                <petclinic:menuItem active="${name eq 'partida'}" url="/partida/new/"
					title="partida">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Crear partida</span>
				</petclinic:menuItem>	

            </div>
        </div>
        </body>
</petclinic:layout>