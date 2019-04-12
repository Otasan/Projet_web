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
    
    protected void affichageAdmin(HttpServletRequest request, HttpServletResponse response, DAO dao) throws DAOException, ServletException, IOException{
        request.setAttribute("user", "admin");
        request.setAttribute("CA", dao.chiffreDaffaire());
        request.setAttribute("articles", dao.typeArticle());
        request.setAttribute("nbClients", dao.nbClients());
        request.setAttribute("nbFournisseur", dao.nbFournisseurs());
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

    private void listeClient(HttpServletRequest request, HttpServletResponse response, DAO dao) throws ServletException, IOException, DAOException {
        List<CustomerEntity> clients = dao.listCustomer();
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("TableClient.jsp").forward(request, response);
    }

}
