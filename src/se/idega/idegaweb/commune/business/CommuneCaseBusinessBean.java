package se.idega.idegaweb.commune.business;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.CaseCode;
import com.idega.business.IBOServiceBean;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author
 * @version 1.0
 */

public class CommuneCaseBusinessBean extends IBOServiceBean implements CommuneCaseBusiness
{
	private CaseCode[] userHiddenCaseCodes;
	
	public CaseBusiness getCaseBusiness()throws RemoteException{
		return (CaseBusiness)this.getServiceInstance(CaseBusiness.class);
	}
	
	public MessageBusiness getMessageBusiness()throws RemoteException{
		return (MessageBusiness)this.getServiceInstance(MessageBusiness.class);
	}
	
	
	
	public CaseCode[] getUserHiddenCaseCodes(){
		if(userHiddenCaseCodes==null){
			try{
				userHiddenCaseCodes = new CaseCode[3];
				//userHiddenCaseCodes = new CaseCode[2];	
				userHiddenCaseCodes[0]=getMessageBusiness().getCaseCodePrintedLetterMessage();
				userHiddenCaseCodes[1]=getMessageBusiness().getCaseCodeUserMessage();
				userHiddenCaseCodes[2]=getMessageBusiness().getCaseCodeSystemArchivationMessage();
			}
			catch(Exception e){
				e.printStackTrace();	
			}
		}
		return userHiddenCaseCodes;
	}
	
	public Collection getAllCasesDefaultVisibleForUser(User user, int startingCase, int numberOfCases) throws RemoteException, FinderException{
		Collection cases = getCaseBusiness().getAllCasesForUserExceptCodes(user,getUserHiddenCaseCodes(), startingCase, numberOfCases);
		return cases;	
	}
	
	public Collection getAllCasesDefaultVisibleForGroup(Group group) throws RemoteException, FinderException{
		Collection cases = getCaseBusiness().getAllCasesForGroupExceptCodes(group,getUserHiddenCaseCodes());
		return cases;	
	}	

}