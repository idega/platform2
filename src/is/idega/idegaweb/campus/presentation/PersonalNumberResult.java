package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractBusiness;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;

import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.util.TextFormat;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class PersonalNumberResult extends Block implements Campus {

  private IWBundle iwb;
  private IWResourceBundle iwrb;
  private String SSN = null;
  private Integer applicantID = null;
  private TextFormat tf;
  private DateFormat df;

  public PersonalNumberResult() {
  }

  public void control(IWContext iwc){
    parse(iwc);
    add(getSSNResult(iwc));
  }

  public String getBundleIdentifier(){
    return CAMPUS_BUNDLE_IDENTIFIER;
  }

  public void parse(IWContext iwc){
    if(iwc.isParameterSet(PersonalNumberSearch.PERSONAL_NUMBER))
      SSN = iwc.getParameter(PersonalNumberSearch.PERSONAL_NUMBER);
    else if(iwc.isParameterSet("appl_info")){
    	applicantID = Integer.valueOf(iwc.getParameter("appl_info"));
    }
  }

  private PresentationObject getSSNResult(IWContext iwc){
    Table T = new Table();
      int col = 1,row = 1;

    if(SSN!=null){
      try{
        List applicants = ContractFinder.findAllNonContractApplicationsBySSN(SSN);
        if(applicants!=null  && !applicants.isEmpty()){
          T.add(getNonContractApplicantInfo(applicants),col,row++);
        }
        List contracts = ContractFinder.findAllContractsBySSN(SSN);
        if(contracts!=null && !contracts.isEmpty()){
          T.add(getContractInfo(contracts),col,row++);
        }
      }
      catch(com.idega.data.IDOFinderException ex){
        ex.printStackTrace();
        T.add(iwrb.getLocalizedString("error_finding_from_ssn","Error in ssn search"),col,row);
      }
      catch(java.sql.SQLException ex){
        ex.printStackTrace();
      }
    }
    else if(applicantID!=null){
    	try {
			Applicant applicant = ((ApplicantHome)IDOLookup.getHome(Applicant.class)).findByPrimaryKey(applicantID);
			T.add(getApplicantInfo(applicant),col,row);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
			T.add(iwrb.getLocalizedString("error_finding_from_id","Error in id search"),col,row);
		}
    }
    else
      T.add(iwrb.getLocalizedString("warning_no_ssn_provided","No ssn provided"),col,row);

    return T;
  }

  private PresentationObject getNonContractApplicantInfo(List applicants){
    DataTable T = new DataTable();
    T.setUseBottom(false);
    T.setUseTop(false);
    T.addTitle(iwrb.getLocalizedString("applicants_without_contracts","Applicants without contracts"));
    T.setTitlesHorizontal(true);
    int col = 1;
    int row = 1;
    T.add(tf.format(iwrb.getLocalizedString("id","ID"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("ssn","SSN"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("name","Name"),tf.HEADER),col++,row);
	//T.add(tf.format(iwrb.getLocalizedString("legal_residence","Legal residence"),tf.HEADER),col++,row);
    //T.add(tf.format(iwrb.getLocalizedString("residence","Residence"),tf.HEADER),col++,row);
	//T.add(tf.format(iwrb.getLocalizedString("zip","Zip"),tf.HEADER),col++,row);
	//T.add(tf.format(iwrb.getLocalizedString("phone","Phone"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("mobile","Mobile"),tf.HEADER),col++,row);

    Iterator iter = applicants.iterator();
    while(iter.hasNext()){
      col = 1;
      row++;
      Applicant applicant = (Applicant) iter.next();
      T.add(getApplicantInfoLink(tf.format(applicant.getPrimaryKey().toString()),(Integer)applicant.getPrimaryKey()),col++,row);
      T.add(tf.format(applicant.getSSN()),col++,row);
      T.add(tf.format(applicant.getFullName()),col++,row);
      T.add(tf.format(applicant.getLegalResidence()),col++,row);
	  T.add(tf.format(applicant.getResidence()),col++,row);
	  T.add(tf.format(applicant.getPO()),col++,row);
	  T.add(tf.format(applicant.getResidencePhone()),col++,row);
      T.add(tf.format(applicant.getMobilePhone()),col++,row);
    }
    return T;
  }

  private PresentationObject getContractInfo(List contracts)throws java.sql.SQLException{
    DataTable T = new DataTable();
     T.setUseBottom(false);
    T.setUseTop(false);
    T.addTitle(iwrb.getLocalizedString("applicants_with_contracts","Applicants with contracts"));
    T.setTitlesHorizontal(true);
    int col = 1;
    int row = 1;
    T.add(tf.format(iwrb.getLocalizedString("id","ID"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("ssn","SSN"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("name","Name"),tf.HEADER),col++,row);
    //T.add(tf.format(iwrb.getLocalizedString("address","Address"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("mobile","Mobile"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("apartment","Apartment"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("from","From"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("to","To"),tf.HEADER),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("status","Status"),tf.HEADER),col++,row);

    ApplicantHome ahome = (ApplicantHome)IDOLookup.getHomeLegacy(Applicant.class);
    Iterator iter = contracts.iterator();
    while(iter.hasNext()){
      col = 1;
      row++;
      Contract contract = (Contract) iter.next();
      Applicant applicant = ahome.findByPrimaryKeyLegacy(contract.getApplicantId().intValue());
      Apartment apartment = BuildingCacher.getApartment(contract.getApartmentId().intValue());
      T.add(getApplicantInfoLink(tf.format(applicant.getPrimaryKey().toString()),(Integer)applicant.getPrimaryKey()),col++,row);
      T.add(tf.format(applicant.getSSN()),col++,row);
      T.add(tf.format(applicant.getFullName()),col++,row);
      //T.add(tf.format(applicant.getLegalResidence()),col++,row);
      T.add(tf.format(applicant.getMobilePhone()),col++,row);
      T.add(tf.format(BuildingCacher.getApartmentString(apartment.getID())),col++,row);
      T.add(tf.format(df.format(contract.getValidFrom())),col++,row);
      T.add(tf.format(df.format(contract.getValidTo())),col++,row);
      T.add(tf.format(ContractBusiness.getLocalizedStatus(iwrb,contract.getStatus())),col++,row);
    }
    return T;
  }
  
  public PresentationObject getApplicantInfo(Applicant applicant){
	DataTable T = new DataTable();
		T.setUseBottom(false);
		T.setUseTop(false);
		T.addTitle(iwrb.getLocalizedString("applicant_info","Applicant info"));
		T.setTitlesHorizontal(false);
		int col = 1;
		int row = 1;
		T.add(tf.format(iwrb.getLocalizedString("id","ID"),tf.HEADER),col,row++);
		T.add(tf.format(iwrb.getLocalizedString("ssn","SSN"),tf.HEADER),col,row++);
		T.add(tf.format(iwrb.getLocalizedString("name","Name"),tf.HEADER),col,row++);
		T.add(tf.format(iwrb.getLocalizedString("legal_residence","Legal residence"),tf.HEADER),col,row++);
		T.add(tf.format(iwrb.getLocalizedString("residence","Residence"),tf.HEADER),col,row++);
		T.add(tf.format(iwrb.getLocalizedString("zip","Zip"),tf.HEADER),col,row++);
		T.add(tf.format(iwrb.getLocalizedString("phone","Phone"),tf.HEADER),col,row++);
		T.add(tf.format(iwrb.getLocalizedString("mobile","Mobile"),tf.HEADER),col,row++);

		if(applicant!=null){
		  row = 1;
		  col++;
		  T.add(tf.format(applicant.getID()),col,row++);
		  T.add(getSSNLink(tf.format(applicant.getSSN()),applicant.getSSN()),col,row++);
		  T.add(tf.format(applicant.getFullName()),col,row++);
		  T.add(tf.format(applicant.getLegalResidence()),col,row++);
		  T.add(tf.format(applicant.getResidence()),col,row++);
		  T.add(tf.format(applicant.getPO()),col,row++);
		  T.add(tf.format(applicant.getResidencePhone()),col,row++);
		  T.add(tf.format(applicant.getMobilePhone()),col,row++);
		}
		return T;
  }
  
  public Link getApplicantInfoLink(Text text, Integer applicantID){
  	Link L = new Link(text);
  	L.addParameter("appl_info",applicantID.toString());
  	return L;
  }
  
  public Link getSSNLink(Text text, String ssn){
	 Link L = new Link(text);
	 L.addParameter(PersonalNumberSearch.PERSONAL_NUMBER,ssn);
	 return L;
   }

  public void main(IWContext iwc){
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tf = TextFormat.getInstance();
    df = DateFormat.getDateInstance(df.SHORT,iwc.getCurrentLocale());
    control(iwc);
  }
}