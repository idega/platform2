/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.GroupChooser;
import com.idega.user.presentation.UserGroupTab;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class ClubDivisionTab extends UserGroupTab{

	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "cdiv_tab_name";

	private static final String DEFAULT_TAB_NAME = "Club Division";

	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

    private static final String HELP_TEXT_KEY = "club_division_tab";

    private TextInput numberField;

    private TextInput ssnField;

    private DateInput foundedField;

    private DropdownMenu connectionToSpecialField;

    private GroupChooser boardGroupField;

    private Text numberText;

    private Text ssnText;

    private Text foundedText;

    private Text connectionToSpecialText;

    private Text boardGroupText;

    private String numberFieldName;

    private String ssnFieldName;

    private String foundedFieldName;

    private String connectionToSpecialFieldName;

    private String boardGroupFieldName;

    public ClubDivisionTab() {
        super();
        IWContext iwc = IWContext.getInstance();
        IWResourceBundle iwrb = getResourceBundle(iwc);

        setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
    }

    public ClubDivisionTab(Group group) {
        this();
        setGroupId(((Integer) group.getPrimaryKey()).intValue());
    }

    public String getBundleIdentifier() {
        return IW_BUNDLE_IDENTIFIER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#initializeFieldNames()
     */
    public void initializeFieldNames() {
        this.numberFieldName = "cdiv_number";
        this.ssnFieldName = "cdiv_ssn";
        this.foundedFieldName = "cdiv_founded";
        this.connectionToSpecialFieldName = "cdiv_special";
        this.boardGroupFieldName = "cdiv_board";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
     */
    public void initializeFieldValues() {
        this.fieldValues = new Hashtable();
        this.fieldValues.put(this.numberFieldName, "");
        this.fieldValues.put(this.ssnFieldName, "");
        this.fieldValues.put(this.foundedFieldName, new IWTimestamp().getDate()
                .toString());
        this.fieldValues.put(this.connectionToSpecialFieldName, "");
        this.fieldValues.put(this.boardGroupFieldName, "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
     */
    public void updateFieldsDisplayStatus() {
    			String number = (String) this.fieldValues.get(this.numberFieldName);
        this.numberField.setContent(number);
        if(number != null && !number.equals("")) {
        		this.numberField.setDisabled(true);
        }
        this.ssnField.setContent((String) this.fieldValues.get(this.ssnFieldName));
        this.foundedField.setContent((String) this.fieldValues.get(this.foundedFieldName));
        String connection = (String) this.fieldValues
                .get(this.connectionToSpecialFieldName);
        this.connectionToSpecialField.setSelectedElement(connection);
        if (connection != null && !connection.equals("")) {
			this.connectionToSpecialField.setDisabled(true);
		}
        try {
            GroupHome home = (GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class);
            String groupId = (String) this.fieldValues.get(this.boardGroupFieldName);

            if (groupId != null && !groupId.equals("")) {
                try {
                    int index = groupId.indexOf("_");
                    groupId = groupId.substring(index + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Group group = home.findByPrimaryKey(new Integer(
                        groupId));
                this.boardGroupField.setSelectedGroup(groupId, group.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#initializeFields()
     */
    public void initializeFields() {
		    	IWContext iwc = IWContext.getInstance();
		  		IWResourceBundle iwrb = getResourceBundle(iwc);
        this.numberField = new TextInput(this.numberFieldName);
        this.ssnField = new TextInput(this.ssnFieldName);
        //		_ssnField.setAsIcelandicSSNumber("Vart�lupr�fun stemmir ekki");
        this.foundedField = new DateInput(this.foundedFieldName);
        this.connectionToSpecialField = new DropdownMenu(
                this.connectionToSpecialFieldName);

        List special = null;
        try {
            special = (List) ((GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class)).findGroupsByType("iwme_league");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (special != null) {
        	final Collator collator = Collator.getInstance(iwc.getLocale());
    			Collections.sort(special,new Comparator() {
    				public int compare(Object arg0, Object arg1) {
    					return collator.compare(((Group) arg0).getName(), ((Group) arg1).getName());
    				}				
    			});
    			this.connectionToSpecialField.addMenuElement("-1",iwrb.getLocalizedString("clubinformationtab.choose_reg_un","Choose a regional union..."));
    		
            Iterator it = special.iterator();
            while (it.hasNext()) {
                Group spec = (Group) it.next();
                this.connectionToSpecialField.addMenuElement(((Integer) spec
                        .getPrimaryKey()).intValue(), spec.getName());
            }
        }

        this.boardGroupField = new GroupChooser(this.boardGroupFieldName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
     */
    public void initializeTexts() {
    		IWContext iwc = IWContext.getInstance();
    		IWResourceBundle iwrb = getResourceBundle(iwc);

    		this.numberText = new Text(iwrb.getLocalizedString(this.numberFieldName, "Number"));
    		this.numberText.setBold();
    		
    		this.ssnText = new Text(iwrb.getLocalizedString(this.ssnFieldName, "SSN") + ":");
    		this.ssnText.setBold();
    		
    		this.foundedText = new Text(iwrb.getLocalizedString(this.foundedFieldName, "Founded") + ":");
    		this.foundedText.setBold();
    		
    		this.connectionToSpecialText = new Text(iwrb.getLocalizedString(this.connectionToSpecialFieldName, "Connection to special") + ":");
    		this.connectionToSpecialText.setBold();
    		
    		this.boardGroupText = new Text(iwrb.getLocalizedString(this.boardGroupFieldName, "Board") + ":");
    		this.boardGroupText.setBold();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
     */
    public void lineUpFields() {
  		Table t = new Table(2, 3);
  		t.setCellpadding(5);
  		t.setCellspacing(0);
  		t.setWidth(Table.HUNDRED_PERCENT);
  		
  		t.add(this.numberText, 1, 1);
  		t.add(Text.getBreak(), 1, 1);
  		t.add(this.numberField, 1, 1);
  		
  		t.add(this.ssnText, 2, 1);
  		t.add(Text.getBreak(), 2, 1);
  		t.add(this.ssnField, 2, 1);
  		
  		t.add(this.foundedText, 1, 2);
  		t.add(Text.getBreak(), 1, 2);
  		t.add(this.foundedField, 1, 2);
  		
  		t.add(this.connectionToSpecialText, 2, 2);
  		t.add(Text.getBreak(), 2, 2);
  		t.add(this.connectionToSpecialField, 2, 2);
  		
  		t.add(this.boardGroupText, 1, 3);
  		t.add(Text.getBreak(), 1, 3);
  		t.add(this.boardGroupField, 1, 3);
  		
  		add(t);
    }

  	public void main(IWContext iwc) {
  		getPanel().addHelpButton(getHelpButton());		
  	}

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
     */
    public boolean collect(IWContext iwc) {
        if (iwc != null) {
            String number = iwc.getParameter(this.numberFieldName);
            String ssn = iwc.getParameter(this.ssnFieldName);
            String founded = iwc.getParameter(this.foundedFieldName);
            String connection = iwc.getParameter(this.connectionToSpecialFieldName);
            String boardGroup = iwc.getParameter(this.boardGroupFieldName);

            if (number != null) {
				this.fieldValues.put(this.numberFieldName, number);
			}
			else {
				this.fieldValues.put(this.numberFieldName, "");
			}
            if (ssn != null) {
				this.fieldValues.put(this.ssnFieldName, ssn);
			}
			else {
				this.fieldValues.put(this.ssnFieldName, "");
			}
            if (founded != null) {
				this.fieldValues.put(this.foundedFieldName, founded);
			}
			else {
				this.fieldValues.put(this.foundedFieldName, "");
			}
            if (connection != null) {
				this.fieldValues.put(this.connectionToSpecialFieldName, connection);
			}
			else {
				this.fieldValues.put(this.connectionToSpecialFieldName, "");
			}
            if (boardGroup != null) {
            	boardGroup = boardGroup.substring(boardGroup.lastIndexOf("_") + 1);
                this.fieldValues.put(this.boardGroupFieldName, boardGroup);
            }
			else {
				this.fieldValues.put(this.boardGroupFieldName, "");
			}

            updateFieldsDisplayStatus();
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
     */
    public boolean store(IWContext iwc) {
        Group group;
        try {
            group = ((GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class)).findByPrimaryKey(new Integer(
                    getGroupId()));

            String number = (String) this.fieldValues.get(this.numberFieldName);
            String ssn = (String) this.fieldValues.get(this.ssnFieldName);
            String founded = (String) this.fieldValues.get(this.foundedFieldName);
            String connection = (String) this.fieldValues
                    .get(this.connectionToSpecialFieldName);
            String board = (String) this.fieldValues.get(this.boardGroupFieldName);

            group.setMetaData(IWMemberConstants.META_DATA_DIVISION_NUMBER, number);
            group.setMetaData(IWMemberConstants.META_DATA_DIVISION_SSN, ssn);
            group.setMetaData(IWMemberConstants.META_DATA_DIVISION_FOUNDED, founded);
            //			group.setMetaData("CLUBDIV_CONN", connection);
            String oldConnection = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
            if ((oldConnection == null || oldConnection.trim().equals("")) && connection != null) {
                String clubName = null;
                Group club = getMemberUserBusiness(iwc).getClubForGroup(group);
                if (club != null) {
                    clubName = club.getName();
                }
                group.setMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION, connection);
                group.store();
                getClubInformationPluginBusiness(iwc).createSpecialConnection(
                        connection, getGroupId(), clubName, iwc);
            }

            group.setMetaData(IWMemberConstants.META_DATA_DIVISION_BOARD,board);

            group.store();
        } catch (RemoteException e) {
            e.printStackTrace(System.err);
            return false;
        } catch (FinderException e) {
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
     */
    public void initFieldContents() {
        Group group;
        try {
            group = ((GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class)).findByPrimaryKey(new Integer(
                    getGroupId()));

            String number = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_NUMBER);
            String ssn = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_SSN);
            String founded = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_FOUNDED);
            String connection = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
            String board = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_BOARD);

            if (number != null) {
                this.fieldValues.put(this.numberFieldName, number);
            }
            if (ssn != null) {
                this.fieldValues.put(this.ssnFieldName, ssn);
            }
            if (founded != null) {
                this.fieldValues.put(this.foundedFieldName, founded);
            }
            if (connection != null) {
                this.fieldValues.put(this.connectionToSpecialFieldName, connection);
            }
            if (board != null) {
                this.fieldValues.put(this.boardGroupFieldName, board);
            }

            updateFieldsDisplayStatus();
        } catch (RemoteException e) {
            e.printStackTrace(System.err);
        } catch (FinderException e) {
            e.printStackTrace(System.err);
        }
    }

    public ClubInformationPluginBusiness getClubInformationPluginBusiness(
            IWApplicationContext iwc) {
        ClubInformationPluginBusiness business = null;
        if (business == null) {
            try {
                business = (ClubInformationPluginBusiness) com.idega.business.IBOLookup
                        .getServiceInstance(iwc,
                                ClubInformationPluginBusiness.class);
            } catch (java.rmi.RemoteException rme) {
                throw new RuntimeException(rme.getMessage());
            }
        }
        return business;
    }

    public MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
        MemberUserBusiness business = null;
        try {
            business = (MemberUserBusiness) com.idega.business.IBOLookup
                    .getServiceInstance(iwc, MemberUserBusiness.class);
        } catch (java.rmi.RemoteException rme) {
            throw new RuntimeException(rme.getMessage());
        }
        return business;
    }

    public Help getHelpButton() {
        IWContext iwc = IWContext.getInstance();
        IWBundle iwb = getBundle(iwc);
        Help help = new Help();
        Image helpImage = iwb.getImage("help.gif");
        help.setHelpTextBundle(MEMBER_HELP_BUNDLE_IDENTIFIER);
        help.setHelpTextKey(HELP_TEXT_KEY);
        help.setImage(helpImage);
        return help;
	}

}