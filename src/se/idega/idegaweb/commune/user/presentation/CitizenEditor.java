/*
 * Created on Aug 18, 2003
 * 
 */
package se.idega.idegaweb.commune.user.presentation;

import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCohabitantFound;
import is.idega.block.family.business.NoSpouseFound;

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
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

import is.idega.block.family.presentation.UserEditor;

/**
 * CitizenEditor
 * 
 * @author aron
 * @version 1.0
 */
public class CitizenEditor extends UserEditor {

	/**
	 * 
	 */
	public CitizenEditor() {
		super();
		setAllowPersonalIdEdit(false);
		setBundleIdentifer(CommuneBlock.IW_BUNDLE_IDENTIFIER);
		setShowSeperators(true);
		setGroupRelationConnectorWindow(CommuneFamilyRelationConnector.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.presentation.UserEditor#presentateUserRelations(com.idega.presentation.IWContext)
	 */
	protected void presentUserRelations(IWContext iwc) throws RemoteException {
		Table relationsTable = new Table();
		relationsTable.setCellspacing(4);
		relationsTable.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		
		if (user != null) {
			addSeperator(iwrb.getLocalizedString("mbe.user_relations", "User relations"));
			CommuneFamilyService familyService = getFamilyService(iwc);

			// parent handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.parents", "Parents")), 1, row++);
			Collection parents = null;
			try {
				parents = familyService.getParentsFor(user);
				if (parents != null && !parents.isEmpty()) {
					for (Iterator iter = parents.iterator(); iter.hasNext();) {
						User parent = (User) iter.next();
						relationsTable.add(getRelatedUserLink(parent), 1, row);
						relationsTable.add(PersonalIDFormatter.format(parent.getPersonalID(), iwc.getCurrentLocale()), 2, row++);
					}
				}
			}
			catch (Exception e1) {
			}
			row++;

			// partner handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.spouse", "Spouse")), 1, row++);
			User partner = null;
			try {
				partner = familyService.getSpouseFor(this.user);
			}
			catch (NoSpouseFound e) {
			}
			catch (Exception e) {
			}
			if (partner != null) {
				relationsTable.add(getRelatedUserLink(partner), 1, row);
				relationsTable.add(PersonalIDFormatter.format(partner.getPersonalID(), iwc.getCurrentLocale()), 2, row++);
			}
			row++;

			// cohabitant handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.cohabitant", "Cohabitant")), 1, row);
			User cohabitant = null;
			try {
				cohabitant = familyService.getCohabitantFor(this.user);
			}
			catch (NoCohabitantFound e) {
			}
			if (cohabitant != null) {
				relationsTable.add(getRelatedUserLink(cohabitant), 1, row);
				relationsTable.add(PersonalIDFormatter.format(cohabitant.getPersonalID(), iwc.getCurrentLocale()), 2, row++);
			}
			row++;

			// biological children handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.parential_children", "Parential children")), 1, row++);
			Collection children = null;
			Collection custodianChildren = null;
			Text star = new Text(" *");
			star.setStyleClass(STYLENAME_HEADER);
			try {
				children = familyService.getChildrenFor(user);
				custodianChildren = familyService.getChildrenInCustodyOf(user);
				if (custodianChildren != null && !custodianChildren.isEmpty()) {
					for (Iterator iter = custodianChildren.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						relationsTable.add(getRelatedUserLink(child), 1, row);
						if (children != null && !children.contains(child)) {
							relationsTable.add(star, 1, row);
						}
						relationsTable.add(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale()), 2, row++);
						row++;
					}
				}
			}
			catch (Exception e2) {
			}
		}
		relationsTable.setWidth(1, "300");
		row++;
		
		Text infoText = new Text(iwrb.getLocalizedString("person_is_only_custodian", "* Not child, person is only custodian"));
		infoText.setStyleClass(STYLENAME_TEXT);
		relationsTable.add(infoText, 1, row++);
		row++;

		relationsTable.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		relationsTable.add(getEditButton(iwc, ACTION_EDIT_RELATIONS), 2, row);

		addToMainPart(relationsTable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.presentation.UserEditor#presentateUserRelations(com.idega.presentation.IWContext)
	 */
	protected void editUserRelations(IWContext iwc) throws RemoteException {
		Table relationsTable = new Table();
		relationsTable.setWidth(Table.HUNDRED_PERCENT);
		relationsTable.setCellspacing(4);
		int row = 1;

		if (user != null) {
			addSeperator(iwrb.getLocalizedString("mbe.user_relations", "User relations"));
			CommuneFamilyService familyService = getFamilyService(iwc);
			// partner handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.spouse", "Spouse")), 1, row);
			User partner = null;
			try {
				partner = familyService.getSpouseFor(this.user);
			}
			catch (NoSpouseFound e) {
			}
			catch (Exception e) {
			}
			if (partner != null) {
				relationsTable.add(getRelatedUserLink(partner), 2, row);
				relationsTable.add(getDisconnectorLink(familyService.getSpouseRelationType(), null, (Integer) user.getPrimaryKey(), (Integer) partner.getPrimaryKey(), getDeleteIcon(iwrb.getLocalizedString("mbe.remove_spouse_relation", "Remove spouse relationship"))), 3, row);
			}

			// cohabitant handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.cohabitant", "Cohabitant")), 5, row);
			User cohabitant = null;
			try {
				cohabitant = familyService.getCohabitantFor(this.user);
			}
			catch (NoCohabitantFound e) {
			}
			if (cohabitant != null) {
				relationsTable.add(getRelatedUserLink(cohabitant), 6, row);
				relationsTable.add(getDisconnectorLink(familyService.getCohabitantRelationType(), null, (Integer) user.getPrimaryKey(), (Integer) cohabitant.getPrimaryKey(), getDeleteIcon(iwrb.getLocalizedString("mbe.remove_cohabitant_relation", "Remove cohabitant relationship"))), 7, row);
			}
			row++;

			// parent handling
			int parentStartRow = row, custodianStartRow = row;
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.parents", "Parents")), 1, row);
			Collection parents = null;
			try {
				parents = familyService.getParentsFor(user);
				if (parents != null && !parents.isEmpty()) {
					for (Iterator iter = parents.iterator(); iter.hasNext();) {
						User parent = (User) iter.next();
						relationsTable.add(getRelatedUserLink(parent), 2, parentStartRow);
						String relationType = familyService.getParentRelationType();
						relationsTable.add(getDisconnectorLink(null, relationType, (Integer) user.getPrimaryKey(), (Integer) parent.getPrimaryKey(), getDeleteIcon(iwrb.getLocalizedString("mbe.remove_parent_relation", "Remove parent relationship"))), 3, parentStartRow);
						parentStartRow++;
					}
				}
			}
			catch (Exception e1) {
			}

			// custodians handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.custodians", "Custodians")), 5, custodianStartRow);
			Collection custodians = null;
			try {
				custodians = familyService.getCustodiansFor(user, false);
				if (custodians != null && !custodians.isEmpty()) {
					for (Iterator iter = custodians.iterator(); iter.hasNext();) {
						User custodian = (User) iter.next();
						relationsTable.add(getRelatedUserLink(custodian), 6, custodianStartRow);
						String relationType = familyService.getCustodianRelationType();
						relationsTable.add(getDisconnectorLink(null, relationType, (Integer) user.getPrimaryKey(), (Integer) custodian.getPrimaryKey(), getDeleteIcon(iwrb.getLocalizedString("mbe.remove_custodian_relation", "Remove custodian relationship"))), 7, custodianStartRow);
						custodianStartRow++;
					}
				}
			}
			catch (Exception e1) {
			}
			row = Math.max(custodianStartRow, parentStartRow) + 1;

			// biological children handling
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.parential_children", "Parential children")), 1, row);
			Collection children = null;
			int childrowstart = row;
			try {
				children = familyService.getChildrenFor(user);
				if (children != null && !children.isEmpty()) {
					for (Iterator iter = children.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						relationsTable.add(getRelatedUserLink(child), 2, row);
						relationsTable.add(getDisconnectorLink(familyService.getParentRelationType(), null, (Integer) user.getPrimaryKey(), (Integer) child.getPrimaryKey(), getDeleteIcon(iwrb.getLocalizedString("mbe.remove_child_relation", "Remove child relationship"))), 3, row);
						row++;
					}
				}
			}
			catch (Exception e2) {
			}
			// custody children handling
			row = childrowstart;
			relationsTable.add(getHeader(iwrb.getLocalizedString("mbe.custody_children", "Custody children")), 5, row);
			try {
				children = familyService.getChildrenInCustodyOf(user);
				if (children != null && !children.isEmpty()) {
					for (Iterator iter = children.iterator(); iter.hasNext();) {
						User child = (User) iter.next();
						relationsTable.add(getRelatedUserLink(child), 6, row);
						relationsTable.add(getDisconnectorLink(familyService.getCustodianRelationType(), null, (Integer) user.getPrimaryKey(), (Integer) child.getPrimaryKey(), getDeleteIcon(iwrb.getLocalizedString("mbe.remove_child_relation", "Remove child relationship"))), 7, row);
						row++;
					}
				}
			}
			catch (NoChildrenFound e3) {
				// e3.printStackTrace();
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

		row++;
		relationsTable.mergeCells(1, row, relationsTable.getColumns(), row);
		relationsTable.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		relationsTable.add(getCancelButton(iwc), 1, row);

		addToMainPart(relationsTable);
		
		presentButtons(iwc);
		presentButtonRegister(iwc);
	}

	/**
	 * Returns the default delete icon with the tooltip specified.
	 * 
	 * @param toolTip
	 *          The tooltip to display on mouse over.
	 * @return Image The delete icon.
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.presentation.UserEditor#storeUserAsDeceased(com.idega.presentation.IWContext,
	 *      java.lang.Integer, java.util.Date)
	 */
	protected void storeUserAsDeceased(IWContext iwc, Integer userID, Date deceasedDate) {
		try {
			getCommuneUserService(iwc).setUserAsDeceased(userID, deceasedDate);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.presentation.UserEditor#presentateButtonRegister(com.idega.presentation.IWContext)
	 */
	protected void presentButtonRegister(IWContext iwc) {
		try {
			int pageID = getParentPageID();
			Integer thisPageID = pageID > 0 ? new Integer(pageID) : null;
			Table bTable = new Table(9, 2);

			CommuneFamilyService logic = getFamilyService(iwc);
			Integer userID = (Integer) user.getPrimaryKey();
			Image addImage = iwb.getImage("shared/edit.gif", 12, 12);
			bTable.setWidth(3, 10);
			bTable.setWidth(6, 10);

			SubmitButton spouseButton = getConnectorButton(iwc, addImage, iwrb.getLocalizedString("mbe.register_spouse", "Register spouse"), userID, logic.getSpouseRelationType(), null, thisPageID);
			bTable.add(spouseButton, 1, 1);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_spouse", "Register spouse")), 2, 1);

			SubmitButton mateButton = getConnectorButton(iwc, addImage, iwrb.getLocalizedString("mbe.register_mate", "Register mate"), userID, logic.getCohabitantRelationType(), null, thisPageID);
			bTable.add(mateButton, 1, 2);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_mate", "Register mate")), 2, 2);

			SubmitButton parentButton = getConnectorButton(iwc, addImage, iwrb.getLocalizedString("mbe.register_parent", "Register parent"), userID, logic.getChildRelationType(), logic.getParentRelationType(), thisPageID);
			bTable.add(parentButton, 4, 1);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_parent", "Register parent")), 5, 1);
			
			SubmitButton custodianButton = getConnectorButton(iwc, addImage, iwrb.getLocalizedString("mbe.register_custodian", "Register custodian"), userID, logic.getChildRelationType(), logic.getCustodianRelationType(), thisPageID);
			bTable.add(custodianButton, 4, 2);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_custodian", "Register custodian")), 5, 2);

			SubmitButton childButton = getConnectorButton(iwc, addImage, iwrb.getLocalizedString("mbe.register_parential_child", "Register parential child"), userID, logic.getParentRelationType(), logic.getChildRelationType(), thisPageID);
			bTable.add(childButton, 7, 1);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_parential_child", "Register parential child")), 8, 1);
			
			SubmitButton custodyChildButton = getConnectorButton(iwc, addImage, iwrb.getLocalizedString("mbe.register_custody_child", "Register custody child"), userID, logic.getCustodianRelationType(), logic.getChildRelationType(), thisPageID);
			bTable.add(custodyChildButton, 7, 2);
			bTable.add(getText(iwrb.getLocalizedString("mbe.register_custody_child", "Register custody child")), 8, 2);
			addButton(bTable);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.presentation.UserEditor#isAllowPersonalIdEdit(com.idega.user.data.User)
	 */
	public boolean isAllowPersonalIdEdit(User user) {
		if (user != null && user.getPersonalID() != null) {
			if (user.getPersonalID().indexOf("TF") != -1)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.presentation.UserEditor#isValidPersonalID(java.lang.String)
	 */
	protected boolean isValidPersonalID(String pid) {
		if (pid.length() == 12)
			return PIDChecker.getInstance().isValid(pid, true);
		return false;

	}

	protected CommuneBusiness getCommuneBusiness(IWContext iwc) {
		try {
			return (CommuneBusiness) IBOLookup.getServiceInstance(iwc, CommuneBusiness.class);
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see is.idega.idegaweb.member.presentation.UserEditor#getCommune(com.idega.user.data.User)
	 */
	protected Commune getCommune(IWContext iwc, User user) {
		try {
			if (user != null) {
				CommuneUserBusiness communeUserService = getCommuneUserService(iwc);
				Group communeGroup = communeUserService.getRootCitizenGroup();
				Integer ID = communeGroup != null ? (Integer) communeGroup.getPrimaryKey() : new Integer(-1);
				if (user.getPrimaryGroupID() == ID.intValue() || user.hasRelationTo(communeGroup)) {
					if (super.showDefaultCommuneOption) {
						return getCommuneBusiness(iwc).getCommuneHome().findDefaultCommune();
					}
					else {
						return null;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
}