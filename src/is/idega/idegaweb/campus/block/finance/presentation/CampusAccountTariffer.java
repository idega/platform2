/*
 * Created on Mar 30, 2004
 *
 */
package is.idega.idegaweb.campus.block.finance.presentation;

import is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusiness;
import is.idega.idegaweb.campus.data.ContractAccountApartment;
import is.idega.idegaweb.campus.data.ContractAccountApartmentHome;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.presentation.AccountTariffer;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;

/**
 * CampusAccountTariffer
 * @author aron 
 * @version 1.0
 */
public class CampusAccountTariffer extends AccountTariffer {
	
	
	
	/* (non-Javadoc)
	 * @see com.idega.block.finance.presentation.AccountTariffer#getAssessmentService()
	 */
	protected AssessmentBusiness getAssessmentService(IWApplicationContext iwac) throws RemoteException {
		return (CampusAssessmentBusiness) IBOLookup.getServiceInstance(iwac,CampusAssessmentBusiness.class);
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.presentation.AccountTariffer#getExternalIDParameter()
	 */
	protected String getExternalIDParameter() {
		return "ca_aprt_id";
	}
	/* (non-Javadoc)
	 * @see com.idega.block.finance.presentation.AccountTariffer#getExternalInfo(com.idega.presentation.IWContext)
	 */
	protected PresentationObject getExternalInfo(IWContext iwc) {
		DataTable T = getDataTable();
		T.setUseBottom(false);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setTitlesVertical(false);
		if(getAccountId()!=null){
		try {
			int row = 1;
			T.add(getHeader(localize("apartment","Apartment")),1,row);
			T.add(getHeader(localize("contract_period","Contract period")),2,row);
			Collection caas =getContractAccountApartmentHome().findByAccount(getAccountId());
			for (Iterator iter = caas.iterator(); iter.hasNext();) {
				ContractAccountApartment caa = (ContractAccountApartment) iter.next();
				
				Apartment apartment = getApartmentHome().findByPrimaryKey(new Integer(caa.getApartmentId()));
				Building building = getBuildingHome().findByPrimaryKey(new Integer(caa.getBuildingId()));
				T.add(getText(apartment.getName()+" ,"+building.getName()),1,1);
				
				DateFormat df = getShortDateFormat(iwc.getCurrentLocale());
				T.add(getText(df.format(caa.getValidFrom())+" - "+df.format(caa.getValidTo())),2,2);
				RadioButton rb = new RadioButton(getExternalIDParameter(),String.valueOf(caa.getApartmentId()));
				T.add(rb,3,2);
			}
			
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
		e.printStackTrace();
		}
		}

		
		
		return T;
	}
	
	public ApartmentHome getApartmentHome()throws RemoteException{
		return (ApartmentHome)IDOLookup.getHome(Apartment.class);
	}
	
	public ContractAccountApartmentHome getContractAccountApartmentHome()throws RemoteException{
		return (ContractAccountApartmentHome)IDOLookup.getHome(ContractAccountApartment.class);
	}
	
	public BuildingHome getBuildingHome()throws RemoteException{
		return (BuildingHome)IDOLookup.getHome(Building.class);
	}
}
