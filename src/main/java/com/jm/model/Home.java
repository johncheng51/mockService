package com.jm.model;

import java.util.*;
import com.jm.mock.MockModel;
import java.io.Serializable;

public class Home extends MockModel implements Serializable{
  private String firstName;
  private String lastName;
  private String email;
  private String homePhone;
  private String workPhone;


 public String getFirstName(){
 return this.firstName;
 }

 public String getLastName(){
 return this.lastName;
 }

 public String getEmail(){
 return this.email;
 }

 public String getHomePhone(){
 return this.homePhone;
 }

 public String getWorkPhone(){
 return this.workPhone;
 }


 public void setFirstName(String firstName) {
 this.firstName=firstName;
 }
 public void setLastName(String lastName) {
 this.lastName=lastName;
 }
 public void setEmail(String email) {
 this.email=email;
 }
 public void setHomePhone(String homePhone) {
 this.homePhone=homePhone;
 }
 public void setWorkPhone(String workPhone) {
 this.workPhone=workPhone;
 }
}

