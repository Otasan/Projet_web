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
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" type="image/x-icon" href="https://geomatiqueagricole.ca/wp-content/uploads/2018/02/logo-ordinateur.png" />
        <title>Page d'accueil Client</title>


        <!-- Custom fonts for this template-->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

        <!-- Custom styles for this template-->
        <link href="css/sb-admin-2.min.css" rel="stylesheet">

    </head>

    <body id="page-top">

        <!-- Page Wrapper -->
        <div id="wrapper">

            <!-- Sidebar -->
            <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

                <!-- Sidebar - Brand -->
                <a class="sidebar-brand d-flex align-items-center justify-content-center" href="#">
                        <i class="far fa-compass"></i>
                    <div class="sidebar-brand-text mx-3">Accueil</div>
                </a>

                <!-- Divider -->
                <hr class="sidebar-divider my-0">

                <li class="nav-item active">
                    <!--Retour vers commandes-->
                    <form id="form-commande" action="<c:url value="CommandeController"/>" method="POST">
                        <button type="submit" name="action" value="afficher" class="btn btn-primary btn-lg" >
                            <i class="fas fa-fw fa-tachometer-alt"></i>
                            Profil
                        </button>
                    </form>
                </li>

                <!-- Divider -->
                <hr class="sidebar-divider">

                <!-- Nav Item - Tables -->
                <li class="nav-item">
                    <!--liste des fournisseurs-->
                    <form id="form-commande" action="<c:url value="CommandeController"/>" method="POST">
                        <button type="submit" name="action" value="fournisseurs" class="btn btn-primary btn-lg" >
                            <i class="fas fa-fw fa-table"></i>
                            Liste des fournisseurs
                        </button>
                    </form>
                </li>

                <li class="nav-item">
                    <!--Liste produits-->
                    <form id="form-commande" action="<c:url value="CommandeController"/>" method="POST">
                        <button type="submit" name="action" value="products" class="btn btn-primary btn-lg" >
                            <i class="fas fa-fw fa-table"></i>
                            Liste des produits
                        </button>
                    </form>
                </li>

                <li class="nav-item">
                    <!--Nouvelle commande-->
                    <form id="form-commande" action="<c:url value="CommandeController"/>" method="POST">
                        <button type="submit" name="action" value="new" class="btn btn-primary btn-lg" >
                            <i class="fas fa-plus-circle"></i>
                            Nouvelle commande
                        </button>
                    </form>
                </li>




                <!-- Divider -->
                <hr class="sidebar-divider d-none d-md-block">

                <!-- Sidebar Toggler (Sidebar) -->
                <div class="text-center d-none d-md-inline">
                    <button class="rounded-circle border-0" id="sidebarToggle"></button>
                </div>

            </ul>
            <!-- End of Sidebar -->

            <!-- Content Wrapper -->
            <div id="content-wrapper" class="d-flex flex-column">

                <!-- Main Content -->
                <div id="content">

                    <!-- Topbar -->
                    <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                        <!-- Sidebar Toggle (Topbar) -->
                        <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                            <i class="fa fa-bars"></i>
                        </button>


                        <!-- Nav Item - Search Dropdown (Visible Only XS) -->
                        <li class="nav-item dropdown no-arrow d-sm-none">
                            <a class="nav-link dropdown-toggle" href="#" id="searchDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-search fa-fw"></i>
                            </a>
                            <!-- Dropdown - Messages -->
                            <div class="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in" aria-labelledby="searchDropdown">
                                <form class="form-inline mr-auto w-100 navbar-search">
                                    <div class="input-group">
                                        <input type="text" class="form-control bg-light border-0 small" placeholder="Search for..." aria-label="Search" aria-describedby="basic-addon2">
                                        <div class="input-group-append">
                                            <button class="btn btn-primary" type="button">
                                                <i class="fas fa-search fa-sm"></i>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </li>

                        <!-- Nav Item - User Information -->
                        <li class="nav-item dropdown no-arrow">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="mr-2 d-none d-lg-inline text-gray-600 small"><%=(String) request.getAttribute("user")%></span>
                                <img class="img-profile rounded-circle" src="data:image/png;base64,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">
                            </a>
                            <!-- Dropdown - User Information -->
                            <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="userDropdown">
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                    <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Déconnexion
                                </a>
                            </div>
                        </li>

                        </ul>

                    </nav>
                    <!-- End of Topbar -->

                    <!-- Begin Page Content -->
                    <div class="container-fluid">

                        <!-- Page Heading -->
                        <div class="d-sm-flex align-items-center justify-content-between mb-4">
                            <h1 class="h3 mb-0 text-gray-800">Bienvenue à la page Client !</h1>
                        </div>

                        <p class="mb-4">Vous trouverez vos commandes ci-dessous. Pour ajouter une commande, il suffit de cliquer sur le bouton à gauche prévu à cet effet. <a target="_blank" href="https://datatables.net"></a>.</p>

                        <!-- DataTales Example -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Commande en cours</h6>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                        <thead>
                                            <tr>
                                                <th>Identifiant Commande</th>
                                                <th>Article</th>
                                                <th>Quantité</th>
                                                <th>Prix TTC</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <!--Ici code J S P-->
                                            <%
                                                List<PurchaseOrder> purchase = (List) request.getAttribute("orders");
                                                for (PurchaseOrder p : purchase) { //debut de la boucle
                                                    int num = p.getOrderNum();
                                                    String pro = p.getProduct();
                                                    int qte = p.getQuantity();
                                                    double price = p.getTotalPrice();
                                            %>
                                            <!--affichage des données séléctionnées-->
                                            <tr>
                                                <th><%=num%></th>
                                                <td><%=pro%></td>
                                                <td><%=qte%></td>
                                                <td><%=price%></td>
                                                <td>
                                                    <form id="form-commande" action="<c:url value="CommandeController"/>" method="POST">
                                                        <table>
                                                            <tr>
                                                                <td>
                                                                    <input type="hidden" value="<%=num%>" name="num">
                                                                    <button type="submit" name="action" value="modifier" class="btn btn-secondary">Modifier la commande</button>
                                                                </td>
                                                                <td>
                                                                    <button type="submit" name="action" value="supprimer" class="btn btn-secondary">Supprimer la commande</button>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </form>
                                                </td>
                                            </tr>
                                            <%}%>
                                        </tbody>
                                    </table>

                                </div>
                            </div>
                        </div>

                    </div>



                    <!-- End of Main Content -->

                    <!-- Footer -->
                    <footer class="sticky-footer bg-white">
                        <div class="container my-auto">
                            <div class="copyright text-center my-auto">
                                <span>Copyright &copy; Abdo, Numa, Rezig</span>
                            </div>
                        </div>
                    </footer>
                    <!-- End of Footer -->

                </div>
                <!-- End of Content Wrapper -->

            </div>
            <!-- End of Page Wrapper -->

            <!-- Scroll to Top Button-->
            <a class="scroll-to-top rounded" href="#page-top">
                <i class="fas fa-angle-up"></i>
            </a>

            <!-- Logout Modal-->
            <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Prêt à partir ?</h5>
                            <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                        <div class="modal-body">Sélectionnez "Déconnexion" si vous souhaitez fermer la session active.</div>
                        <div class="modal-footer">
                            <button class="btn btn-secondary" type="button" data-dismiss="modal">Annuler</button>
                            <form action="<c:url value="LoginController"/>"  method="POST">
                                <button class="btn btn-secondary bg-primary" type="submit" name="action" value="logout">Déconnexion</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Bootstrap core JavaScript-->
            <script src="vendor/jquery/jquery.min.js"></script>
            <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

            <!-- Core plugin JavaScript-->
            <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

            <!-- Custom scripts for all pages-->
            <script src="js/sb-admin-2.min.js"></script>

    </body>

</html>