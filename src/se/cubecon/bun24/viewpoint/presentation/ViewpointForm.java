package se.cubecon.bun24.viewpoint.presentation;

import com.idega.business.IBOLookup;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.*;
import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * ViewpointForm is an IdegaWeb block that inputs and handles viewpoints from
 * all kind of system users. It is based on session ejb classes in
 * {@link se.cubecon.bun24.viewpoint.business} and entity ejb classes in
 * {@link se.cubecon.bun24.viewpoint.data}.
 * <p>
 * The user enters viewpoints by first selecting in a category tree and then
 * writes the message. This makes it possible for the system to act as a
 * broker when deciding who should be able to manage the viewpoint and send an
 * answer.
 * <p>
 * Last modified: $Date: 2002/11/29 12:17:48 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.20 $
 * @see com.idega.business
 * @see com.idega.presentation
 * @see com.idega.presentation.text
 * @see com.idega.presentation.ui
 * @see com.idega.user.business
 * @see com.idega.user.data
 * @see javax.ejb
 * @see se.cubecon.bun24.viewpoint.business
 * @see se.cubecon.bun24.viewpoint.data
 */
public class ViewpointForm extends CommuneBlock {
	public final static String PARAM_ACTION = "vp_action";
	public final static String PARAM_CATEGORY = "vp_category";
	public final static String PARAM_SUBJECT = "vp_subject";
	public final static String PARAM_MESSAGE = "vp_message";
	public final static String PARAM_ANSWER = "vp_answer";
	public final static String PARAM_VIEWPOINT_ID = "vp_viewpoint_id";

    private final static int UNKNOWN_ACTION = -1;
	public final static int SHOWTOPCATEGORIESFORM_ACTION = 0;
	public final static int SHOWSUBCATEGORIESFORM_ACTION = 1;
	public final static int REGISTERVIEWPOINT_ACTION = 2;
	public final static int SHOWVIEWPOINT_ACTION = 3;
	public final static int ACCEPTTOHANDLEVIEWPOINT_ACTION = 4;
	public final static int ANSWERVIEWPOINT_ACTION = 5;

