package se.cubecon.bun24.viewpoint.presentation;

import com.idega.data.IDOLookup;
import com.idega.block.process.data.CaseCode;
import com.idega.business.IBOLookup;
import com.idega.presentation.*;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.Text;
import com.idega.user.Converter;
import com.idega.user.data.*;
import com.idega.user.business.UserBusiness;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import se.idega.idegaweb.commune.business.CommuneCaseBusiness;
import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 */
public class ViewpointForm extends CommuneBlock {
    public final static String PARAM_ACTION = "vp_action";
	public final static String PARAM_CATEGORY = "vp_category";
	public final static String PARAM_SUBJECT = "vp_subject";
	public final static String PARAM_MESSAGE = "vp_message";
    public final static String PARAM_ANSWER = "vp_answer";
    public final static String PARAM_VIEWPOINT_ID = "vp_viewpoint_id";

    public final static int SHOWTOPCATEGORIESFORM_ACTION = 1;
    public final static int SHOWSUBCATEGORIESFORM_ACTION = 2;
    public final static int REGISTERVIEWPOINT_ACTION = 3;
    public final static int SHOWACCEPTFORM_ACTION = 4;
    public final static int SHOWANSWERFORM_ACTION = 5;
    public final static int NOTLOGGEDON_ACTION = 6;
    public final static int ACCEPTTOHANDLEVIEWPOINT_ACTION = 7;
    public final static int ANSWERVIEWPOINT_ACTION = 8;

