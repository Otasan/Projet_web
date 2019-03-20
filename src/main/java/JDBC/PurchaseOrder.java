/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;



import java.sql.Date;

/**
 *
 * @author Utilisateur
 */

public class PurchaseOrder{
    private int orderNum;
    private int customerId;
    private String product;
    private Prix price;
    private Date salesDate;
    private Date shippingDate;
    private String freightCompany;

    public PurchaseOrder(int orderNum, int customerId, String product, Prix price, Date salesDate, Date shippingDate, String freightCompany) {
        this.orderNum = orderNum;
        this.customerId = customerId;
        this.product=product;
        this.price=price;
        this.salesDate=salesDate;
        this.shippingDate=shippingDate;
        this.freightCompany=freightCompany;
    }

    public Integer getOrderNum() {
        return orderNum;
    }
    public int getCustomerId(){
        return customerId;
    }
    public String getProduct(){
        return product;
    }
    public Prix getPrice() {
        return price;
    }
    public Date getSalesDate() {
        return salesDate;
    }
    public Date getShippingDate() {
        return shippingDate;
    }
    public String getFreightCompany() {
        return freightCompany;
    }
    
    public float getTotalPrice(){
        return price.total();
    }
    public float getDiscount(){
        return price.getDiscountRate();
    }
    public float getShipping(){
        return price.getShippingCost();
    }
    public float getPurchaseCost(){
        return price.getPurchasePrice();
    }
    public int getQuantity(){
        return price.getQuantity();
    }
}