	private final static String ANSWER_KEY = "viewpoint.answer";
	private final static String ANSWER_DEFAULT = "Svar till medborgare";
	private final static String APPLIES_KEY = "viewpoint.applies";
	private final static String APPLIES_DEFAULT = "Avser";
	private final static String CONFIRMANSWERSENT_KEY
        = "viewpoint.confirmAnswerSent";
	private final static String CONFIRMANSWERSENT_DEFAULT
        = "Ditt svar har nu skickats till medborgaren.";
	private final static String CONFIRMENTERVIEWPOINT_KEY
        = ViewpointBusiness.CONFIRMENTERVIEWPOINT_KEY;
	private final static String CONFIRMENTERVIEWPOINT_DEFAULT
        = ViewpointBusiness.CONFIRMENTERVIEWPOINT_DEFAULT;
	private final static String CONFIRMSETHANDLER_KEY
        = "viewpoint.confirmSetHandler";
	private final static String CONFIRMSETHANDLER_DEFAULT
        = "Du är nu registrerad som handläggare för det här ärendet.";
	private final static String CONTINUE_KEY = "viewpoint.continue";
	private final static String CONTINUE_DEFAULT = "Fortsätt";
	private final static String DESCRIPTION1_KEY = "viewpoint.description1";
	private final static String DESCRIPTION1_DEFAULT
        = "När du använder 'Synpunkter' i Nacka24 så ska du ange vilken typ av "
        + "synpunkter du vill lämna. Det möjliggör för oss att länka dem till "
        + "rätt tjänsteman för snabb handläggning och respons. De olika "
        + "kategorierna ger också möjlighet till en systematisk uppföljning av "
        + "hur medborgarna uppfattar verksamhet och service i kommunen.";
	private final static String DESCRIPTION2_KEY = "viewpoint.description2";
	private final static String DESCRIPTION2_DEFAULT
        = "Problem ska i första hand lösas där de uppstår. Om du har "
        + "synpunkter på hur en enskild skola eller förskola fungerar så ska "
        + "du därför i första hand vända dig till personal, rektor eller "
        + "förskolechef.";
	private final static String ENTERSUBCATEGORY_KEY
        = "viewpoint.enterSubCategory";
	private final static String ENTERSUBCATEGORY_DEFAULT
        = "Vilket underområde vill du ge synpunkter om?";
	private final static String ENTERTOPCATEGORY_KEY
        = "viewpoint.enterTopCategory";
	private final static String ENTERTOPCATEGORY_DEFAULT
        = "Vilket område vill du ge synpunkter om?";
	private final static String FROMCITIZEN_KEY = "viewpoint.fromCitizen";
	private final static String FROMCITIZEN_DEFAULT = "Från medborgare";
	private final static String IACCEPTTOHANDLETHISVIEWPOINT_KEY
        = "viewpoint.iAcceptToHandleThisViewpoint";
	private final static String IACCEPTTOHANDLETHISVIEWPOINT_DEFAULT
        = "Jag accepterar att handlägga den här synpunkten";
	private final static String MESSAGE_KEY = "viewpoint.message";
	private final static String MESSAGE_DEFAULT = "Meddelande";
    private final static String NOTAUTHORIZEDTOSHOWVIEWPOINT_KEY
        = "viewpoint.notAuthorizedToShowViewpoint";
    private final static String NOTAUTHORIZEDTOSHOWVIEWPOINT_DEFAULT
        = "Du har inte rättigheter att se den här synpunkten. För att kunna se"
        + " en synpunkt måste du antingen ha matat in den själv, vara"
        + " synpunktens handläggare eller ha rättigheter att bli synpunktens"
        + " handläggare.";
	private final static String NOTLOGGEDON_KEY = "viewpoint.notLoggedOn";
	private final static String NOTLOGGEDON_DEFAULT
        = "Du måste vara inloggad för att använda den här funktionen.";
	private final static String SENDANSWERTOCITIZEN_KEY
        = "viewpoint.sendAnswerToCitizen";
	private final static String SENDANSWERTOCITIZEN_DEFAULT
        = "Skicka svar till medborgare";
	private final static String SUBJECT_KEY = "viewpoint.subject";
	private final static String SUBJECT_DEFAULT = "Rubrik";
	private final static String SUBMITVIEWPOINT_KEY
        = "viewpoint.submitViewpoint";
	private final static String SUBMITVIEWPOINT_DEFAULT = "Skicka synpunkt";
	private final static String VIEWPOINTS_KEY = "viewpoint.viewpoints";
	private final static String VIEWPOINTS_DEFAULT = "Synpunkter";
	private final static String GOBACKTOMYPAGE_KEY = "viewpoint.goBackToMyPage";
	private final static String GOBACKTOMYPAGE_DEFAULT
        = "Tillbaka till Min sida";

	private final static String UNKNOWN_PAGE = "Unknown Page";

	private int userHomePageId = -1;

	/**
	 * main is the event handler of ViewpointForm. It can handle the following
	 * events set in {@link #PARAM_ACTION}:<br>
	 * - {@link #SHOWTOPCATEGORIESFORM_ACTION}<br>
	 * - {@link #SHOWSUBCATEGORIESFORM_ACTION}<br>
	 * - {@link #REGISTERVIEWPOINT_ACTION}<br>
	 * - {@link #SHOWVIEWPOINT_ACTION}<br>
	 * - {@link #ACCEPTTOHANDLEVIEWPOINT_ACTION}<br>
	 * - {@link #ANSWERVIEWPOINT_ACTION}<br>
	 *
	 * @param iwc session data like user info etc.
	 */
	public void main(final IWContext iwc) {
		setResourceBundle (getResourceBundle(iwc));

        if (!iwc.isLoggedOn()) {
            add (getLocalizedHeader (NOTLOGGEDON_KEY, NOTLOGGEDON_DEFAULT));
            return;
        }

		try {
			switch (getActionId (iwc)) {
				case SHOWTOPCATEGORIESFORM_ACTION :
					showTopCategoriesForm (iwc);
					break;

				case SHOWSUBCATEGORIESFORM_ACTION :
					showSubCategoriesForm (iwc);
					break;

				case REGISTERVIEWPOINT_ACTION :
					registerViewPoint (iwc);
					break;

				case SHOWVIEWPOINT_ACTION :
                    showViewpoint (iwc);
					break;

				case ACCEPTTOHANDLEVIEWPOINT_ACTION :
					acceptToHandleViewpoint(iwc);
					break;

				case ANSWERVIEWPOINT_ACTION :
					answerViewpoint(iwc);
					break;

				default :
                    add (UNKNOWN_PAGE);
                    throw new IllegalArgumentException
                            ("Tried to apply unknown action "
                             + getActionId (iwc) + " to class "
                             + getClass ().getName ());
			}
		}
		catch (final Exception exception) {
			add(new ExceptionWrapper(exception, this));
		}
	}

