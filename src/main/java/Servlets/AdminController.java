/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import JDBC.CustomerEntity;
import JDBC.DAO;
import JDBC.DAOException;
import JDBC.DataSourceFactory;
import JDBC.ManufacturerEntity;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Utilisateur
 */
@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
public class AdminController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String mail = (String) session.getAttribute("email");
        DAO dao = new DAO(DataSourceFactory.getDataSource());
        String action = (String) request.getParameter("action");
        if(mail.equalsIgnoreCase("admin@admin.ad")){
            request.setAttribute("user", "Admin");
            try{
                switch(action){
                    case "afficherAdmin":
                        affichageAdmin(request, response, dao);
                        break;
                    case "fournisseurs":
                        listeManufacturer(request, response, dao);
                        break;
                    case "clients":
                        listeClient(request, response, dao);
                        break;
                    case "updateDate":
                        updateDate(request, response, dao);
                        break; 
                }
            } catch (DAOException ex) {
                if(session != null){
                    session.invalidate();
                }
                request.getRequestDispatcher("Login.jsp").forward(request, response); 
            }
        }
        else{
            if(session != null){
                session.invalidate();
            }
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
    
    /**
     * Permet d'afficher la page administrateur sans parametres
     * @param request
     * @param response
     * @param dao
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    protected void affichageAdmin(HttpServletRequest request, HttpServletResponse response, DAO dao) throws DAOException, ServletException, IOException{
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        request.setAttribute("start-ca-article", dao.preDate);
        request.setAttribute("end-ca-article", df.format(d));
        request.setAttribute("start-zone-geo", dao.preDate);
        request.setAttribute("end-zone-geo", df.format(d));
        request.setAttribute("start-client", dao.preDate);
        request.setAttribute("end-client", df.format(d));
        request.setAttribute("user", "Admin");
        request.setAttribute("CA", dao.chiffreDaffaire());
        request.setAttribute("nbClients", dao.nbClients());
        request.setAttribute("articles", dao.typeArticle());
        request.setAttribute("nbFournisseur", dao.nbFournisseurs());
        try {
            request.setAttribute("loca", dao.caParZipPourPeriode(df.parse(dao.preDate),d));
            request.setAttribute("prod", dao.caParTypeProduitPourPeriode(df.parse(dao.preDate),d));
            request.setAttribute("cli", dao.caParClientPourPeriode(df.parse(dao.preDate),d));
        } catch (ParseException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("PageAdmin.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * affiche la page TableFournisseur.jsp pour l'utilisateur connecté
     * @param request
     * @param response
     * @param dao
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void listeManufacturer(HttpServletRequest request, HttpServletResponse response, DAO dao) throws DAOException, ServletException, IOException {
        List<ManufacturerEntity> manufaturers = dao.listeManufacturer();
        request.setAttribute("manufaturers", manufaturers);
        request.getRequestDispatcher("TableFournisseurAdmin.jsp").forward(request, response);
    }

    /**
     * Affiche la liste des clients
     * @param request
     * @param response
     * @param dao
     * @throws ServletException
     * @throws IOException
     * @throws DAOException 
     */
    private void listeClient(HttpServletRequest request, HttpServletResponse response, DAO dao) throws ServletException, IOException, DAOException {
        List<CustomerEntity> clients = dao.listCustomer();
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("TableClient.jsp").forward(request, response);
    }

    /**
     * Permet l'affichage de la page administrateur avec des paramètres
     * @param request
     * @param response
     * @param dao
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void updateDate(HttpServletRequest request, HttpServletResponse response, DAO dao) throws DAOException, ServletException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dArticle = df.parse(request.getParameter("start-ca-article"));
            Date eArticle = df.parse(request.getParameter("end-ca-article"));
            Date dZone = df.parse(request.getParameter("start-zone-geo"));
            Date eZone = df.parse(request.getParameter("end-zone-geo"));
            Date dClient = df.parse(request.getParameter("start-client"));
            Date eClient = df.parse(request.getParameter("end-client"));
            request.setAttribute("start-ca-article", df.format(dArticle));
            request.setAttribute("end-ca-article", df.format(eArticle));
            request.setAttribute("start-zone-geo", df.format(dZone));
            request.setAttribute("end-zone-geo", df.format(eZone));
            request.setAttribute("start-client", df.format(dClient));
            request.setAttribute("end-client", df.format(eClient));
            request.setAttribute("user", "Admin");
            request.setAttribute("CA", dao.chiffreDaffaire());
            request.setAttribute("nbClients", dao.nbClients());
            request.setAttribute("articles", dao.typeArticle());
            request.setAttribute("nbFournisseur", dao.nbFournisseurs());
            request.setAttribute("loca", dao.caParZipPourPeriode(dZone, eZone));
            request.setAttribute("prod", dao.caParTypeProduitPourPeriode(dArticle, eArticle));
            request.setAttribute("cli", dao.caParClientPourPeriode(dClient, eClient));
            request.getRequestDispatcher("PageAdmin.jsp").forward(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
