/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.business;


import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCohabitantFound;
import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.business.NoSpouseFound;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashSet;
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
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;

import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.location.data.Address;
import com.idega.data.IDOStoreException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.TimePeriod;

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
			FamilyLogic familyService = userService.getMemberFamilyLogic();
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
					if(isEqual(address,addr)){
						family.setSpouse(spouse);
					}
				}
				if(cohabitant!=null){
					// is spouse the same person as cohabitant
					//if(spouse!=null && !spouse.getPrimaryKey().toString().equals(cohabitant.getPrimaryKey().toString()) ){
						addr = userService.getUserAddress1(((Integer)cohabitant.getPrimaryKey()).intValue());
						if(isEqual(address,addr))
							family.setCohabitant(cohabitant);
					//}
				}
				if(parentialChildren!=null && !parentialChildren.isEmpty()){
					Collection childs = new Vector(parentialChildren.size());
					for (Iterator iter = parentialChildren.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						addr = userService.getUserAddress1(((Integer)child.getPrimaryKey()).intValue());
						if(isEqual(address,addr)){
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
						if(isEqual(address,addr)){
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



	/**
	 * Returns the sibling order according to Check & Peng rules.
	 * Most importantly, it only involves children i pre-school
	 */
	public int getSiblingOrder(User child, TimePeriod period) throws RemoteException, SiblingOrderException {
		return getSiblingOrder (child, new HashMap (), period);
	}

	private boolean hasValidContract
		(final User child, final TimePeriod period) throws RemoteException {
		try {
			getChildCareContractHome ().findContractByChildAndPeriod (child, period);
			return true;
		} catch (FinderException e) {
			return false;
		}
	}

	/**
	 * Returns the sibling order according to Check & Peng rules.
	 * Most importantly, it only involves children i pre-school
	 */
	public int getSiblingOrder(User child, Map siblingOrders, TimePeriod period) throws RemoteException, SiblingOrderException {
		UserBusiness userBus = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		//First see if the child already has been given a sibling order
		Integer order = (Integer)siblingOrders.get(child.getPrimaryKey());
		if(order != null)
		{
			return order.intValue();	//Sibling order already calculated.
		}
		if (!hasValidContract (child, period)) {
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

		Collection adults;
		try {
			adults = getCustodiansAndTheirPartners(child);
		} catch (NoCustodianFound e1) {
			throw new SiblingOrderException("No custodians found for the child.");
		}

		//Loop through all adults
		Iterator adultIter = adults.iterator();
		while(adultIter.hasNext()){
			User adult = (User)adultIter.next();
			Iterator siblingsIter;
			try {
				siblingsIter = getMemberFamilyLogic ().getChildrenInCustodyOf (adult).iterator();
				//Itterate through their kids
				while(siblingsIter.hasNext())
				{
					User sibling = (User) siblingsIter.next();		
					try {
						Address siblingAddress = userBus.getUsersMainAddress(sibling);
						if (hasValidContract (sibling, period)
								&& isEqual (childAddress, siblingAddress)) {
							SortableSibling sortableSibling = new SortableSibling(sibling);
							if(!sortedSiblings.contains(sortableSibling)){
								sortedSiblings.add(sortableSibling);
							}
						}
					} catch (NullPointerException e) {
						e.printStackTrace ();
						String childName = child.getPersonalID() + " " + child.getName () + (child.getPrimaryKey ().equals (sibling.getPrimaryKey ()) ? "" : " or " + sibling.getPersonalID() + " " + sibling.getName ());
						throw new SiblingOrderException (childName + " probably has missing fields in address", e);
					}
				}
			} catch (NoChildrenFound e) {
				// no problem, all related adults dont have children, iterate to next
			} catch (RemoteException e2) {
				e2.printStackTrace();
				throw new SiblingOrderException (child.getName() + " invoice.DBError");
			}
		}
	
		//Store the sorting order
		Iterator sortedIter = sortedSiblings.iterator();
		int orderNr = 1;
		while(sortedIter.hasNext()){
			SortableSibling sortableSibling = (SortableSibling)sortedIter.next();
			siblingOrders.put(sortableSibling.getSibling().getPrimaryKey(),new Integer(orderNr));
//			log("Added child "+sortableSibling.getSibling()+" as sibling "+orderNr+" out of "+sortedSiblings.size());
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

	public Collection getCustodiansAndTheirPartners(User child) throws RemoteException, NoCustodianFound {
		//Gather up a collection of the parents and their cohabitants
		Collection parents = getMemberFamilyLogic ().getCustodiansFor(child);

		//Adults hold all the adults that could be living on the same address
		Collection adults = new HashSet(parents);
		Iterator parentIter = parents.iterator();
		//Itterate through parents
		while(parentIter.hasNext()){
			User parent = (User)parentIter.next();
			try {
				adults.add(getMemberFamilyLogic ().getCohabitantFor(parent));
			} catch (NoCohabitantFound e) {
				// no problem, custodian don't need to have cohabitant
			}
			try {
				adults.add(getMemberFamilyLogic ().getSpouseFor(parent));
			} catch (NoSpouseFound e) {
				// no problem, custodian don't need to have spouse
			}
		}
		return adults;
	}

	public Collection getCustodiansAndTheirPartnersAtSameAddress(User child) throws RemoteException, NoCustodianFound {
		Collection adults = getCustodiansAndTheirPartners (child);
		Collection result = new HashSet ();
		final Address childAddress = getUserBusiness ().getUsersMainAddress (child);
		for (Iterator i = adults.iterator (); i.hasNext ();) {
			final User adult = (User) i.next ();
			final Address adultAddress
					= getUserBusiness ().getUsersMainAddress(adult);
			if (null != childAddress && null != adultAddress
					&& isEqual (childAddress, adultAddress)) {
				result.add (adult);
			}
		}
		return result;
	}

	/**
	 * Compares two addresses on street name, the number part of street number,
	 * postal code and country. Address.isEqualTo compares the whole number part,
	 * including nb, 1tr etc., which isn't sufficiant here. This a formal request
	 * from the customer.
	 */
	private static boolean isEqual (final Address address1,
																	 final Address address2) {
		// In order to make this method as fast as possible...
		// - retreive fields lazy - return as soon as differ is known
		// - compare what's expected to be most differing field first
		// - fields joined from other tables with foreign keys are considered late

		try {
			// if this is the same address row, then return immediately
			if(null == address1 || null == address2){
				return false;
			}
			if (address1.getPrimaryKey ().equals (address2.getPrimaryKey ())) {
				return true;
			}
			if(null == address1.getStreetName() || null == address2.getStreetName()){
				return false;
			}
			if (address1.getStreetName ()
					.equalsIgnoreCase	(address2.getStreetName ())) {
				// they have same street name
				if (startingNumberPart (address1.getStreetNumber ())
						.equals	(startingNumberPart (address2.getStreetNumber ()))) {
					// they have same starting number
					if ((null != address1.getPostalCode() && null != address2.getPostalCode ()) &&
							address1.getPostalCode ().equals (address2.getPostalCode ())) {
						// they have same postal code
						if (address1.getCountryId () == address2.getCountryId ()) {
							// they have same country id
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private static String startingNumberPart (final String string) {
		return (string != null ? string : "").trim ().split ("\\D", 2) [0];
	}

	protected UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) getServiceInstance(UserBusiness.class);
	}

	protected FamilyLogic getMemberFamilyLogic() throws RemoteException {
		return (FamilyLogic) getServiceInstance(FamilyLogic.class);
	}
}
