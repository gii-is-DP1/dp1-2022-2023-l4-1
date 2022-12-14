<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<petclinic:layout pageName="jugadores">
    <h2>
        <c:if test="${jugador['new']}">New </c:if> Jugador
    </h2>
    <form:form modelAttribute="jugador" class="form-horizontal" id="add-jugador-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="First Name" name="user.nombre"/>
            <petclinic:inputField label="Last Name" name="user.apellidos"/>
            <petclinic:inputField label="Username" name="user.username"/>
            <petclinic:inputField label="Password" name="user.password"/>
            <petclinic:inputField label="Email" name="user.email"/>
            <petclinic:inputField label="Extra" name="extra"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${jugador['new']}">
                        <button class="btn btn-default" type="submit">Add Jugador</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Jugador</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>

