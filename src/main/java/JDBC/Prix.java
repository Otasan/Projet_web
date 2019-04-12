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
    private float markup;
    private float discountRate;
    private float shippingCost;
    private int quantite;
    
    public Prix(float rate, float prix, float marge, float livraison, int qte){
        purchasePrice=prix;
        markup = marge;
        discountRate=rate;
        shippingCost=livraison;
        quantite = qte;
    }
    
    public float total(){
        float total = purchasePrice*(1+markup/100)*(1-discountRate/100)*quantite+shippingCost;
        total = (float) (Math.round(total*100.0)/100.0);
        return total;
    }
    
    public float gains(){
        float gain =(purchasePrice*(1+markup/100) - purchasePrice*(1-discountRate/100))*quantite;
        gain = (float) (Math.round(gain*100.0)/100.0);
        return gain;
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
    public float getMarkup(){
        return markup;
    }
    public void setQuantity(int qte){
        quantite = qte;
    }
}
