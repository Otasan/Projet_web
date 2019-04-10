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
public class ProductEntity {
    private int id;
    private String manufacturer;
    private String code;
    private float prix;
    private String nom;
    
    public ProductEntity(int id, String manu, String code, float prix, String nom){
        this.id=id;
        this.manufacturer=manu;
        this.code=code;
        this.prix=prix;
        this.nom=nom;
    }
    
    public int getID(){
        return id;
    }
    public String getManufacturer(){
        return manufacturer;
    }
    public String getCode(){
        return code;
    }
    public float getPrix(){
        return prix;
    }
    public String getNom(){
        return nom;
    }
}
