/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.user.presentation;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoChildrenFound;
import is.idega.idegaweb.member.business.NoCustodianFound;
import is.idega.idegaweb.member.business.NoSpouseFound;
import is.idega.idegaweb.member.presentation.UserEditor;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;

/**
 * CitizenEditor
 * @author aron 
 * @version 1.0
 */

public class CitizenEditor extends UserEditor {
	
	

	/**
	 * 
	 */
	public CitizenEditor() {
		super();
		setShowMiddleNameInSearch(false);
		setLastNameLength(14);
		setFirstNameLength(14);
		setPersonalIDLength(12);
		setBundleIdentifer(CommuneBlock.IW_BUNDLE_IDENTIFIER);
		// TODO Auto-generated constructor stub
	}
	

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserEditor#presentateUserRelations(com.idega.presentation.IWContext)
	 */
	protected void presentateUserRelations(IWContext iwc) throws RemoteException {
		Table relationsTable = new Table();
		int row = 1;
		relationsTable.setCellspacing(4);
		if(user!= null){
			CommuneUserBusiness userService = getCommuneUserService(iwc);
			MemberFamilyLogic familyService = getFamilyService(iwc);
			
			//partner handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.spouse","Spouse")),1,row);
			User partner = null;
			try {
				 //System.out.println("geting spouse  of "+user.getName());
				 partner = familyService.getSpouseFor(this.user);
			}
			catch (NoSpouseFound e) {
				
			}
			
			if(partner!=null){
				relationsTable.add(getRelatedUserLink(partner),2,row);
				relationsTable.add(getDisConnectorLink(familyService.getSpouseRelationType(),(Integer)partner.getPrimaryKey(),(Integer)user.getPrimaryKey(),getDeleteIcon(iwrb.getLocalizedString("mbe.remove_spouse_relation","Remove spouse relationship"))),3,row);
			}
			row++;
			
			// custodians handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.custodians","Custodians")),1,row);
			Collection custodians = null;
			try {
				//System.out.println("geting custodians  of "+user.getName());
				custodians = userService.getParentsForChild(user);
				if(custodians!=null && !custodians.isEmpty()){
					for (Iterator iter = custodians.iterator(); iter.hasNext();) {
						User custodian = (User) iter.next();
						relationsTable.add(getRelatedUserLink(custodian),2,row);
						relationsTable.add(getDisConnectorLink(familyService.getCustodianRelationType(),(Integer)custodian.getPrimaryKey(),(Integer)user.getPrimaryKey(),getDeleteIcon(iwrb.getLocalizedString("mbe.remove_custodian_relation","Remove custodian relationship"))),3,row);
						row++;
					}
				}
			}
			catch (NoCustodianFound e1) {
				
			}
			catch (EJBException e1) {
				
			}
			row++;
			
			// children handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.children","Children")),1,row);
			Collection children = null;
			try {
				//System.out.println("geting children in custody of "+user.getName());
				children = userService.getChildrenForUser(user);
				if(children!=null && !children.isEmpty()){
					for (Iterator iter = children.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						relationsTable.add(getRelatedUserLink(child),2,row);
						relationsTable.add(getDisConnectorLink(familyService.getChildRelationType(),(Integer)child.getPrimaryKey(),(Integer)user.getPrimaryKey(),getDeleteIcon(iwrb.getLocalizedString("mbe.remove_child_relation","Remove child relationship"))),3,row);
						row++;
					}
				}
			}
			catch (NoChildrenFound e2) {
				
			}
			catch (EJBException e2) {
				
			}
			
		}
		
		addToMainPart(relationsTable);
	}
	
	public void presentateButtons(IWContext iwc) {
			Table buttonTable = new Table();
			int row = 1, col = 1;
			SubmitButton save = new SubmitButton(iwrb.getLocalizedString("mbe.save", "Save"),PRM_SAVE,user.getPrimaryKey().toString());
			setStyle(save,STYLENAME_BUTTON);
		
			buttonTable.add(save, col++, row);
			try {
				MemberFamilyLogic logic = getFamilyService(iwc);
				
				//Image regSpouse = iwrb.getLocalizedImageButton("mbe.register_spouse","Register spouse");
				//regSpouse.setToolTip(iwrb.getLocalizedString("mbe.tooltip.register_spouse","Try to attach a spouse relationship to user"));
				//Link spouseLink = getConnectorLink((Integer) user.getPrimaryKey(),logic.getSpouseRelationType(),regSpouse);
				SubmitButton spouseButton = getConnectorButton(iwc,iwrb.getLocalizedString("mbe.register_spouse","Register spouse"),(Integer) user.getPrimaryKey(),logic.getSpouseRelationType());
				buttonTable.add(spouseButton, col++, row);
				
				//Image regCustodian = iwrb.getLocalizedImageButton("mbe.register_custodian","Register custodian");
				//regCustodian.setToolTip(iwrb.getLocalizedString("mbe.tooltip.register_custodian","Try to attach a custodian relationship to user"));
				//Link custodianLink = getConnectorLink((Integer) user.getPrimaryKey(),logic.getChildRelationType(),regCustodian);
				//buttonTable.add(custodianLink, col++, row);
				SubmitButton custodianButton = getConnectorButton(iwc,iwrb.getLocalizedString("mbe.register_custodian","Register custodian"),(Integer) user.getPrimaryKey(),logic.getChildRelationType());
				buttonTable.add(custodianButton, col++, row);
				
				
				//Image regChild = iwrb.getLocalizedImageButton("mbe.register_child","Register child");
				//regChild.setToolTip(iwrb.getLocalizedString("mbe.tooltip.register_child","Try to attach a child relationship to user"));
				//Link childLink = getConnectorLink((Integer) user.getPrimaryKey(),logic.getCustodianRelationType(),regChild);
				//buttonTable.add(childLink, col++, row);
				SubmitButton childButton = getConnectorButton(iwc,iwrb.getLocalizedString("mbe.register_child","Register child"),(Integer) user.getPrimaryKey(),logic.getCustodianRelationType());
				buttonTable.add(childButton, col++, row);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			
			
			addToMainPart(buttonTable);
		}
	
	/**
	 * Returns the default delete icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The delete icon.
	 */
	protected Image getDeleteIcon(String toolTip) {
		Image deleteImage = iwb.getImage("shared/delete.gif", 12, 12);
		deleteImage.setToolTip(toolTip);
		return deleteImage;
	}
	
	
	public MemberFamilyLogic getFamilyService(IWContext iwc) throws RemoteException{
		return (MemberFamilyLogic) IBOLookup.getServiceInstance(iwc,MemberFamilyLogic.class);
	}
	
	public CommuneUserBusiness getCommuneUserService(IWContext iwc) throws RemoteException{
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc,CommuneUserBusiness.class);	
	}

}
