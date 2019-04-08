<%-- 
    Document   : ChangeOrder
    Created on : 8 avr. 2019, 09:04:48
    Author     : Utilisateur
--%>
<%@page import="java.util.List"%>
<%@page import="JDBC.PurchaseOrder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <form action="<c:url value="CommandeController"/>" method="POST">
            <table>
                <tr>
                    <th scope="col">Identifiant Commande</th>
                    <th scope="col">Article</th>
                    <th scope="col">Quantité</th>
                    <th scope="col">Prix TTC</th>
                </tr>
                <tr>
                    <td>
                        <%
                            int id = (Integer)request.getAttribute("id");
                            String valAffiche="";
                            if(id>0){
                                valAffiche=""+id;
                            }
                            else{
                                valAffiche="Nouvelle Commande";
                            }
                        %>
                        <%=valAffiche%>
                        <input type="hidden" name="id" value="<%=id%>"/>
                    </td>
                    <td>
                        <select name="produit">
                            <%
                                List<String> produits = (List)request.getAttribute("products");
                                String sel = "";
                                String selectedProd = (String)request.getAttribute("selected");
                                for(String p:produits){
                                    if(selectedProd.equals(p)){
                                        sel="selected";
                                    }
                                    else{
                                        sel="";
                                    }
                            %>
                            <option value="<%=p%>" <%=sel%>><%=p%></option>
                            <%}%>
                        </select>
                    </td>
                    <td>
                        <%
                            int val = (Integer)request.getAttribute("quantite");
                        %>
                        <input type="number" name="quantite" min="1" value="<%=val%>"/>
                    </td>
                    <td>
                        <!--Prix calculé à partir de la commande-->
                    </td>
                    <td>
                        <button type="submit" name="action" value="valider">Valider la commande</button>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
