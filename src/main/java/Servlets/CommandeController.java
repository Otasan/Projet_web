/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import JDBC.DAO;
import JDBC.DAOException;
import JDBC.DataSourceFactory;
import JDBC.Prix;
import JDBC.PurchaseOrder;
import java.io.IOException;
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
@WebServlet(name = "CommandeController", urlPatterns = {"/CommandeController"})
public class CommandeController extends HttpServlet {

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
        HttpSession session = request.getSession();
        String mail = (String) session.getAttribute("email");
        DAO dao = new DAO(DataSourceFactory.getDataSource());
        try {
            int id = dao.identification(mail);

            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "new":
                        nouvelleCommande(request, response, dao, id);
                        break;
                    case "valider":
                        validerCommande(request, response, dao, id);
                        break;
                    case "updatePrix":
                        Prix p = calculPrix(request, response, id, dao);
                        updatePrix(request, response, dao, p);
                        break;
                    case "supprimer":
                        int num = Integer.parseInt(request.getParameter("num"));
                        dao.supprimerCommande(num);
                        displayCommande(request, response, dao, id);
                        break;
                    case "modifier":
                        int nume = Integer.parseInt(request.getParameter("num"));
                        updateCommande(request, response, dao, nume, id);
                        break;
                }
            }
        } catch (DAOException ex) {
            Logger.getLogger(CommandeController.class.getName()).log(Level.SEVERE, null, ex);
            session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }

    protected void nouvelleCommande(HttpServletRequest request, HttpServletResponse response, DAO dao, int idClient) throws ServletException, IOException, DAOException {
        List<String> products = dao.listeProduits();
        request.setAttribute("products", products);
        request.setAttribute("selected", products.get(0));
        Prix p = dao.getPrix(1, products.get(0), idClient);
        request.setAttribute("prix", p.total());
        request.setAttribute("id", -1);
        request.setAttribute("quantite", 1);
        request.getRequestDispatcher("ChangeOrder.jsp").forward(request, response);
    }
    
    protected void updateCommande(HttpServletRequest request, HttpServletResponse response, DAO dao, int num, int idClient) throws ServletException, IOException, DAOException {
        List<String> products = dao.listeProduits();
        PurchaseOrder p = dao.getPurchaseOrder(num);
        request.setAttribute("products", products);
        request.setAttribute("selected", p.getProduct());
        Prix pr = dao.getPrix(p.getQuantity(), p.getProduct(), idClient);
        request.setAttribute("prix", pr.total());
        request.setAttribute("id", num);
        request.setAttribute("quantite", p.getQuantity());
        request.getRequestDispatcher("ChangeOrder.jsp").forward(request, response);
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

    protected void validerCommande(HttpServletRequest request, HttpServletResponse response, DAO dao, int idClient) throws DAOException, ServletException, IOException {
        int num = Integer.parseInt(request.getParameter("id"));
        String product = (String)request.getParameter("produit");
        int qte =Integer.parseInt(request.getParameter("quantite"));
        if(num<=0){
            num=dao.nouveauIDCommande();
            PurchaseOrder p = new PurchaseOrder(num, idClient, product, dao.getPrix(qte, product, idClient), new Date());
            dao.ajouterCommande(p);
        }
        else{
            PurchaseOrder p = dao.getPurchaseOrder(num);
            p.setQuantity(qte);
            p.setProduct(product);
            dao.modifierCommande(p);
        }
        displayCommande(request, response, dao, idClient);
    }

    private Prix calculPrix(HttpServletRequest request, HttpServletResponse response, int id, DAO dao) throws DAOException {
        int qte = Integer.parseInt(request.getParameter("quantite"));
        String product = (String)request.getParameter("produit");
        Prix p = dao.getPrix(qte, product, id);
        return p;
    }

    private void displayCommande(HttpServletRequest request, HttpServletResponse response, DAO dao, int idClient) throws DAOException, ServletException, IOException {
        List mesCommandes = dao.getPurchaseOrderByClient(idClient);
        request.setAttribute("orders", mesCommandes);
        request.getRequestDispatcher("UserOrders.jsp").forward(request, response);
    }
    
    private void updatePrix(HttpServletRequest request, HttpServletResponse response, DAO dao, Prix pr) throws DAOException, ServletException, IOException{
        List<String> products = dao.listeProduits();
        request.setAttribute("products", products);
        request.setAttribute("selected", request.getParameter("produit"));
        request.setAttribute("prix", pr.total());
        request.setAttribute("id", Integer.parseInt(request.getParameter("id")));
        request.setAttribute("quantite", Integer.parseInt(request.getParameter("quantite")));
        request.getRequestDispatcher("ChangeOrder.jsp").forward(request, response);
    }

}
