package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractTariff;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.finance.business.FinanceService;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffHome;
import com.idega.block.finance.data.TariffIndex;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FloatInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class ContractTariffWindow extends CampusWindow {
	private static final String PARAMETER_SAVE = "save";

	private static final String PARAMETER_CHANGE_KEY_STATUS_DATE = "change_key_status_date";

	private static final String PARAMETER_CHANGE_KEY_STATUS = "change_key_status";

	private static final String PARAMETER_NEW_EMAIL = "new_email";

	private static final String PARAMETER_MOVING_DATE = "moving_date";

	private static final String PARAMETER_SYNC_DATES = "sync_dates";

	private static final String PARAMETER_END_OLD_CONTRACT = "end_old_contr";

	private static final String PARAMETER_TO_DATE = "to_date";

	private static final String PARAMETER_FROM_DATE = "from_date";

	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;

	public final static String prmContractId = "cam_contract_id";

	private boolean isAdmin;
	private boolean isLoggedOn;
	private String login = null;
	private String passwd = null;
	private boolean print = false;

	private Group group = null;
	private User eUser = null;
	private Contract contract = null;
	private Applicant applicant = null;
	private User contractUser = null;
	private Collection newContracts = null;
	private IWTimestamp lastDate = null;
	private Integer contractId = new Integer(-1);

	private ContractService ContractBusiness;

	private String errMsg = "";

	public ContractTariffWindow() {
		setWidth(530);
		setHeight(370);
		setResizable(true);
	}

	protected void control(IWContext iwc) {
		init(iwc);
		if (iwc.isParameterSet(PARAMETER_SAVE)) {
			doSaveContractTariff(iwc);
				add(getHeader(localize("tariff_saved",
						"Tariff saved")));
		} 
		
		add(getEditTable(iwc));

	}

	private void init(IWContext iwc) {
		contractId = Integer.valueOf(iwc.getParameter(prmContractId));
		try {
			contract = ContractBusiness.getContractHome().findByPrimaryKey(contractId);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);

		return LinkTable;
	}

	private DropdownMenu getAccountKeyDropdown(Collection accoutKeys, String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(0, "--");
		if (accoutKeys != null) {
			for (Iterator iter = accoutKeys.iterator(); iter.hasNext();) {
				AccountKey key = (AccountKey) iter.next();
				drp.addMenuElement(key.getPrimaryKey().toString(), key.getName() + " (" + key.getInfo() + ")");
			}
			drp.setSelectedElement("-1");
		}
		return drp;
	}
	
	private DropdownMenu getIndexDropdown(Collection indeces, String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElementFirst("-1", "--"); // localize("index","Index"));
		if (indeces != null) {
			for (Iterator iter = indeces.iterator(); iter.hasNext();) {
				TariffIndex ti = (TariffIndex) iter.next();
				drp.addMenuElement(ti.getType(), ti.getName());
			}
			drp.setSelectedElement("-1");
		}

		return drp;
	}
	
	public FinanceService getFinanceService(IWApplicationContext iwac) throws RemoteException {
		return (FinanceService) IBOLookup.getServiceInstance(iwac, FinanceService.class);
	}
	
	private PresentationObject getEditTable(IWContext iwc) {
		DataTable T = new DataTable();
		T.setWidth("100%");
		T.setTitlesHorizontal(true);
		SubmitButton save = new SubmitButton("savetariffs", localize("save",
				"Save"));
		T.addButton(save);

		int row = 1;
		int col = 1;

		T.add(getHeader("Nr"), col++, row);
		T.add(getHeader(localize("name", "Name")), col++, row);
		T.add(getHeader(localize("amount", "Amount")), col++, row);
		T.add(getHeader(localize("account_key", "Account key")), col++, row);
		T.add(getHeader(localize("index", "Index")), col++, row);
		T.add(getHeader(localize("updated", "Updated")), col++, row);
		T.add(getHeader(localize("delete", "Delete")), col++, row);
		row++;

		Collection contractTariffEntries = getContractTariffEntries(iwc);
		if (contractTariffEntries == null || contractTariffEntries.isEmpty()) {
			contractTariffEntries = createContractTariffEntries(iwc);
		}
		
		if (contractTariffEntries != null && !contractTariffEntries.isEmpty()) {
			Map mapOfIndices = null;
			Collection accountKeys = null;
			try {
				accountKeys = getFinanceService(iwc).getAccountKeyHome().findByCategory(new Integer(36));
				mapOfIndices = getFinanceService(iwc).mapOfTariffIndicesByTypes();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Iterator it = contractTariffEntries.iterator();
			while (it.hasNext()) {
				ContractTariff t = (ContractTariff) it.next();
				T.add(getText(Integer.toString(row - 1)), 1, row);
				T.add(getText(t.getName()), 2, row);
				T.add(getText(Float.toString(t.getPrice())), 3, row);
				T.add(getText(t.getAccountKey().getName() + " (" + t.getAccountKey().getInfo() + ")"), 4, row);
				if (t.getIndexType() != null) {
					if (mapOfIndices != null && mapOfIndices.containsKey(t.getIndexType())) {
						TariffIndex ti = (TariffIndex) mapOfIndices.get(t.getIndexType());
						T.add(getText(ti.getName()), 5, row);
					} else {
						T.add(getText(t.getIndexType()), 5, row);	
					}						
				} else {
					T.add(getText(""), 5, row);					
				}
				if (t.getIndexUpdated() != null) {
					T.add(getText(t.getIndexUpdated().toString()), 6, row);
				} else {
					T.add(getText(""), 6, row);					
				}
				T.add(new CheckBox(), 7, row++);				
			}
			
			//add 5 empty lines
			for (int i = 1; i < 6; i++) {
				T.add(getText(Integer.toString(row - 1)), 1, row);
				T.add(new TextInput("test"), 2, row);
				T.add(new FloatInput("test"), 3, row);
				T.add(getAccountKeyDropdown(accountKeys, "test"), 4, row);
				T.add(getIndexDropdown(mapOfIndices.values(), "test"), 5, row);
				T.add(getText(""), 6, row);
				T.add(getText(""), 7, row++);				
			}
		} else {
			T.add(getText("Unable to create entries for contract"));
		}		

		Form F = new Form();
		F.add(new HiddenInput(prmContractId, contract
				.getPrimaryKey().toString()));
		F.add(T);
		return F;
	}

	private Collection getContractTariffEntries(IWContext iwc) {
		try {
			return getContractService(iwc).getContractTariffHome().findByContract(contract);
		} catch (Exception e) {
		} 
		
		return null;
	}
	
	private ContractTariff insertContractTariffEntry(IWContext iwc, Tariff tariff) {
		try {
			ContractTariff ct = getContractService(iwc).getContractTariffHome().create();
			ct.setAccountKey(tariff.getAccountKeyId());
			ct.setContract(contract);
			ct.setIndexType(tariff.getIndexType());
			ct.setIndexUpdated(tariff.getIndexUpdated());
			ct.setIsDeleted(false);
			ct.setName(tariff.getName());
			ct.setPrice(tariff.getPrice());
			ct.setUseIndex(tariff.getUseIndex());
			ct.store();
			
			return ct;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Collection createContractTariffEntries(IWContext iwc) {
		Collection ret = new ArrayList();
		try {
			Collection tariffs = ((TariffHome) IDOLookup.getHome(Tariff.class)).findByTariffGroup(new Integer(9));
			Tariff tariff;
			char cAttribute;
			int attributeId = -1;

			// For each tariff (Inner loop)
			for (Iterator iter2 = tariffs.iterator(); iter2
					.hasNext();) {
				tariff = (Tariff) iter2.next();
				String sAttribute = tariff
						.getTariffAttribute();
				// If we have an tariff attribute
				if (sAttribute != null) {
					attributeId = -1;
					cAttribute = sAttribute.charAt(0);
					// If All
					if (cAttribute == BuildingCacher.CHARALL) {
						insertContractTariffEntry(iwc, tariff);
					}
					// other than all
					else {
						// attribute check
						if (sAttribute.length() >= 3) {
							attributeId = Integer
									.parseInt(sAttribute
											.substring(2));
							switch (cAttribute) {
							case BuildingCacher.CHARTYPE:
								// Apartment type
								if (attributeId == contract.getApartment().getApartmentTypeId()) {
									ContractTariff t = insertContractTariffEntry(iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARCATEGORY:
								// Apartment category
								if (attributeId == ((Integer)contract.getApartment().getApartmentType().getApartmentSubcategory().getApartmentCategory().getPrimaryKey()).intValue()) {
									ContractTariff t = insertContractTariffEntry(iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARBUILDING:
								// Building
								if (attributeId == contract.getApartment().getFloor().getBuildingId()) {
									ContractTariff t = insertContractTariffEntry(iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARFLOOR:
								// Floor
								if (attributeId == contract.getApartment().getFloorId()) {
									ContractTariff t = insertContractTariffEntry(iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARCOMPLEX:
								// Complex
								if (attributeId == contract.getApartment().getFloor().getBuilding().getComplexId()) {
									ContractTariff t = insertContractTariffEntry(iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARAPARTMENT:
								// Apartment
								if (attributeId == contract.getApartmentId().intValue()) {
									ContractTariff t = insertContractTariffEntry(iwc, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							} //switch
						} // attribute check
					} 
				}
			}
		} catch(Exception e) {
		}
		
		return ret;
	}
	
	private boolean doSaveContractTariff(IWContext iwc) {
		try {
			Contract eContract = ContractBusiness.getContractHome()
					.findByPrimaryKey(contractId);

			IWTimestamp from = null;
			IWTimestamp to = null;
			IWTimestamp move = null;
			IWTimestamp changeKeyStatusAt = null;
			
			String sfrom = iwc.getParameter(PARAMETER_FROM_DATE);
			if (sfrom != null && sfrom.length() == 10) {
				from = new IWTimestamp(sfrom);
			}
			String to_date = iwc.getParameter(PARAMETER_TO_DATE);
			if (to_date != null && to_date.length() == 10) {
				to = new IWTimestamp(to_date);
			}
			boolean endOld = iwc.isParameterSet(PARAMETER_END_OLD_CONTRACT);
			boolean syncDates = iwc.isParameterSet(PARAMETER_SYNC_DATES);
			String move_date = iwc.getParameter(PARAMETER_MOVING_DATE);
			if (move_date != null && move_date.length() == 10) {
				move = new IWTimestamp(move_date);
			}
			
			boolean changeKeyStatus = iwc.isParameterSet(PARAMETER_CHANGE_KEY_STATUS);
			String changeKeyStatusDate = iwc.getParameter(PARAMETER_CHANGE_KEY_STATUS_DATE);
			if (changeKeyStatusDate != null && changeKeyStatusDate.length() == 10) {
				changeKeyStatusAt = new IWTimestamp(changeKeyStatusDate);
			}

			if (endOld) {
				ContractBusiness
						.endContract((Integer) contract.getPrimaryKey(), move,
								"", syncDates);
			}

			if (from != null && to != null)
				if (from.isLaterThan(new IWTimestamp(contract.getValidTo()))) {
					Contract c = ContractBusiness.createNewContract(
							(Integer) contractUser.getPrimaryKey(),
							(Integer) applicant.getPrimaryKey(), contract
									.getApartmentId(), from.getDate(), to
									.getDate(), new Integer(contract.getApplicationID()));
					
					if (c != null && changeKeyStatus) {
						c.setChangeKeyStatusAt(changeKeyStatusAt.getTimestamp());
						c.setChangeKeyStatusTo(true);
						c.store();
						
						contract.setChangeKeyStatusAt(changeKeyStatusAt.getTimestamp());
						contract.setChangeKeyStatusTo(false);
						contract.store();
					}
					
					if (contract.getApplication() != null) {
						c.setApplication(contract.getApplication());
					}
					
					return c != null;
				} else {
					this.errMsg = localize("contracts_overlap",
							"Contracts overlap");
					return false;
				}
			else
				System.err.println("no dates in renewal");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public void main(IWContext iwc) throws Exception {
		eUser = iwc.getCurrentUser();
		isLoggedOn = iwc.isLoggedOn();
		ContractBusiness = getContractService(iwc);
		control(iwc);
	}	
}