package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoChildrenFound;
import is.idega.idegaweb.member.business.NoCustodianFound;
import is.idega.idegaweb.member.business.NoSiblingFound;
import is.idega.idegaweb.member.business.NoSpouseFound;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import com.idega.user.presentation.GroupSelectionDoubleBox;
import com.idega.user.presentation.UserTab;


/**
 * Title:        
 * Description:
 * Copyright: Idega Software (c) 2002
 * Company: Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class GroupApplicationTab extends UserTab {

  private Text spouseText;
  private Text childrenText;
  private Text custodiansText;
  private Text siblingsText;
  
  private User user;
	private Table frameTable;
	private Table custodianTable;
	private Table spouseTable;
	private Table childrenTable;
	private Table siblingsTable;

  public GroupApplicationTab() {
    super();
    super.setName("Applications");
  }

  public void init(){}
  public void updateFieldsDisplayStatus() {}
  public void initializeFields() {}
  public void initializeFieldNames() {}
  public void initializeFieldValues() {}
  public boolean collect(IWContext iwc) { initFieldContents(); return true; }
  public boolean store(IWContext iwc) { 
  		
  	return true; 
  }
  
  public void initFieldContents() {
   

    this.empty();
    
    frameTable = new Table(1,1);
    frameTable.setCellpadding(0);
    frameTable.setCellspacing(0);
    
    frameTable.add(new GroupSelectionDoubleBox());
    this.add(frameTable);


  }

  public void initializeTexts() {
    
  }

  public void lineUpFields() {
  }
  
	private GroupApplicationBusiness getGroupApplicationBusiness(IWApplicationContext iwac) throws RemoteException {
		return (GroupApplicationBusiness) IBOLookup.getServiceInstance(iwac, GroupApplicationBusiness.class);	
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return "is.idega.idegaweb.member";
	}
}