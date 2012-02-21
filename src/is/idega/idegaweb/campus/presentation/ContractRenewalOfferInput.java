package is.idega.idegaweb.campus.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HelpButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

public class ContractRenewalOfferInput extends Block{

	public static final String OFFER_UUID = "offer_uuid";

	public static final int LAYOUT_VERTICAL = 1;

	public static final int LAYOUT_HORIZONTAL = 2;

	public static final int LAYOUT_STACKED = 3;

	private int inputLength = 10;

	private int layout = -1;

	private int pageId;

	private int offerTextSize;

	private String backgroundImageUrl = null;

	private String offerWidth = "";

	private String offerHeight = "";

	private String offerText;

	private String colour = "";

	private String offerTextColour;

	private String styleAttribute = "font-size: 10pt";

	private String textStyles = "font-family: Arial,Helvetica,sans-serif; font-size: 8pt; font-weight: bold; color: #000000; text-decoration: none;";

	private String submitButtonAlignment;

	private boolean hasHelpButton = false;

	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";

	private Table outerTable;

	private Form myForm;

	private String submitButtonText = null;

	protected IWResourceBundle iwrb;

	protected IWBundle iwb;

	public ContractRenewalOfferInput() {
		super();
		setDefaultValues();
	}

	public void main(IWContext iwc) throws Exception {
		this.iwb = getBundle(iwc);
		this.iwrb = getResourceBundle(iwc);

		this.submitButtonText = this.iwrb.getLocalizedString("get", "Get");

		this.offerText = this.iwrb.getLocalizedString("renewal.uuid",
				"UUID");
		setup();

		this.outerTable.add(this.myForm);
		add(this.outerTable);
	}

