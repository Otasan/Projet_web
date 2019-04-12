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
public class CustomerEntity {
	// TODO : ajouter les autres propriétés
	private int customerId;
	private String name;
	private String email;
        private String city;
        private String state;
        private String phone;
        private String fax;

	public CustomerEntity(int customerId, String name, String email) {
		this.customerId = customerId;
		this.name = name;
		this.email = email;
	}

        public CustomerEntity(int customerId, String name, String email, String city, String state, String phone, String fax){
		this.customerId = customerId;
		this.name = name;
		this.email = email;
                this.city=city;
                this.fax=fax;
                this.phone=phone;
                this.state=state;
        }
        
	/**
	 * Get the value of customerId
	 *
	 * @return the value of customerId
	 */
	public int getCustomerId() {
		return customerId;
	}

	/**
	 * Get the value of name
	 *
	 * @return the value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the value of addressLine1
	 *
	 * @return the value of addressLine1
	 */
	public String getEmail() {
		return email;
	}
        
        public String getCity(){
            return city;
        }
        
        public String getFax(){
            return fax;
        }
        
        public String getPhone(){
            return phone;
        }
        public String getState(){
            return state;
        }


}