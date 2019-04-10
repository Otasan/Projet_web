/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import JDBC.DAO;
import JDBC.DAOException;
import JDBC.DataSourceFactory;
import Listener.UtilisateursConnectes;
import java.io.IOException;
import java.util.List;
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
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

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
        if(request.getParameter("action") == null){
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
        else{
            String action = request.getParameter("action");
            switch(action){
                case "login":
                    checkLogin(request, response);
                    break;
                case "logout":
                    checkLogout(request, response);
                    break;
            }
        }
    }
    
    protected void checkLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String mdp = request.getParameter("mdp");
        String jspView="";
        if(email!=null&&mdp!=null){
            if(email.equals("admin@admin.ad") && mdp.equals("1234")){
                jspView="Connecte.jsp"; //TODO: Créer la page admin
            }
            else{
                try{
                    DAO dao = new DAO(DataSourceFactory.getDataSource());
                    String predictedMdp = ""+dao.identification(email);
                    if(mdp.equals(predictedMdp)){
                        jspView="UserOrders.jsp";
                        request.setAttribute("Customer", mdp);
                        ajouterCommandes(mdp, request, dao);
                        HttpSession session = request.getSession(true); // démarre la session
			session.setAttribute("email", email);
                    }
                    else{
                        jspView="Login.jsp";
                    }
                }
                catch(DAOException ex){
                    System.out.println(ex);
                }
            }
        }
        request.getRequestDispatcher(jspView).forward(request, response);
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


    protected void checkLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        String jspView="Login.jsp";
        request.getRequestDispatcher(jspView).forward(request, response);
    }
    
    protected void ajouterCommandes(String mdp, HttpServletRequest request, DAO dao) throws DAOException{
        List mesCommandes = dao.getPurchaseOrderByClient(Integer.parseInt(mdp));
        request.setAttribute("orders", mesCommandes);
    }
}

