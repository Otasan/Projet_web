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
    private int productId;
    private int quantity;
    private float shippingCost;
    private Date salesDate;
    private Date shippingDate;
    private String freightCompany;

    public PurchaseOrder(int orderNum, int customerId, int productId, int quantity, float shippingCost, Date salesDate, Date shippingDate, String freightCompany) {
        this.orderNum = orderNum;
        this.customerId = customerId;
        this.productId=productId;
        this.quantity=quantity;
        this.shippingCost=shippingCost;
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
    
    public int getProductId(){
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getShippingCost() {
        return shippingCost;
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
}
