package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.accounting.presentation.SchoolAccountingCommuneBlock;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.event.SchoolEventListener;

import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.presentation.UserChooser;

/**
 * @author Laddi
 * */
public class SchoolClassBuilder extends SchoolAccountingCommuneBlock {
	
	private final String PARAMETER_ACTION = "scb_action";
	private final String PARAMETER_CLASS_NAME = "scb_class_name";
	private final String PARAMETER_TEACHER_ID = "scb_teacher_id";

	private final int ACTION_NEW = 1;
	private final int ACTION_SAVE = 2;
	private final int ACTION_DELETE = 3;
	private final int ACTION_EDIT = 4;
	
	private int action = -1;
	
	private boolean multibleSchools = false;
	private boolean showBunRadioButtons = false;
		

	public SchoolClassBuilder() {
	}
	
	public void init(IWContext iwc) throws RemoteException {
		parseAction(iwc);
		drawForm(iwc);
	}
	
	private void parseAction(IWContext iwc) throws RemoteException {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
			
		if (action == ACTION_SAVE) {
			String name = iwc.getParameter(PARAMETER_CLASS_NAME);
			String sTeacherId = iwc.getParameter(PARAMETER_TEACHER_ID);
			int iTeacherId = -1;
			if (sTeacherId != null) {
				try {
					iTeacherId = Integer.parseInt(sTeacherId);	
				}catch (NumberFormatException n) {
					debug(n.getMessage());
				}	
			}
			getBusiness().getSchoolBusiness().storeSchoolClass(getSchoolClassID(), name, getSchoolID(), getSchoolSeasonID(), getSchoolYearID(), iTeacherId);
		}	
		else if (action == ACTION_DELETE) {
			Collection users = getBusiness().getSchoolBusiness().findStudentsInClass(getSchoolClassID());
			if (users != null && !users.isEmpty()) {
				Iterator iter = users.iterator();
				int previousSeasonID = getBusiness().getPreviousSchoolSeasonID(getSchoolSeasonID());
				while (iter.hasNext()) {
					SchoolClassMember student = (SchoolClassMember) iter.next();
					SchoolChoice choice = getBusiness().getSchoolChoiceBusiness().findByStudentAndSchoolAndSeason(student.getClassMemberId(), getSchoolID(), getSchoolSeasonID());
					getBusiness().setNeedsSpecialAttention(student.getClassMemberId(), previousSeasonID, false);
					if (choice != null)
						getBusiness().getSchoolChoiceBusiness().setAsPreliminary(choice, iwc.getCurrentUser());
				}
			}
			getBusiness().getSchoolBusiness().invalidateSchoolClass(getSchoolClassID());
		}	
	}
	
	private void drawForm(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setEventListener(SchoolEventListener.class);
		
		Table table = new Table(1,3);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setHeight(2, "6");
		form.add(table);
		
		table.add(getNavigationTable(false, multibleSchools, showBunRadioButtons),1,1);
		table.add(getClassTable(iwc),1,3);
		
		add(form);
	}
	
	private Table getClassTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setColumns(4);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setWidth(1,"50%");
		table.setWidth(2,"50%");
		table.setWidth(3,"12");
		table.setWidth(4,"12");
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		int row = 1;
		table.add(getSmallHeader(localize("school.class_name", "Class name")),1,row);
		table.add(getSmallHeader(localize("school.teacher", "Teacher")),2,row);
		HiddenInput classID = new HiddenInput(getSession().getParameterSchoolClassID(),"-1");
		if (action == ACTION_EDIT)
			classID.setValue(getSchoolClassID());
		table.add(classID,3,row++);

