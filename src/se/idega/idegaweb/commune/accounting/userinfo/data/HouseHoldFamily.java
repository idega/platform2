/*
 * Created on Dec 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.data;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


import com.idega.core.location.data.Address;
import com.idega.user.data.User;

/**
 * HouseHoldFamily
 * @author aron 
 * @version 1.0
 */
public class HouseHoldFamily {
	
	User head = null;
	User spouse = null;
	User cohabitant = null;
	Collection parentialChildren = null;
	Collection custodyChildren = null;
	Address address = null;
	
	public HouseHoldFamily(){
	
	}
	
	public HouseHoldFamily(User head){
		this.head = head;
	}
	
	public User getHeadOfFamily(){
		return head;
	}
	
	public void setHeadOfFamily(User head){
		this.head = head;
	}
	public User getSpouse(){
		return spouse;
	}
	public void setSpouse(User spouse){
		this.spouse = spouse;
	}
	public User getCohabitant(){
		return cohabitant;
	}
	public void setCohabitant(User cohabitant){
		this.cohabitant = cohabitant;
	}
	public Collection getParentialChildren(){
		return parentialChildren;
	}
	public void setParentialChildren(Collection children){
		this.parentialChildren = children;
	}
	public Collection getCustodyChildren(){
		return custodyChildren;
	}
	public void setCustodyChildren(Collection children){
		this.custodyChildren = children;
	}
	public Address getAddress(){
		return address;
	}
	public void setAddress(Address address){
		this.address = address;
	}
	
	public boolean hasSpouse(){
		return spouse!=null;
	}
	public boolean hasCohabitant(){
		return cohabitant!=null;
	}
	public boolean hasAddress(){
		return address!=null;
	}
	public boolean  hasParentialChildren(){
		return parentialChildren!=null && !parentialChildren.isEmpty();
	}
	public boolean  hasCustodyChildren(){
		return custodyChildren!=null && !custodyChildren.isEmpty();
	}
	
	public boolean hasChildren(){
		return hasParentialChildren() || hasCustodyChildren();
	}
	
	public Collection getChildren(){
		Collection children = new Vector();
		Map childMap = new Hashtable();
		if(hasParentialChildren()){
			for (Iterator iter = parentialChildren.iterator(); iter.hasNext();) {
				User child = (User) iter.next();
				if(!childMap.containsKey(child.getPrimaryKey())){
					children.add(child);
					childMap.put(child.getPrimaryKey(),child.getPrimaryKey());
				}
			}
		}
		if(hasCustodyChildren()){
			for (Iterator iter = custodyChildren.iterator(); iter.hasNext();) {
				User child = (User) iter.next();
				if(!childMap.containsKey(child.getPrimaryKey())){
					children.add(child);
					childMap.put(child.getPrimaryKey(),child.getPrimaryKey());
				}
			}
		}
		return children;
	}
	
}
