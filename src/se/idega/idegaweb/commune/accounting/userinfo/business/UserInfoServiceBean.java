/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.business;


import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoChildrenFound;
import is.idega.idegaweb.member.business.NoCohabitantFound;
import is.idega.idegaweb.member.business.NoCustodianFound;
import is.idega.idegaweb.member.business.NoSpouseFound;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;


import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncome;
import se.idega.idegaweb.commune.accounting.userinfo.data.BruttoIncomeHome;
import se.idega.idegaweb.commune.accounting.userinfo.data.HouseHoldFamily;
import se.idega.idegaweb.commune.accounting.userinfo.data.InvoiceReceiver;
import se.idega.idegaweb.commune.accounting.userinfo.data.InvoiceReceiverHome;
import se.idega.idegaweb.commune.accounting.userinfo.data.SortableSibling;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOStoreException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * UserInfoServiceBean
 * @author aron 
 * @version 1.0
 */

public class UserInfoServiceBean extends IBOServiceBean implements UserInfoService {
	
	public BruttoIncome createBruttoIncome(Integer userID,Float income, Date validFrom,Integer creatorID) throws RemoteException{
		BruttoIncome bruttoIncome = null;
		try {
			bruttoIncome = getBruttoIncomeHome().create();
			bruttoIncome.setIncome(income);
			bruttoIncome.setValidFrom((java.sql.Date)validFrom);
			bruttoIncome.setUser(userID);
			if (creatorID != null) {
				bruttoIncome.setCreator(creatorID);
			}
			bruttoIncome.setCreated(new Timestamp(System.currentTimeMillis()));
			bruttoIncome.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		return bruttoIncome;
	}
	
	public BruttoIncomeHome getBruttoIncomeHome() throws RemoteException{
		return (BruttoIncomeHome) getIDOHome(BruttoIncome.class);
	}

	/**
	 * Sets the specified user as invoice receiver.
	 * @param user the user to set as invoice receiver
	 * @author Anders Lindman
	 */
	public InvoiceReceiver createInvoiceReceiver(User user) {
		InvoiceReceiver ir = null;
		try {
			InvoiceReceiverHome home = getInvoiceReceiverHome();
			try {
				ir = home.findByPrimaryKey(user.getPrimaryKey());
			} catch (FinderException e) {
				ir = home.create();
				ir.setUser(user);
			}
			ir.setIsReceiver(true);
			ir.store();
		} catch (Exception e) {}
		return ir;
	}
	
	/**
	 * @author Anders Lindman 
	 */
	public InvoiceReceiverHome getInvoiceReceiverHome() throws RemoteException {
		return (InvoiceReceiverHome) getIDOHome(InvoiceReceiver.class);
	}
	
	public ChildCareContractHome getChildCareContractHome() throws RemoteException {
		return (ChildCareContractHome) getIDOHome(ChildCareContract.class);
	}
	
	/**
	 * Returns true if the user with the specified id is an invoice receiver.
	 * @author Anders Lindman
	 */
	public boolean isInvoiceReceiver(int userId) {
		boolean isReceiver = false;
		try {
			InvoiceReceiver ir = getInvoiceReceiverHome().findByUser(userId);
			isReceiver = ir.getIsReceiver();
		} catch (Exception e) {}
		return isReceiver;
	}
	
	/**
	 * Returns true if the specified user is an invoice receiver.
	 * @author Anders Lindman
	 */
	public boolean isInvoiceReceiver(User user) {
		int userId = -1;
		try {
			userId = ((Integer) user.getPrimaryKey()).intValue();
		} catch (Exception e) {}
		return isInvoiceReceiver(userId);
	}

	/**
	 * Returns a HouseHoldFamily created from head of family
	 * @param headOfFamilly
	 * @return
	 */
	public HouseHoldFamily getHouseHoldFamily(User head)throws RemoteException{
		if(head!=null){
			CommuneUserBusiness userService = (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
			MemberFamilyLogic familyService = userService.getMemberFamilyLogic();
			User spouse = null;
			try {
				spouse = familyService.getSpouseFor(head);
			}
			catch (NoSpouseFound e) {
				
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			User cohabitant = null;
			try {
				cohabitant = familyService.getCohabitantFor(head);
			}
			catch (NoCohabitantFound e1) {
				
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			Collection parentialChildren= null;
			try {
				parentialChildren = familyService.getChildrenFor(head);
			}
			catch (NoChildrenFound e2) {
				
			}
			catch (RemoteException e2) {
				e2.printStackTrace();
			}
			Collection custodyChildren= null;
			try {
				custodyChildren = familyService.getChildrenInCustodyOf(head);
			}
			catch (NoChildrenFound e3) {
				
			}
			catch (RemoteException e3) {
				e3.printStackTrace();
			}
			Address address = userService.getUserAddress1(((Integer)head.getPrimaryKey()).intValue());
			HouseHoldFamily family = new HouseHoldFamily(head);
			Address addr;
			if(address!=null){
				family.setAddress(address);
				if(spouse!=null){
					addr = userService.getUserAddress1(((Integer)spouse.getPrimaryKey()).intValue());
					if(compareAddresses(addr,address)){
						family.setSpouse(spouse);
					}
				}
				if(cohabitant!=null){
					// is spouse the same person as cohabitant
					//if(spouse!=null && !spouse.getPrimaryKey().toString().equals(cohabitant.getPrimaryKey().toString()) ){
						addr = userService.getUserAddress1(((Integer)cohabitant.getPrimaryKey()).intValue());
						if(compareAddresses(addr,address))
							family.setCohabitant(cohabitant);
					//}
				}
				if(parentialChildren!=null && !parentialChildren.isEmpty()){
					Collection childs = new Vector(parentialChildren.size());
					for (Iterator iter = parentialChildren.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						addr = userService.getUserAddress1(((Integer)child.getPrimaryKey()).intValue());
						if(compareAddresses(addr,address)){
							childs.add(child);
						}
					}
					if(!childs.isEmpty())
						family.setParentialChildren(childs);
				}
				if(custodyChildren!=null && !custodyChildren.isEmpty()){
					Collection childs = new Vector(custodyChildren.size());
					for (Iterator iter = custodyChildren.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						addr = userService.getUserAddress1(((Integer)child.getPrimaryKey()).intValue());
						if(compareAddresses(addr,address)){
							childs.add(child);
						}
					}
					if(!childs.isEmpty())
						family.setCustodyChildren(childs);
				}
			
			}
			return family;
		}
		return null;
	}
	private boolean compareAddresses(Address one,Address two){
		if(one!=null && two!=null){
			boolean b=  (one.getStreetName().equalsIgnoreCase(two.getStreetName())
					&& one.getStreetNumber().equalsIgnoreCase(two.getStreetNumber())
					&& comparePostal(one.getPostalCode(),two.getPostalCode()));
			return b;
		}
		return false;
	}
	
	private boolean comparePostal(PostalCode one,PostalCode two){
		if(one!=null && two !=null){
			boolean b =  (one.getPostalCode().equalsIgnoreCase(two.getPostalCode())
			&& one.getName().equalsIgnoreCase(two.getName()));
			return b;
		}
		
		return false;
	}


	public boolean isSameAddress (final Address address1,
																final Address address2) {
		if (null == address1 || null == address2) return false;
		final boolean isSameStreet = address1.getStreetAddress ()
				.equalsIgnoreCase	(address2.getStreetAddress ());
		final boolean isSameZipCode = address1.getPostalCode ().getPostalCode ()
				.equals (address2.getPostalCode ().getPostalCode ());
		return isSameStreet && isSameZipCode;
	}

	/**
	 * Returns the sibling order according to Check & Peng rules.
	 * Most importantly, it only involves children i pre-school
	 */
	public int getSiblingOrder(User child, IWTimestamp startPeriod) throws RemoteException, SiblingOrderException {
		return getSiblingOrder (child, new HashMap (), startPeriod);
	}

	private boolean hasValidContract
		(final User child, final IWTimestamp startPeriod) throws RemoteException {
		try {
			getChildCareContractHome ().findValidContractByChild
					(((Integer) child.getPrimaryKey()).intValue(),startPeriod.getDate());
			return true;
		} catch (FinderException e) {
			return false;
		}
	}

	/**
	 * Returns the sibling order according to Check & Peng rules.
	 * Most importantly, it only involves children i pre-school
	 */
	public int getSiblingOrder(User child, Map siblingOrders, IWTimestamp startPeriod) throws RemoteException, SiblingOrderException {
		UserBusiness userBus = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		//First see if the child already has been given a sibling order
		Integer order = (Integer)siblingOrders.get(child.getPrimaryKey());
		if(order != null)
		{
			return order.intValue();	//Sibling order already calculated.
		}
		if (!hasValidContract (child, startPeriod)) {
			throw new SiblingOrderException (child.getName() + " has no contract");
		}	
		TreeSet sortedSiblings = new TreeSet();		//Container for the siblings that keeps them in sorted order
		Address childAddress = userBus.getUsersMainAddress(child);
		if (null == childAddress) {
			throw new SiblingOrderException (child.getPersonalID() + " " + child.getName () + " has no Address");
		}

		//Add the child to the sibbling collection right away to make sure it will be in there
		//This should really happen in the main loop below as well, but this is done since
		//the realtionships seem to be a bit unreliable
		sortedSiblings.add(new SortableSibling(child));

		//Gather up a collection of the parents and their cohabitants
		Collection parents;		//Collection parents only hold the biological parents
		MemberFamilyLogic familyLogic = (MemberFamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), MemberFamilyLogic.class);
		try {
			parents = familyLogic.getCustodiansFor(child);
		} catch (NoCustodianFound e1) {
			throw new SiblingOrderException("No custodians found for the child.");
		}
		//Adults hold all the adults that could be living on the same address
		Collection adults = new ArrayList(parents);
		Iterator parentIter = parents.iterator();
		//Itterate through parents
		while(parentIter.hasNext()){
			User parent = (User)parentIter.next();
			try {
				adults.add(familyLogic.getCohabitantFor(parent));
			} catch (NoCohabitantFound e) {
				// no problem, custodian don't need to have cohabitant
			}
			try {
				adults.add(familyLogic.getSpouseFor(parent));
			} catch (NoSpouseFound e) {
				// no problem, custodian don't need to have spouse
			}
		}

		//Loop through all adults
		Iterator adultIter = adults.iterator();
		while(adultIter.hasNext()){
			User adult = (User)adultIter.next();
			Iterator siblingsIter;
			try {
				siblingsIter = familyLogic.getChildrenInCustodyOf (adult).iterator();
				//Itterate through their kids
				while(siblingsIter.hasNext())
				{
					User sibling = (User) siblingsIter.next();		
					try {
						Address siblingAddress = userBus.getUsersMainAddress(sibling);
						if (hasValidContract (sibling, startPeriod)
								&& isSameAddress (childAddress, siblingAddress)) {
							SortableSibling sortableSibling = new SortableSibling(sibling);
							if(!sortedSiblings.contains(sortableSibling)){
								sortedSiblings.add(sortableSibling);
							}
						}
					} catch (NullPointerException e) {
						e.printStackTrace ();
						String childName = child.getPersonalID() + " " + child.getName () + (child.getPrimaryKey ().equals (sibling.getPrimaryKey ()) ? "" : " or " + sibling.getPersonalID() + " " + sibling.getName ());
						throw new SiblingOrderException (childName + " probably has missing fields in address");
					}
				}
			} catch (RemoteException e2) {
				e2.printStackTrace();
				throw new SiblingOrderException (child.getName() + " invoice.DBError");
			} catch (NoChildrenFound e) {
				e.printStackTrace();
				throw new SiblingOrderException (child.getName() + " invoice.NoChildrenFound");
			}
		}
	
		//Store the sorting order
		Iterator sortedIter = sortedSiblings.iterator();
		int orderNr = 1;
		while(sortedIter.hasNext()){
			SortableSibling sortableSibling = (SortableSibling)sortedIter.next();
			siblingOrders.put(sortableSibling.getSibling().getPrimaryKey(),new Integer(orderNr));
			log("Added child "+sortableSibling.getSibling()+" as sibling "+orderNr+" out of "+sortedSiblings.size());
			orderNr++;
		}
	
		//Look up the order of the child that started the whole thing
		order = (Integer)siblingOrders.get(child.getPrimaryKey());
		if(order != null)
		{
			return order.intValue();
		}
		//This should really never happen
		throw new SiblingOrderException("Could not find the sibling order.");
	}

}
