package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
/*
import java.util.ArrayList;

import javax.ejb.CreateException;

import se.idega.idegaweb.commune.accounting.regulations.business.IntervalConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionParameter;
import com.idega.util.Age;
*/
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusinessHome;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;

import com.idega.data.IDOLookup;

/**
 * @author Joakim
 */
public class SubventionCalculation implements SpecialCalculationType{
	
	public void handle(Regulation reg, ChildCareContract cont){
/*
		try {
			RegulationsBusiness regBus = getRegulationsBusinessHome().create();
			//TODO (JJ) get the childcareID
			String operationTypechildcare = "";
			String childcareType = "";
			//TODO (JJ) Tryggvi, Goran and Anders trying to figure out how this should work!
			//childcareType = cont.getApplication().getProvider().;
//			childcareID = Operation.getChildcareID();
			//int age = cont.getChild().get???
			int hours = cont.getCareTime();
			Age age = new Age(cont.getChild().getDateOfBirth());
			ArrayList conditions = new ArrayList();
			ConditionParameter param = new ConditionParameter();
			conditions.add(new ConditionParameter(IntervalConstant.ACTIVITY,childcareType));
			conditions.add(new ConditionParameter(IntervalConstant.HOURS,new Integer(hours)));
			conditions.add(new ConditionParameter(IntervalConstant.AGE,new Integer(age.getYears())));
			
			//Select a specific row from the regulation, given the following restrictions
			Regulation checkReg = regBus.getRegulationBy(
				operationTypechildcare,										//The ID that selects barnomsorg in the regulation
				PaymentFlowConstant.OUT, 							//The payment flow is out
				new java.sql.Date(new java.util.Date().getTime()),	//The Should be for the currently valid daterange
				RegSpecConstant.CHECK,								//The ruleSpecType shall be Check
				RuleTypeConstant.DERIVED,							//The conditiontype
				conditions);										//The conditions that need to fulfilled
				
			int checkAmount = checkReg.getAmount().intValue();
		} catch (RemoteException e) {
			// TODO (JJ) Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO (JJ) Auto-generated catch block
			e.printStackTrace();
		}
*/
	}
	
	public RegulationsBusinessHome getRegulationsBusinessHome() throws RemoteException {
		return (RegulationsBusinessHome) IDOLookup.getHome(RegulationsBusiness.class);
	}
}
