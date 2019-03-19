/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

import java.sql.Connection;
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
        this.myDataSource = dataSource;
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
    
    public List<CustomerEntity> getCustomerByName(String name) throws DAOException{
        List<CustomerEntity> res = new ArrayList();
        String sql = "SELECT * FROM CUSTOMER WHERE NAME = ?";
        try(Connection connexion = myDataSource.getConnection();
            PreparedStatement stmt = connexion.prepareStatement(sql);){
            stmt.setString(1,name);
            try(ResultSet r = stmt.executeQuery()){
                while (r.next()){ // Tant qu'il y a des enregistrements
                    // On récupère les champs nécessaires de l'enregistrement courant
                    int id = r.getInt("CUSTOMER_ID");
                    String address = r.getString("ADDRESSLINE1");
                    // On crée l'objet entité
                    CustomerEntity c = new CustomerEntity(id, name, address);
                    // On l'ajoute à la liste des résultats
                    res.add(c);
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
