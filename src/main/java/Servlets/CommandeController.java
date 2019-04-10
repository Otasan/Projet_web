/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import JDBC.DAO;
import JDBC.DAOException;
import JDBC.DataSourceFactory;
import JDBC.ManufacturerEntity;
import JDBC.Prix;
import JDBC.ProductEntity;
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
        HttpSession session = request.getSession(false);
        String mail = (String) session.getAttribute("email");
        DAO dao = new DAO(DataSourceFactory.getDataSource());
        try {
            int id = dao.identification(mail);
            request.setAttribute("user", dao.getCustomerName(mail));

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
                    case "products":
                        listeProduit(request, response, dao);
                        break;
                    case "fournisseurs":
                        listeManufacturer(request, response, dao);
                        break;
                    case "afficher":
                        displayCommande(request, response, dao, id);
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

    /**
     * Permet d'ouvrir la page ChangeOrder.jsp anvec les parametres nécéssaires pour une nouvelle commande
     * @param request
     * @param response
     * @param dao
     * @param idClient
     * @throws ServletException
     * @throws IOException
     * @throws DAOException 
     */
    protected void nouvelleCommande(HttpServletRequest request, HttpServletResponse response, DAO dao, int idClient) throws ServletException, IOException, DAOException {
        List<String> products = dao.listeNomsProduits();
        request.setAttribute("products", products);
        request.setAttribute("selected", products.get(0));
        Prix p = dao.getPrix(1, products.get(0), idClient);
        request.setAttribute("prix", p.total());
        request.setAttribute("id", -1);
        request.setAttribute("quantite", 1);
        request.getRequestDispatcher("ChangeOrder.jsp").forward(request, response);
    }
    
    /**
     * Permet d'ouvrir la page ChangeOrder.jsp anvec les parametres nécéssaires pour une commande existante
     * @param request
     * @param response
     * @param dao
     * @param num
     * @param idClient
     * @throws ServletException
     * @throws IOException
     * @throws DAOException 
     */
    protected void updateCommande(HttpServletRequest request, HttpServletResponse response, DAO dao, int num, int idClient) throws ServletException, IOException, DAOException {
        List<String> products = dao.listeNomsProduits();
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

    /**
     * Ajoute la commande modifiée/nouvelle commande dans la base de données et renvoie à la page UserOrders.jsp
     * @param request
     * @param response
     * @param dao
     * @param idClient
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
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

    /**
     * calcul le prix pour une comande en cours d'édition. Utilisée lors de l'affichage automatique du prix sur la page ChangeOrder.jsp
     * @param request
     * @param response
     * @param id
     * @param dao
     * @return
     * @throws DAOException 
     */
    private Prix calculPrix(HttpServletRequest request, HttpServletResponse response, int id, DAO dao) throws DAOException {
        int qte = Integer.parseInt(request.getParameter("quantite"));
        String product = (String)request.getParameter("produit");
        Prix p = dao.getPrix(qte, product, id);
        return p;
    }

    /**
     * affiche la page UserOrders.jsp pour l'utilisateur connecté
     * @param request
     * @param response
     * @param dao
     * @param idClient
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void displayCommande(HttpServletRequest request, HttpServletResponse response, DAO dao, int idClient) throws DAOException, ServletException, IOException {
        List mesCommandes = dao.getPurchaseOrderByClient(idClient);
        request.setAttribute("orders", mesCommandes);
        request.getRequestDispatcher("UserOrders.jsp").forward(request, response);
    }
    
    /**
     * Mets à jour le prix dans la page ChangeOrder.jsp. Utilisée lors de l'affichage automatique du prix sur la page ChangeOrder.jsp
     * @param request
     * @param response
     * @param dao
     * @param pr
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void updatePrix(HttpServletRequest request, HttpServletResponse response, DAO dao, Prix pr) throws DAOException, ServletException, IOException{
        List<String> products = dao.listeNomsProduits();
        request.setAttribute("products", products);
        request.setAttribute("selected", request.getParameter("produit"));
        request.setAttribute("prix", pr.total());
        request.setAttribute("id", Integer.parseInt(request.getParameter("id")));
        request.setAttribute("quantite", Integer.parseInt(request.getParameter("quantite")));
        request.getRequestDispatcher("ChangeOrder.jsp").forward(request, response);
    }
    
    /**
     * affiche la page TableProduit.jsp pour l'utilisateur connecté
     * @param request
     * @param response
     * @param dao
     * @throws DAOException
     * @throws ServletException
     * @throws IOException 
     */
    private void listeProduit(HttpServletRequest request, HttpServletResponse response, DAO dao) throws DAOException, ServletException, IOException{
        List<ProductEntity> products = dao.listeProduits();
        request.setAttribute("produits", products);
        request.getRequestDispatcher("TableProduit.jsp").forward(request, response);
    }

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
        request.getRequestDispatcher("TableFournisseur.jsp").forward(request, response);
    }

}
