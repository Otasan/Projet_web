/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

/**
 *
 * @author Utilisateur
 */
public class ManufacturerEntity {
    private int id;
    private String name;
    private String city;
    private String state;
    private String phone;
    private String fax;
    private String mail;
    
    public ManufacturerEntity(int id, String name, String city, String state, String phone, String fax, String mail){
        this.id=id;
        this.city=city;
        this.fax=fax;
        this.mail=mail;
        this.name=name;
        this.phone=phone;
        this.state=state;
    }
    
    public int getID(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getCity(){
        return city;
    }
    public String getState(){
        return state;
    }
    public String getPhone(){
        return phone;
    }
    public String getFax(){
        return fax;
    }
    public String getMail(){
        return mail;
    }
}
