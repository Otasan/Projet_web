/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

import static JDBC.DataSourceFactory.getDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.hsqldb.jdbc.JDBCDataSource;

/**
 *
 * @author Utilisateur
 */
public class DAOTest {
	private DAO myDAO; // L'objet à tester
	private DataSource myDataSource; // La source de données à utiliser
	private static Connection myConnection ; // La connection à la BD de test
        private Prix myPrix;
	

	@Before
	public void setUp() throws SQLException, IOException, SqlToolError {
		// On utilise la base de données de test
		myDataSource = getDataSource();
		myConnection = myDataSource.getConnection();
		// On crée le schema de la base de test
		//executeSQLScript(myConnection, "schema.sql");
		// On y met des données
		//executeSQLScript(myConnection, "bigtestdata.sql");	
		
		myDAO = new DAO(myDataSource);
	}

	@After
	public void tearDown() throws IOException, SqlToolError, SQLException {
		myConnection.close(); // La base de données de test est détruite ici
             	myDAO = null; // Pas vraiment utile
	}
	
        
	public static DataSource getTestDataSource() {
		org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
		ds.setUser("sa");
		ds.setPassword("sa");
		return ds;
	}
        
	private void executeSQLScript(Connection connexion, String filename)  throws IOException, SqlToolError, SQLException {
		// On initialise la base avec le contenu d'un fichier de test
		String sqlFilePath = DAOTest.class.getResource(filename).getFile();
		SqlFile sqlFile = new SqlFile(new File(sqlFilePath));

		sqlFile.setConnection(connexion);
		sqlFile.execute();
		sqlFile.closeReader();		
	}
	
	/**
	 * Test of numberOfCustomers method, of class DAO.
	 * @throws simplejdbc.DAOException
	 */
	@Test
	public void testNumberOfCustomers() throws DAOException {
		int result = myDAO.numberOfCustomers();
		assertEquals(13, result);
	}
        
        /**
         * Test of the method getCustomerByEmail of class DAO
         * @throws DAOException 
         */
        @Test
        public void testGetCustomerByEmail() throws DAOException{
            int id = myDAO.getCustomerByEmail("jumboeagle@example.com").getCustomerId();
            assertEquals("1 failed",1,id);
            try{
                myDAO.getCustomerByEmail("o").getCustomerId();
                fail();
            }
            catch(Exception ex){}
        }
        
        /**
         * Test of the method getPurchaseOrderByClient of class DAO
         * @throws DAOException 
         */
        @Test
        public void testgetPurchaseOrderByClient() throws DAOException{
            for(PurchaseOrder p: myDAO.getPurchaseOrderByClient(1)){
                assertTrue("Ah "+p.getOrderNum(),p.getOrderNum() == 10398001 || p.getOrderNum() == 10398005);
            }
        }
        
        @Test
        public void testPrix(){
            float rate = 0;
            float shipping = 10;
            float price = 10;
            int quantity = 1;
            myPrix = new Prix(rate, shipping,price, quantity);
            assertEquals(price*quantity*(1-rate/100)+shipping, myPrix.total(), 0.01);
        }
    
}
