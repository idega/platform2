/*
 * Created on May 29, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.contract.business;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractCategory;
import com.idega.block.contract.data.ContractCategoryHome;
import com.idega.block.contract.data.ContractHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.util.IWTimestamp;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */
public class ContractServiceBean extends IBOServiceBean implements ContractService {
	public boolean removeContractFile(int iFileId, int iContractID) {
		try {
			ICFile file = ((ICFileHome) IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(iFileId));
			Contract contract = ((ContractHome) IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(iContractID));
			contract.removeFileFromContract(file);
			//file.removeFrom(Contract.class,iContractID);
			file.remove();
			return true;
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
		catch (RemoveException e) {
			e.printStackTrace();
		} catch (IDORemoveRelationshipException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public  Contract createAndPrintContract(int userID, int iCategoryId) throws IDOLookupException{
			IWTimestamp today = IWTimestamp.RightNow();
			IWTimestamp tomorrow = IWTimestamp.RightNow().getNextDay();
			Contract theReturn = getContractHome().create(userID,iCategoryId,today,tomorrow,"C",(Map)null);
			ContractWriter.writePDF(((Integer)theReturn.getPrimaryKey()).intValue(),iCategoryId,"ContractX.PDF");
			return theReturn;
		}
		
	public  ContractHome getContractHome() throws IDOLookupException{
			return (ContractHome) IDOLookup.getHome(Contract.class);
		}
	
		public  ContractCategoryHome getContractCategoryHome() throws IDOLookupException{
			return (ContractCategoryHome)IDOLookup.getHome(ContractCategory.class);
		}
}