	private int getActionId (final IWContext iwc) {
		int result = UNKNOWN_ACTION;

		if (iwc.isParameterSet(PARAM_ACTION)) {
            try {
                result = Integer.parseInt (iwc.getParameter (PARAM_ACTION));
            } catch (final NumberFormatException e) {
                e.printStackTrace ();
                result = UNKNOWN_ACTION;
            }
        }

        if (result == UNKNOWN_ACTION) {
            if (iwc.isParameterSet (PARAM_VIEWPOINT_ID)) {
                result = SHOWVIEWPOINT_ACTION;
            } else {
                result = SHOWTOPCATEGORIESFORM_ACTION;
            }
        }
        
		return result;
	}

	private void showTopCategoriesForm (final IWContext iwc)
        throws RemoteException, FinderException {
		final Form form = new Form();
        addHiddenActionParameterToForm (form, SHOWSUBCATEGORIESFORM_ACTION);
		final Text text1 = new Text (getLocalizedString (DESCRIPTION1_KEY,
                                                         DESCRIPTION1_DEFAULT));
		final Text text2 = new Text (getLocalizedString (DESCRIPTION2_KEY,
                                                         DESCRIPTION2_DEFAULT));
		final RadioGroup radioGroup = new RadioGroup (PARAM_CATEGORY);
		final TopCategory [] categories
                = getViewpointBusiness (iwc).findAllTopCategories ();
		for (int i = 0; i < categories.length; i++) {
			final String id = categories[i].getPrimaryKey().toString();
			radioGroup.addRadioButton (id, getSmallText
                                       (categories[i].getName ()));
			radioGroup.setSelected (id);
		}
		final SubmitButton submit = getSubmitButton (CONTINUE_KEY,
                                                     CONTINUE_DEFAULT);
		final Table table = new Table ();
		table.setWidth (getWidth ());
		table.setCellpadding (0);
		table.setCellspacing (0);
		int row = 1;

		table.add (text1, 1, row++);
		table.setHeight (row++, 12);
		table.add (text2, 1, row++);
		table.setHeight (row++, 12);
		table.add (getLocalizedHeader (ENTERTOPCATEGORY_KEY,
                                       ENTERTOPCATEGORY_DEFAULT), 1, row++);
		table.add (radioGroup, 1, row++);
		table.setHeight (row++, 12);
		table.add (submit, 1, row++);
		form.add (table);
		add (form);
	}

