<%-- 
    Document   : UserOrders
    Created on : 5 avr. 2019, 10:07:45
    Author     : roxan
--%>

<%@page import="java.util.List"%>
<%@page import="JDBC.PurchaseOrder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="utf-8">
        <title>Commandes utilisateur</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <table class="table table-striped table-dark">
            <thead>
                <tr>
                    <th scope="col">Identifiant Commande</th>
                    <th scope="col">Article</th>
                    <th scope="col">Quantité</th>
                    <th scope="col">Prix TTC</th>
                    <th scope="col">
                        <form action="<c:url value="CommandeController"/>" method="POST">
                            <button type="submit" name="action" value="new" class="btn btn-secondary">Nouvelle commande</button>
                        </form>
                    </th>
                </tr>
            </thead>
        <!--Ici code JSP-->
            <tbody>
                <% 
                    List<PurchaseOrder> purchase = (List) request.getAttribute("orders");
                    for(PurchaseOrder p: purchase){ //debut de la boucle
                        int num =p.getOrderNum();
                        String pro =p.getProduct();
                        int qte = p.getQuantity();
                        double price = p.getTotalPrice();
                %>
                <tr>
                    <!--affichage des données séléctionnées-->
                    <th><%=num%></th>
                    <td><%=pro%></td>
                    <td><%=qte%></td>
                    <td><%=price%></td>
                    <td>
                        <form action="<c:url value="CommandeController"/>" method="POST">
                            <input type="hidden" value="<%=num%>" name="num">
                            <button type="submit" name="action" value="modifier" class="btn btn-secondary">Modifier la commande</button>
                            <button type="submit" name="action" value="supprimer" class="btn btn-secondary">Supprimer la commande</button>
                        </form>
                    </td>
                </tr>
                <% } //fin de la boucle %>
            </tbody>
        </table>
    </body>
</html>
