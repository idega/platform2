package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author Gimmi
 */
public class CommuneEditor extends CommuneBlock {

	private String ACTION = "ce_a";
	private String ACTION_PARAMETER_NEW = "ce_n";
	private String ACTION_PARAMETER_EDIT = "ce_e";
	private String ACTION_PARAMETER_DELETE = "ce_d";
	private String ACTION_PARAMETER_SAVE = "ce_sn";
	private String PARAMETER_NAME = "ce_pn";
	private String PARAMETER_CODE = "ce_pc";
	private String PARAMETER_COMMUNE_ID = "ce_pci";
	private String PARAMETER_DEFAULT = "ce_pdf";

	IWResourceBundle iwrb = null;

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = getResourceBundle();
		getForm(iwc);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private void getForm(IWContext iwc) throws RemoteException, Exception {
		Form form = new Form();
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		form.add(table);
		
		boolean addInputs = false;
		int communeId = -1;
		String action = iwc.getParameter(ACTION);
		if (action == null) {action = "";}
		if (action.equals(ACTION_PARAMETER_DELETE)) {
			if (!deleteCommune(iwc)) {
				add(getErrorText(iwrb.getLocalizedString("commune.commune_not_deleted","Commune was not deleted")));
			}
		} else if (action.equals(ACTION_PARAMETER_SAVE)) {
			addInputs = !saveCommune(iwc);
			if (iwc.isParameterSet(PARAMETER_COMMUNE_ID)) {
				communeId = Integer.parseInt(iwc.getParameter(PARAMETER_COMMUNE_ID));
			}
		} else if (action.equals(ACTION_PARAMETER_NEW)) {
			addInputs = true;
		} else if (action.equals(ACTION_PARAMETER_EDIT) && iwc.isParameterSet(PARAMETER_COMMUNE_ID)) {
			addInputs = true;
			communeId = Integer.parseInt(iwc.getParameter(PARAMETER_COMMUNE_ID));
		}
		
		int row = 1;
		
		table.add(super.getSmallHeader(iwrb.getLocalizedString("commune.name", "Name")), 1, row);
		table.add(super.getSmallHeader(iwrb.getLocalizedString("commune.code", "Code")), 2, row);
		table.add(super.getSmallHeader(iwrb.getLocalizedString("commune.default", "Default")), 3, row);
		
		Collection communes = getCommuneBusiness(iwc).getCommunes();
		Iterator iter = communes.iterator();
		Commune commune;
		Link delLink;
		Link editLink;
		RadioButton rad;
		Object communePK;
		Object defaultCommunePK = "";
		try {
			Commune defaultCommune = getCommuneBusiness(iwc).getCommuneHome().findDefaultCommune();
			if (defaultCommune != null) {
				defaultCommunePK = defaultCommune.getPrimaryKey();
			}
		} catch (Exception e) {}
		while (iter.hasNext()) {
			commune = (Commune) iter.next();
			communePK = commune.getPrimaryKey();
			if ((communeId == Integer.parseInt(commune.getPrimaryKey().toString()) && addInputs)) {
				row = addInputFields(iwc, table, communeId, row);
			} else {
				++row;
				
				if (!addInputs) {
					editLink = new Link(getSmallText(commune.getCommuneName()));
					editLink.addParameter(ACTION, ACTION_PARAMETER_EDIT);
					editLink.addParameter(PARAMETER_COMMUNE_ID, communePK.toString());
					table.add(editLink, 1, row);
					table.add(getSmallText(commune.getCommuneCode()), 2, row);
					rad = new RadioButton(PARAMETER_DEFAULT, communePK.toString());
					if (communePK.toString().equals(defaultCommunePK.toString())) {
						rad.setSelected(true);
					}
					table.add(rad, 3, row);
				
					delLink = new Link(super.getDeleteIcon(iwrb.getLocalizedString("commune.delete","Delete")));
					delLink.addParameter(ACTION, ACTION_PARAMETER_DELETE);
					delLink.addParameter(PARAMETER_COMMUNE_ID, commune.getPrimaryKey().toString());
					editLink = new Link(super.getEditIcon(iwrb.getLocalizedString("commune.edit","Edit")));
					editLink.addParameter(ACTION, ACTION_PARAMETER_EDIT);
					editLink.addParameter(PARAMETER_COMMUNE_ID, communePK.toString());
					table.add(editLink, 4, row);
					table.add(delLink, 5, row);
				} else {
					table.add(getSmallText(commune.getCommuneName()), 1, row);
					table.add(getSmallText(commune.getCommuneCode()), 2, row);
					if (communePK.toString().equals(defaultCommunePK.toString())) {
						table.add(getSmallText("X"),3, row);
					}
				}
			}
		}
		
		if (communeId <= 0 && addInputs) {
			row = addInputFields(iwc, table, communeId, row);
		}
		
		++row;
			SubmitButton saveCommune = (SubmitButton) getButton(new SubmitButton(iwrb.getLocalizedString("commune.save","Save"), ACTION, ACTION_PARAMETER_SAVE));
			table.add(saveCommune, 1, row);
		if (!addInputs) {
			SubmitButton newCommune = (SubmitButton) getButton(new SubmitButton(iwrb.getLocalizedString("commune.new","New"), ACTION, ACTION_PARAMETER_NEW));
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
			table.add(newCommune, 1, row);
		} else {
			table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
			BackButton back = (BackButton) getButton(new BackButton(iwrb.getLocalizedString("commune.cancel","Cancel")));
			table.add(back, 1, row);
		}
		
		table.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		table.setRowColor(1, getHeaderColor());
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnWidth(3, "30");
		table.setColumnWidth(4, "10");
		table.setColumnWidth(5, "10");
		
		add(form);
	}
	
