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
        numberFieldName = "cdiv_number";
        ssnFieldName = "cdiv_ssn";
        foundedFieldName = "cdiv_founded";
        connectionToSpecialFieldName = "cdiv_special";
        boardGroupFieldName = "cdiv_board";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
     */
    public void initializeFieldValues() {
        fieldValues = new Hashtable();
        fieldValues.put(numberFieldName, "");
        fieldValues.put(ssnFieldName, "");
        fieldValues.put(foundedFieldName, new IWTimestamp().getDate()
                .toString());
        fieldValues.put(connectionToSpecialFieldName, "");
        fieldValues.put(boardGroupFieldName, "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
     */
    public void updateFieldsDisplayStatus() {
    			String number = (String) fieldValues.get(numberFieldName);
        numberField.setContent(number);
        if(number != null && !number.equals("")) {
        		numberField.setDisabled(true);
        }
        ssnField.setContent((String) fieldValues.get(ssnFieldName));
        foundedField.setContent((String) fieldValues.get(foundedFieldName));
        String connection = (String) fieldValues
                .get(connectionToSpecialFieldName);
        connectionToSpecialField.setSelectedElement(connection);
        if (connection != null && !connection.equals(""))
                connectionToSpecialField.setDisabled(true);
        try {
            GroupHome home = (GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class);
            String groupId = (String) fieldValues.get(boardGroupFieldName);

            if (groupId != null && !groupId.equals("")) {
                try {
                    int index = groupId.indexOf("_");
                    groupId = groupId.substring(index + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Group group = (Group) (home.findByPrimaryKey(new Integer(
                        groupId)));
                boardGroupField.setSelectedGroup(groupId, group.getName());
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
        numberField = new TextInput(numberFieldName);
        ssnField = new TextInput(ssnFieldName);
        //		_ssnField.setAsIcelandicSSNumber("Vart�lupr�fun stemmir ekki");
        foundedField = new DateInput(foundedFieldName);
        connectionToSpecialField = new DropdownMenu(
                connectionToSpecialFieldName);

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
    			connectionToSpecialField.addMenuElement("-1",iwrb.getLocalizedString("clubinformationtab.choose_reg_un","Choose a regional union..."));
    		
            Iterator it = special.iterator();
            while (it.hasNext()) {
                Group spec = (Group) it.next();
                connectionToSpecialField.addMenuElement(((Integer) spec
                        .getPrimaryKey()).intValue(), spec.getName());
            }
        }

        boardGroupField = new GroupChooser(boardGroupFieldName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
     */
    public void initializeTexts() {
    		IWContext iwc = IWContext.getInstance();
    		IWResourceBundle iwrb = getResourceBundle(iwc);

    		numberText = new Text(iwrb.getLocalizedString(numberFieldName, "Number"));
    		numberText.setBold();
    		
    		ssnText = new Text(iwrb.getLocalizedString(ssnFieldName, "SSN") + ":");
    		ssnText.setBold();
    		
    		foundedText = new Text(iwrb.getLocalizedString(foundedFieldName, "Founded") + ":");
    		foundedText.setBold();
    		
    		connectionToSpecialText = new Text(iwrb.getLocalizedString(connectionToSpecialFieldName, "Connection to special") + ":");
    		connectionToSpecialText.setBold();
    		
    		boardGroupText = new Text(iwrb.getLocalizedString(boardGroupFieldName, "Board") + ":");
    		boardGroupText.setBold();
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
  		
  		t.add(numberText, 1, 1);
  		t.add(Text.getBreak(), 1, 1);
  		t.add(numberField, 1, 1);
  		
  		t.add(ssnText, 2, 1);
  		t.add(Text.getBreak(), 2, 1);
  		t.add(ssnField, 2, 1);
  		
  		t.add(foundedText, 1, 2);
  		t.add(Text.getBreak(), 1, 2);
  		t.add(foundedField, 1, 2);
  		
  		t.add(connectionToSpecialText, 2, 2);
  		t.add(Text.getBreak(), 2, 2);
  		t.add(connectionToSpecialField, 2, 2);
  		
  		t.add(boardGroupText, 1, 3);
  		t.add(Text.getBreak(), 1, 3);
  		t.add(boardGroupField, 1, 3);
  		
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
            String number = iwc.getParameter(numberFieldName);
            String ssn = iwc.getParameter(ssnFieldName);
            String founded = iwc.getParameter(foundedFieldName);
            String connection = iwc.getParameter(connectionToSpecialFieldName);
            String boardGroup = iwc.getParameter(boardGroupFieldName);

            if (number != null)
                fieldValues.put(numberFieldName, number);
            else
                fieldValues.put(numberFieldName, "");
            if (ssn != null)
                fieldValues.put(ssnFieldName, ssn);
            else
                fieldValues.put(ssnFieldName, "");
            if (founded != null)
                fieldValues.put(foundedFieldName, founded);
            else
                fieldValues.put(foundedFieldName, "");
            if (connection != null)
                fieldValues.put(connectionToSpecialFieldName, connection);
            else
                fieldValues.put(connectionToSpecialFieldName, "");
            if (boardGroup != null)
                fieldValues.put(boardGroupFieldName, boardGroup);
            else
                fieldValues.put(boardGroupFieldName, "");

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
            group = (Group) (((GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class)).findByPrimaryKey(new Integer(
                    getGroupId())));

            String number = (String) fieldValues.get(numberFieldName);
            String ssn = (String) fieldValues.get(ssnFieldName);
            String founded = (String) fieldValues.get(foundedFieldName);
            String connection = (String) fieldValues
                    .get(connectionToSpecialFieldName);
            String board = (String) fieldValues.get(boardGroupFieldName);

            group.setMetaData(IWMemberConstants.META_DATA_DIVISION_NUMBER, number);
            group.setMetaData(IWMemberConstants.META_DATA_DIVISION_SSN, ssn);
            group.setMetaData(IWMemberConstants.META_DATA_DIVISION_FOUNDED, founded);
            //			group.setMetaData("CLUBDIV_CONN", connection);
            String oldConnection = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
            if (oldConnection == null && connection != null) {
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
            group = (Group) (((GroupHome) com.idega.data.IDOLookup
                    .getHome(Group.class)).findByPrimaryKey(new Integer(
                    getGroupId())));

            String number = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_NUMBER);
            String ssn = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_SSN);
            String founded = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_FOUNDED);
            String connection = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
            String board = group.getMetaData(IWMemberConstants.META_DATA_DIVISION_BOARD);

            if (number != null) {
                fieldValues.put(numberFieldName, number);
            }
            if (ssn != null) {
                fieldValues.put(ssnFieldName, ssn);
            }
            if (founded != null) {
                fieldValues.put(foundedFieldName, founded);
            }
            if (connection != null) {
                fieldValues.put(connectionToSpecialFieldName, connection);
            }
            if (board != null) {
                fieldValues.put(boardGroupFieldName, board);
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