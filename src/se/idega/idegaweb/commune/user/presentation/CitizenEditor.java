/*
 * Created on Aug 18, 2003
 *
 */
package se.idega.idegaweb.commune.user.presentation;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCohabitantFound;
import is.idega.block.family.business.NoSpouseFound;
import is.idega.idegaweb.member.presentation.UserEditor;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.EJBException;

import se.idega.idegaweb.commune.business.CommuneFamilyService;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Commune;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;
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
		//setShowMiddleNameInSearch(true);
		setLastNameLength(25);
		setFirstNameLength(25);
		setPersonalIDLength(15);
		//setShowMiddleNameInput(false);
		setAllowPersonalIdEdit(false);
		setBundleIdentifer(CommuneBlock.IW_BUNDLE_IDENTIFIER);
		setShowSeperators(true);
		// allow temporary pid search
		setLegalNonDigitPIDLetters("TFtf");
		setGroupRelationConnectorWindow(CommuneFamilyRelationConnector.class);
		
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserEditor#presentateUserRelations(com.idega.presentation.IWContext)
	 */
	protected void presentateUserRelations(IWContext iwc) throws RemoteException {
		
		Table relationsTable = new Table();
		//relationsTable.setBorder(1);
		//relationsTable.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		relationsTable.setCellspacing(4);
		if (user != null) {
			addSeperator(iwrb.getLocalizedString("mbe.user_relations","User relations"));
			//CommuneUserBusiness userService = getCommuneUserService(iwc);
			CommuneFamilyService familyService = getFamilyService(iwc);
			//partner handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.spouse", "Spouse")), 1, row);
			User partner = null;
			try {
				//System.out.println("geting spouse  of "+user.getName());
				partner = familyService.getSpouseFor(this.user);
			}
			catch (NoSpouseFound e) {
			}
			catch (Exception e) {
			}
			if (partner != null) {
				relationsTable.add(getRelatedUserLink(partner), 2, row);
				relationsTable.add(
					getDisConnectorLink(
						familyService.getSpouseRelationType(),
						null,
						(Integer) user.getPrimaryKey(),
						(Integer) partner.getPrimaryKey(),
						getDeleteIcon(
							iwrb.getLocalizedString("mbe.remove_spouse_relation", "Remove spouse relationship"))),
					3,
					row);
			}
			
			// cohabitant handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.cohabitant", "Cohabitant")), 5, row);
			User cohabitant = null;
			try {
				//System.out.println("geting spouse  of "+user.getName());
				cohabitant = familyService.getCohabitantFor(this.user);
			}
			catch (NoCohabitantFound e) {
			}
			if (cohabitant != null) {
				relationsTable.add(getRelatedUserLink(cohabitant), 6, row);
				relationsTable.add(
					getDisConnectorLink(
						familyService.getCohabitantRelationType(),
						null,
						(Integer) user.getPrimaryKey(),
						(Integer) cohabitant.getPrimaryKey(),
						getDeleteIcon(
							iwrb.getLocalizedString("mbe.remove_cohabitant_relation", "Remove cohabitant relationship"))),
					7,
					row);
			}
			row++;
			
			//	parent handling
			int parentStartRow = row,custodianStartRow = row; 
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.parents", "Parents")), 1, row);
			Collection parents = null;
			try {
				//System.out.println("geting custodians  of "+user.getName());
				parents = familyService.getParentsFor(user);
				//custodians = userService.getParentsForChild(user);
				if (parents != null && !parents.isEmpty()) {
					for (Iterator iter = parents.iterator(); iter.hasNext();) {
						User parent = (User) iter.next();
						relationsTable.add(getRelatedUserLink(parent), 2, parentStartRow);
						String relationType = familyService.getParentRelationType();
						relationsTable.add(
							getDisConnectorLink(
								null,
								relationType,
								(Integer) user.getPrimaryKey(),
								(Integer) parent.getPrimaryKey(),
								getDeleteIcon(
									iwrb.getLocalizedString(
										"mbe.remove_parent_relation",
										"Remove parent relationship"))),
							3,
							parentStartRow);
						parentStartRow++;
					}
				}
			}
			catch (Exception e1) {
			}
			//row++;
			// custodians handling
			
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.custodians", "Custodians")), 5, custodianStartRow);
			Collection custodians = null;
			try {
				//System.out.println("geting custodians  of "+user.getName());
				custodians = familyService.getCustodiansFor(user, false);
				//custodians = userService.getParentsForChild(user);
				if (custodians != null && !custodians.isEmpty()) {
					for (Iterator iter = custodians.iterator(); iter.hasNext();) {
						User custodian = (User) iter.next();
						relationsTable.add(getRelatedUserLink(custodian), 6, custodianStartRow);
						String relationType = familyService.getCustodianRelationType();
						relationsTable.add(
							getDisConnectorLink(
								null,
								relationType,
								(Integer) user.getPrimaryKey(),
								(Integer) custodian.getPrimaryKey(),
								getDeleteIcon(
									iwrb.getLocalizedString(
										"mbe.remove_custodian_relation",
										"Remove custodian relationship"))),
							7,
							custodianStartRow);
						custodianStartRow++;
					}
				}
			}
			catch (Exception e1) {
			}
			row=Math.max(custodianStartRow,parentStartRow)+1;
			// biological children handling
			
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.parential_children", "Parential children")),1,				row);
			Collection children = null;
			int childrowstart = row;
			try {
				//System.out.println("geting children in custody of "+user.getName());
				children = familyService.getChildrenFor(user);
				if (children != null && !children.isEmpty()) {
					for (Iterator iter = children.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						relationsTable.add(getRelatedUserLink(child), 2, row);
						relationsTable.add(
							getDisConnectorLink(
								familyService.getParentRelationType(),
								null,
								(Integer) user.getPrimaryKey(),
								(Integer) child.getPrimaryKey(),
								getDeleteIcon(
									iwrb.getLocalizedString("mbe.remove_child_relation", "Remove child relationship"))),
							3,
							row);
						row++;
					}
				}
			}
			catch (Exception e2) {
			}
			//	custody children handling
			row = childrowstart;
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.custody_children", "Custody children")), 5, row);
			try {
				children = familyService.getChildrenInCustodyOf(user);
				if (children != null && !children.isEmpty()) {
					for (Iterator iter = children.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						relationsTable.add(getRelatedUserLink(child), 6, row);
						relationsTable.add(
							getDisConnectorLink(
								familyService.getCustodianRelationType(),
								null,
								(Integer) user.getPrimaryKey(),
								(Integer) child.getPrimaryKey(),
								getDeleteIcon(
									iwrb.getLocalizedString("mbe.remove_child_relation", "Remove child relationship"))),
							7,
							row);
						row++;
					}
				}
			}
			catch (NoChildrenFound e3) {
				//e3.printStackTrace();
			}
			catch (RemoteException e3) {
				e3.printStackTrace();
			}
			catch (EJBException e3) {
				e3.printStackTrace();
			}
		}
		relationsTable.setWidth(2, "150");
		
		relationsTable.setWidth(6, "150");
		addToMainPart(relationsTable);
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
	public CommuneFamilyService getFamilyService(IWContext iwc) throws RemoteException {
		return (CommuneFamilyService) IBOLookup.getServiceInstance(iwc, CommuneFamilyService.class);
	}
	public CommuneUserBusiness getCommuneUserService(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserEditor#storeUserAsDeceased(com.idega.presentation.IWContext, java.lang.Integer, java.util.Date)
	 */
	protected void storeUserAsDeceased(IWContext iwc, Integer userID, Date deceasedDate) {
		try {
			getCommuneUserService(iwc).setUserAsDeceased(userID, deceasedDate);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserEditor#presentateButtonRegister(com.idega.presentation.IWContext)
	 */
	protected void presentateButtonRegister(IWContext iwc) {
		try {
			int pageID = getParentPageID();
			Integer thisPageID = pageID>0 ?new Integer(pageID):null;
			Table bTable = new Table(9,2);
			
			CommuneFamilyService logic = getFamilyService(iwc);
			Integer userID = (Integer) user.getPrimaryKey();
			Image addImage = iwb.getImage("shared/edit.gif", 12, 12);
			bTable.setWidth(3,10);
			bTable.setWidth(6,10);
			
			//Image regSpouse = iwrb.getLocalizedImageButton("mbe.register_spouse","Register spouse");
			//regSpouse.setToolTip(iwrb.getLocalizedString("mbe.tooltip.register_spouse","Try to attach a spouse relationship to user"));
			//Link spouseLink = getConnectorLink((Integer) user.getPrimaryKey(),logic.getSpouseRelationType(),regSpouse);
			SubmitButton spouseButton =
				getConnectorButton(
					iwc,addImage,
					iwrb.getLocalizedString("mbe.register_spouse", "Register spouse"),
					userID,
					logic.getSpouseRelationType(),
					null,thisPageID);
			bTable.add(spouseButton,1,1);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_spouse", "Register spouse")),2,1);
			SubmitButton mateButton =
				getConnectorButton(
					iwc,addImage,
					iwrb.getLocalizedString("mbe.register_mate", "Register mate"),
					userID,
					logic.getCohabitantRelationType(),
					null,thisPageID);
			bTable.add(mateButton,1,2);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_mate", "Register mate")),2,2);
			//Image regCustodian = iwrb.getLocalizedImageButton("mbe.register_custodian","Register custodian");
			//regCustodian.setToolTip(iwrb.getLocalizedString("mbe.tooltip.register_custodian","Try to attach a custodian relationship to user"));
			//Link custodianLink = getConnectorLink((Integer) user.getPrimaryKey(),logic.getChildRelationType(),regCustodian);
			//buttonTable.add(custodianLink, col++, row);
			SubmitButton parentButton =
				getConnectorButton(
					iwc,addImage,
					iwrb.getLocalizedString("mbe.register_parent", "Register parent"),
					userID,
					logic.getChildRelationType(),
					logic.getParentRelationType(),thisPageID);
			bTable.add(parentButton,4,1);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_parent", "Register parent")),5,1);
			SubmitButton custodianButton =
				getConnectorButton(
					iwc,addImage,
					iwrb.getLocalizedString("mbe.register_custodian", "Register custodian"),
					userID,
					logic.getChildRelationType(),
					logic.getCustodianRelationType(),thisPageID);
			bTable.add(custodianButton,4,2);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_custodian", "Register custodian")),5,2);
			//Image regChild = iwrb.getLocalizedImageButton("mbe.register_child","Register child");
			//regChild.setToolTip(iwrb.getLocalizedString("mbe.tooltip.register_child","Try to attach a child relationship to user"));
			//Link childLink = getConnectorLink((Integer) user.getPrimaryKey(),logic.getCustodianRelationType(),regChild);
			//buttonTable.add(childLink, col++, row);
			SubmitButton childButton =
				getConnectorButton(
					iwc,addImage,
					iwrb.getLocalizedString("mbe.register_parential_child", "Register parential child"),
					userID,
					logic.getParentRelationType(),
					logic.getChildRelationType(),thisPageID);
			bTable.add(childButton,7,1);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_parential_child", "Register parential child")),8,1);
			SubmitButton custodyChildButton =
				getConnectorButton(
					iwc,addImage,
					iwrb.getLocalizedString("mbe.register_custody_child", "Register custody child"),
					userID,
					logic.getCustodianRelationType(),
					logic.getChildRelationType(),thisPageID);
			bTable.add(custodyChildButton,7,2);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_custody_child", "Register custody child")),8,2);
			addButton(bTable);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserEditor#presentateUserGroup(com.idega.presentation.IWContext)
	 */
	
	protected void presentateUserGroup(IWContext iwc) {
	}
	/*
		// TODO Auto-generated method stub
		Table groupTable = new Table(2, 3);
		Text tGroup = new Text(iwrb.getLocalizedString("mbe.commune", "Commune"));
		tGroup.setStyleClass(headerFontStyleName);
		groupTable.mergeCells(1, 1, 2, 1);
		groupTable.add(tGroup, 1, 1);

		Group communeGroup = null;
		try {
			CommuneUserBusiness communeUserService = getCommuneUserService(iwc);
			communeGroup = communeUserService.getRootCitizenGroup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Integer ID = communeGroup != null ? (Integer) communeGroup.getPrimaryKey() : new Integer(-1);
		DropdownMenu communes = new DropdownMenu(PRM_COMMUNE_ID);
		try {
			SelectorUtility selUtil = new SelectorUtility();
			selUtil.getSelectorFromIDOEntities(communes, getCommuneBusiness(iwc).getCommunes(), "getCommuneName");
			CommuneHome commHome = (CommuneHome) IDOLookup.getHome(Commune.class);
//			if (user)
//			Commune defaultComm = commHome.findDefaultCommune();
//			if (defailtComm != null) {
				
//			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		RadioButton rbYes = new RadioButton(prm_primary_group_id, ID.toString());
		RadioButton rbNo = new RadioButton(prm_primary_group_id, "-1");
		if (user != null) {
			if (user.hasRelationTo(communeGroup)) {
				//communes.setSelectedElement()
			}
			if (user.getPrimaryGroupID() == ID.intValue() || user.hasRelationTo(communeGroup))
				rbYes.setSelected(true);
			else
				rbNo.setSelected(true);
		}
		else {
			rbYes.setSelected(true);
		}
		groupTable.add(communes, 1, 2);
		//groupTable.add(rbYes, 1, 2);
		//Text tYes = new Text(iwrb.getLocalizedString("mbe.citizen_in_commune_yes", "Yes"));
		//tYes.setStyleClass(textFontStyleName);
		//groupTable.add(tYes, 2, 2);
		//groupTable.add(rbNo, 1, 3);
		//Text tNo = new Text(iwrb.getLocalizedString("mbe.citizen_in_commune_no", "No"));
		//tNo.setStyleClass(textFontStyleName);
		//groupTable.add(tNo, 2, 3);
		addToMainPart(groupTable);
	}
	*/
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserEditor#isAllowPersonalIdEdit(com.idega.user.data.User)
	 */
	public boolean isAllowPersonalIdEdit(User user) {
		if (user != null && user.getPersonalID()!=null) {
			if (user.getPersonalID().indexOf("TF") != -1)
				return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserEditor#isValidPersonalID(java.lang.String)
	 */
	protected boolean isValidPersonalID(String pid) {
		if(pid.length()==12)
			return PIDChecker.getInstance().isValid(pid,true);
		return false;

	}

	protected CommuneBusiness getCommuneBusiness(IWContext iwc) {
		try {
			return (CommuneBusiness) IBOLookup.getServiceInstance(iwc, CommuneBusiness.class);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.presentation.UserEditor#getCommune(com.idega.user.data.User)
	 */
	protected Commune getCommune(IWContext iwc, User user) {
		try {
			if (user != null) {
				CommuneUserBusiness communeUserService = getCommuneUserService(iwc);
				Group communeGroup = communeUserService.getRootCitizenGroup();
				Integer ID = communeGroup != null ? (Integer) communeGroup.getPrimaryKey() : new Integer(-1);
				if ( user.getPrimaryGroupID() == ID.intValue() || user.hasRelationTo(communeGroup) ) {
					if (super.showDefaultCommuneOption) {
						return getCommuneBusiness(iwc).getCommuneHome().findDefaultCommune();
					} else {
						return null;
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return null;
	}

}
