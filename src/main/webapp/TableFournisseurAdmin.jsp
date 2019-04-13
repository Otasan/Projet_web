<%-- 
    Document   : TableFournisseurAdmin
    Created on : 12 avr. 2019, 11:49:11
    Author     : Utilisateur
--%>

<%@page import="JDBC.ManufacturerEntity"%>
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
        <title>Page fournisseurs admin</title>


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
                    <div class="sidebar-brand-icon rotate-n-15">
                        <i class="fas fa-laugh-wink"></i>
                    </div>
                    <div class="sidebar-brand-text mx-3">Accueil</div>
                </a>

                <!-- Divider -->
                <hr class="sidebar-divider my-0">


                <li class="nav-item active">
                    <!--retour au Dashboard-->
                    <form id="form-commande" action="<c:url value="AdminController"/>" method="POST">
                        <button type="submit" name="action" value="afficherAdmin" class="nav-link bg-primary" >
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
                        <button type="submit" name="action" value="clients" class="nav-link bg-primary" >
                            <i class="fas fa-fw fa-table"></i>
                            Liste des Clients
                        </button>
                    </form>
                </li>
                <!-- Nav Item - Tables -->
                <li class="nav-item">
                    <!--liste des fournisseurs-->
                    <form id="form-commande" action="<c:url value="AdminController"/>" method="POST">
                        <button type="submit" name="action" value="fournisseurs" class="nav-link bg-primary" >
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
                                <span class="mr-2 d-none d-lg-inline text-gray-600 small">Admin</span>
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
                            <h1 class="h3 mb-0 text-gray-800">Fournisseurs</h1>
                        </div>

                        <p class="mb-4">Vous trouverez ci-dessous nos fournisseurs.<a target="_blank" href="https://datatables.net"></a></p>

                        <!-- DataTales Example -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Liste des fournisseurs</h6>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                        <thead>
                                            <tr>
                                                <th>Identifiant Fournisseur</th>
                                                <th>Nom</th>
                                                <th>Ville</th>
                                                <th>Etat</th>
                                                <th>E-mail</th>
                                                <th>Téléphone</th>
                                                <th>Fax</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                List<ManufacturerEntity> fournisseurs = (List)request.getAttribute("manufaturers");
                                                for(ManufacturerEntity m:fournisseurs){
                                                    int id = m.getID();
                                                    String name = m.getName();
                                                    String city = m.getCity();
                                                    String fax = m.getFax();
                                                    String mail = m.getMail();
                                                    String phone = m.getPhone();
                                                    String state = m.getState();
                                            %>
                                            <tr>
                                                <th><%=id%></th>
                                                <td><%=name%></td>
                                                <td><%=city%></td>
                                                <td><%=state%></td>
                                                <td><%=mail%></td>
                                                <td><%=phone%></td>
                                                <td><%=fax%></td>
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
                            <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
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
