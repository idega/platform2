package se.idega.idegaweb.commune.accounting.update.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.user.data.User;

/**
 * Sets the relation between the SchoolClass and SchoolType tables if possible
 * loggs all other realtionships so that they can be set manually
 * @author Joakim
 *
 */
public class SchoolClassSchoolTypeBusinessBean extends IBOServiceBean 
implements SchoolClassSchoolTypeBusiness {
	List ret;
	
	/**
	 * Sets the relation between the SchoolClass and SchoolType tables if possible
	 * loggs all other realtionships so that they can be set manually
	 */
	public List setClassTypeRelation(){
		//ret = new ArrayList();
		try {
			Iterator classIter = getSchoolClassHome().findAll().iterator();
			while (classIter.hasNext()) {
				try{
					SchoolClass schoolClass = (SchoolClass) classIter.next();
					School provider = schoolClass.getSchool();
					try {
						Vector schoolTypes = (Vector)provider.getSchoolTypes();
						int schoolTypeId=-1;
						if(schoolTypes.size()==0){
							SchoolYear year = null;//schoolClass.getSchoolYear();
							if(year!=null){
								schoolTypeId = year.getSchoolTypeId();
							}
							if(schoolTypeId==-1){
								addMessage("Could not find any schooltype for Class="+schoolClass.getName()+"  School="+provider.getName()+
																		"  PK="+schoolClass.getPrimaryKey());
							}
						} else if(schoolTypes.size()>1){
							SchoolYear year = null;//schoolClass.getSchoolYear();
							if(year!=null){
								schoolTypeId = year.getSchoolTypeId();
							}
							if(schoolTypeId==-1){
								String str = "Several schooltypes for Class="+schoolClass.getName()+"  School="+provider.getName();
								for(int i=0; i<schoolTypes.size();i++){
									str += "\n<br>\t - SchoolType="+((SchoolType)schoolTypes.get(i)).getName()+"  PK="+schoolClass.getPrimaryKey();
								}
								addMessage(str);
							}

						} else {
							schoolTypeId=(((Integer)((SchoolType)schoolTypes.get(0)).getPrimaryKey()).intValue());
						}
						
						if(schoolTypeId!=-1){
							//Set the relation
							schoolClass.setSchoolTypeId(schoolTypeId);
							schoolClass.store();
						}
					} catch (IDORelationshipException e1) {
						addMessage("Erro getting schooltype from school "+provider.getName()+"  PK="+provider.getPrimaryKey());
						e1.printStackTrace();
					}
				}catch(Exception e){
					addMessage("Exception created! Relationship could not be set!");
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			addMessage("RemoteException created, halting execution. Relations not set!");
			e.printStackTrace();
		} catch (FinderException e) {
			addMessage("FinderException created, halting execution. Relations not set!");
			e.printStackTrace();
		}
		return getLinesList();
	}

	/**
	 * Dumps message to console and adds it to the ret list
	 * @param s
	 */	
	private void addMessage(String s){
		getLinesList().add(s);
		System.out.println(s);
	}
	
	private List getLinesList(){
		if(ret==null){
			ret = new ArrayList();
		}
		return ret;
	}
	
	
	
	protected SchoolHome getSchoolHome() throws RemoteException
	{
		return (SchoolHome) IDOLookup.getHome(School.class);
	}
	
	protected SchoolTypeHome getSchoolTypeHome() throws RemoteException
	{
		return (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
	}
	
	protected SchoolCategoryHome getSchoolCategoryHome() throws RemoteException
	{
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}
	
	protected SchoolClassHome getSchoolClassHome() throws RemoteException
	{
		return (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
	}

	protected SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException
	{
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}
	
	protected SchoolSeasonHome getSchoolSeasonHome() throws RemoteException
	{
		return (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
	}
	
	protected ChildCareContractHome getChildcareContractHome() throws RemoteException
	{
		return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}

	/**
	 * Sets the relation between the Contract and SchoolPlacement if possible
	 * loggs all other realtionships so that they can be set manually
	 */
	public List setContractPlacementRelation(){
		//ret = new ArrayList();
		try {
			SchoolCategory category = this.getSchoolCategoryHome().findChildcareCategory();
			Collection childcareSchoolTypes = this.getSchoolTypeHome().findAllByCategory(category.getCategory());

			Iterator contractIter = getChildcareContractHome().findAll().iterator();
			while (contractIter.hasNext()) {
				try{
					ChildCareContract contract = (ChildCareContract) contractIter.next();
					User child = contract.getChild();
					int childId= ((Integer)child.getPrimaryKey()).intValue();				
						Collection members = getSchoolClassMemberHome().findByStudentAndTypes(childId,childcareSchoolTypes);
						if(members.size()==1){
							SchoolClassMember mem = (SchoolClassMember)members.iterator().next();
							if(mem!=null){
								contract.setSchoolClassMember(mem);
								contract.store();
							}
						}
						else if(members.size()>1){
							String str = "Found more than one placements for child with name="+child.getName()+"  personalId="+child.getPersonalID()+
																	"  PK="+child.getPrimaryKey();
							Iterator iter = members.iterator();
							while(iter.hasNext()){
								SchoolClassMember mem = (SchoolClassMember)iter.next();
								str += "\n<br>\t - SchoolClassMemberId="+mem.getPrimaryKey();
							}

							addMessage(str);
						}
						else{
							addMessage("Could not find any placements for child with name="+child.getName()+"  personalId="+child.getPersonalID()+
																	"  PK="+child.getPrimaryKey()+", ChildCareContractId="+contract.getPrimaryKey());
						}

				}catch(Exception e){
					addMessage("Exception created! Relationship could not be set!");
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			addMessage("RemoteException created, halting execution. Relations not set!");
			e.printStackTrace();
		} catch (FinderException e) {
			addMessage("FinderException created, halting execution. Relations not set!");
			e.printStackTrace();
		}
		return getLinesList();
	}

	public List setSchoolClassSchoolTypeAndContractPlacementRelations(){	
		addMessage("---------------------------------------------------------------------------------------------");
		addMessage("SchoolClassSchoolTypeBusinessBean: Setting SchoolClass and SchoolType relations:");
		addMessage("---------------------------------------------------------------------------------------------");
		this.setClassTypeRelation();
		addMessage("---------------------------------------------------------------------------------------------");
		addMessage("SchoolClassSchoolTypeBusinessBean: Setting Contract and SchoolClassMember relations:");
		addMessage("---------------------------------------------------------------------------------------------");		this.setContractPlacementRelation();
		this.setContractPlacementRelation();
		addMessage("---------------------------------------------------------------------------------------------");
		addMessage("SchoolClassSchoolTypeBusinessBean: Done");
		addMessage("---------------------------------------------------------------------------------------------");
		return getLinesList();
	}
}
