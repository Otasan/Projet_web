<%-- 
    Document   : Login
    Created on : 5 avr. 2019, 09:17:33
    Author     : roxan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="fr">
    <head>
      <meta charset="utf-8">
      <title>Identification</title>
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
      <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <div class="row justify-content-center">
            <form class="col-xs-12 col-lg-6 text-center border border-light p-5" action="<c:url value="LoginController"/>" method="POST">

                <div class="h4">Identification</div>

                <!-- Email -->
                <input type="email" id="defaultLoginFormEmail" class="form-control mb-4" placeholder="Nom d'utilisateur" name="email">

                <!-- Password -->
                <input type="password" id="defaultLoginFormPassword" class="form-control mb-4" placeholder="Mot de passe" name="mdp">

                <!-- Sign in button -->
                <button class="btn btn-info btn-block my-4" type="submit" name="action" value="login">S'identifier</button>

            </form>
        </div>
    </body>
</html>