	private int addInputFields(IWContext iwc, Table table, int communeId, int row) throws RemoteException, Exception {
		++row;
		TextInput nameInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME));
		TextInput codeInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_CODE));
		nameInput.setAsNotEmpty(iwrb.getLocalizedString("commune.name_input_empty","The NAME input field cannot be empty"));
		codeInput.setAsNotEmpty(iwrb.getLocalizedString("commune.code_input_empty","The CODE input field cannot be empty"));
		codeInput.setAsIntegers(iwrb.getLocalizedString("commune.code_can_only_contain_numbers","Code can only contain numbers"));
		table.add(nameInput, 1, row);
		table.add(codeInput, 2, row);
		if (communeId > 0) {
			Commune comm = getCommuneBusiness(iwc).getCommune(communeId);
			nameInput.setContent(comm.getCommuneName());
			codeInput.setContent(comm.getCommuneCode());
			table.add(new HiddenInput(PARAMETER_COMMUNE_ID, Integer.toString(communeId)));
		} else {
			String name = iwc.getParameter(PARAMETER_NAME);
			String code = iwc.getParameter(PARAMETER_CODE);
			if (name != null) {
				nameInput.setContent(name);
				codeInput.setContent(code);			
			}
		}
		return row;
	}

	private boolean saveCommune(IWContext iwc) {
		String name = iwc.getParameter(PARAMETER_NAME);
		String code = iwc.getParameter(PARAMETER_CODE);
		String commId = iwc.getParameter(PARAMETER_COMMUNE_ID);
		String newDefaultComm = iwc.getParameter(PARAMETER_DEFAULT);
	
		boolean continueSave = true;
		
		if (name != null && code != null && !code.equals("") && !name.equals("")) {
			try {
				if (code.length() != 4) {
					add(getErrorText(iwrb.getLocalizedString("commune.code_must_be_of_the_length_4", "Code must be of the length 4")+Text.getBreak()));
					continueSave = false;
				}
				try {
					Integer.parseInt(code);
				} catch (NumberFormatException n) {
					add(getErrorText(iwrb.getLocalizedString("commune.code_can_only_contain_numbers","Code can only contain numbers")+Text.getBreak()));
					continueSave = false;
				}
				CommuneHome cHome = getCommuneBusiness(iwc).getCommuneHome();
				Commune codeComm = getCommuneBusiness(iwc).getCommuneByCode(code);
				Commune nameComm = getCommuneBusiness(iwc).getCommuneByName(name);
				Commune comm;
				if (commId != null) {
					comm = cHome.findByPrimaryKey(new Integer(commId));
					if (codeComm != null && !codeComm.getPrimaryKey().equals(comm.getPrimaryKey())) {
						add(getErrorText(iwrb.getLocalizedString("commune.code_already_exists","Code already exists, please enter a new code.")+Text.getBreak()));
						continueSave = false;
					}
					if (nameComm != null && !nameComm.getPrimaryKey().equals(comm.getPrimaryKey())) {
						add(getErrorText(iwrb.getLocalizedString("commune.commune_name_already_exists","Name already exists, please enter a new name.")+Text.getBreak()));
						continueSave = false;
					}
						
				} else {
					comm = cHome.create();
					if (codeComm != null) {
						add(getErrorText(iwrb.getLocalizedString("commune.code_already_exists","Code already exists, please enter a new code.")+Text.getBreak()));
						continueSave = false;
					}
					if (nameComm != null) {
						add(getErrorText(iwrb.getLocalizedString("commune.commune_name_already_exists","Name already exists, please enter a new name.")+Text.getBreak()));
						continueSave = false;
					}
				}
				
				if (continueSave) {
					comm.setCommuneName(name);
					comm.setCommuneCode(code);
					comm.store();
				}
				
				return continueSave;
				
			} catch (Exception e) {
				e.printStackTrace(System.err);
				add(getErrorText(iwrb.getLocalizedString("commune.commune_not_saved","Commune was not saved")+Text.getBreak()));
			}
		} else if (newDefaultComm != null) {
			try {
				Commune defaultCommune = getCommuneBusiness(iwc).getDefaultCommune();
				
				if (defaultCommune == null) {
					System.out.println("Setting new default commune");
					Commune comm = getCommuneBusiness(iwc).getCommuneHome().findByPrimaryKey(new Integer(newDefaultComm));
					comm.setIsDefault(true);
					comm.store();
				} else {
					if ( (new Integer(newDefaultComm)).intValue() != ( new Integer(defaultCommune.getPrimaryKey().toString())).intValue() ) {
						System.out.println("Changing default commune");
						Commune comm = getCommuneBusiness(iwc).getCommuneHome().findByPrimaryKey(new Integer(newDefaultComm));
						comm.setIsDefault(true);
						comm.store();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				add(getErrorText(iwrb.getLocalizedString("commune.commune_not_saved","Commune was not saved")+Text.getBreak()));
			}
		}
		
		return true;
	}
	
	private boolean deleteCommune(IWContext iwc) throws RemoteException, Exception {
		String commId = iwc.getParameter(PARAMETER_COMMUNE_ID);
		if (commId != null) {
			Commune comm = getCommuneBusiness(iwc).getCommune(Integer.parseInt(commId));
			try {
				comm.remove();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	protected CommuneBusiness getCommuneBusiness(IWContext iwc) throws Exception {
		return (CommuneBusiness) IBOLookup.getServiceInstance(iwc, CommuneBusiness.class);
	}

}
