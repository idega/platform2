package se.idega.idegaweb.commune.accounting.update.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;

/**
 * Sets the relation between the SchoolClass and SchoolType tables if possible
 * loggs all other realtionships so that they can be set manually
 * @author Joakim
 *
 */
public class SchoolClassSchoolTypeBusinessBean extends IBOServiceBean 
implements SchoolClassSchoolTypeBusiness {
	ArrayList ret;
	
	/**
	 * Sets the relation between the SchoolClass and SchoolType tables if possible
	 * loggs all other realtionships so that they can be set manually
	 */
	public ArrayList setClassTypeRelation(){
		ret = new ArrayList();
		try {
			Iterator classIter = getSchoolClassHome().findAll().iterator();
			while (classIter.hasNext()) {
				try{
					SchoolClass schoolClass = (SchoolClass) classIter.next();
					School provider = schoolClass.getSchool();
					try {
						Vector schoolType = (Vector)provider.getSchoolTypes();
						if(schoolType.size()==0){
								addMessage("Could not find any schooltype for Class "+schoolClass.getName()+"  School="+provider.getName()+
										"  PK="+schoolClass.getPrimaryKey());
						} else if(schoolType.size()>1){
							for(int i=0; i<schoolType.size();i++){
								addMessage("Several schooltypes for Class "+schoolClass.getName()+"  School="+provider.getName()+"  SchoolType="+
										((SchoolType)schoolType.get(i)).getName()+"  PK="+schoolClass.getPrimaryKey());
							}
						} else {
							//Set the relation
							schoolClass.setSchoolTypeId(((Integer)((SchoolType)schoolType.get(0)).getPrimaryKey()).intValue());
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
		return ret;
	}

	/**
	 * Dumps message to console and adds it to the ret list
	 * @param s
	 */	
	private void addMessage(String s){
		ret.add(s);
		System.out.println(s);
	}
	
	protected SchoolHome getSchoolHome() throws RemoteException
	{
		return (SchoolHome) IDOLookup.getHome(School.class);
	}
	
	protected SchoolTypeHome getSchoolTypeHome() throws RemoteException
	{
		return (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
	}
	
	protected SchoolClassHome getSchoolClassHome() throws RemoteException
	{
		return (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
	}
	
}