	private void setup() {
		Table offerTable = new Table(1, 2);
		offerTable.setBorder(0);
		offerTable.setWidth(this.offerWidth);
		offerTable.setHeight(this.offerHeight);
		if (!this.colour.equals("")) {
			offerTable.setColor(this.colour);
		}
		offerTable.setCellpadding(0);
		offerTable.setCellspacing(0);
		if (!"".equals(this.backgroundImageUrl)) {
			offerTable
					.setBackgroundImage(new Image(this.backgroundImageUrl));
		}

		HelpButton helpButton = new HelpButton(this.iwrb.getLocalizedString(
				"renewal.help_headline", "Offer help"),
				this.iwrb.getLocalizedString("help", "Help"));

		Text offerText = new Text(this.offerText);
		if (this.offerTextSize != -1) {
			offerText.setFontSize(this.offerTextSize);
		}

		if (this.offerTextColour != null) {
			offerText.setFontColor(this.offerTextColour);
		}

		offerText.setFontStyle(this.textStyles);

		Table inputTable;

		TextInput offer = new TextInput(OFFER_UUID);
		offer.setMarkupAttribute("style", this.styleAttribute);
		offer.setSize(this.inputLength);

		switch (this.layout) {
		case LAYOUT_HORIZONTAL:
			inputTable = new Table(3, 2);
			inputTable.setBorder(0);
			if (!(this.colour.equals(""))) {
				inputTable.setColor(this.colour);
			}
			inputTable.setCellpadding(0);
			inputTable.setCellspacing(0);
			inputTable.setAlignment(2, 1, "right");
			inputTable.setAlignment(2, 2, "right");
			inputTable.setWidth("100%");

			inputTable.add(offerText, 2, 1);
			inputTable.add(offer, 2, 2);
			inputTable.setAlignment(2, 1, "right");
			inputTable.setAlignment(2, 2, "right");

			offerTable.add(inputTable, 1, 1);
			break;

		case LAYOUT_VERTICAL:
			inputTable = new Table(3, 3);
			inputTable.setBorder(0);
			if (!(this.colour.equals(""))) {
				inputTable.setColor(this.colour);
			}
			inputTable.setCellpadding(0);
			inputTable.setCellspacing(0);
			inputTable.mergeCells(1, 2, 3, 2);
			inputTable.addText("", 1, 2);
			inputTable.setHeight(2, "10");
			inputTable.setAlignment(1, 1, "right");
			inputTable.setAlignment(1, 3, "right");

			inputTable.add(offerText, 1, 1);
			inputTable.add(offer, 3, 1);

			offerTable.add(inputTable, 1, 1);
			break;

		case LAYOUT_STACKED:
			inputTable = new Table(1, 2);
			inputTable.setBorder(0);
			inputTable.setCellpadding(0);
			inputTable.setCellspacing(0);
			inputTable.addText("", 1, 2);
			inputTable.setHeight(1, "2");
			if (!(this.colour.equals(""))) {
				inputTable.setColor(this.colour);
			}
			inputTable.setAlignment(1, 1, "left");
			inputTable.setAlignment(1, 2, "left");

			inputTable.add(offerText, 1, 1);
			inputTable.add(offer, 1, 2);

			offerTable.setAlignment(1, 1, "center");
			offerTable.add(inputTable, 1, 1);
			break;
		}

		Table submitTable = new Table(1, 1);
		if (this.hasHelpButton) {
			submitTable = new Table(2, 1);
		}
		submitTable.setBorder(0);
		if (!this.colour.equals("")) {
			submitTable.setColor(this.colour);
		}
		submitTable.setRowVerticalAlignment(1, "middle");
		if (!this.hasHelpButton) {
			submitTable.setAlignment(1, 1, this.submitButtonAlignment);
		} else {
			submitTable.setAlignment(2, 1, "right");
		}
		submitTable.setWidth("100%");

		if (!this.hasHelpButton) {
			submitTable.add(new SubmitButton("renewal.submit", this.submitButtonText),
					1, 1);
		} else {
			submitTable.add(new SubmitButton("renewal.submit", this.submitButtonText),
					2, 1);
			submitTable.add(helpButton, 1, 1);
		}

		offerTable.add(submitTable, 1, 2);
		this.myForm.add(offerTable);
		if (this.pageId > 0) {
			this.myForm.setPageToSubmitTo(this.pageId);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void setHelpButton(boolean usehelp) {
		this.hasHelpButton = usehelp;
	}

	public void addHelpButton() {
		this.hasHelpButton = true;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	private void setDefaultValues() {
		this.offerWidth = "148";
		this.offerHeight = "89";
		this.submitButtonAlignment = "center";
		this.layout = LAYOUT_VERTICAL;

		this.outerTable = new Table();
		this.outerTable.setCellpadding(0);
		this.outerTable.setCellspacing(0);

		this.myForm = new Form();
		this.myForm.add(new HiddenInput("cam_fact_view", "50"));
		this.myForm.setMethod("post");
	}

	public void setVertical() {
		this.layout = LAYOUT_VERTICAL;
	}

	public void setHorizontal() {
		this.layout = LAYOUT_HORIZONTAL;
	}

	public void setStacked() {
		this.layout = LAYOUT_STACKED;
	}

	public void setStyle(String styleAttribute) {
		this.styleAttribute = styleAttribute;
	}

	public void setInputLength(int inputLength) {
		this.inputLength = inputLength;
	}

	public void setReferenceTextSize(int size) {
		this.offerTextSize = size;
	}

	public void setReferenceTextColor(String color) {
		this.offerTextColour = color;
	}

	public void setColor(String color) {
		this.colour = color;
	}

	public void setHeight(String height) {
		this.offerHeight = height;
	}

	public void setWidth(String width) {
		this.offerWidth = width;
	}

	public void setBackgroundImageUrl(String url) {
		this.backgroundImageUrl = url;
	}

	public void setSubmitButtonAlignment(String alignment) {
		this.submitButtonAlignment = alignment;
	}

	public void setTextStyle(String styleAttribute) {
		this.textStyles = styleAttribute;
	}

	public void setPage(com.idega.core.builder.data.ICPage page) {
		this.pageId = page.getID();
	}

	public synchronized Object clone() {
		ContractRenewalOfferInput obj = null;
		try {
			obj = (ContractRenewalOfferInput) super.clone();
			if (this.outerTable != null) {
				obj.outerTable = (Table) this.outerTable.clone();
			}
			if (this.myForm != null) {
				obj.myForm = (Form) this.myForm.clone();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}

}
