package is.idega.block.nationalregister.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.business.NoParentFound;
import is.idega.block.family.business.NoSiblingFound;
import is.idega.block.family.business.NoSpouseFound;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.idega.business.IBOLookupException;
import com.idega.user.data.User;


/**
 * @author gimmi
 */
public class Relations{
	private User user = null;
	private User spouse = null;
	private Collection child = new ArrayList();
	private Collection parent = new ArrayList();
	private Collection isCustodianFor = new ArrayList();
	private Collection hasCustodian = new ArrayList();
	private Collection sibling = new ArrayList();
	private FamilyLogic memFamLog = null;
	
	public Relations(FamilyLogic famLog) {
		memFamLog = famLog;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setSpouse(User spouse) {
		this.spouse = spouse;
	}
	
	public void addChild(User child) {
		if (!this.child.contains(child)) {
			this.child.add(child);
		}
	}
	
	public void addParent(User parent) {
		if (!this.parent.contains(parent)) {
			this.parent.add(parent);
		}
	}
	
	public void addIsCustodianFor(User user) {
		if (!this.isCustodianFor.contains(user)) {
			this.isCustodianFor.add(user);
		}
	}
	
	public void addHasCustodian(User user) {
		if (!this.hasCustodian.contains(user)) {
			this.hasCustodian.add(user);
		}
	}	
	
	public void addSibling(User user) {
		if (!this.sibling.contains(user)) {
			this.sibling.add(user);
		}
	}		

	public User getSpouse() {
		return spouse;
	}
	
	public Collection getHasCustodians() {
		return this.hasCustodian;
	}
	
	public Collection getIsCustodianFor() {
		return this.isCustodianFor;
	}
	
	public Collection getParents() {
		return this.parent;
	}
	
	public Collection getSiblings() {
		return this.sibling;
	}
	
	public Collection getChildren() {
		return this.child;
	}
	
	public void dumpInfo(){
		System.out.println("Relations for user "+user.getName()+" - "+user.getPersonalID());
		if(null!=spouse){
			System.out.println("spouse: "+spouse+" - "+spouse.getPersonalID());
		}
		Iterator iter = child.iterator();
		while(iter.hasNext()){
			User user = (User)iter.next();
			System.out.println("child: "+user.getName()+" - "+user.getPersonalID());
		}
		iter = parent.iterator();
		while(iter.hasNext()){
			User user = (User)iter.next();
			System.out.println("parent: "+user.getName()+" - "+user.getPersonalID());
		}
		iter = isCustodianFor.iterator();
		while(iter.hasNext()){
			User user = (User)iter.next();
			System.out.println("is custodian for: "+user.getName()+" - "+user.getPersonalID());
		}
		iter = hasCustodian.iterator();
		while(iter.hasNext()){
			User user = (User)iter.next();
			System.out.println("has custodian: "+user.getName()+" - "+user.getPersonalID());
		}
		iter = sibling.iterator();
		while(iter.hasNext()){
			User user = (User)iter.next();
			System.out.println("sibling: "+user.getName()+" - "+user.getPersonalID());
		}
	}
	
	public void setForUser(User user) throws IBOLookupException, RemoteException{
		this.user = user;
		try {
			spouse = memFamLog.getSpouseFor(user);
		}
		catch (NoSpouseFound e) {
			spouse = null;
		}

		try {
			child = memFamLog.getChildrenFor(user);
		}
		catch (NoChildrenFound e1) {
		}

		try {
			parent = memFamLog.getParentsFor(user);
		}
		catch (NoParentFound e) {
		}
		
		try {
			isCustodianFor = memFamLog.getChildrenInCustodyOf(user);
		}
		catch (NoChildrenFound e) {
		}
		
		try {
			hasCustodian = memFamLog.getCustodiansFor(user);
		}
		catch (NoCustodianFound e) {
		}

		try {
			sibling = memFamLog.getSiblingsFor(user);
		}
		catch (NoSiblingFound e) {
		}
	}

	public static Relations inANotB(Relations a, Relations b, FamilyLogic famLoc) throws IllegalArgumentException {
		Relations c = new Relations(famLoc);
		if (a.user != b.user) {
			throw new IllegalArgumentException("Relations must contain same users");
		} else {
			c.user = a.user;
		}
		c.spouse = a.spouse;
		c.child = new ArrayList(a.child);
		c.hasCustodian = new ArrayList(a.hasCustodian);
		c.isCustodianFor = new ArrayList(a.isCustodianFor);
		c.parent = new ArrayList(a.parent);
		c.sibling = new ArrayList(a.sibling);
		

		if (a.spouse != null && a.spouse.equals(b.spouse)) {
			c.spouse = null;
		}
		
		Iterator iter = b.child.iterator();
		while (iter.hasNext()) {
			c.child.remove((User) iter.next());
		}
		iter = b.hasCustodian.iterator();
		while (iter.hasNext()) {
			c.hasCustodian.remove((User) iter.next());
		}
		iter = b.isCustodianFor.iterator();
		while (iter.hasNext()) {
			c.isCustodianFor.remove((User) iter.next());
		}
		iter = b.parent.iterator();
		while (iter.hasNext()) {
			c.parent.remove((User) iter.next());
		}
		iter = b.sibling.iterator();
		while (iter.hasNext()) {
			c.sibling.remove((User) iter.next());
		}
		
		return c;
	}
}
