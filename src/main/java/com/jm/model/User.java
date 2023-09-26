package com.jm.model;



import com.jm.mock.MockModel;
import java.util.*;


public class User extends  MockModel {
    private int    id;
    private String firstname;
    private String lastname;
    private String username; 
    private String password;
    private String email;
    private int ranking = 0;
    private boolean admin = false;
    private Address homeAddress;
    private Address billingAddress;
    private Address shippingAddress;
    private Date created;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getRanking() { return ranking; }
    public void setRanking(int ranking) { this.ranking = ranking; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }

    public Address getHomeAddress() { return homeAddress; }
    public void setHomeAddress(Address homeAddress) { this.homeAddress = homeAddress; }

    public Address getBillingAddress() { return billingAddress; }
    public void setBillingAddress(Address billingAddress) { this.billingAddress = billingAddress; }

    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }
    public Date getCreated() { return created; }
    
    public static User sample(){
        User user=new User();
        user.mock();
        return user;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
