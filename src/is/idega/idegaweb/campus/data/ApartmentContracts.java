package is.idega.idegaweb.campus.data;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;

import java.util.Collection;
import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.building.data.Apartment;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;
/**
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class ApartmentContracts {
	private Apartment apartment;
	private Collection contracts;
	private IWTimestamp nextDate;
	private String[] statuses;
	
	public ApartmentContracts(){
		this(null,null);
	}
	public ApartmentContracts(Apartment apartment,String[] statuses) {
		this.apartment = apartment;
		nextDate = IWTimestamp.RightNow();
		this.statuses = statuses;
		lookupContracts();
	}
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}
	public void addContract(Contract contract) {
		contracts.add(contract);
		checkAdd(contract);
	}
	public boolean hasContracts(){
		return contracts!=null;
	}
	public void setContracts(Collection contracts) {
		contracts.addAll(contracts);
		java.util.Iterator I = contracts.iterator();
		while (I.hasNext()) {
			Contract contract = (Contract) I.next();
			checkAdd(contract);
		}
	}
	private void checkAdd(Contract C) {
		//if(C.getStatus().equals(C.statusSigned) || C.getStatus().equals(C.statusResigned)){
		IWTimestamp date = new IWTimestamp(C.getValidTo());
		if (nextDate != null && date.isLaterThan(nextDate)) {
			nextDate = date;
			//System.err.println(date.toString());
		}
		//}
	}
	public Apartment getApartment() {
		return apartment;
	}
	public Collection getContracts() {
		return contracts;
	}
	public Date getNextDate() {
		return (Date) nextDate.getDate();
	}
	
	private void lookupContracts() {
		if(apartment!=null){
			try {
				ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
				if(statuses!=null){
					Collection cons = cHome.findByApartmentAndStatus((Integer) apartment.getPrimaryKey(),statuses);
					setContracts(cons);
				}
				else{
					Collection cons = cHome.findByApartmentID((Integer) apartment.getPrimaryKey());
					setContracts(cons);
				}
			}
			catch (IDOLookupException e) {
				e.printStackTrace();
			}
			catch (EJBException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
	}
}
