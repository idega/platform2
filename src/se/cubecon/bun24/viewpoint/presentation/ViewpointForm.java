package se.cubecon.bun24.viewpoint.presentation;

import com.idega.block.process.data.CaseCode;
import com.idega.builder.data.IBPage;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.*;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.*;
import com.idega.user.Converter;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.*;
import se.idega.idegaweb.commune.business.CommuneCaseBusiness;
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

    public final static String ANSWER_KEY = "viewpoint.answer";
    public final static String ANSWER_DEFAULT = "Svar till medborgare";
    public final static String APPLIES_KEY = "viewpoint.applies";
    public final static String APPLIES_DEFAULT = "Avser";
    public final static String CONFIRMANSWERSENT_KEY
        = "viewpoint.confirmAnswerSent";
    public final static String CONFIRMANSWERSENT_DEFAULT
        = "Ditt svar har nu skickats till medborgaren.";
    public final static String CONFIRMENTERVIEWPOINT_KEY
        = "viewpoint.confirmEnterViewpoint";
    public final static String CONFIRMENTERVIEWPOINT_DEFAULT
        = "Tack för din synpunkt. Den är nu registrerad som ett ärende på "
        + "BUN24. En handläggare kommer att hantera och besvara ärendet.";
    public final static String CONFIRMSETHANDLER_KEY
        = "viewpoint.confirmSetHandler";
    public final static String CONFIRMSETHANDLER_DEFAULT
        = "Du är nu registrerad som handläggare för det här ärendet.";
    public final static String CONTINUE_KEY = "viewpoint.continue";
    public final static String CONTINUE_DEFAULT = "Fortsätt";
    public final static String DESCRIPTION1_KEY = "viewpoint.description1";
    public final static String DESCRIPTION1_DEFAULT
        = "När du använder 'Synpunkter' i BUN24 så ska du ange vilken typ av "
        + "synpunkter du vill lämna. Det möjliggör för oss att länka dem till "
        + "rätt tjänsteman för snabb handläggning och respons. De olika "
        + "kategorierna ger också möjlighet till en systematisk uppföljning av "
        + "hur medborgarna uppfattar verksamhet och service i kommunen.";
    public final static String DESCRIPTION2_KEY = "viewpoint.description2";
    public final static String DESCRIPTION2_DEFAULT
        = "Problem ska i första hand lösas där de uppstår. Om du har "
        + "synpunkter på hur en enskild skola eller förskola fungerar så ska "
        + "du därför i första hand vända dig till personal, rektor eller "
        + "förskolechef.";
    public final static String ENTERSUBCATEGORY_KEY
        = "viewpoint.enterSubCategory";
    public final static String ENTERSUBCATEGORY_DEFAULT
        = "Vilket underområde vill du ge synpunkter om?";
    public final static String ENTERTOPCATEGORY_KEY
        = "viewpoint.enterTopCategory";
    public final static String ENTERTOPCATEGORY_DEFAULT
        = "Vilket område vill du ge synpunkter om?";
    public final static String FROMCITIZEN_KEY = "viewpoint.fromCitizen";
    public final static String FROMCITIZEN_DEFAULT = "Från medborgare";
    public final static String IACCEPTTOHANDLETHISVIEWPOINT_KEY
        = "viewpoint.iAcceptToHandleThisViewpoint";
    public final static String IACCEPTTOHANDLETHISVIEWPOINT_DEFAULT
        = "Jag accepterar att handlägga den här synpunkten";
    public final static String MESSAGE_KEY = "viewpoint.message";
    public final static String MESSAGE_DEFAULT = "Meddelande";
    public final static String NOTLOGGEDON_KEY = "viewpoint.notLoggedOn";
    public final static String NOTLOGGEDON_DEFAULT
        = "Du måste vara inloggad för att använda den här funktionen.";
    public final static String SENDANSWERTOCITIZEN_KEY
        = "viewpoint.sendAnswerToCitizen";
    public final static String SENDANSWERTOCITIZEN_DEFAULT
        = "Skicka svar till medborgare";
    public final static String SUBJECT_KEY = "viewpoint.subject";
    public final static String SUBJECT_DEFAULT = "Rubrik";
    public final static String SUBMITVIEWPOINT_KEY
        = "viewpoint.submitViewpoint";
    public final static String SUBMITVIEWPOINT_DEFAULT = "Skicka synpunkt";
    public final static String VIEWPOINTS_KEY = "viewpoint.viewpoints";
    public final static String VIEWPOINTS_DEFAULT = "Synpunkter";
    public final static String GOBACKTOMYPAGE_KEY = "viewpoint.goBackToMyPage";
    public final static String GOBACKTOMYPAGE_DEFAULT
        = "Gå tillbaka till min sida";

    public final static String UNKNOWN_PAGE = "Unknown Page";

    private int userHomePageId = -1;    

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
                    add (getLocalizedHeader (NOTLOGGEDON_KEY,
                                             NOTLOGGEDON_DEFAULT));
                    break;

                case ACCEPTTOHANDLEVIEWPOINT_ACTION:
                    acceptToHandleViewpoint (iwc);
                    break;

                case ANSWERVIEWPOINT_ACTION:
                    answerViewpoint (iwc);
                    break;

                default:
                    add (UNKNOWN_PAGE);
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
		SubmitButton submit = new SubmitButton
                (getLocalizedString (IACCEPTTOHANDLETHISVIEWPOINT_KEY,
                                     IACCEPTTOHANDLETHISVIEWPOINT_DEFAULT));
		submit.setAsImageButton(true);
		final Table table = new Table (1, 9);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (getLocalizedHeader (APPLIES_KEY, APPLIES_DEFAULT), 1,
                   row);
		table.add (new Break(), 1, row);
        table.add (new Text (viewpoint.getCategory ()), 1, row++);
        table.add (getLocalizedHeader (FROMCITIZEN_KEY, FROMCITIZEN_DEFAULT),
                   1, row);
		table.add (new Break(), 1, row);
        table.add (new Text (user.getName ()), 1, row++);
        table.add (getLocalizedHeader (SUBJECT_KEY, SUBJECT_DEFAULT), 1, row);
		table.add (new Break(), 1, row);
        table.add (new Text (viewpoint.getSubject ()), 1, row++);
        table.add (getLocalizedHeader (MESSAGE_KEY, MESSAGE_DEFAULT), 1, row);
		table.add (new Break(), 1, row);
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
                (getLocalizedString (CONFIRMSETHANDLER_KEY,
                                     CONFIRMSETHANDLER_DEFAULT));
		text.setWidth (Table.HUNDRED_PERCENT);
		final Table table = new Table (1, 2);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (text, 1, row++);
        table.add (getUserHomePageLink (iwc), 1, row++);
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
 		final Text text
                = new Text (getLocalizedString (CONFIRMANSWERSENT_KEY,
                                                CONFIRMANSWERSENT_DEFAULT));
		text.setWidth (Table.HUNDRED_PERCENT);
		final Table table = new Table (1, 2);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (text, 1, row++);
        table.add (getUserHomePageLink (iwc), 1, row++);
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
		SubmitButton submit = new SubmitButton (getLocalizedString
                                                (SENDANSWERTOCITIZEN_KEY,
                                                 SENDANSWERTOCITIZEN_DEFAULT));
		submit.setAsImageButton(true);
		final Table table = new Table (1, 10);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (getLocalizedHeader (APPLIES_KEY, APPLIES_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getCategory ()), 1, row++);
        table.add (getLocalizedHeader (FROMCITIZEN_KEY, FROMCITIZEN_DEFAULT), 1,
                   row);
		table.add(new Break(), 1, row);
        table.add (new Text (user.getName ()), 1, row++);
        table.add (getLocalizedHeader (SUBJECT_KEY, SUBJECT_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getSubject ()), 1, row++);
        table.add (getLocalizedHeader (MESSAGE_KEY, MESSAGE_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
        table.add (new Text (viewpoint.getMessage ()), 1, row++);
        table.add (getLocalizedHeader (ANSWER_KEY, ANSWER_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
        table.add (textArea, 1, row++);
		table.add (submit, 1, row++);
		form.add (table);
		add (form);
    }

    private void registerViewPoint (final IWContext iwc)
        throws RemoteException, CreateException, FinderException {
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
 		final Text text1
                = new Text (getLocalizedString (CONFIRMENTERVIEWPOINT_KEY,
                                                CONFIRMENTERVIEWPOINT_DEFAULT));
		text1.setWidth (Table.HUNDRED_PERCENT);
		final Table table = new Table (1, 2);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (text1, 1, row++);
        table.add (getUserHomePageLink (iwc), 1, row++);
		add (table);
    }

	private void showSubCategoriesForm (final IWContext iwc)
        throws RemoteException, FinderException {
        if (!iwc.isParameterSet (PARAM_CATEGORY)) {
            showTopCategoriesForm (iwc);
            return;
        }
        
		final Form form = new Form();
		form.add (new HiddenInput (PARAM_ACTION,
                                   REGISTERVIEWPOINT_ACTION + ""));
		final DropdownMenu dropdown = new DropdownMenu (PARAM_CATEGORY);
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
                 (SUBMITVIEWPOINT_KEY, SUBMITVIEWPOINT_DEFAULT));
		submit.setAsImageButton(true);

		final Table table = new Table (1, 5);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (getLocalizedHeader (ENTERSUBCATEGORY_KEY,
                                       ENTERSUBCATEGORY_DEFAULT), 1, row++);
        table.add (dropdown, 1, row++);
        table.add (getLocalizedHeader (SUBJECT_KEY, SUBJECT_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
        table.add (textInput, 1, row++);
        table.add (getLocalizedHeader (VIEWPOINTS_KEY, VIEWPOINTS_DEFAULT), 1,
                   row);
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
        final String description1 = getLocalizedString (DESCRIPTION1_KEY,
                                                        DESCRIPTION1_DEFAULT);
 		final Text text1 = new Text (description1);
		text1.setWidth (Table.HUNDRED_PERCENT);
        final String description2 = getLocalizedString (DESCRIPTION2_KEY,
                                                        DESCRIPTION2_DEFAULT);
        final Text text2 = new Text (description2);
		text2.setWidth (Table.HUNDRED_PERCENT);
        final RadioGroup radioGroup = new RadioGroup (PARAM_CATEGORY);
        final TopCategory [] categories
                = getViewpointBusiness (iwc).findAllTopCategories ();
        for (int i = 0; i < categories.length; i++) {
            final String id = categories [i].getPrimaryKey ().toString ();
            radioGroup.addRadioButton (id,
                                       new Text (categories [i].getName ()));
            radioGroup.setSelected (id);
        }
		SubmitButton submit = new SubmitButton
                (getLocalizedString (CONTINUE_KEY, CONTINUE_DEFAULT));
		submit.setAsImageButton(true);

		final Table table = new Table (1,5);
        int row = 1;
		table.setWidth (600);
		table.setCellspacing (0);
		table.setCellpadding (14);
		table.setColor (getBackgroundColor());
        table.add (text1, 1, row++);
        table.add (text2, 1, row++);
        table.add (getLocalizedHeader (ENTERTOPCATEGORY_KEY,
                                       ENTERTOPCATEGORY_DEFAULT), 1, row++);
        table.add (radioGroup, 1, row++);
		table.add (submit, 1, row++);

		form.add (table);
		add (form);
	}

    private Link getUserHomePageLink (final IWContext iwc)
        throws RemoteException {
        final Text userHomePageText
                = new Text (getLocalizedString (GOBACKTOMYPAGE_KEY,
                                                GOBACKTOMYPAGE_DEFAULT));
        final Link link = new Link (userHomePageText);
        link.setPage(iwc.getCurrentUser ().getHomePageID ());
        return (link);
    }

    private String getLocalizedString (final String key, final String value) {
        return getResourceBundle ().getLocalizedString (key, value);
    }

	private ViewpointBusiness getViewpointBusiness (final IWContext iwc)
        throws RemoteException {
		return (ViewpointBusiness)
                IBOLookup.getServiceInstance(iwc, ViewpointBusiness.class);
	}
}
