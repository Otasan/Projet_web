<%-- 
    Document   : PageAdmin
    Created on : 11 avr. 2019, 14:56:08
    Author     : Utilisateur
--%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" type="image/x-icon" href="https://geomatiqueagricole.ca/wp-content/uploads/2018/02/logo-ordinateur.png" />
        <title>Page d'accueil Admin</title>


        <!-- Custom fonts for this template-->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

        <!-- Custom styles for this template-->
        <link href="css/sb-admin-2.min.css" rel="stylesheet">

        <!--script carte-->
        <script src="vendor/jquery/jquery.min.js"></script>
        <script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>
        <script type="text/javascript" src="js/graphs.js"></script>

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
                    <!--retour au Dashboard-->
                    <form id="form-commande" action="<c:url value="AdminController"/>" method="POST">
                        <button type="submit" name="action" value="afficherAdmin" class="btn btn-primary btn-lg" >
                            <i class="fas fa-fw fa-tachometer-alt"></i>
                            Dashboard
                        </button>
                    </form>
                </li>


                <!-- Divider -->
                <hr class="sidebar-divider">

                <!-- Nav Item - Tables -->
                <li class="nav-item">
                    <!--liste des clients-->
                    <form id="form-commande" action="<c:url value="AdminController"/>" method="POST">
                        <button type="submit" name="action" value="clients" class="btn btn-primary btn-lg" >
                            <i class="fas fa-fw fa-table"></i>
                            Liste des Clients
                        </button>
                    </form>
                </li>
                <!-- Nav Item - Tables -->
                <li class="nav-item">
                    <!--liste des fournisseurs-->
                    <form id="form-commande" action="<c:url value="AdminController"/>" method="POST">
                        <button type="submit" name="action" value="fournisseurs" class="btn btn-primary btn-lg" >
                            <i class="fas fa-fw fa-table"></i>
                            Liste des fournisseurs
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
                                <img class="img-profile rounded-circle" src="https://page42.org/wp-content/uploads/2017/03/robot-icon-30508.jpg">
                            </a>
                            <!-- Dropdown - User Information -->
                            <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="userDropdown">

                                <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>

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
                            <h1 class="h3 mb-0 text-gray-800">Bienvenue à la page Admin !</h1>
                        </div>

                        <div class="row">

                            <!-- Earnings (Monthly) Card Example -->
                            <div class="col-xl-3 col-md-6 mb-4">
                                <div class="card border-left-primary shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">Chiffre d'affaires</div>
                                                <div class="h5 mb-0 font-weight-bold text-gray-800"><%= request.getAttribute("CA")%>€</div>
                                                <input type="hidden" value="<%= request.getAttribute("CA")%>" id="chiffreAffaire"/>
                                            </div>
                                            <div class="col-auto">
                                                <i class="fas fa-euro-sign fa-2x text-gray-300"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Earnings (Monthly) Card Example -->
                            <div class="col-xl-3 col-md-6 mb-4">
                                <div class="card border-left-success shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-success text-uppercase mb-1">Nombre de Fournisseurs</div>
                                                <div class="h5 mb-0 font-weight-bold text-gray-800"><%= request.getAttribute("nbFournisseur")%></div>
                                            </div>
                                            <div class="col-auto">
                                                <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>


                            <!-- Pending Requests Card Example -->
                            <div class="col-xl-3 col-md-6 mb-4">
                                <div class="card border-left-warning shadow h-100 py-2">
                                    <div class="card-body">
                                        <div class="row no-gutters align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">Nombre de clients</div>
                                                <div class="h5 mb-0 font-weight-bold text-gray-800"><%= request.getAttribute("nbClients")%></div>
                                            </div>
                                            <div class="col-auto">
                                                <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <form action="<c:url value="AdminController"/>" method="POST">
                            <div class="row card shadow mb-4">
                                <!-- Card Header - Accordion -->
                                <!-- CA/article-->
                                <a href="#collapseCardCAArticle" class="d-block card-header py-3" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="collapseCardExample">
                                    <h6 class="m-0 font-weight-bold text-primary">Chiffres d'affaire par catégorie d'article, en choisissant la période</h6>
                                </a>
                                <!-- Card Content - Collapse -->
                                <div class="collapse show" class="card-body" id="collapseCardCAArticle">
                                    <ul>
                                        <li>
                                            <label for="start">Entrer la date de début de période:</label>
                                            <input type="date" id="start-ca-article" name="start-ca-article" value="<%=(String) request.getAttribute("start-ca-article")%>"/>
                                        </li>
                                        <li>
                                            <label for="start">Entrer la date de fin de période:</label>
                                            <input type="date" id="end-ca-article" name="end-ca-article" value="<%=(String) request.getAttribute("end-ca-article")%>"/>
                                        </li>
                                        <li>
                                            <button type="submit" name="action" value="updateDate">Valider</button>
                                        </li>
                                    </ul>
                                    <div class="dropdown" id='input-pie'>
                                        <%
                                            Map<String, Float> prod = (Map) request.getAttribute("prod");
                                            Set<String> types = prod.keySet();
                                            for (String t : types) {
                                        %>
                                        <input type="hidden" class="input-pie" id="<%=t%>" value="<%=prod.get(t)%>"/>
                                        <%}%>
                                        <div id="pie_affichage" ></div>
                                    </div>
                                </div>
                                <a href="#collapseCardZoneGeo" class="d-block card-header py-3" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="collapseCardExample">
                                    <h6 class="m-0 font-weight-bold text-primary">Chiffres d'affaire par zone géographique, en choisissant la période</h6>
                                </a>
                                <!-- Card Content - Collapse -->
                                <div class="collapse show" class="card-body" id="collapseCardZoneGeo">
                                    <ul>
                                        <li>
                                            <label for="start">Entrer la date de début de période:</label>
                                            <input type="date" id="start-zone-geo" name="start-zone-geo" value="<%=(String) request.getAttribute("start-zone-geo")%>">
                                        </li>
                                        <li>
                                            <label for="start">Entrer la date de fin de période:</label>
                                            <input type="date" id="end-zone-geo" name="end-zone-geo" value="<%=(String) request.getAttribute("end-zone-geo")%>">
                                        </li>
                                        <li>
                                            <button type="submit" name="action" value="updateDate">Valider</button>
                                        </li>
                                    </ul>
                                    <div class="dropdown" id="input-carte">
                                        <%
                                            Map<Integer, Float> zip = (Map) request.getAttribute("loca");
                                            Set<Integer> d = zip.keySet();
                                            for (int z : d) {
                                        %>
                                        <input type="hidden" class="input-carte" id="<%=z%>" value="<%=zip.get(z)%>"/>
                                        <%}%>
                                        <div id="carte_affichage" ></div>
                                    </div>
                                </div>
                                <a href="#collapseCardTemps" class="d-block card-header py-3" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="collapseCardExample">
                                    <h6 class="m-0 font-weight-bold text-primary">Chiffres d'affaire par client, en choisissant la période</h6>
                                </a>
                                <!-- Card Content - Collapse -->
                                <div class="collapse show" class="card-body" id="collapseCardTemps">
                                    <ul>
                                        <li>
                                            <label for="start">Entrer la date de début de période:</label>
                                            <input type="date" id="start-client" name="start-client" value="<%=(String) request.getAttribute("start-client")%>">
                                        </li>
                                        <li>
                                            <label for="start">Entrer la date de fin de période:</label>
                                            <input type="date" id="end-client" name="end-client" value="<%=(String) request.getAttribute("end-client")%>">
                                        </li>
                                        <li>
                                            <button type="submit" name="action" value="updateDate">Valider</button>
                                        </li>
                                    </ul>
                                    <div class="dropdown" id="input-bar">
                                        <%
                                            Map<String, Float> client = (Map) request.getAttribute("cli");
                                            Set<String> cu = client.keySet();
                                            for (String c : cu) {
                                        %>
                                        <input type="hidden" class="input-bar" id="<%=c%>" value="<%=client.get(c)%>"/>
                                        <%}%>
                                        <div id="bar_affichage"></div>
                                    </div>
                                </div>


                            </div>
                        </form>


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
                </div>
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
