package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.invoice.business.BatchRunQueue;
import se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness;
import se.idega.idegaweb.commune.accounting.invoice.business.SchoolCategoryNotFoundException;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.CalendarMonth;

/**
 * Starts the batch run that will create billing and invoicing information
 * according to the parameters set in the UI.
 * 
 * @author Roar
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceBusiness
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class TestPosts extends InvoiceBatchStarter {

    private static final String KEY_PROVIDER = "provider";

    private static String PAR_PROVIDER = KEY_PROVIDER;

    private School _currentSchool = null;

    private SchoolCategory _currentSchoolCategory = null;

    protected PresentationObject getShoolDropDown() {
        final School loggedInUsersProvider = getSchoolByLoggedInUser(getIWContext());
        if (loggedInUsersProvider != null) {
            return new HiddenInput(PAR_PROVIDER, ""
                    + loggedInUsersProvider.getPrimaryKey());

        } else if (isCentralAdministrator(getIWContext())) {
            DropdownMenu dropDown = getDropdownMenu(PAR_PROVIDER,
                    getSchools(getIWContext()), "getSchoolName");
            dropDown.setToSubmit(false);
            if (getSelectedSchool() != null) {
                dropDown.setSelectedElement((String) getSelectedSchool()
                        .getPrimaryKey());
            }
            return getInputContainer("cacc_testposts_school", "School",
                    dropDown);

        } else {
            return new Text("");
        }
    }

	protected void handleSave(IWContext iwc, String schoolCategory) {
		if(isBatchAlreadyRun()){
			add(getLocalizedText("se.idega.idegaweb.commune.accounting.invoice.presentation.testposts.already_run", "Batch already run"));	
		}else{
			try{
				InvoiceBusiness invoiceBusiness = (InvoiceBusiness)IBOLookup.getServiceInstance(getIWContext(), InvoiceBusiness.class);
				invoiceBusiness.removeTestRecordsForProvider(new CalendarMonth(getParamMonth()), schoolCategory, getSelectedSchool());
			}catch(IBOLookupException ex){
				ex.printStackTrace();
			}catch(RemoveException ex){
				ex.printStackTrace();
			}catch(RemoteException ex) {
				ex.printStackTrace();
			}
			super.handleSave(iwc, schoolCategory); //This call will result addBatchRunToQueue( ) being called
		}
	}
	
	private boolean isBatchAlreadyRun(){
		IWContext iwc = getIWContext();
		boolean batchRun = true;		

		PaymentHeaderHome home = null;

		try {
			home = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class));
		} catch (IDOLookupException e) {
			e.printStackTrace();
			return true;
		}		
				
		//Todo: rather use a has- method than a find- method for this
		if (getParamMonth() != null){
		}
		try{
			home.findBySchoolCategoryAndSchoolAndPeriodAndStatus(getSelectedSchool(), getCurrentSchoolCategory(iwc), new CalendarMonth (), "P");
		} catch (FinderException e1) {
			try {
				home.findBySchoolCategoryAndSchoolAndPeriodAndStatus(getSelectedSchool(), getCurrentSchoolCategory(iwc), new CalendarMonth (getParamMonth()), "U");
			} catch (FinderException e2) {
				batchRun = false;	//no P or U statuses found
			}
		}		

		return batchRun;
	}
		
    
	private School getSelectedSchool(){
		if (_currentSchool == null && getIWContext().isParameterSet(PAR_PROVIDER)){
			try{
				SchoolHome schoolHome = (SchoolHome) IDOLookup.getHome(School.class);			
				int currentSchoolId = new Integer(getIWContext().getParameter(PAR_PROVIDER)).intValue();
				_currentSchool = schoolHome.findByPrimaryKey("" + currentSchoolId);	
			}catch(RemoteException ex){
				ex.printStackTrace();
			}catch(FinderException ex){ 
				ex.printStackTrace();
			}		
		}		
		return _currentSchool;
	}    

    protected void addBatchRunToQueue(Date month, Date readDate,
            String schoolCategory, IWContext iwc)
            throws SchoolCategoryNotFoundException {
        BatchRunQueue.addBatchRunToQueue(month, readDate, schoolCategory,
			getSelectedSchool(), iwc, true);
    }

    private Collection getSchools(IWContext iwc) {
        Collection providers = new ArrayList();
        try {
            SchoolHome home = (SchoolHome) IDOLookup.getHome(School.class);
            SchoolCategory category = getCurrentSchoolCategory(iwc);
            if (category != null) {
                providers = home.findAllByCategory(category);
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (FinderException ex) {
            ex.printStackTrace();
        }
        return providers;
    }

    public SchoolCategory getCurrentSchoolCategory(IWContext iwc) {
        if (_currentSchoolCategory == null) {

            try {
                SchoolBusiness schoolBusiness = (SchoolBusiness) IBOLookup
                        .getServiceInstance(iwc.getApplicationContext(),
                                SchoolBusiness.class);
                String opField = getSession().getOperationalField();
                _currentSchoolCategory = schoolBusiness.getSchoolCategoryHome()
                        .findByPrimaryKey(opField);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
        }

        return _currentSchoolCategory;
    }

    /** The following methods is stolen from PaymentRecordMaintenance* */
    private School getSchoolByLoggedInUser(final IWContext context) {
        User user = context.getCurrentUser();
        School school = null;
        if (null != user) {
            try {
                SchoolUserBusiness business = getSchoolUserBusiness();
                Collection schoolIds = business.getSchools(user);
                if (!schoolIds.isEmpty()) {
                    Object schoolId = schoolIds.iterator().next();
                    school = getSchoolBusiness().getSchool(schoolId);
                }
            } catch (FinderException e) {
                // no problem, no school found
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return school;
    }

    private boolean isCentralAdministrator(final IWContext context) {
        try {
            // first see if we have cached certificate
            final String sessionKey = getClass() + ".isCentralAdministrator";
            final User verifiedCentralAdmin = (User) context
                    .getSessionAttribute(sessionKey);
            final User user = context.getCurrentUser();

            if (null != verifiedCentralAdmin
                    && user.equals(verifiedCentralAdmin)) {
            // certificate were cached
            return true; }

            // since no cert were cached, check current users group instaed
            final int groupId = getCommuneUserBusiness()
                    .getRootAdministratorGroupID();
            final GroupHome home = (GroupHome) IDOLookup.getHome(Group.class);
            final Group communeGroup = home.findByPrimaryKey(new Integer(
                    groupId));
            final Collection usersGroups = getUserBusiness().getUserGroups(
                    ((Integer) user.getPrimaryKey()).intValue());
            if (usersGroups != null
                    && communeGroup != null
                    && (usersGroups.contains(communeGroup) || user
                            .getPrimaryKey().equals(new Integer(1)))) {
                // user is allaowed, cache certificate and return true
                context.setSessionAttribute(sessionKey, user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
        return (CommuneUserBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), CommuneUserBusiness.class);
    }

    private SchoolUserBusiness getSchoolUserBusiness() throws RemoteException {
        return (SchoolUserBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), SchoolUserBusiness.class);
    }

    private SchoolBusiness getSchoolBusiness() throws RemoteException {
        return (SchoolBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), SchoolBusiness.class);
    }

    private UserBusiness getUserBusiness() throws RemoteException {
        return (UserBusiness) IBOLookup.getServiceInstance(
                getIWApplicationContext(), UserBusiness.class);
    }

}