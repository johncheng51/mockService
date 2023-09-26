package com.jm.model;



import com.jm.mock.MockModel;


public class Address extends MockModel {
    private String street;
    private String zipcode;
    private String city;
    
    @Override
    public int hashCode(){
        return 1;
    }
    
  
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public static Address sample(){
        Address address=new Address();
        address.mock();
        return address;
    }
}
