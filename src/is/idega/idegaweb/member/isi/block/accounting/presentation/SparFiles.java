package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;
import is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusiness;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;

public class SparFiles extends CashierSubWindowTemplate {

	protected static final String LABEL_DIVISION = "vf_division";

	protected static final String LABEL_GROUP = "vf_group";

	protected static final String LABEL_BATCH_NUMBER = "vf_batch_number";

	protected static final String LABEL_CREATED = "vf_created";

	protected static final String LABEL_CONTRACT_NUMBER = "vf_contract_number";

	protected static final String LABEL_MARKED = "vf_marked";

	protected static final String LABEL_FILE_NAME = "vf_file_name";
	
    protected static final String ELEMENT_ALL_DIVISIONS = "isi_acc_vf_all_divisions";

    protected static final String ELEMENT_ALL_GROUPS = "isi_acc_vf_all_groups";

	public SparFiles() {
		super();
	}

	public void main(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		Table t = new Table();
		t.setCellpadding(5);

		int row = 1;
		Text labelDivision = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
				"Division"));
		labelDivision.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb
				.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelBatchNr = new Text(iwrb.getLocalizedString(
				LABEL_BATCH_NUMBER, "Batch number"));
		labelBatchNr.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelCreated = new Text(iwrb.getLocalizedString(LABEL_CREATED,
				"Created"));
		labelCreated.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelContract = new Text(iwrb.getLocalizedString(
				LABEL_CONTRACT_NUMBER, "Contract number"));
		labelContract.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelFileName = new Text(iwrb.getLocalizedString(LABEL_FILE_NAME,
				"File name"));
		labelFileName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		Collection contracts = null;
		try {
			CreditCardType visa = this.getAccountingBusiness(iwc).getVisaCreditCardType();
			contracts = this.getAccountingBusiness(iwc).findAllCreditCardContractByClubAndDivisionAndType(getClub(), getDivision(), visa);
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
		
		Collection batches = null;
		try {
			batches = getExportBusiness(iwc).findAllBatchesByContract(contracts);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		t.add(labelDivision, 1, row);
		t.add(labelGroup, 2, row);
		t.add(labelBatchNr, 3, row);
		t.add(labelCreated, 4, row);
		t.add(labelContract, 5, row);
		t.add(labelFileName, 6, row++);

		if (batches != null) {
			Iterator it = batches.iterator();
			while (it.hasNext()) {
				Batch batch = (Batch) it.next();
				
				if (batch.getCreditCardContract() != null) {
					CreditCardContract contract = batch.getCreditCardContract();
					if (contract.getDivision() != null) {
						t.add(contract.getDivision().getName(), 1, row);
					}
					else {
						t.add(iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS, "All divisions"), 1, row);
					}
					
					if (contract.getGroup() != null) {
						t.add(contract.getGroup().getName(), 2, row);						
					}
					else {
						t.add(iwrb.getLocalizedString(ELEMENT_ALL_GROUPS, "All groups"), 2, row);											
					}
				}
				else {
					t.add(iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS, "All divisions"), 1, row);
					t.add(iwrb.getLocalizedString(ELEMENT_ALL_GROUPS, "All groups"), 2, row);					
				}

				String batchNumber = null;
				if (batch.getBatchNumber() != null
						&& !"".equals(batch.getBatchNumber())) {
					batchNumber = batch.getBatchNumber();
				} else {
					batchNumber = batch.getPrimaryKey().toString();
				}

				t.add(batchNumber, 3, row);
								
//				Link batchLink = new Link(batchNumber);
//				batchLink.setParameter(SendFiles.BATCH_ID, batchNumber);
//				batchLink.setWindowToOpen(EntriesInBatch.class);
//				t.add(batchLink, 1, row);

				if (batch.getCreated() != null) {
					IWTimestamp created = new IWTimestamp(batch.getCreated());
					t.add(created.getDateString("dd.MM.yyyy HH:mm:ss"), 4, row);
				}

				if (batch.getCreditCardContract() != null) {
					t.add(batch.getCreditCardContract().getContractNumber(), 5,
							row);
				}

				if (batch.getCreditCardFileId() > 0) {
					String displayString = batch.getCreditCardFileName();
					if (displayString == null) {
						displayString = "File";
					}
					/*GenericButton button = new GenericButton(displayString);
					button.setFileToOpen(batch.getCreditCardFileId());

					t.add(button, 6, row);*/
					
					DownloadLink link = new DownloadLink(batch.getCreditCardFileId());
					link.setAlternativeFileName(displayString);
					link.setText(displayString);
					
					t.add(link, 6, row);
				}

				row++;
			}
		}

		//f.maintainParameter(CashierWindow.ACTION);
		//f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		//f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		//f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);

//		f.add(inputTable);
//		f.add(t);
		add(t);
	}

	protected ExportBusiness getExportBusiness(IWApplicationContext iwc) {
		try {
			return (ExportBusiness) IBOLookup.getServiceInstance(iwc,
					ExportBusiness.class);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;
	}
}