		Collection schoolClasses = getBusiness().getSchoolBusiness().findSchoolClassesBySchoolAndSeasonAndYear(getSchoolID(), getSchoolSeasonID(), getSchoolYearID());
		if (!schoolClasses.isEmpty()) {
			Iterator iter = schoolClasses.iterator();
			while (iter.hasNext()) {
				SchoolClass element = (SchoolClass) iter.next();
				User teacher = null;
				
				SubmitButton edit = (SubmitButton) getStyledInterface(new SubmitButton(getEditIcon(""),PARAMETER_ACTION,String.valueOf(ACTION_EDIT)));
				edit.setValueOnClick(getSession().getParameterSchoolClassID(), element.getPrimaryKey().toString());
				edit.setDescription(localize("school.edit_class","Edit this class"));

				SubmitButton delete = (SubmitButton) getStyledInterface(new SubmitButton(getDeleteIcon(""),PARAMETER_ACTION,String.valueOf(ACTION_DELETE)));
				delete.setValueOnClick(getSession().getParameterSchoolClassID(), element.getPrimaryKey().toString());
				delete.setDescription(localize("school.delete_class","Delete this class"));
				if (getBusiness().getSchoolBusiness().getNumberOfStudentsInClass(((Integer)element.getPrimaryKey()).intValue()) > 0)
					delete.setSubmitConfirm(localize("school.confirm_class_delete","This class has students, delete anyway?"));
				
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				if (action == ACTION_EDIT && getSchoolClassID() == ((Integer)element.getPrimaryKey()).intValue()) {
					TextInput nameInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CLASS_NAME));
					nameInput.setValue(element.getName());
					
					Collection userPKs;
					try {
						userPKs = getSchoolUserBusiness(iwc).getTeacherUserIds(getSchoolID());
						UserChooser uc = new UserChooser(PARAMETER_TEACHER_ID);
						uc.setValidUserPks(userPKs);
						table.add(uc,2,row);
					} catch (FinderException e) {
						e.printStackTrace(System.err);
					}
//					contTable.add(uc, 1, 7);
//					TextInput teacherInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_TEACHER_ID));
//					teacherInput.setValue(localize("school.disabled","disabled"));
//					teacherInput.setDisabled(true);
					
					table.add(nameInput,1,row);
//					table.add(teacherInput,2,row);
					table.add(new HiddenInput(getSession().getParameterSchoolClassID(),element.getPrimaryKey().toString()),3,row++);
				}
				else {
					table.add(getSmallText(element.getName()),1,row);
					if ( teacher != null )
						table.add(getSmallText(teacher.getName()),2,row);
					table.add(edit,3,row);
					table.add(delete,4,row++);
				}
			}
		}
		
		if (action == ACTION_NEW) {
			TextInput nameInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CLASS_NAME));
			
			Collection users;
			try {
				users = getSchoolUserBusiness(iwc).getTeachers(getSchoolID());
				UserChooser uc = new UserChooser(PARAMETER_TEACHER_ID);
				uc.setValidUserPks(users);
				table.add(uc,2,row);
			} catch (FinderException e) {
				e.printStackTrace(System.err);
			}
//			TextInput teacherInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_TEACHER_ID));
//			teacherInput.setValue(localize("school.disabled","disabled"));
//			teacherInput.setDisabled(true);
			
			table.add(nameInput,1,row);
//			table.add(teacherInput,2,row++);
			++row;
		}
		
		table.setHeight(row++, 6);
		
		SubmitButton newButton = (SubmitButton) getStyledInterface(new SubmitButton(localize("school.new","New"),PARAMETER_ACTION,String.valueOf(ACTION_NEW)));
		newButton.setDescription(localize("school.create_new_class","Create new clas"));
		newButton.setValueOnClick(getSession().getParameterSchoolClassID(), "-1");
		
		SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("save","Save"),PARAMETER_ACTION,String.valueOf(ACTION_SAVE)));
		submit.setDescription(localize("school.save_class","Save class"));
		
		if (!(action == ACTION_EDIT || action == ACTION_NEW) && getSchoolYearID() != -1)
			table.add(newButton,1,row);
		if ((action == ACTION_EDIT || action == ACTION_NEW) && getSchoolYearID() != -1)
			table.add(submit,1,row);
		table.mergeCells(1, row, table.getColumns(), row);
		table.setRowColor(1, getHeaderColor());
		
		return table;
	}

	private SchoolUserBusiness getSchoolUserBusiness(IWContext iwc) throws RemoteException {
		return (SchoolUserBusiness) IBOLookup.getServiceInstance(iwc, SchoolUserBusiness.class);
	}
	
	/** setters */
	public void setMultipleSchools(boolean multiple) {
		this.multibleSchools = multiple;
	}

	/**
	 * Turns on/off view of radiobuttons for showing BUN administrated shools or not
	 * @param show
	 */
	public void setShowBunRadioButtons(boolean show){
		this.showBunRadioButtons = show;		
	}
		
}