	public void main (final IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));

        try {
            switch (parseInput (iwc)) {
                case SHOWTOPCATEGORIESFORM_ACTION:
                    showTopCategoriesForm (iwc);
                    break;
                    
                case SHOWSUBCATEGORIESFORM_ACTION:
                    showSubCategoriesForm (iwc);
                    break;
                    
                case REGISTERVIEWPOINT_ACTION:
                    registerViewPoint (iwc);
                    break;

                case SHOWACCEPTFORM_ACTION:
                    showAcceptForm (iwc);
                    break;

                case SHOWANSWERFORM_ACTION:
                    showAnswerForm (iwc);
                    break;

                case NOTLOGGEDON_ACTION:
                    add (getLocalizedHeader ("viewpoint.notLoggedOn",
                 "Du måste vara inloggad för att använda den här funktionen."));
                    break;

                case ACCEPTTOHANDLEVIEWPOINT_ACTION:
                    acceptToHandleViewpoint (iwc);
                    break;

                case ANSWERVIEWPOINT_ACTION:
                    answerViewpoint (iwc);
                    break;

                default:
                    add ("Unknown Page");
                    break;
            }
        } catch (final Exception exception) {
            add (new ExceptionWrapper(exception, this));
        }
	}

    private int parseInput (final IWContext iwc) {
        int action = NOTLOGGEDON_ACTION;

        if (iwc.isLoggedOn()) {
            if (iwc.isParameterSet (PARAM_ACTION)) {
                action = Integer.parseInt (iwc.getParameter (PARAM_ACTION));
            } else {
                action = SHOWTOPCATEGORIESFORM_ACTION;
            }                
        }
        
        return action;
    }
    
    private void showAcceptForm (final IWContext iwc)
        throws RemoteException, FinderException {
        final ViewpointBusiness viewpointBusiness = getViewpointBusiness (iwc);
        final int viewpointId
                = Integer.parseInt (iwc.getParameter (PARAM_VIEWPOINT_ID));
        final Viewpoint viewpoint
                = viewpointBusiness.findViewpoint (viewpointId);
		final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (iwc, UserBusiness.class);
        final User user = userBusiness.getUser (viewpoint.getUserId ());
		final Form form = new Form();
		form.add (new HiddenInput (PARAM_ACTION,
                                   ACCEPTTOHANDLEVIEWPOINT_ACTION + ""));
		form.add (new HiddenInput (PARAM_VIEWPOINT_ID, viewpointId + ""));
		SubmitButton submit
                = new SubmitButton (getResourceBundle().getLocalizedString
                                    ("viewpoint.iAcceptToHandleThisViewpoint",
                                     "I accept to handle this viewpoint"));
		submit.setAsImageButton(true);
		final Table table = new Table (1, 9);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (getLocalizedHeader ("viewpoint.category", "Category"), 1,
                   row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getCategory ()), 1, row++);
        table.add (getLocalizedHeader ("viewpoint.fromCitizen", "From Citizen"),
                   1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (user.getName ()), 1, row++);
        table.add (getLocalizedHeader ("viewpoint.subject", "Subject"), 1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getSubject ()), 1, row++);
        table.add (getLocalizedHeader ("viewpoint.message", "Message"), 1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getMessage ()), 1, row++);
		table.add (submit, 1, row++);
		form.add (table);
		add (form);
    }

    private void acceptToHandleViewpoint (final IWContext iwc)
        throws RemoteException, FinderException {
        // 1. parse input
        final int viewpointId
                = Integer.parseInt (iwc.getParameter (PARAM_VIEWPOINT_ID));

        // 2. registerhandler
        final ViewpointBusiness viewpointBusiness = getViewpointBusiness (iwc);
        viewpointBusiness.registerHandler (viewpointId, iwc.getCurrentUser ());

        // 3. print feedback
 		final Text text = new Text
                ("You are now registered as the handler of this case.");
		text.setWidth (Table.HUNDRED_PERCENT);
		final Table table = new Table (1, 1);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (text, 1, 1);
		add (table);
    }

    private void answerViewpoint (final IWContext iwc)
        throws RemoteException, CreateException, FinderException,
               RemoveException {
        // 1. parse input 
        final int viewpointId
                = Integer.parseInt (iwc.getParameter (PARAM_VIEWPOINT_ID));
        final String answer = iwc.getParameter (PARAM_ANSWER);

        // 2. register answer
        final ViewpointBusiness viewpointBusiness = getViewpointBusiness (iwc);
        viewpointBusiness.answerAndDeregisterViewpoint (viewpointId, answer);

        // 3. print feedback
 		final Text text = new Text
                ("Your answered has now been sent to the Citizen.");
		text.setWidth (Table.HUNDRED_PERCENT);
		final Table table = new Table (1, 1);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (text, 1, 1);
		add (table);
    }

    private void showAnswerForm (final IWContext iwc)
        throws RemoteException, FinderException {
        final ViewpointBusiness viewpointBusiness = getViewpointBusiness (iwc);
        final int viewpointId
                = Integer.parseInt (iwc.getParameter (PARAM_VIEWPOINT_ID));
        final Viewpoint viewpoint
                = viewpointBusiness.findViewpoint (viewpointId);
		final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (iwc, UserBusiness.class);
        final User user = userBusiness.getUser (viewpoint.getUserId ());
		final Form form = new Form();
		form.add (new HiddenInput (PARAM_ACTION,
                                   ANSWERVIEWPOINT_ACTION + ""));
		form.add (new HiddenInput (PARAM_VIEWPOINT_ID, viewpointId + ""));
		final TextArea textArea = new TextArea (PARAM_ANSWER);
        textArea.setColumns (40);
        textArea.setRows (10);
		SubmitButton submit
                = new SubmitButton (getResourceBundle().getLocalizedString
                                    ("viewpoint.sendAnswerToCitizen",
                                     "Send answer to citizen"));
		submit.setAsImageButton(true);
		final Table table = new Table (1, 10);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (getLocalizedHeader ("viewpoint.category", "Category"), 1,
                   row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getCategory ()), 1, row++);
        table.add (getLocalizedHeader ("viewpoint.fromCitizen", "From Citizen"),
                   1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (user.getName ()), 1, row++);
        table.add (getLocalizedHeader ("viewpoint.subject", "Subject"), 1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getSubject ()), 1, row++);
        table.add (getLocalizedHeader ("viewpoint.message", "Message"), 1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getMessage ()), 1, row++);
        table.add (getLocalizedHeader ("viewpoint.answer", "Answer to Citizen"),
                   1, row);
		table.add(new Break(), 1, row);
        table.add (textArea, 1, row++);
		table.add (submit, 1, row++);
		form.add (table);
		add (form);
    }

    private void registerViewPoint (final IWContext iwc)
        throws RemoteException, CreateException, FinderException {
        //final Category.SubCategory category
        //        = (Category.SubCategory) getCategory (iwc);
        final ViewpointBusiness viewpointBusiness = getViewpointBusiness (iwc);
        final int categoryId
                = new Integer (iwc.getParameter (PARAM_CATEGORY)).intValue ();
        final SubCategory category
                = viewpointBusiness.findSubCategory (categoryId);
        final Group handlerGroup = category.getHandlerGroup ();
        final int handlerGroupId
                = ((Integer) handlerGroup.getPrimaryKey ()).intValue ();
        viewpointBusiness.createViewpoint
                (iwc.getCurrentUser (), iwc.getParameter (PARAM_SUBJECT),
                 iwc.getParameter (PARAM_MESSAGE), category.getName (),
                 handlerGroupId);
 		final Text text1 = new Text ("Tack för dina synpunkter. De är nu registrerade som ett ärende på BUN24. En handläggare kommer att hantera och besvara ärendet.");
		text1.setWidth (Table.HUNDRED_PERCENT);
		final Table table = new Table (1, 1);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (text1, 1, 1);
		add (table);
    }

	private void showSubCategoriesForm (final IWContext iwc)
        throws RemoteException, FinderException {
		final Form form = new Form();
		form.add (new HiddenInput (PARAM_ACTION,
                                   REGISTERVIEWPOINT_ACTION + ""));
		final DropdownMenu dropdown = new DropdownMenu (PARAM_CATEGORY);
        //final Category.TopCategory topCategory
        //        = (Category.TopCategory) getCategory (iwc);
        //final Category [] categories = topCategory.getSubCategories ();
        final int topCategoryId
                = new Integer (iwc.getParameter (PARAM_CATEGORY)).intValue ();
        final SubCategory [] categories
                = getViewpointBusiness (iwc).findSubCategories (topCategoryId);
        for (int i = 0; i < categories.length; i++) {
            final String id = categories [i].getPrimaryKey ().toString ();
            dropdown.addMenuElementFirst (id, categories [i].getName ());
        }
		final TextInput textInput = new TextInput(PARAM_SUBJECT);
		textInput.setLength (40);
		final TextArea textArea = new TextArea (PARAM_MESSAGE);
        textArea.setColumns (40);
        textArea.setRows (10);
		SubmitButton submit = new SubmitButton
                (getResourceBundle().getLocalizedString
                 ("viewpoint.submitViewpoint", "Skicka synpunkt"));
		submit.setAsImageButton(true);

		final Table table = new Table (1, 5);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (getLocalizedHeader
                   ("viewpoint.enterSubCategory",
                    "Vilket underområde vill du ge synpunkter om?"), 1, row++);
        table.add (dropdown, 1, row++);
        table.add (getLocalizedHeader
                   ("viewpoint.subject", "Rubrik"), 1, row);
		table.add(new Break(), 1, row);
        table.add (textInput, 1, row++);
        table.add (getLocalizedHeader
                   ("viewpoint.details", "Synpunkter"), 1, row);
		table.add(new Break(), 1, row);
        table.add (textArea, 1, row++);
		table.add (submit, 1, row++);

		form.add (table);
		add (form);
	}

	private void showTopCategoriesForm (final IWContext iwc)
        throws RemoteException, FinderException {
		final Form form = new Form();
		form.add (new HiddenInput (PARAM_ACTION, SHOWSUBCATEGORIESFORM_ACTION
                                   + ""));

 		final Text text1 = new Text ("Problem ska i första hand lösas där de uppstår. Om du har synpunkter på hur en enskild skola eller förskola fungerar så ska du därför i första hand vända dig till personal, rektor eller förskolechef.");
		text1.setWidth (Table.HUNDRED_PERCENT);

        final Text text2 = new Text ("När du använder \"Synpunkter\" i BUN24 så ska du ange vilken typ av synpunkter du vill lämna. Det möjliggör för oss att länka dem till rätt tjänsteman för snabb handläggning och respons. De olika kategorierna ger också möjlighet till en systematisk uppföljning av hur medborgarna uppfattar verksamhet och service i kommunen.");
		text2.setWidth (Table.HUNDRED_PERCENT);
        final RadioGroup radioGroup = new RadioGroup (PARAM_CATEGORY);
        final TopCategory [] categories
                = getViewpointBusiness (iwc).findAllTopCategories ();
        //final Category [] categories = Category.getTopCategories ();
        for (int i = 0; i < categories.length; i++) {
            final String id = categories [i].getPrimaryKey ().toString ();
            radioGroup.addRadioButton (id,
                                       new Text (categories [i].getName ()));
            radioGroup.setSelected (id);
        }
		SubmitButton submit
                = new SubmitButton (getResourceBundle().getLocalizedString
                                    ("viewpoint.continue", "Continue"));
		submit.setAsImageButton(true);

		final Table table = new Table (1,5);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (text1, 1, row++);
        table.add (text2, 1, row++);
        table.add (getLocalizedHeader
                   ("viewpoint.enterTopCategory",
                    "Vilket område vill du ge synpunkter om?"), 1, row++);
        table.add (radioGroup, 1, row++);
		table.add (submit, 1, row++);

		form.add (table);
		add (form);
	}

    private Category getCategory (final IWContext iwc) {
        Category result = null;
        final String categoryIdAsString = iwc.getParameter (PARAM_CATEGORY);
        if (categoryIdAsString != null) {
            final int categoryId = Integer.parseInt (categoryIdAsString);
            result = Category.getCategory (categoryId);
        }
        return result;
    }

	private ViewpointBusiness getViewpointBusiness (IWContext iwc)
        throws RemoteException {
		return (ViewpointBusiness)
                IBOLookup.getServiceInstance(iwc, ViewpointBusiness.class);
	}
}
