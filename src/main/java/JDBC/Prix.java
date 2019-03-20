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
public class Prix {
    private float purchasePrice;
    private float discountRate;
    private float shippingCost;
    private int quantite;
    
    public Prix(float rate, float prix, float livraison, int qte){
        purchasePrice=prix;
        discountRate=rate;
        shippingCost=livraison;
        quantite = qte;
    }
    
    public float total(){
        return purchasePrice*quantite*(1-discountRate/100)+shippingCost;
    }
    
    public float getPurchasePrice(){
        return purchasePrice;
    }
    public float getDiscountRate(){
        return discountRate;
    }
    public float getShippingCost(){
        return shippingCost;
    }
    public int getQuantity(){
        return quantite;
    }
}
