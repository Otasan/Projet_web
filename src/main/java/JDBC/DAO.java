/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Aabdo
 */
public class DAO {

    protected final DataSource myDataSource;

    /**
     *
     * @param dataSource la source de données à utiliser
     */
    public DAO(DataSource dataSource) {
        myDataSource = dataSource;
    }

    /**
     *
     * @return le nombre d'enregistrements dans la table CUSTOMER
     * @throws DAOException
     */
    public int numberOfCustomers() throws DAOException {
        int result = 0;

        String sql = "SELECT COUNT(*) AS NUMBER FROM CUSTOMER";
        // Syntaxe "try with resources" 
        // cf. https://stackoverflow.com/questions/22671697/try-try-with-resources-and-connection-statement-and-resultset-closing
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                Statement stmt = connection.createStatement(); // On crée un statement pour exécuter une requête
                ResultSet rs = stmt.executeQuery(sql) // Un ResultSet pour parcourir les enregistrements du résultat
                ) {
            if (rs.next()) { // Pas la peine de faire while, il y a 1 seul enregistrement
                // On récupère le champ NUMBER de l'enregistrement courant
                result = rs.getInt("NUMBER");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }

        return result;
    }
    
    /**
     * returns Customer information by using email as an identifyer
     * @param email
     * @return
     * @throws DAOException 
     */
    public CustomerEntity getCustomerByEmail(String email) throws DAOException{
        CustomerEntity res=null;
        String sql = "SELECT * FROM CUSTOMER WHERE EMAIL LIKE ?";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setString(1,email);
            try(ResultSet r = stmt.executeQuery()){
                if(r.next()){
                    int id = r.getInt("CUSTOMER_ID");
                    String name = r.getString("EMAIL");
                    res = new CustomerEntity(id, name, email);
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
    
    /**
     * Returns all the purchase orders made by customer with the given id
     * @param id
     * @return
     * @throws DAOException 
     */
    public List<PurchaseOrder> getPurchaseOrderByClient (int id) throws DAOException{
        List<PurchaseOrder> res = new ArrayList();
        String sql = "SELECT * FROM PURCHASE_ORDER INNER JOIN PRODUCT USING(PRODUCT_ID) WHERE CUSTOMER_ID = ?";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1,id);
            try(ResultSet r = stmt.executeQuery()){
                while (r.next()){
                    int orderNum = r.getInt("order_num");
                    int customerId = r.getInt("customer_id");
                    String product=r.getString("description");
                    Prix price = getPrix(orderNum);
                    Date salesDate=r.getDate("sales_date");
                    Date shippingDate=r.getDate("shipping_date");
                    String freightCompany=r.getString("freight_company");
                    res.add(new PurchaseOrder(orderNum, customerId, product, price, salesDate, shippingDate, freightCompany));
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
    
    /**
     * get discount rate for a specific order.
     * Used by getPrix
     * @param orderNum
     * @return
     * @throws DAOException 
     */
    public float getRate(int orderNum) throws DAOException{
        float rate =0;
        //récupère uniquement la remise
        String sqlRate = "SELECT RATE FROM APP.PURCHASE_ORDER \n" +
                        "INNER JOIN APP.CUSTOMER USING (CUSTOMER_ID) \n" +
                        "INNER JOIN APP.PRODUCT USING (PRODUCT_ID) \n" +
                        "INNER JOIN APP.PRODUCT_CODE ON PROD_CODE=PRODUCT_CODE \n" +
                        "INNER JOIN APP.DISCOUNT_CODE ON APP.DISCOUNT_CODE.DISCOUNT_CODE=APP.CUSTOMER.DISCOUNT_CODE \n" +
                        "WHERE ORDER_NUM = ? AND APP.DISCOUNT_CODE.DISCOUNT_CODE=APP.PRODUCT_CODE.DISCOUNT_CODE";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sqlRate);){
            stmt.setInt(1,orderNum);
            try(ResultSet r = stmt.executeQuery()){
                if(r.next()){
                    rate = r.getInt("RATE");
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return rate;
    }
    
    public float getRate(int client, int produit) throws DAOException{
        float rate = 0;
        String sql = "SELECT RATE FROM DISCOUNT_CODE "
                + "INNER JOIN CUSTOMER USING (DISCOUNT_CODE) "
                + "INNER JOIN PRODUCT_CODE USING(DISCOUNT_CODE) "
                + "INNER JOIN PRODUCT ON PRODUCT.PRODUCT_CODE=PRODUCT_CODE.PROD_CODE "
                + "WHERE PRODUCT_ID=? AND CUSTOMER_ID=?";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1,produit);
            stmt.setInt(2,client);
            try(ResultSet r = stmt.executeQuery()){
                if(r.next()){
                    rate = r.getInt("RATE");
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return rate;
    }
    
    /**
     * get price from one order as a Prix object.
     * Used by getPurchaseOrder
     * @param orderNum
     * @return
     * @throws DAOException 
     */
    public Prix getPrix(int orderNum) throws DAOException{
        float rate = getRate(orderNum);
        Prix p = null;
        String sql = "Select SHIPPING_COST, PURCHASE_COST, QUANTITY, MARKUP FROM PURCHASE_ORDER "
                + "INNER JOIN PRODUCT USING(PRODUCT_ID) "
                + "WHERE ORDER_NUM = ?";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1,orderNum);
            try(ResultSet r = stmt.executeQuery()){
                if(r.next()){
                    p = new Prix(rate, r.getFloat("PURCHASE_COST"), r.getFloat("MARKUP"), r.getFloat("SHIPPING_COST"), r.getInt("QUANTITY"));
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return p;
    }
    
    public Prix getPrix(int quantite, String produit, int client) throws DAOException{
        int idProduit = getProductIdByName(produit);
        float rate = getRate(client, idProduit);
        Prix p=null;
        String sqlProduit = "Select PURCHASE_COST, MARKUP FROM PRODUCT WHERE PRODUCT_ID=? ";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sqlProduit);){
            stmt.setInt(1,idProduit);
            try(ResultSet rs =stmt.executeQuery()){
                if(rs.next()){
                    p = new Prix(rate, rs.getFloat("PURCHASE_COST"), rs.getFloat("MARKUP"), 0, quantite);
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return p;
    }
    
    /**
     * Retourne l'ID d'un client pour une adresse mail.
     * @param email
     * @return
     * @throws DAOException 
     */
    public int identification(String email) throws DAOException{
        int res = -1;
        String sql = "SELECT CUSTOMER_ID FROM CUSTOMER WHERE EMAIL = ?";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setString(1,email);
            try(ResultSet r = stmt.executeQuery()){
                if(r.next()){
                    res = r.getInt("CUSTOMER_ID");
                }
                else{
                    throw new DAOException("Client : "+email+" introuvable");
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
    
    /**
     * Renvoie Product_ID à partir de la description.
     * @param name
     * @return
     * @throws DAOException 
     */
    public int getProductIdByName(String name) throws DAOException{
        String sql = "SELECT PRODUCT_ID FROM PRODUCT WHERE DESCRIPTION=?";
        int prod=-1;
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setString(1,name);
            try(ResultSet r = stmt.executeQuery()){
                if(r.next()){
                    prod = r.getInt("PRODUCT_ID");
                }
                else{
                    throw new DAOException("Produit : "+name+" introuvable");
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return prod;
    }
    
    /**
     * Permet de modifier une commande pour le order_num précisé dans p.
     * @param p
     * @throws DAOException 
     */
    public void modifierCommande(PurchaseOrder p) throws DAOException{
        int produit = getProductIdByName(p.getProduct());
        String sql = "UPDATE PURCHASE_ORDER "
                + "SET PRODUCT_ID = ?, "
                + "QUANTITY = ? "
                + "WHERE ORDER_NUM = ?";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1, produit);
            stmt.setInt(2, p.getQuantity());
            stmt.setInt(3, p.getOrderNum());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
    }
    
    /**
     * Supprime l'entrée dont le Order_num est précisée dans p.
     * @param p
     * @throws DAOException 
     */
    public void supprimerCommande(PurchaseOrder p) throws DAOException{
        String sql="DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM = ?";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1, p.getOrderNum());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
    }
    
    /**
     * supprime la commande de numéro num
     * @param num
     * @throws DAOException 
     */
    public void supprimerCommande(int num) throws DAOException{
        String sql="DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM = ?";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1, num);
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
    }
    
    /**
     * Ajoute une la commmande p à la table Purchase_order
     * @param p
     * @throws DAOException 
     */
    public void ajouterCommande(PurchaseOrder p) throws DAOException{
        String sql="INSERT INTO PURCHASE_ORDER VALUES(?,?,?,?,?,?,?,?)";
        int product_id = getProductIdByName(p.getProduct());
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1, p.getOrderNum());
            stmt.setInt(2, p.getCustomerId());
            stmt.setInt(3, product_id);
            stmt.setInt(4, p.getQuantity());
            stmt.setFloat(5, p.getShipping());
            stmt.setDate(6, p.getSalesDate());
            stmt.setDate(7, p.getShippingDate());
            stmt.setString(8, p.getFreightCompany());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
    }
    
    /**
     * 
     * @return une List de tous les attributs "DESCRIPTION" de la table product
     * @throws DAOException 
     */
    public List<String> listeNomsProduits() throws DAOException{
        String sql = "SELECT DESCRIPTION FROM PRODUCT";
        ArrayList<String> res = new ArrayList();
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                res.add(rs.getString("DESCRIPTION"));
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
    
    /**
     * Trouve aléatoirement un ID commande positif qui n'est pas utilisé actuellement
     * @return
     * @throws DAOException 
     */
    public int nouveauIDCommande() throws DAOException{
        String sql = "SELECT ORDER_NUM FROM PURCHASE_ORDER";
        ArrayList<Integer> lesID = new ArrayList();
        
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                lesID.add(rs.getInt("ORDER_NUM"));
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        Random rand = new Random();
        int res = rand.nextInt();
        while(lesID.contains(res) || res<=0){
            res=rand.nextInt();
        }
        return res;
    }
    
    /**
     * retourne la PurchaseOrder Ayant le numero num
     * @param num
     * @return
     * @throws DAOException 
     */
    public PurchaseOrder getPurchaseOrder(int num) throws DAOException{
        PurchaseOrder p = null;
        String sql = "SELECT * FROM PURCHASE_ORDER "
                + "INNER JOIN PRODUCT USING (PRODUCT_ID) "
                + "WHERE ORDER_NUM=?";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1, num);
            try(ResultSet r = stmt.executeQuery();){
                if(r.next()){
                    int orderNum = r.getInt("order_num");
                    int customerId = r.getInt("customer_id");
                    String product=r.getString("description");
                    Prix price = getPrix(orderNum);
                    Date salesDate=r.getDate("sales_date");
                    Date shippingDate=r.getDate("shipping_date");
                    String freightCompany=r.getString("freight_company");
                    p= new PurchaseOrder(orderNum, customerId, product, price, salesDate, shippingDate, freightCompany);
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return p;
    }
    
    /**
     * 
     * @return une Liste de ProductEntity représentant la relatino PRODUCT
     * @throws DAOException 
     */
    public List<ProductEntity> listeProduits() throws DAOException{
        String sql = "SELECT * FROM PRODUCT "
                + "INNER JOIN MANUFACTURER USING(MANUFACTURER_ID) "
                + "ORDER BY PRODUCT_CODE, DESCRIPTION ASC";
        ArrayList<ProductEntity> res = new ArrayList();
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                int id = rs.getInt("PRODUCT_ID");
                String manu = rs.getString("NAME");
                String code = rs.getString("PRODUCT_CODE");
                float cost = rs.getFloat("PURCHASE_COST");
                String nom = rs.getString("DESCRIPTION");
                res.add(new ProductEntity(id, manu, code, cost, nom));
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
}
