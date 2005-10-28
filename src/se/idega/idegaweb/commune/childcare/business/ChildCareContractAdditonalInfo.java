package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;

/**
 * This bean allows to retreive additional info about parents when child care 
 * contract is generated 
 * 
 * @author Dainis
 *
 */
public class ChildCareContractAdditonalInfo {
    User parent1; 
    User parent2;
    CommuneUserBusiness userBusiness;

    public ChildCareContractAdditonalInfo(User parent1, User parent2, CommuneUserBusiness userBusiness){
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.userBusiness = userBusiness;
    }
    
    public String getParentHomePhoneNumber(User parent){
        String s = "";
        
        if (parent != null) {
            try {
                Phone p = userBusiness.getUsersHomePhone(parent);
                s = p.getNumber();                
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NoPhoneFoundException e) {
            }
        }        
        return s;
    }
    
    public String getParentWorkPhoneNumber(User parent){
        String s = "";
        
        if (parent != null) {
            try {
                Phone p = userBusiness.getUsersWorkPhone(parent);
                s = p.getNumber();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NoPhoneFoundException e) {
            }
        }  
        
        return s;
    }  
    
    public String getParentEmailAddress(User parent){
        String s = "";
        
        if (parent!= null) {            
            try {               
                Email email = userBusiness.getEmail(parent);
                if(email != null) {
                    s = email.getEmailAddress();
                }
            } catch (RemoteException e) {                
                e.printStackTrace();
            }            
        }
        
        return s;
    }
    
    public String getParentMobilePhoneNumber(User parent){
        String s = "";
        
        if (parent != null) {
            try {
                Phone p = userBusiness.getUsersMobilePhone(parent);
                s = p.getNumber();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NoPhoneFoundException e) {
            }
        }  
        
        return s;
    }     
    
    public String getParent1HomePhoneNumber() {
        return this.getParentHomePhoneNumber(parent1);
    }
    
    public String getParent2HomePhoneNumber() {
        return this.getParentHomePhoneNumber(parent2);
    } 
    
    public String getParent1WorkPhoneNumber() {
        return this.getParentWorkPhoneNumber(parent1);
    }
    
    public String getParent2WorkPhoneNumber() {
        return this.getParentWorkPhoneNumber(parent2);
    }  
    
    public String getParent1EmailAddress() {
        return getParentEmailAddress(parent1);
    }
    
    public String getParent2EmailAddress() {
        return getParentEmailAddress(parent2);
    }
    
    public String getParent1MobilePhoneNumber() {
        return getParentMobilePhoneNumber(parent1);
    }
    
    public String getParent2MobilePhoneNumber() {
        return getParentMobilePhoneNumber(parent2);
    }    
    
}
