/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author Utilisateur
 */
public class UtilisateursConnectes implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // On initialise le nombre d'utilisateurs connectés dans le contexte d'application
        sce.getServletContext().setAttribute("numberConnected", 0);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // On incrémente le nombre d'utilisateurs
        int connected = (Integer) se.getSession().getServletContext().getAttribute("numberConnected");
        connected++;
        // On stocke ce nombre dans le contexte d'application
        se.getSession().getServletContext().setAttribute("numberConnected", connected);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // On décrémente le nombre d'utilisateurs
        int connected = (Integer) se.getSession().getServletContext().getAttribute("numberConnected");
        connected--;
        // On stocke ce nombre dans le contexte d'application
        se.getSession().getServletContext().setAttribute("numberConnected", connected);
    }
}
