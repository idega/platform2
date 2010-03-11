package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CampusApplicationHome;

import java.util.Collection;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.building.business.ApartmentHolder;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.ApartmentViewHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class EntityHolder {

	User eUser;

	//int applicantID = -1;

	//int applicationID = -1;

	Applicant eApplicant;

	Application eApplication;

	CampusApplication eCampusApplication;

	ApartmentHolder apartmentHolder;

	Contract eContract;

	Collection emails;

	CampusApplicationHolder holder;

	String cypher;

	public EntityHolder(Contract eContract) {
		this.eContract = eContract;
		//applicantID = eContract.getApplicantId().intValue();
		if (eContract.getApplication() != null) {
			eApplication = eContract.getApplication();
			try {
				CampusApplicationHome caHome = (CampusApplicationHome) IDOLookup
						.getHome(CampusApplication.class);
				this.eCampusApplication = caHome
						.findByApplicationId(((Integer) eApplication
								.getPrimaryKey()).intValue());
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (EJBException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		init();
	}

	public EntityHolder(int iContractId) {
		try {

			ContractHome cHome = (ContractHome) IDOLookup
					.getHome(Contract.class);
			eContract = cHome.findByPrimaryKey(new Integer(iContractId));

			//applicantID = eContract.getApplicantId().intValue();
			if (eContract.getApplication() != null) {
				eApplication = eContract.getApplication();
				try {
					CampusApplicationHome caHome = (CampusApplicationHome) IDOLookup
							.getHome(CampusApplication.class);
					this.eCampusApplication = caHome
							.findByApplicationId(((Integer) eApplication
									.getPrimaryKey()).intValue());
				} catch (IDOLookupException e) {
					e.printStackTrace();
				} catch (EJBException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}
			}
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public EntityHolder(Application eApplication) {
		this.eApplication =  eApplication;
		init();
	}

	public EntityHolder(Applicant eApplicant, Application app,
			CampusApplication camApp, String cypher) {
		//this.applicantID = ((Integer) eApplicant.getPrimaryKey()).intValue();
		this.eApplicant = eApplicant;
		this.eApplication = app;
		this.eCampusApplication = camApp;
		this.cypher = cypher;
		init();
	}

	private void init() {
		try {
			holder = CampusApplicationFinder.getApplicationInfo(eApplication);
			if (eApplicant == null) {
				eApplicant = holder.getApplicant();
			}

			if (eApplication == null) {
				eApplication = holder.getApplication();
			}

			if (eCampusApplication == null) {
				eCampusApplication = holder.getCampusApplication();
			}

			if (eContract != null) {
				eUser = ((UserHome) com.idega.data.IDOLookup
						.getHome(User.class)).findByPrimaryKey(eContract
						.getUserId());
				if (eUser != null)
					apartmentHolder = new ApartmentHolder(
							((ApartmentViewHome) IDOLookup
									.getHome(ApartmentView.class))
									.findByPrimaryKey(eContract
											.getApartmentId()));
			}

			if (emails == null && eCampusApplication != null) {
				emails = new Vector();
				emails.add(eCampusApplication.getEmail());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public User getUser() {
		return this.eUser;
	}

	public Applicant getApplicant() {
		return this.eApplicant;
	}

	public Contract getContract() {
		return this.eContract;
	}

	public Collection getEmails() {
		return this.emails;
	}

	public ApartmentHolder getApartmentHolder() {
		return apartmentHolder;
	}

	public Application getApplication() {
		return eApplication;
	}

	public CampusApplication getCampusApplication() {
		return eCampusApplication;
	}

	public CampusApplicationHolder getApplicationHolder() {
		return holder;
	}
}