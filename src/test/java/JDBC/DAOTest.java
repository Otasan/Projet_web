/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

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

/**
 *
 * @author Utilisateur
 */
public class DAOTest {
    private DAO myDAO; // L'objet à tester
    private DataSource myDataSource; // La source de données à utiliser
    private static Connection myConnection ; // La connection à la BD de test
	

    @Before
    public void setUp() throws SQLException, IOException, SqlToolError {
    	// On utilise la base de données de test
    	myDataSource = getTestDataSource();
    	myConnection = myDataSource.getConnection();
    	// On crée le schema de la base de test
	executeSQLScript(myConnection, "schema.sql");
	// On y met des données
	executeSQLScript(myConnection, "bigtestdata.sql");		
	
	myDAO = new DAO(myDataSource);
    }
    
    @Before
    public static void setUpClass() {
    }
    
    @After
    public static void tearDownClass() {
    }
    
    private void executeSQLScript(Connection connexion, String filename)  throws IOException, SqlToolError, SQLException {
	// On initialise la base avec le contenu d'un fichier de test
	String sqlFilePath = DAOTest.class.getResource(filename).getFile();
	SqlFile sqlFile = new SqlFile(new File(sqlFilePath));
	sqlFile.setConnection(connexion);
	sqlFile.execute();
	sqlFile.closeReader();		
    }
    public static DataSource getTestDataSource() {
	org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
	ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
	ds.setUser("sa");
	ds.setPassword("sa");
	return ds;
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
