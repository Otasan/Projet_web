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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public final String preDate = "2011-05-24";

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
        String sql = "SELECT * FROM PURCHASE_ORDER INNER JOIN PRODUCT USING(PRODUCT_ID) WHERE CUSTOMER_ID = ? ORDER BY ORDER_NUM ASC";
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
    
    /**
     * get discount rate for a specific user and product.
     * @param client
     * @param produit
     * @return
     * @throws DAOException 
     */
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
    
    /**
     * get price for the quantity of a product for a client.
     * @param quantite
     * @param produit
     * @param client
     * @return
     * @throws DAOException 
     */
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
     * @return une Liste de ProductEntity représentant la relatinon PRODUCT
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
                float cost = rs.getFloat("PURCHASE_COST")*(1+rs.getFloat("MARKUP")/100);
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
    
    /**
     * 
     * @return une Liste de ManufacturerEntity représentant la relatinon MANUFACTURER
     * @throws DAOException 
     */
    public List<ManufacturerEntity> listeManufacturer() throws DAOException{
        ArrayList<ManufacturerEntity> res = new ArrayList();
        String sql="SELECT * FROM MANUFACTURER ORDER BY MANUFACTURER_ID ASC";
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                int id = rs.getInt("MANUFACTURER_ID");
                String name = rs.getString("NAME");
                String city = rs.getString("CITY");
                String state = rs.getString("STATE");
                String phone = rs.getString("PHONE");
                String fax = rs.getString("FAX");
                String mail = rs.getString("EMAIL");
                res.add(new ManufacturerEntity(id, name, city, state, phone, fax, mail));
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }

    /**
     * 
     * @param mail
     * @return le nom du client ayant l'email mail
     * @throws DAOException 
     */
    public String getCustomerName(String mail) throws DAOException {
        String res="";
        String sql = "SELECT NAME FROM CUSTOMER WHERE EMAIL=?";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setString(1, mail);
            try(ResultSet r = stmt.executeQuery();){
                if(r.next()){
                   res=r.getString("NAME");
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
     * 
     * @return nombre de fournisseurs
     * @throws DAOException 
     */
    public int nbFournisseurs() throws DAOException{
        int res=0;
        String sql = "SELECT COUNT(*) AS NB FROM MANUFACTURER";
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next()){
                res = rs.getInt("NB");
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
    
    /**
     * 
     * @return nombre de clients
     * @throws DAOException 
     */
    public int nbClients() throws DAOException{
        int res=0;
        String sql = "SELECT COUNT(*) AS NB FROM CUSTOMER";
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next()){
                res = rs.getInt("NB");
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }

    /**
     * 
     * @return Liste de tout les customers
     * @throws DAOException 
     */
    public List<CustomerEntity> listCustomer() throws DAOException {
        ArrayList<CustomerEntity> res = new ArrayList();
        String sql="SELECT * FROM CUSTOMER ORDER BY CUSTOMER_ID ASC";
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                int id = rs.getInt("CUSTOMER_ID");
                String name = rs.getString("NAME");
                String mail = rs.getString("EMAIL");
                String city = rs.getString("CITY");
                String state = rs.getString("STATE");
                String fax = rs.getString("FAX");
                String phone = rs.getString("PHONE");
                res.add(new CustomerEntity(id,name,mail,city,state,phone,fax));
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
    
    /**
     * 
     * @return chiffre d'affaire total
     * @throws DAOException 
     */
    public float chiffreDaffaire() throws DAOException{
        float res = 0;
        ArrayList<Prix> lesPrix = new ArrayList();
        String sql = "SELECT ORDER_NUM FROM PURCHASE_ORDER";
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                Prix p = getPrix(rs.getInt("ORDER_NUM"));
                res += p.gains();
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    } 
    
    /**
     * 
     * @return liste de tout les types d'articles
     * @throws DAOException 
     */
    public List<String> typeArticle() throws DAOException{
        ArrayList<String> res = new ArrayList();
        String sql="SELECT DESCRIPTION FROM PRODUCT_CODE ORDER BY DESCRIPTION ASC";
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
     * 
     * @param zip
     * @param debut
     * @param fin
     * @return le chiffre d'affaire pour un marché
     * @throws DAOException 
     */
    public float caPourZip(int zip, Date debut, Date fin) throws DAOException{
        float res = 0;
        String sql = "SELECT ORDER_NUM FROM PURCHASE_ORDER "
                + "INNER JOIN CUSTOMER USING (CUSTOMER_ID) "
                + "WHERE CUSTOMER.ZIP = ? AND "
                + "SALES_DATE BETWEEN ? AND ?";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1, zip);
            stmt.setDate(2, debut);
            stmt.setDate(3, fin);
            try(ResultSet r = stmt.executeQuery();){
                while(r.next()){
                   Prix p =getPrix(r.getInt("ORDER_NUM"));
                   res += p.gains();
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
     * 
     * @param debut
     * @param fin
     * @return le chiffre d'affaire de chaque zone géographique
     * @throws DAOException 
     */
    public Map<Integer,Float> caParZipPourPeriode(java.util.Date debut, java.util.Date fin) throws DAOException {
        HashMap<Integer, Float> res = new HashMap();
        String sql = "SELECT ZIP_CODE FROM MICRO_MARKET";
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                int zip = rs.getInt("ZIP_CODE");
                res.put(zip, caPourZip(zip, new Date(debut.getTime()), new Date(fin.getTime())));
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
    
    /**
     * 
     * @param prodCode
     * @param debut
     * @param fin
     * @return le chiffre d'affaires pour un type de produit
     * @throws DAOException 
     */
    public Float caPourTypeProduit(String prodCode, Date debut, Date fin) throws DAOException{
        float res =0;
        String sql = "SELECT ORDER_NUM FROM PURCHASE_ORDER "
                + "INNER JOIN PRODUCT USING (PRODUCT_ID) "
                + "WHERE PRODUCT_CODE LIKE ? AND "
                + "SALES_DATE BETWEEN ? AND ?";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setString(1, prodCode);
            stmt.setDate(2, debut);
            stmt.setDate(3, fin);
            try(ResultSet r = stmt.executeQuery();){
                while(r.next()){
                   Prix p =getPrix(r.getInt("ORDER_NUM"));
                   res += p.gains();
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
     * 
     * @param debut
     * @param fin
     * @return le chiffre d'affaire de chaque produit
     * @throws DAOException 
     */
    public Map<String,Float> caParTypeProduitPourPeriode(java.util.Date debut, java.util.Date fin) throws DAOException{
        HashMap<String, Float> res=new HashMap();
        String sql = "SELECT PROD_CODE, DESCRIPTION FROM PRODUCT_CODE";
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                String desc = rs.getString("DESCRIPTION");
                res.put(desc, caPourTypeProduit(rs.getString("PROD_CODE"),new Date(debut.getTime()),new Date(fin.getTime())));
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }

    /**
     * 
     * @param id
     * @param debut
     * @param fin
     * @return le chiffre d'affaires pour un client
     * @throws DAOException 
     */
    public float caPourClient(int id, Date debut, Date fin) throws DAOException{
        float res =0;
        String sql = "SELECT ORDER_NUM FROM PURCHASE_ORDER "
                + "WHERE CUSTOMER_ID = ? AND "
                + "SALES_DATE BETWEEN ? AND ?";
        try(Connection connexion = myDataSource.getConnection();
                PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1, id);
            stmt.setDate(2, debut);
            stmt.setDate(3, fin);
            try(ResultSet r = stmt.executeQuery();){
                while(r.next()){
                   Prix p =getPrix(r.getInt("ORDER_NUM"));
                   res += p.gains();
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
     * 
     * @param debut
     * @param fin
     * @return  le chiffre d'affaire de chaque client
     * @throws DAOException 
     */
    public Map<String,Float> caParClientPourPeriode(java.util.Date debut, java.util.Date fin) throws DAOException {
        HashMap<String,Float> res=new HashMap();
        String sql = "SELECT CUSTOMER_ID, NAME FROM CUSTOMER";
        try(Connection connexion = myDataSource.getConnection();
                Statement stmt = connexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                String name = rs.getString("NAME");
                res.put(name, caPourClient(rs.getInt("CUSTOMER_ID"),new Date(debut.getTime()),new Date(fin.getTime())));
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
}