	private void showSubCategoriesForm (final IWContext iwc)
        throws RemoteException, FinderException {
		if (!iwc.isParameterSet (PARAM_CATEGORY)) {
			showTopCategoriesForm (iwc);
			return;
		}
		final Form form = new Form();
        addHiddenActionParameterToForm (form, REGISTERVIEWPOINT_ACTION);
		final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (PARAM_CATEGORY));
		final int topCategoryId
                = Integer.parseInt (iwc.getParameter (PARAM_CATEGORY));
		final SubCategory [] categories
                = getViewpointBusiness (iwc).findSubCategories (topCategoryId);
		for (int i = 0; i < categories.length; i++) {
			final String id = categories [i].getPrimaryKey ().toString();
			dropdown.addMenuElement (id, categories [i].getName ());
		}
		final TextInput textInput = (TextInput) getStyledInterface
                (new TextInput (PARAM_SUBJECT));
		textInput.setLength (40);
		final TextArea textArea
                = (TextArea) getStyledInterface (new TextArea (PARAM_MESSAGE));
		textArea.setColumns (40);
		textArea.setRows (10);
		final SubmitButton submit = getSubmitButton (SUBMITVIEWPOINT_KEY,
                                                     SUBMITVIEWPOINT_DEFAULT);
		final Table table = new Table ();
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (0);
		int row = 1;
		table.add (getLocalizedSmallHeader
                   (ENTERSUBCATEGORY_KEY, ENTERSUBCATEGORY_DEFAULT), 1, row++);
		table.add (dropdown, 1, row++);
		table.setHeight (row++, 12);
		table.add (getLocalizedSmallHeader (SUBJECT_KEY, SUBJECT_DEFAULT), 1,
                   row++);
		table.add (textInput, 1, row++);
		table.setHeight (row++, 12);
		table.add (getLocalizedSmallHeader (VIEWPOINTS_KEY, VIEWPOINTS_DEFAULT),
                   1, row++);
		table.add (textArea, 1, row++);
		table.setHeight (row++, 12);
		table.add (submit, 1, row++);
		form.add (table);
		add (form);
	}

    private void showViewpoint (final IWContext iwc) throws RemoteException,
                                                            FinderException {
        if (!iwc.isParameterSet (PARAM_VIEWPOINT_ID)) {
            add (UNKNOWN_PAGE);
            return;
        }

        final User currentUser = iwc.getCurrentUser();
        final int currentUserId
                = ((Integer) currentUser.getPrimaryKey ()).intValue ();
		final ViewpointBusiness viewpointBusiness = getViewpointBusiness(iwc);
		final int viewpointId
                = Integer.parseInt (iwc.getParameter (PARAM_VIEWPOINT_ID));
		final Viewpoint viewpoint
                = viewpointBusiness.findViewpoint (viewpointId);
        final User handler = viewpoint.getOwner ();
        final UserBusiness userBusiness = (UserBusiness) IBOLookup
                .getServiceInstance (iwc, UserBusiness.class);
        final Collection currentUsersGroups
                = userBusiness.getUserGroups (currentUserId);
        final Group handlerGroup = viewpoint.getHandlerGroup ();
        final boolean isCurrentUserHandler = handler != null && currentUserId
                == ((Integer) handler.getPrimaryKey ()).intValue ();
        final boolean isCurrentUserOriginator
                = currentUserId == viewpoint.getUserId ();
        final boolean isCurrentUserPotentialHandler
                = handler == null && currentUsersGroups.contains (handlerGroup);
        if (isCurrentUserPotentialHandler) {
            showAcceptForm (iwc);
        } else if (isCurrentUserHandler && !viewpoint.isAnswered ()) {
            showAnswerForm (iwc);
        } else if (isCurrentUserOriginator || isCurrentUserHandler) {
            add (createViewpointTable (viewpoint, iwc));
        } else {
            add (getLocalizedHeader (NOTAUTHORIZEDTOSHOWVIEWPOINT_KEY,
                                     NOTAUTHORIZEDTOSHOWVIEWPOINT_DEFAULT));
        }
    }

	private void showAcceptForm(final IWContext iwc) throws RemoteException, FinderException {
		final ViewpointBusiness viewpointBusiness = getViewpointBusiness(iwc);
		final int viewpointId = Integer.parseInt(iwc.getParameter(PARAM_VIEWPOINT_ID));
		final Viewpoint viewpoint = viewpointBusiness.findViewpoint(viewpointId);
        /*
		final UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		final User user = userBusiness.getUser(viewpoint.getUserId());
        */
		final Form form = new Form();
		form.add(new HiddenInput(PARAM_ACTION, ACCEPTTOHANDLEVIEWPOINT_ACTION + ""));
		form.add(new HiddenInput(PARAM_VIEWPOINT_ID, viewpointId + ""));

        /*
		final Table table = new Table();
		table.setWidth(getWidth());
		table.setCellspacing(0);
		table.setCellpadding(getCellpadding());
		int row = 1;

		table.add(getLocalizedSmallHeader(APPLIES_KEY, APPLIES_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getCategory()), 1, row++);
		table.add(getLocalizedSmallHeader(FROMCITIZEN_KEY, FROMCITIZEN_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(user.getName()), 1, row++);
		table.add(getLocalizedSmallHeader(SUBJECT_KEY, SUBJECT_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getSubject()), 1, row++);
		table.add(getLocalizedSmallHeader(MESSAGE_KEY, MESSAGE_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getMessage()), 1, row++);
        */
        final Table table = createViewpointTable (viewpoint, iwc);
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(getLocalizedString(IACCEPTTOHANDLETHISVIEWPOINT_KEY, IACCEPTTOHANDLETHISVIEWPOINT_DEFAULT)));
		table.add(submit, 1, 5);
		form.add(table);
		add(form);
	}

    private Table createViewpointTable
        (final Viewpoint viewpoint, final IWContext iwc) throws RemoteException{
		final Table table = new Table();
		table.setWidth(getWidth());
		table.setCellspacing(0);
		table.setCellpadding(getCellpadding());
		int row = 1;

		table.add(getLocalizedSmallHeader(APPLIES_KEY, APPLIES_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getCategory()), 1, row++);
		table.add(getLocalizedSmallHeader(FROMCITIZEN_KEY, FROMCITIZEN_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		final UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		final User user = userBusiness.getUser(viewpoint.getUserId());
		table.add(new Text(user.getName ()), 1, row++);
		table.add(getLocalizedSmallHeader(SUBJECT_KEY, SUBJECT_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getSubject()), 1, row++);
		table.add(getLocalizedSmallHeader(MESSAGE_KEY, MESSAGE_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getMessage()), 1, row++);
        return table;
    }

	private void acceptToHandleViewpoint(final IWContext iwc) throws RemoteException, FinderException {
		// 1. parse input
		final int viewpointId = Integer.parseInt(iwc.getParameter(PARAM_VIEWPOINT_ID));

		// 2. registerhandler
		final ViewpointBusiness viewpointBusiness = getViewpointBusiness(iwc);
		viewpointBusiness.registerHandler(viewpointId, iwc.getCurrentUser());

		// 3. print feedback
		final Text text = new Text(getLocalizedString(CONFIRMSETHANDLER_KEY, CONFIRMSETHANDLER_DEFAULT));
		text.setWidth(Table.HUNDRED_PERCENT);
		final Table table = new Table(1, 2);
		int row = 1;
		table.setWidth(600);
		table.setCellspacing(0);
		table.setCellpadding(14);
		table.setColor(getBackgroundColor());
		table.add(text, 1, row++);
		table.add(getUserHomePageLink(iwc), 1, row++);
		add(table);
	}

	private void showAnswerForm(final IWContext iwc) throws RemoteException, FinderException {
		final ViewpointBusiness viewpointBusiness = getViewpointBusiness(iwc);
		final int viewpointId = Integer.parseInt(iwc.getParameter(PARAM_VIEWPOINT_ID));
		final Viewpoint viewpoint = viewpointBusiness.findViewpoint(viewpointId);
        /*
		final UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		final User user = userBusiness.getUser(viewpoint.getUserId());
*/
		final Form form = new Form();
		form.add(new HiddenInput(PARAM_ACTION, ANSWERVIEWPOINT_ACTION + ""));
		form.add(new HiddenInput(PARAM_VIEWPOINT_ID, viewpointId + ""));
		final TextArea textArea = new TextArea(PARAM_ANSWER);
		textArea.setColumns(40);
		textArea.setRows(10);
		SubmitButton submit = new SubmitButton(getLocalizedString(SENDANSWERTOCITIZEN_KEY, SENDANSWERTOCITIZEN_DEFAULT));
		submit.setAsImageButton(true);

        /*
		final Table table = new Table(1, 10);
		int row = 1;
		table.setWidth(600);
		table.setCellspacing(0);
		table.setCellpadding(14);
		table.setColor(getBackgroundColor());
		table.add(getLocalizedHeader(APPLIES_KEY, APPLIES_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getCategory()), 1, row++);
		table.add(getLocalizedHeader(FROMCITIZEN_KEY, FROMCITIZEN_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(user.getName()), 1, row++);
		table.add(getLocalizedHeader(SUBJECT_KEY, SUBJECT_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getSubject()), 1, row++);
		table.add(getLocalizedHeader(MESSAGE_KEY, MESSAGE_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(new Text(viewpoint.getMessage()), 1, row++);
        */
        final Table table = createViewpointTable (viewpoint, iwc);
        int row = 5;
		table.add(getLocalizedHeader(ANSWER_KEY, ANSWER_DEFAULT), 1, row);
		table.add(new Break(), 1, row);
		table.add(textArea, 1, row++);
		table.add(submit, 1, row++);
		form.add(table);
		add(form);
	}

	private void answerViewpoint(final IWContext iwc) throws RemoteException, CreateException, FinderException, RemoveException {
		// 1. parse input 
		final int viewpointId = Integer.parseInt(iwc.getParameter(PARAM_VIEWPOINT_ID));
		final String answer = iwc.getParameter(PARAM_ANSWER);

		// 2. register answer
		final ViewpointBusiness viewpointBusiness = getViewpointBusiness(iwc);
		viewpointBusiness.answerAndDeregisterViewpoint(viewpointId, answer);

		// 3. print feedback
		final Text text = new Text(getLocalizedString(CONFIRMANSWERSENT_KEY, CONFIRMANSWERSENT_DEFAULT));
		text.setWidth(Table.HUNDRED_PERCENT);
		final Table table = new Table(1, 2);
		int row = 1;
		table.setWidth(600);
		table.setCellspacing(0);
		table.setCellpadding(14);
		table.setColor(getBackgroundColor());
		table.add(text, 1, row++);
		table.add(getUserHomePageLink(iwc), 1, row++);
		add(table);
	}

	private void registerViewPoint(final IWContext iwc)
        throws RemoteException, CreateException, FinderException {
		final ViewpointBusiness viewpointBusiness = getViewpointBusiness (iwc);
		final int subCategoryId
                = new Integer (iwc.getParameter (PARAM_CATEGORY)).intValue();
		final SubCategory subCategory
                = viewpointBusiness.findSubCategory (subCategoryId);
        final TopCategory topCategory = subCategory.getTopCategory ();
		final Group handlerGroup = subCategory.getHandlerGroup ();
		final int handlerGroupId
                = ((Integer) handlerGroup.getPrimaryKey()).intValue();
		viewpointBusiness.createViewpoint
                (iwc.getCurrentUser (), iwc.getParameter (PARAM_SUBJECT),
                 iwc.getParameter (PARAM_MESSAGE), topCategory.getName ()
                 + "/" + subCategory.getName (), handlerGroupId);
		final Text text1
                = new Text (getLocalizedString (CONFIRMENTERVIEWPOINT_KEY,
                                                CONFIRMENTERVIEWPOINT_DEFAULT));
		text1.setWidth (Table.HUNDRED_PERCENT);
		final Table table = new Table ();
		int row = 1;
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (0);
		table.add (text1, 1, row++);
		table.setHeight (row++, 12);
		table.add (getUserHomePageLink (iwc), 1, row++);
		add (table);
	}

    private static void addHiddenActionParameterToForm (final Form form,
                                                        final int action) {
        final HiddenInput hidden = new HiddenInput (PARAM_ACTION, action + "");
		form.add (hidden);
    }

    private SubmitButton getSubmitButton (final String key,
                                          final String defaultName) {
        final String name
                = getResourceBundle().getLocalizedString (key, defaultName);
		return (SubmitButton) getButton (new SubmitButton (name));
    }

	private Link getUserHomePageLink (final IWContext iwc)
        throws RemoteException {
		final Text userHomePageText
                = new Text (getLocalizedString (GOBACKTOMYPAGE_KEY,
                                                GOBACKTOMYPAGE_DEFAULT));
 		final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (iwc, UserBusiness.class);
        final User user = iwc.getCurrentUser ();
		final Link link = new Link (userHomePageText);
        link.setPage (userBusiness.getHomePageIDForUser (user));
		return (link);
	}

	private String getLocalizedString(final String key, final String value) {
		return getResourceBundle().getLocalizedString(key, value);
	}

	private ViewpointBusiness getViewpointBusiness (final IWContext iwc)
        throws RemoteException {
		return (ViewpointBusiness) IBOLookup.getServiceInstance
                (iwc, ViewpointBusiness.class);
	}
}
