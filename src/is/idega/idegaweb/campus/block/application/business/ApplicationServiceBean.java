/*
 * Created on Apr 5, 2004
 *
 */
package is.idega.idegaweb.campus.block.application.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.AppliedHome;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CampusApplicationHome;
import is.idega.idegaweb.campus.block.application.data.CurrentResidency;
import is.idega.idegaweb.campus.block.application.data.CurrentResidencyHome;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupation;
import is.idega.idegaweb.campus.block.application.data.SpouseOccupationHome;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.block.mailinglist.business.EntityHolder;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.UserTransaction;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectHome;
import com.idega.block.application.data.ApplicationSubjectInfo;
import com.idega.block.application.data.ApplicationSubjectInfoHome;
import com.idega.block.application.data.Status;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.ComplexTypeView;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.data.SimpleQuerier;
import com.idega.util.IWTimestamp;

/**
 * ApplicationServiceBean
 * @author aron 
 * @version 1.0
 */
	
	public class ApplicationServiceBean extends IBOServiceBean implements ApplicationService {

		public void storeApplicationSubject(String description,IWTimestamp expires) throws CreateException,RemoteException{
			if(description.length() > 0){
				ApplicationSubject AS = getSubjectHome().create();
				AS.setDescription(description);
				AS.setExpires(expires.getDate());
				AS.store();
			}
		}
		
		public void storeApplicationStatus(Integer ID, String status) {
			UserTransaction t = getSessionContext().getUserTransaction();
			try {

				t.begin();
				CampusApplication CA = getCampusApplicationHome().findByPrimaryKey(ID);
				Application A = CA.getApplication();
				String oldStatus = A.getStatus();
				A.setStatus(status);
				A.store();
				Applicant Appli = A.getApplicant();

				if (((oldStatus == null) || (!oldStatus.equals(Status.APPROVED.toString())))
					&& status.equals(Status.APPROVED.toString())) {
					// send out approval letter ( try to do with listeners )
					getMailingListService().processMailEvent(new EntityHolder(Appli), LetterParser.APPROVAL);

					// make transfers on waitinglists
					createWaitinglistTransfers(Appli, CA);
				}
				else if (status.equals(Status.REJECTED.toString())){
					getMailingListService().processMailEvent(new EntityHolder(Appli), LetterParser.REJECTION);
				}
				t.commit();

			}
			catch (Exception e) {
				try {
					t.rollback();
				}
				catch (javax.transaction.SystemException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			}
		}

		public void createWaitinglistTransfers(Applicant Appli, CampusApplication CA)
			throws CreateException, RemoteException, FinderException,SQLException {
			if (CA != null) {
				Collection L = getAppliedHome().findByApplicationID(((Integer)CA.getPrimaryKey()));
				java.util.Iterator it = L.iterator();
				if (it != null) {
					while (it.hasNext()) {
						Applied applied = (Applied) it.next();
						WaitingList wl = ((WaitingListHome) getIDOHome(WaitingList.class)).create();
						wl.setApartmentTypeId(applied.getApartmentTypeId());
						wl.setComplexId(applied.getComplexId().intValue());
						wl.setTypeApplication();
						wl.setApplicantId(((Integer) Appli.getPrimaryKey()).intValue());
						wl.setOrder(0);
						wl.setChoiceNumber(applied.getOrder());
						wl.setLastConfirmationDate(IWTimestamp.getTimestampRightNow());
						wl.store();
						wl.setOrder(((Integer) wl.getPrimaryKey()).intValue());
						String level = CA.getPriorityLevel();
						if (level.equals("A"))
							wl.setPriorityLevelA();
						else if (level.equals("B"))
							wl.setPriorityLevelB();
						else if (level.equals("C"))
							wl.setPriorityLevelC();
						else if (level.equals("D"))
							wl.setPriorityLevelD();
						else if (level.equals("E"))
							wl.setPriorityLevelE();
						else if (level.equals("T")) {
							wl.setPriorityLevelC();
							wl.setTypeTransfer();
							wl = getRightPlaceForTransfer(wl);
						}
						wl.store();
					}
				}
			}
		}

		public void garbageApplication(Integer ID) throws RemoteException {
			try {

				Application A = getApplicationHome().findByPrimaryKey(ID);
				A.setStatus(Status.GARBAGE.toString());
				A.store();
			}
			catch (IDOStoreException e) {
				throw new RemoteException(e.getMessage());
			}
			catch (IllegalStateException e) {
				throw new RemoteException(e.getMessage());
			}
			catch (FinderException e) {
				throw new RemoteException(e.getMessage());
			}

		}

		public void storePriorityLevel(Integer ID, String level) throws RemoteException {

			try {
				CampusApplication CA = getCampusApplicationHome().findByPrimaryKey(ID);

				if (CA != null) {
					CA.setPriorityLevel(level);
					CA.store();
				}
			}
			catch (IDOStoreException e) {
				throw new RemoteException(e.getMessage());
			}
			catch (FinderException e) {
				throw new RemoteException(e.getMessage());
			}

		}

		public void createApplicationSubject(String sDesc, String sDate) throws CreateException, RemoteException {
			if (sDesc.length() > 0) {
				ApplicationSubjectHome asHome = (ApplicationSubjectHome) getIDOHome(ApplicationSubject.class);
				ApplicationSubject as = asHome.create();
				as.setDescription(sDesc);
				as.setExpires(new IWTimestamp(sDate).getDate());
				as.store();
			}
		}

		private void storeApplicantInfo(
			Applicant applicant,
			CampusApplication campusApplication,
			ApplicantInfo applicantInfo) {

			campusApplication.setEmail(applicantInfo.getEmail());
			campusApplication.setFaculty(applicantInfo.getFaculty());
			campusApplication.setStudyTrack(applicantInfo.getTrack());
			applicant.setLegalResidence(applicantInfo.getLegalResidence());
			applicant.setSSN(applicantInfo.getSsn());
			applicant.setStatus("S");
			applicant.setPO(applicantInfo.getPO());
			applicant.setResidencePhone(applicantInfo.getPhone());
			applicant.setMobilePhone(applicantInfo.getMobile());
			applicant.setResidence(applicantInfo.getCurrentResidence());
			if (applicantInfo.getName() != null) {
				StringTokenizer st = new StringTokenizer(applicantInfo.getName());
				if (st.hasMoreTokens()) {
					applicant.setFirstName(st.nextToken());
				}
				String mid = "";
				if (st.hasMoreTokens()) {
					mid = (st.nextToken());
				}

				if (st.hasMoreTokens()) {
					applicant.setLastName(st.nextToken());
					applicant.setMiddleName(mid);
				}
				else {
					applicant.setLastName(mid);
				}
			}
		}

		public void addApplicantChild(Applicant parentApplicant, Map chi, String childName, String childSSN, int childId)
			throws RemoteException, CreateException, SQLException {
			Applicant child = (Applicant) chi.get(new Integer(childId));
			if (child == null) {
				child = getApplicantHome().create();
			}
			if (!childName.equals(child.getName())) {
				child.setFullName(childName);
			}
			child.setSSN(childSSN);
			child.setStatus("C");
			child.store();
			parentApplicant.addChild(child);

		}

		public Application storeWholeApplication(
			Integer campusApplicationID,
			Integer subjectID,
			ApplicantInfo applicantInfo,
			ApartmentInfo apartmentInfo,
			SpouseInfo spouseInfo,
			List childrenInfo) {

			javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

			try {
				t.begin();
				Application application = null;
				Applicant applicant = null;
				CampusApplication campusApplication = null;
				Applicant spouse = null;
				Vector children = null;
				if (campusApplicationID.intValue() > 0) {
					campusApplication = getCampusApplicationHome().findByPrimaryKey(campusApplicationID);
					application = campusApplication.getApplication();
					applicant = application.getApplicant();
					Iterator iter = applicant.getChildren();
					if (iter != null) {
						Applicant a;
						while (iter.hasNext()) {
							a = (Applicant) iter.next();
							if (a.getStatus().equals("P")) {
								spouse = a;
							}
							else if (a.getStatus().equals("C")) {
								if (children == null)
									children = new Vector();
								children.add(a);
							}
						}
					}

				}
				else {
					applicant = getApplicantHome().create();
					applicant.store();
					applicant.addChild(applicant);

					application = getApplicationHome().create();
					application.setApplicantId(new Integer(applicant.getPrimaryKey().toString()).intValue());
					application.setSubmitted(IWTimestamp.getTimestampRightNow());
					application.setStatusSubmitted();
					application.setSubjectId(subjectID.intValue());
					application.store();

					campusApplication = getCampusApplicationHome().create();
					campusApplication.setAppApplicationId(new Integer(application.getPrimaryKey().toString()).intValue());
					campusApplication.setPriorityLevel("A");
					campusApplication.store();
				}

				if (application != null && applicant != null && campusApplication!=null) {
					Collection applieds = getAppliedHome().findByApplicationID((Integer) campusApplication.getPrimaryKey());
					storeApplicantInfo(applicant, campusApplication, applicantInfo);
					storeApartmentInfo(campusApplication, applieds, apartmentInfo);
					storeSpouseInfo(campusApplication, spouseInfo);
					storeSpouse(applicant, spouse, spouseInfo);
					storeChildrenInfo(applicant, children, childrenInfo);

					applicant.store();
					campusApplication.store();

				}
				t.commit();
				return application;
			}
			catch (Exception e) {
				try {
					t.rollback();
				}
				catch (javax.transaction.SystemException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			}
			return null;
		}

		private void storeSpouseInfo(CampusApplication campusApplication, SpouseInfo info) {
			campusApplication.setSpouseStudyBeginMonth(info.getStudyBeginMonth().intValue());
			campusApplication.setSpouseStudyBeginYear(info.getStudyBeginYear().intValue());
			campusApplication.setSpouseStudyEndMonth(info.getStudyEndMonth());
			campusApplication.setSpouseStudyEndYear(info.getStudyEndYear());
			campusApplication.setSpouseStudyTrack(info.getTrack());
			campusApplication.setSpouseSchool(info.getSchool());
		}

		private void storeChildrenInfo(Applicant superApplicant, List children, List childrenInfo)
			throws CreateException, RemoteException, SQLException {

			if (childrenInfo != null && !childrenInfo.isEmpty()) {
				int count = childrenInfo.size();
				Hashtable chi = new Hashtable();
				if (children != null) {
					for (int i = 0; i < children.size(); i++) {
						Applicant child = (Applicant) children.get(i);
						chi.put(new Integer(child.getPrimaryKey().toString()), child);
					}
				}
				for (int i = 0; i < count; i++) {
					ChildInfo info = (ChildInfo) childrenInfo.get(i);
					if (info.getName() != null && info.getName().length() > 0) {

						Applicant child = (Applicant) chi.get(info.getID());
						if (child == null) {
							child = getApplicantHome().create();
						}
						if (!info.getName().equals(child.getName())) {
							child.setFullName(info.getName());
						}
						child.setSSN(info.getSsn());
						child.setStatus("C");
						child.store();
						superApplicant.addChild(child);
					}
				}
			}

		}

		private void storeSpouse(Applicant superApplicant, Applicant spouse, SpouseInfo info)
			throws SQLException, RemoteException, CreateException {
			if (info.getName() != null && info.getName().length() > 0) {

				if (spouse == null) {
					spouse = getApplicantHome().create();

				}

				if (!info.getName().equals(spouse.getName())) {
					spouse.setFullName(info.getName());
				}
				spouse.setSSN(info.getSsn());
				spouse.setStatus("P");
				spouse.store();
				superApplicant.addChild(spouse);
			}
		}

		public void storeApartmentInfo(
			CampusApplication campusApplication,
			Collection capplieds,
			ApartmentInfo apartmentInfo)
			throws RemoteException, CreateException {

		
			List applieds = null;
			if (capplieds != null && !capplieds.isEmpty()) {
				applieds = new Vector();
				for (Iterator iterator = capplieds.iterator(); iterator.hasNext();) {
					applieds.add(iterator.next());
				}

			}

			if (campusApplication == null)
				campusApplication = getCampusApplicationHome().create();
			if (apartmentInfo.getRentFrom() != null)
				campusApplication.setHousingFrom(apartmentInfo.getRentFrom().getDate());

			campusApplication.setWantFurniture(apartmentInfo.getFurniture().booleanValue());
			campusApplication.setOnWaitinglist(apartmentInfo.getWaitOnList().booleanValue());

			if (apartmentInfo.getComment() != null)
				campusApplication.setOtherInfo(apartmentInfo.getComment());

			String key1 = apartmentInfo.getAppliedKey1();
			String key2 = apartmentInfo.getAppliedKey2();
			String key3 = apartmentInfo.getAppliedKey3();
			if (key1 != null && key2 != null && key3 != null) {
				Applied applied1 = null;
				Applied applied2 = null;
				Applied applied3 = null;
				if (applieds != null) {
					applied1 = (Applied) applieds.get(0);
				}
				else {
					applied1 = getAppliedHome().create();
					applieds = (List) new Vector();
					applieds.add(applied1);
				}
				int type = ApartmentTypeComplexHelper.getPartKey(key1, 1);
				int complex = ApartmentTypeComplexHelper.getPartKey(key1, 2);
				applied1.setApartmentTypeId(type);
				applied1.setApplicationId(new Integer(campusApplication.getPrimaryKey().toString()).intValue());
				applied1.setComplexId(complex);
				applied1.setOrder(1);

				if ((key2 != null)) {
					if (applieds.size() >= 2) {
						applied2 = (Applied) applieds.get(1);
					}
					else {
						applied2 = getAppliedHome().create();
					}
					type = ApartmentTypeComplexHelper.getPartKey(key2, 1);
					complex = ApartmentTypeComplexHelper.getPartKey(key2, 2);
					applied2.setApartmentTypeId(type);
					applied2.setApplicationId(new Integer(campusApplication.getPrimaryKey().toString()).intValue());
					applied2.setComplexId(complex);
					applied2.setOrder(2);

				}

				if ((key3 != null)) {
					if (applieds.size() >= 3) {
						applied3 = (Applied) applieds.get(2);
					}
					else {
						applied3 = getAppliedHome().create();
					}
					type = ApartmentTypeComplexHelper.getPartKey(key3, 1);
					complex = ApartmentTypeComplexHelper.getPartKey(key3, 2);
					applied3.setApartmentTypeId(type);
					applied3.setApplicationId(new Integer(campusApplication.getPrimaryKey().toString()).intValue());
					applied3.setComplexId(complex);
					applied3.setOrder(3);
				}

				/*
				if(applied3 == null && lApplied != null && lApplied.size() >= 3){
				((Applied)lApplied.get(2)).setID(-3);
				}
				if(applied2 == null && lApplied != null && lApplied.size() >= 2){
				((Applied)lApplied.get(1)).setID(-3);
				}
				*/
				if (applied1 != null && "-1".equals(key1)) {
					//        System.err.println("deleting 1");

					try {
						applied1.remove();
					}
					catch (EJBException e) {
						e.printStackTrace();
					}
					catch (RemoveException e) {
						e.printStackTrace();
					}

					applied1 = null;
					if (applied2 != null) {
						applied2.setOrder(1);
					}
					if (applied3 != null) {
						applied3.setOrder(2);
					}

				}
				if (applied2 != null && "-1".equals(key2)) {

					try {
						applied2.remove();
					}
					catch (EJBException e) {
						e.printStackTrace();
					}
					catch (RemoveException e) {
						e.printStackTrace();
					}

					applied2 = null;
					if (applied3 != null)
						applied3.setOrder(1);
				}
				if (applied3 != null && "-1".equals(key3)) {

					try {
						applied3.remove();
					}
					catch (EJBException e) {
						e.printStackTrace();
					}
					catch (RemoveException e) {
						e.printStackTrace();
					}

					applied3 = null;
				}
				if (applied1 != null)
					applied1.store();
				//V.add(applied1);
				if (applied2 != null)
					applied2.store();
				//V.add(applied2);
				if (applied3 != null)
					applied3.store();
				//V.add(applied3);

			}
			else {
				System.err.println("no key parameters for apartment");
			}
		}

		public Collection getWaitinglists(Integer applicantId) {

			try {
				Collection li = getWaitingListHome().findByApplicantID(applicantId);
				if (li != null && !li.isEmpty()) {
					updateWatingListToRightOrder(li);
				}
				return (li);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			return null;
		}

		public CampusApplicationHolder getApplicationInfo(int applicationId) {
			CampusApplicationHolder cah = null;
			Application a = null;

			try {
				a = getApplicationHome().findByPrimaryKey(new Integer(applicationId));
				cah = getApplicationInfo(a);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}

			return (cah);
		}

		public CampusApplicationHolder getApplicationInfo(Application a) {
			CampusApplicationHolder cah = null;
			CampusApplication ca = null;
			Collection resultSet = null;
			Vector applied = null;
			Applicant applicant = null;
			Contract contract = null;
			Vector wl = null;
			if (a != null) {
				try {
					resultSet = getCampusApplicationHome().findAllByApplicationId(((Integer) a.getPrimaryKey()).intValue());
					if (resultSet != null && !resultSet.isEmpty()) {
						ca = (CampusApplication) resultSet.iterator().next();
					
						resultSet = getAppliedHome().findByApplicationID((Integer) ca.getPrimaryKey());
					
						if (resultSet != null && !resultSet.isEmpty()) {
							applied = new Vector(resultSet);
						}
					}
					// Applicant
					applicant = getApplicantHome().findByPrimaryKey(new Integer(a.getApplicantId()));
					
					// Contracts
					ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
					resultSet = cHome.findByApplicantID((Integer) applicant.getPrimaryKey());
					if (resultSet != null && !resultSet.isEmpty()) {
						contract = (Contract) resultSet.iterator().next();
					}
					
					// Waitinglist entries
					resultSet = getWaitinglists(( Integer)applicant.getPrimaryKey());	
					if (resultSet != null){
						wl = new Vector(resultSet);
					}
					cah = new CampusApplicationHolder(a, applicant, ca, applied, contract, wl);
					return cah;
				}
				catch (IDOLookupException e) {
					e.printStackTrace();
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
				catch (EJBException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		public CampusApplicationHolder getApplicantInfo(int iApplicantId) {
			CampusApplicationHolder CAH = null;
			Applicant eApplicant = null;

			try {
				eApplicant = getApplicantHome().findByPrimaryKey(new Integer(iApplicantId));
				CAH = getApplicantInfo(eApplicant);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}

			return CAH;
		}

		public CampusApplicationHolder getApplicantInfo(Applicant eApplicant) {
			CampusApplicationHolder cah = null;
			if (eApplicant != null) {
				try {

					Collection applications = getApplicationHome().findByApplicantID((Integer) eApplicant.getPrimaryKey());
					if (applications != null && !applications.isEmpty()) {
						Application eApplication = (Application) applications.iterator().next();
						Collection camApplications =
							getCampusApplicationHome().findAllByApplicationId(
								((Integer) eApplication.getPrimaryKey()).intValue());
						if (camApplications != null && !camApplications.isEmpty()) {
							CampusApplication eCampusApplication = (CampusApplication) camApplications.iterator().next();
							Collection applieds =
								getAppliedHome().findByApplicationID((Integer) eCampusApplication.getPrimaryKey());
							Vector v = null;
							if (applieds != null && !applieds.isEmpty()) {
								v = new Vector(applieds.size());
								for (Iterator iter = v.iterator(); iter.hasNext();) {
									v.add(iter.next());
								}
							}

							cah = new CampusApplicationHolder(eApplication, eApplicant, eCampusApplication, v);
						}
					}

				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
				catch (EJBException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
			}
			return (cah);
		}

		private void updateWatingListToRightOrder(Collection li) {
			Iterator it = li.iterator();
			while (it.hasNext()) {
				WaitingList wl = (WaitingList) it.next();
				if ((wl.getApartmentTypeId() != null) && (wl.getComplexId() != null)) {
					StringBuffer sql = new StringBuffer("select count(*) from ");
					sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getEntityTableName());
					sql.append(" where ((");
					sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getOrderColumnName());
					sql.append(" <= ");
					sql.append(wl.getOrder().toString());
					sql.append(" and ");
					sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getPriorityColumnName());
					sql.append(" = '");
					sql.append(wl.getPriorityLevel());
					sql.append("') or (");
					sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getPriorityColumnName());
					sql.append(" < '");
					sql.append(wl.getPriorityLevel());
					sql.append("')) and ");
					sql.append(
						is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getApartmentTypeIdColumnName());
					sql.append(" = ");
					sql.append(wl.getApartmentTypeId().toString());
					sql.append(" and ");
					sql.append(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.getComplexIdColumnName());
					sql.append(" = ");
					sql.append(wl.getComplexId().toString());

					int count = 0;

					try {
						count = getWaitingListHome().create().getCountOfRecords(sql.toString());
					}
					catch (RemoteException e) {
						e.printStackTrace();
					}
					catch (FinderException e) {
						e.printStackTrace();
					}
					catch (CreateException e) {
						e.printStackTrace();
					}

					if (count < 0)
						count = 1;

					wl.setOrder(count);
				}
			}
		}
		public String[] getApplicantEmail(int iApplicantId) {
			try {
			StringBuffer sql = new StringBuffer("select c.email from ");
			IDOEntityDefinition def = IDOLookup.getEntityDefinitionForClass(CampusApplication.class);
			sql.append(def.getUniqueEntityName()).append(" c,");
			sql.append(com.idega.block.application.data.ApplicationBMPBean.getEntityTableName()).append(" b ");
			sql.append(" where c.app_application_id = b.app_application_id ");
			sql.append(" and b.app_applicant_id = ");
			sql.append(iApplicantId);
			
				return com.idega.data.SimpleQuerier.executeStringQuery(sql.toString());
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			return new String[0];
		}
		
		public  BuildingService getBuildingService() throws RemoteException {
			return (BuildingService) getServiceInstance(BuildingService.class);
		}
		
		public MailingListService getMailingListService() throws RemoteException {
			return (MailingListService) getServiceInstance(MailingListService.class);
		}

		public ApplicantHome getApplicantHome() throws RemoteException {
			return (ApplicantHome) getIDOHome(Applicant.class);
		}

		public ApplicationHome getApplicationHome() throws RemoteException {
			return (ApplicationHome) getIDOHome(Application.class);
		}

		public AppliedHome getAppliedHome() throws RemoteException {
			return (AppliedHome) getIDOHome(Applied.class);
		}

		public CampusApplicationHome getCampusApplicationHome() throws RemoteException {
			return (CampusApplicationHome) getIDOHome(CampusApplication.class);
		}

		public WaitingListHome getWaitingListHome() throws RemoteException {
			return (WaitingListHome) getIDOHome(WaitingList.class);
		}
		
		public ApplicationSubjectHome getSubjectHome() throws RemoteException{
			return (ApplicationSubjectHome) getIDOHome(ApplicationSubject.class);
		}
		
		public ApplicationSubjectInfoHome getSubjectInfoHome() throws RemoteException{
				return (ApplicationSubjectInfoHome) getIDOHome(ApplicationSubjectInfo.class);
			}
		
		public CurrentResidencyHome getResidencyHome() throws RemoteException{
			return (CurrentResidencyHome)getIDOHome(CurrentResidency.class);
		}
		
		public SpouseOccupationHome getSpouseOccupationHome() throws RemoteException{
				return (SpouseOccupationHome)getIDOHome(SpouseOccupation.class);
			}

		public int getMaxTransferInWaitingList(int typeId, int cmplxId) {
			StringBuffer sql = new StringBuffer("select max(app.ordered) ");
			sql.append(" from cam_waiting_list app ");
			sql.append(" where app.bu_apartment_type_id = ");
			sql.append(typeId);
			sql.append(" and app.bu_complex_id =");
			sql.append(cmplxId);
			sql.append(" and app.list_type = 'T'");
		
			int maxId = 0;
			try {
				maxId = SimpleQuerier.executeIntQuery(sql.toString());
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		
			if (maxId < 0)
				maxId = 0;
			return maxId;
		}

		public  WaitingList getRightPlaceForTransfer(WaitingList wl)throws RemoteException,FinderException {
			
			int cmplx = wl.getComplexId().intValue();
			int aprttype = wl.getApartmentTypeId().intValue();
			
			int lastTransfer =getMaxTransferInWaitingList(aprttype,cmplx);
		
			Collection transfers = getWaitingListHome().findNextForTransferByApartmentTypeAndComplex(aprttype,cmplx,lastTransfer);
			
			if (transfers.size() > 4) {
				java.util.Iterator it = transfers.iterator();
				WaitingList wl2 = null;
				for (int i = 0; i < 4; i++)
					wl2 = (WaitingList)it.next();
					
				wl.setOrder(wl2.getOrder());
			}
		
			return wl;
		}
		
		public Collection getComplexTypeHelpersByCategory(Integer categoryID)throws RemoteException,FinderException{
			Collection coll = getBuildingService().getComplexTypeViewHome().findByCategory(categoryID);
			return getComplexTypeHelpers(coll);
		}
		
		public Collection getComplexTypeHelpers()throws RemoteException, FinderException{
			Collection coll =getBuildingService().getComplexTypeViewHome().findAll();
			return getComplexTypeHelpers(coll);
		}
		
		public Collection getComplexTypeHelpers(Collection complexTypes){
			ArrayList list =new ArrayList(complexTypes.size());
			for (Iterator iter = complexTypes.iterator(); iter.hasNext();) {
				ComplexTypeView entity = (ComplexTypeView) iter.next();
				
				ApartmentTypeComplexHelper appHelp = new ApartmentTypeComplexHelper();
				appHelp.setKey(entity.getApartmentTypeID().intValue(),entity.getComplexID().intValue());
				appHelp.setName(entity.getApartmentTypeName() + " (" + entity.getComplexName() + ")");
				list.add(appHelp);
			}
			return list;
		}
	}
	

