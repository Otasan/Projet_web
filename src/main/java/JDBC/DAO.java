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
        String sql = "Select SHIPPING_COST, PURCHASE_COST, QUANTITY FROM PURCHASE_ORDER "
                + "INNER JOIN PRODUCT USING(PRODUCT_ID) "
                + "WHERE ORDER_NUM = ?";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1,orderNum);
            try(ResultSet r = stmt.executeQuery()){
                if(r.next()){
                    p = new Prix(rate, r.getFloat("PURCHASE_COST"), r.getFloat("SHIPPING_COST"), r.getInt("QUANTITY"));
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return p;
    }
}
