package is.idega.idegaweb.campus.block.application.business;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import java.util.Vector;

/**
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class CampusApplicationHolder {
	private Application eApplication = null;

	private Applicant eApplicant = null;

	private CampusApplication eCampusApplication = null;

	private Contract eContract = null;

	private Vector vApplied = null;

	private Vector vWaitingList = null;

	/**
	 * 
	 */
	public CampusApplicationHolder(Application application,
			Applicant applicant, CampusApplication campusApplication,
			Vector vapplied) {
		initialize(application, applicant, campusApplication, vapplied, null,
				null);
	}

	/**
	 * 
	 */
	public CampusApplicationHolder(Application application,
			Applicant applicant, CampusApplication campusApplication,
			Vector vapplied, Contract contract, Vector waitingList) {
		initialize(application, applicant, campusApplication, vapplied,
				contract, waitingList);
	}

	/**
	 * 
	 */
	public void setApplication(Application application) {
		eApplication = application;
	}

	/**
	 * 
	 */
	public void setApplicant(Applicant applicant) {
		eApplicant = applicant;
	}

	/**
	 * 
	 */
	public void setCampusApplication(CampusApplication application) {
		eCampusApplication = application;
	}

	/**
	 * 
	 */
	public void setApplied(Vector applied) {
		vApplied = applied;
	}

	/**
	 * 
	 */
	public Application getApplication() {
		return eApplication;
	}

	/**
	 * 
	 */
	public CampusApplication getCampusApplication() {
		return eCampusApplication;
	}

	/**
	 * 
	 */
	public Applicant getApplicant() {
		return eApplicant;
	}

	/**
	 * 
	 */
	public Vector getApplied() {
		return vApplied;
	}

	/**
	 * 
	 */
	public void setContract(Contract contract) {
		eContract = contract;
	}

	/**
	 * 
	 */
	public Contract getContract() {
		return eContract;
	}

	/**
	 * 
	 */
	public void setWaitingList(Vector waitingList) {
		vWaitingList = waitingList;
	}

	/**
	 * 
	 */
	public Vector getWaitingList() {
		return vWaitingList;
	}

	private void initialize(Application application, Applicant applicant,
			CampusApplication campusApplication, Vector vapplied,
			Contract contract, Vector waitingList) {
		eApplicant = applicant;
		eApplication = application;
		eCampusApplication = campusApplication;
		vApplied = vapplied;
		eContract = contract;
		vWaitingList = waitingList;
	}
}