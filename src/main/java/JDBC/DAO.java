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
    
    public List<PurchaseOrder> getPurchaseOrderByClient (int id) throws DAOException{
        List<PurchaseOrder> res = new ArrayList();
        String sql = "SELECT * FROM PURCHASE_ORDER WHERE CUSTOMER_ID = ?";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setInt(1,id);
            try(ResultSet r = stmt.executeQuery()){
                while (r.next()){
                    int orderNum = r.getInt("order_num");
                    int customerId = r.getInt("customer_id");
                    int productId=r.getInt("product_id");
                    int quantity=r.getInt("quantity");
                    float shippingCost=r.getFloat("shipping_cost");
                    Date salesDate=r.getDate("sales_date");
                    Date shippingDate=r.getDate("shipping_date");
                    String freightCompany=r.getString("freight_company");
                    res.add(new PurchaseOrder(orderNum, customerId, productId, quantity, shippingCost, salesDate, shippingDate, freightCompany));
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return res;
    }
}
