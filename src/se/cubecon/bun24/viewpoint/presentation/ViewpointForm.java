package se.cubecon.bun24.viewpoint.presentation;

import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.business.*;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import javax.servlet.http.HttpSession;
import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.util.PIDChecker;

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
 * Last modified: $Date: 2004/11/03 10:07:17 $ by $Author: gimmi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.51 $
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
    public final static String PARAM_SSN = "vp_ssn";
	public final static String PARAM_VIEWPOINT_ID = "vp_viewpoint_id";
	public final static String PARAM_ROAD_ID = "vp_road_id";
	public final static String PARAM_GROUP_ID = "vp_group_id";
	public final static String PARAM_NAME = "vp_name";
	public final static String PARAM_EMAIL = "vp_email";

	private final static String SHOWTOPCATEGORIESFORM_ACTION
        = "vp_showtopcategoriesform_action";
	private final static String SHOWSUBCATEGORIESFORM_ACTION
        = "vp_showsubcategoriesform_action";
	private final static String REGISTERVIEWPOINT_ACTION
        = "vp_registerviewpoint_action";
	public final static String SHOWVIEWPOINT_ACTION
        = "vp_showviewpoint_action";
	private final static String ACCEPTTOHANDLEVIEWPOINT_ACTION
        = "vp_accepttohandleviewpoint_action";
	private final static String ANSWERVIEWPOINT_ACTION
        = "vp_answerviewpoint_action";
	private final static String FORWARDVIEWPOINT_ACTION
        = "vp_forwardviewpoint_action";
	private final static String SHOWFORWARDFORM_ACTION
        = "vp_showforwardform_action";
    private final static String CANCEL_ACTION = "vp_cancel_action";

	private final static String ANSWER_KEY = "viewpoint.answer";
	private final static String ANSWER_DEFAULT = "Svar till medborgare";
	private final static String APPLIES_KEY = "viewpoint.applies";
	private final static String APPLIES_DEFAULT = "Avser";
	private final static String CANCEL_KEY = "viewpoint.cancel";
	private final static String CANCEL_DEFAULT = "Avbryt";
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
        = "När du använder 'Synpunkter' så ska du ange vilken typ av "
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
	private final static String EMAIL_KEY = "viewpoint.email";
	private final static String EMAIL_DEFAULT = "E-post";
	private final static String ENTERSUBCATEGORY_KEY
        = "viewpoint.enterSubCategory";
	private final static String ENTERSUBCATEGORY_DEFAULT
        = "Vilket underområde vill du ge synpunkter om?";
	private final static String ENTERROAD_KEY
        = "viewpoint.enterRoad";
	private final static String ENTERROAD_DEFAULT
        = "Gata - om relevant";
	private final static String ENTERTOPCATEGORY_KEY
        = "viewpoint.enterTopCategory";
	private final static String ENTERTOPCATEGORY_DEFAULT
        = "Vilket område vill du ge synpunkter om?";
	private final static String FORWARDTO_KEY = "viewpoint.forwardTo";
	private final static String FORWARDTO_DEFAULT = "Vidarebefordra till:";
	private final static String FORWARDEDTO_KEY = "viewpoint.forwardedTo";
	private final static String FORWARDEDTO_DEFAULT = "vidarebefordrad till";
	private final static String FORWARD_KEY = "viewpoint.forward";
	private final static String FORWARD_DEFAULT = "Vidarebefordra";
	private final static String FROMCITIZEN_KEY = "viewpoint.fromCitizen";
	private final static String FROMCITIZEN_DEFAULT = "Från medborgare";
	private final static String GROUPNAME_KEY = "viewpoint.groupName";
	private final static String GROUPNAME_DEFAULT = "Gruppnamn";
	private final static String GROUPID_KEY = "viewpoint.groupId";
	private final static String IACCEPTTOHANDLETHISVIEWPOINT_KEY
        = "viewpoint.iAcceptToHandleThisViewpoint";
	private final static String IACCEPTTOHANDLETHISVIEWPOINT_DEFAULT
        = "Jag accepterar att handlägga den här synpunkten";
	private final static String LOGINNAME_KEY = "viewpoint.loginName";
	//private final static String LOGINNAME_DEFAULT = "Användarnamn";
	private final static String MESSAGE_KEY = "viewpoint.message";
	private final static String MESSAGE_DEFAULT = "Meddelande";
	private final static String NAME_KEY = "viewpoint.name";
	private final static String NAME_DEFAULT = "Namn";
    private final static String NOTAUTHORIZEDTOSHOWVIEWPOINT_KEY
        = "viewpoint.notAuthorizedToShowViewpoint";
    private final static String NOTAUTHORIZEDTOSHOWVIEWPOINT_DEFAULT
        = "Du har inte rättigheter att se den här synpunkten. För att kunna se"
        + " en synpunkt måste du antingen ha matat in den själv, vara"
        + " synpunktens handläggare eller ha rättigheter att bli synpunktens"
        + " handläggare.";
	private final static String ROADINFO_KEY = "viewpoint.roadInfo";
	private final static String ROADINFO_DEFAULT = "Gata";
	private final static String SSN_KEY = "viewpoint.ssn";
	private final static String SSN_DEFAULT = "Personnummer";
	private final static String SENDANSWERTOCITIZEN_KEY
        = "viewpoint.sendAnswerToCitizen";
	private final static String SENDANSWERTOCITIZEN_DEFAULT
        = "Skicka svar till medborgare";
	private final static String SUBJECT_KEY = "viewpoint.subject";
	private final static String SUBJECT_DEFAULT = "Rubrik";
	private final static String SUBMITVIEWPOINT_KEY
        = "viewpoint.submitViewpoint";
	private final static String SUBMITVIEWPOINT_DEFAULT = "Skicka synpunkt";
	private final static String USERID_KEY = "viewpoint.userId";
	private final static String VIEWPOINTS_KEY = "viewpoint.viewpoints";
	private final static String VIEWPOINTS_DEFAULT = "Synpunkter";
	private final static String VIEWPOINT_KEY = "viewpoint.viewpoint";
	private final static String VIEWPOINT_DEFAULT = "Synpunkt";
	private final static String GOBACKTOMYPAGE_KEY = "viewpoint.goBackToMyPage";
	private final static String GOBACKTOMYPAGE_DEFAULT
        = "Tillbaka till Min sida";

	private final static String UNKNOWN_PAGE = "Unknown Page";

	/**
	 * main is the event handler of ViewpointForm.
	 *
	 * @param context session data like user info etc.
	 */
	public void main(final IWContext context) {
		setResourceBundle (getResourceBundle(context));
        final boolean isLoggedOn = context.isLoggedOn ();

		try {
            if (!isUserIdentified (context)) {
                showIdentificationForm ();
            } else if (context.isParameterSet (SHOWSUBCATEGORIESFORM_ACTION)) {
                showSubCategoriesForm (context);
            } else if (context.isParameterSet (REGISTERVIEWPOINT_ACTION)) {
                registerViewpoint (context);
            } else if (context.isParameterSet (CANCEL_ACTION)) {
                add (getStartPageLink (context));
            } else if (isLoggedOn && context.isParameterSet
                       (ACCEPTTOHANDLEVIEWPOINT_ACTION)) {
                acceptToHandleViewpoint (context);
            } else if (isLoggedOn
                       && context.isParameterSet (ANSWERVIEWPOINT_ACTION)) {
                answerViewpoint (context);
            } else if (isLoggedOn
                       && context.isParameterSet (SHOWFORWARDFORM_ACTION)) {
                showForwardForm (context);
            } else if (isLoggedOn
                       && context.isParameterSet (FORWARDVIEWPOINT_ACTION)) {
                forwardViewpoint (context);
            } else if (isLoggedOn
                       && context.isParameterSet (PARAM_VIEWPOINT_ID)) {
                showViewpoint (context);
            } else {
                showTopCategoriesForm (context);
            }
        } catch (final Exception exception) {
            logWarning ("Exception caught in " + getClass ().getName ()
                                + " " + (new Date ()).toString ());
            logWarning ("Parameters:");
            final Enumeration enumer = context.getParameterNames ();
            while (enumer.hasMoreElements ()) {
                final String key = (String) enumer.nextElement ();
                logWarning ('\t' + key + "='"
                                    + context.getParameter (key) + "'");
            }
            exception.printStackTrace ();
            add ("Det inträffade ett fel. Försök igen senare.");
		}
	}

	private void showIdentificationForm () {
		final Form form = new Form();
        final Text useSsnHeader = getSmallHeader ("Du som redan har medborgakonto");
        final Text useSsnText = getSmallText ("Om du redan har ett medborgarkonto så kan du lämna en synpunkt utan att logga in genom att ange ditt perssonnummer nedan:");
		final TextInput ssnInput = (TextInput) getStyledInterface
                (new TextInput (PARAM_SSN));
		ssnInput.setLength (10);
		final SubmitButton submit = getSubmitButton
                (SHOWTOPCATEGORIESFORM_ACTION, CONTINUE_KEY, CONTINUE_DEFAULT);
        final Text applyHeader = getSmallHeader ("Om du vill ansöka om medborgarkonto");
        final Text applyText = getSmallText ("Om du bor i kommunen, men ännu inte har skaffat medborgarkonto så kan du göra en ansökan nu genom att klicka på ansökningslänken i övre marginalen.");
        final Text submitNameAndEmailHeader = getSmallHeader ("Du som bara vill lämna en synpunkt");
        final Text submitNameAndEmailText = getSmallText ("Du kan också lämna en synpunkt genom att ange ditt namn och din e-post nedan:");
		final TextInput nameInput = (TextInput) getStyledInterface
                (new TextInput (PARAM_NAME));
		nameInput.setLength (40);
		final TextInput emailInput = (TextInput) getStyledInterface
                (new TextInput (PARAM_EMAIL));
		nameInput.setLength (50);


		final Table table = new Table ();
		table.setWidth (getWidth ());
		table.setCellpadding (0);
		table.setCellspacing (0);
		int row = 1;
		table.add (useSsnHeader, 1, row++);
		table.setHeight (row++, 12);
		table.add (useSsnText, 1, row++);
		table.setHeight (row++, 12);
        final Table ssnTable = new Table (2, 1);
        ssnTable.setWidth(1, 100);
		ssnTable.add (getLocalizedHeader (SSN_KEY, SSN_DEFAULT), 1, 1);
        ssnTable.add (ssnInput, 2, 1);
        table.add (ssnTable, 1, row++);
		table.setHeight (row++, 12);
		table.add (submit, 1, row++);
		table.setHeight (row++, 24);
		table.add (applyHeader, 1, row++);
		table.setHeight (row++, 12);
		table.add (applyText, 1, row++);
		table.setHeight (row++, 24);
		table.add (submitNameAndEmailHeader, 1, row++);
		table.setHeight (row++, 12);
		table.add (submitNameAndEmailText, 1, row++);
		table.setHeight (row++, 12);
        final Table nameAndEmailTable = new Table (2, 2);
        nameAndEmailTable.setWidth(1, 100);
		nameAndEmailTable.add (getLocalizedHeader
                               (NAME_KEY, NAME_DEFAULT), 1, 1);
        nameAndEmailTable.add (nameInput, 2, 1);
		nameAndEmailTable.add (getLocalizedHeader
                               (EMAIL_KEY, EMAIL_DEFAULT), 1, 2);
        nameAndEmailTable.add (emailInput, 2, 2);
        table.add (nameAndEmailTable, 1, row++);
		table.setHeight (row++, 12);
		table.add (submit, 1, row++);
		form.add (table);
		add (form);
	}

	private void showTopCategoriesForm (final IWContext context)
        throws RemoteException, FinderException {
		final Form form = new Form();
		final Text text1 = new Text (getLocalizedString (DESCRIPTION1_KEY,
                                                         DESCRIPTION1_DEFAULT));
		final Text text2 = new Text (getLocalizedString (DESCRIPTION2_KEY,
                                                         DESCRIPTION2_DEFAULT));
		final DropdownMenu categoryDropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (PARAM_CATEGORY));
		final TopCategory [] categories
                = getViewpointBusiness (context).findAllTopCategories ();
		for (int i = 0; i < categories.length; i++) {
			final String id = categories[i].getPrimaryKey().toString();
			categoryDropdown.addMenuElement (id, categories [i].getName ());
		}
		final SubmitButton submit = getSubmitButton
                (SHOWSUBCATEGORIESFORM_ACTION, CONTINUE_KEY, CONTINUE_DEFAULT);
		final Table table = new Table ();
		table.setWidth (getWidth ());
		table.setCellpadding (0);
		table.setCellspacing (0);
		int row = 1;

		table.add (text1, 1, row++);
		table.setHeight (row++, 12);
		table.add (text2, 1, row++);
		table.setHeight (row++, 12);
		table.add (getLocalizedSmallHeader (ENTERTOPCATEGORY_KEY,
                                            ENTERTOPCATEGORY_DEFAULT), 1, row++);
		table.setHeight (row++, 12);
		table.add (categoryDropdown, 1, row++);
		table.setHeight (row++, 12);
		table.add (submit, 1, row++);
		form.add (table);
		add (form);
	}

	private void showSubCategoriesForm (final IWContext context)
        throws RemoteException, FinderException {
		if (!context.isParameterSet (PARAM_CATEGORY)) {
			showTopCategoriesForm (context);
			return;
		}
		final Form form = new Form();
		final DropdownMenu categoryDropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (PARAM_CATEGORY));
		final DropdownMenu roadDropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (PARAM_ROAD_ID));
		final int topCategoryId
                = Integer.parseInt (context.getParameter (PARAM_CATEGORY));
        final ViewpointBusiness viewpointBusiness
                = getViewpointBusiness (context);
		final SubCategory [] categories
                = viewpointBusiness.findSubCategories (topCategoryId);
		for (int i = 0; i < categories.length; i++) {
			final String id = categories [i].getPrimaryKey ().toString();
			categoryDropdown.addMenuElement (id, categories [i].getName ());
		}
		final RoadResponsible [] roads
                = viewpointBusiness.findAllRoadResponsible ();
        roadDropdown.addMenuElement ("-1", "Ej relevant");
		for (int i = 0; i < roads.length; i++) {
            final RoadResponsible road = roads [i];
			final String id = road.getPrimaryKey ().toString();
			roadDropdown.addMenuElement (id, road.getRoad () + " ("
                                     + road.getArea () + ")");
		}
		final TextInput textInput = (TextInput) getStyledInterface
                (new TextInput (PARAM_SUBJECT));
		textInput.setLength (40);
		final TextArea textArea
                = (TextArea) getStyledInterface (new TextArea (PARAM_MESSAGE));
		textArea.setColumns (40);
		textArea.setRows (10);
        textArea.setMaximumCharacters (400);
		final SubmitButton submit = getSubmitButton
                (REGISTERVIEWPOINT_ACTION, SUBMITVIEWPOINT_KEY,
                 SUBMITVIEWPOINT_DEFAULT);
		final Table table = new Table ();
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (0);
		int row = 1;
		table.add (getLocalizedSmallHeader
                   (ENTERSUBCATEGORY_KEY, ENTERSUBCATEGORY_DEFAULT), 1, row++);
		table.setHeight (row++, 6);
		table.add (categoryDropdown, 1, row++);
		table.setHeight (row++, 12);
		table.add (getLocalizedSmallHeader (ENTERROAD_KEY, ENTERROAD_DEFAULT),
                   1, row++);
		table.setHeight (row++, 6);
		table.add (roadDropdown, 1, row++);
		table.setHeight (row++, 12);
		table.add (getLocalizedSmallHeader (SUBJECT_KEY, SUBJECT_DEFAULT), 1,
                   row++);
		table.setHeight (row++, 6);
		table.add (textInput, 1, row++);
		table.setHeight (row++, 12);
		table.add (getLocalizedSmallHeader (VIEWPOINTS_KEY, VIEWPOINTS_DEFAULT),
                   1, row++);
		table.setHeight (row++, 6);
		table.add (textArea, 1, row++);
		table.setHeight (row++, 12);
		table.add (submit, 1, row++);
		form.add (table);
		add (form);
	}

    private void showViewpoint (final IWContext context) throws RemoteException,
                                                            FinderException {
        if (!context.isParameterSet (PARAM_VIEWPOINT_ID)) {
            add (UNKNOWN_PAGE);
            return;
        }

        final User currentUser = context.getCurrentUser ();
        final int currentUserId
                = ((Integer) currentUser.getPrimaryKey ()).intValue ();
		final ViewpointBusiness viewpointBusiness
                = getViewpointBusiness(context);
		final int viewpointId
                = Integer.parseInt (context.getParameter (PARAM_VIEWPOINT_ID));
		final Viewpoint viewpoint
                = viewpointBusiness.findViewpoint (viewpointId);
        final User handler = viewpoint.getOwner ();
        final UserBusiness userBusiness = (UserBusiness) IBOLookup
                .getServiceInstance (context, UserBusiness.class);
        final Collection currentUsersGroups
                = userBusiness.getUserGroups (currentUserId);
        final Group handlerGroup = viewpoint.getHandlerGroup ();
        final boolean isCurrentUserHandler = handler != null && currentUserId
                == ((Integer) handler.getPrimaryKey ()).intValue ();
        final Integer userId = viewpoint.getUserId ();
        final boolean isCurrentUserOriginator
                = null != userId && userId.intValue () == currentUserId;
        final boolean isCurrentUserPotentialHandler
                = handler == null && currentUsersGroups.contains (handlerGroup);
        if (isCurrentUserPotentialHandler) {
            // ask user if he wants to handle this viewpoint
            showAcceptForm (context);
        } else if (isCurrentUserHandler && !viewpoint.isAnswered ()) {
            // user handles this viewpoint, let him send answer
            showAnswerForm (context);
        } else if (isCurrentUserOriginator || isCurrentUserHandler) {
            // user just wants to see the viewpoint
            final Table table = createViewpointTable (viewpoint, context);
            int row = 6;
            if (viewpoint.isAnswered ()) {
                table.add (getLocalizedSmallHeader (ANSWER_KEY, ANSWER_DEFAULT),
                           1, row);
                table.add (new Break (), 1, row);
                table.add (new Text (viewpoint.getAnswer ()), 1, row++);
            }              
            table.add(getStartPageLink (context), 1, row++);
            add (table);
        } else {
            // user is not authorized to see this particular viewpoint
            add (getLocalizedHeader (NOTAUTHORIZEDTOSHOWVIEWPOINT_KEY,
                                     NOTAUTHORIZEDTOSHOWVIEWPOINT_DEFAULT));
        }
    }

	private void showAcceptForm(final IWContext context) throws RemoteException,
                                                            FinderException {
		final ViewpointBusiness viewpointBusiness
                = getViewpointBusiness (context);
		final int viewpointId
                = Integer.parseInt (context.getParameter (PARAM_VIEWPOINT_ID));
		final Viewpoint viewpoint
                = viewpointBusiness.findViewpoint (viewpointId);
		final Form form = new Form();
		form.add(new HiddenInput (PARAM_VIEWPOINT_ID, viewpointId + ""));
        final Table table = createViewpointTable (viewpoint, context);
		final SubmitButton submit
                = getSubmitButton (ACCEPTTOHANDLEVIEWPOINT_ACTION,
                                   IACCEPTTOHANDLETHISVIEWPOINT_KEY,
                                   IACCEPTTOHANDLETHISVIEWPOINT_DEFAULT);
        int row = 6;
		table.setHeight (row++, 12);
		table.add (submit, 1, row++);
		table.add (getSubmitButton (CANCEL_ACTION, CANCEL_KEY, CANCEL_DEFAULT),
                   1, row++);
		form.add(table);
		add(form);
	}

	private void acceptToHandleViewpoint(final IWContext context)
        throws RemoteException, FinderException {
		// 1. parse input
		final int viewpointId
                = Integer.parseInt(context.getParameter(PARAM_VIEWPOINT_ID));

		// 2. registerhandler
		final ViewpointBusiness viewpointBusiness
                = getViewpointBusiness(context);
		viewpointBusiness.registerHandler(viewpointId,
                                          context.getCurrentUser ());

		// 3. print feedback
		final Text text
                = new Text(getLocalizedString(CONFIRMSETHANDLER_KEY,
                                              CONFIRMSETHANDLER_DEFAULT));
		text.setWidth(Table.HUNDRED_PERCENT);
		final Table table = new Table(1, 2);
		int row = 1;
		table.setWidth(600);
		table.setCellspacing(0);
		table.setCellpadding(14);
		table.setColor(getBackgroundColor());
		table.add(text, 1, row++);
		table.add(getStartPageLink (context), 1, row++);
		add(table);
	}

	private void showAnswerForm(final IWContext context) throws RemoteException,
                                                            FinderException {
        // 1. find viewpoint
		final ViewpointBusiness viewpointBusiness
                = getViewpointBusiness (context);
		final int viewpointId
                = Integer.parseInt (context.getParameter (PARAM_VIEWPOINT_ID));
		final Viewpoint viewpoint
                = viewpointBusiness.findViewpoint (viewpointId);

        // 2. add answer form
		final TextArea textArea = new TextArea (PARAM_ANSWER);
		textArea.setColumns (40);
		textArea.setRows (10);
        textArea.setMaximumCharacters (400);
        final SubmitButton answerButton
                = getSubmitButton (ANSWERVIEWPOINT_ACTION,
                                   SENDANSWERTOCITIZEN_KEY,
                                   SENDANSWERTOCITIZEN_DEFAULT);
        final SubmitButton cancelButton
                = getSubmitButton (CANCEL_ACTION, CANCEL_KEY, CANCEL_DEFAULT);
        final Table buttonTable = new Table ();
		buttonTable.add (answerButton, 1, 1);
		buttonTable.add (cancelButton, 2, 1);

        // 3. add forward form
        final SubmitButton forwardButton = getSubmitButton
                (SHOWFORWARDFORM_ACTION, FORWARD_KEY, FORWARD_DEFAULT);
        final Table table = createViewpointTable (viewpoint, context);
        int row = 6;
		table.add (getLocalizedHeader(ANSWER_KEY, ANSWER_DEFAULT), 1, row);
		table.add (new Break(), 1, row);
		table.add (textArea, 1, row++);
		table.setHeight (row++, 12);
        table.add (buttonTable, 1, row++);
		table.setHeight (row++, 24);
		table.add (getLocalizedHeader
                   (FORWARDTO_KEY, FORWARDTO_DEFAULT.toUpperCase ()), 1, row++);
        final Table radioButtonTable = new Table (3, 2);
        final RadioButton button1 = getRadioButton (FORWARDTO_KEY,
                                                   GROUPNAME_KEY);
        button1.setSelected ();
        final TextInput textInput = (TextInput) getStyledInterface
                (new TextInput (GROUPNAME_KEY));
        textInput.setLength (40);
        radioButtonTable.add (button1, 1, 1);
        final String headerText = getLocalizedString (GROUPNAME_KEY,
                                                      GROUPNAME_DEFAULT);
        radioButtonTable.add (getSmallHeader (headerText + ":"), 2, 1);
        radioButtonTable.add (textInput, 3, 1);

        final RadioButton button2 = getRadioButton (FORWARDTO_KEY, GROUPID_KEY);
        radioButtonTable.add (button2, 1, 2);
		final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (PARAM_GROUP_ID));
        Group [] groups = viewpointBusiness.findAllHandlingGroups ();
        for (int i = 0; i < groups.length; i++) {
            dropdown.addMenuElement (groups [i].getPrimaryKey ().toString (),
                                     groups [i].getName ());
        }
        radioButtonTable.add (dropdown, 2, 2);
        table.add (radioButtonTable, 1, row++);
		table.setHeight (row++, 12);
		table.add (forwardButton, 1, row++);

        // 4. create actual form object
		final Form form = new Form();
		form.add(new HiddenInput (PARAM_VIEWPOINT_ID, viewpointId + ""));
		form.add (table);
		add (form);
	}

	private void answerViewpoint(final IWContext context) throws
        RemoteException, CreateException, FinderException {
		// 1. parse input 
		final int viewpointId
                = Integer.parseInt(context.getParameter(PARAM_VIEWPOINT_ID));
		final String answer = context.getParameter(PARAM_ANSWER);

		// 2. register answer
		final ViewpointBusiness viewpointBusiness
                = getViewpointBusiness(context);
		viewpointBusiness.answerAndDeregisterViewpoint(viewpointId, answer);

		// 3. print feedback
		final Text text
                = new Text(getLocalizedString(CONFIRMANSWERSENT_KEY,
                                              CONFIRMANSWERSENT_DEFAULT));
		text.setWidth(Table.HUNDRED_PERCENT);
		final Table table = new Table(1, 2);
		int row = 1;
		table.setWidth(600);
		table.setCellspacing(0);
		table.setCellpadding(14);
		table.setColor(getBackgroundColor());
		table.add(text, 1, row++);
		table.add(getStartPageLink (context), 1, row++);
		add(table);
	}


	private void forwardViewpoint (final IWContext context) {
		final Table table = new Table ();
		int row = 1;
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (0);
        add (table);
        final String forwardType = context.getParameter (FORWARDTO_KEY);       
        Link homeLink = null;
        
        try { 
            final int viewpointId = Integer.parseInt
                    (context.getParameter (PARAM_VIEWPOINT_ID));
            final ViewpointBusiness viewpointBusiness
                    = getViewpointBusiness (context);
            homeLink = getStartPageLink (context);
            String receiverName = null;
            if (null != forwardType && forwardType.equals (LOGINNAME_KEY)) {
                // Login name entered
                final String loginName = context.getParameter (LOGINNAME_KEY);
                final User receiver = getUserByLogin (context, loginName);
                viewpointBusiness.registerHandler (viewpointId, receiver);
                receiverName = receiver.getName ();
            } else if (null != forwardType && forwardType.equals (SSN_KEY)) {
                // SSN entered
                final String ssn = getSsn (context, SSN_KEY);
                final User receiver = getUserBySsn (context, ssn);
                viewpointBusiness.registerHandler (viewpointId, receiver);
                receiverName = receiver.getName ();
            } else if (null != forwardType && forwardType.equals (USERID_KEY)) {
                // User id entered
                final String userId = context.getParameter (USERID_KEY);
                final User receiver = getUserById (context, userId);
                viewpointBusiness.registerHandler (viewpointId, receiver);
                receiverName = receiver.getName ();
            } else if (null != forwardType
                       && forwardType.equals (GROUPNAME_KEY)) {
                // Group name entered
                final String groupName = context.getParameter (GROUPNAME_KEY);
                final Group receiver = getGroupByName (context, groupName);
                viewpointBusiness.registerHandler (viewpointId, receiver);
                receiverName = receiver.getName ();
            } else if (null != forwardType
                       && forwardType.equals (GROUPID_KEY)) {
                // Group id entered
                final String groupId = context.getParameter (PARAM_GROUP_ID);
                final Group receiver = getGroupById (context, groupId);
                viewpointBusiness.registerHandler (viewpointId, receiver);
                receiverName = receiver.getName ();
            } else {
                table.add (new Text("Okänd typ av vidarebefordran"), 1, row++);
            }            
            
            table.add (new Text(getLocalizedString
                                (VIEWPOINT_KEY, VIEWPOINT_DEFAULT)
                                + " " + viewpointId + " " + getLocalizedString
                                (FORWARDEDTO_KEY,
                                 FORWARDEDTO_DEFAULT).toLowerCase ()
                                + " " + receiverName), 1, row++);
        } catch (final FinderException e) {
            table.add (new Text (e.getMessage ()), 1, row++);
        } catch (final RemoteException e) {
            e.printStackTrace ();
            table.add (new Text ("Ett fel inträffade."), 1, row++);
        }
        
        if (null != homeLink) {
            table.setHeight (row++, 12);
            table.add (homeLink, 1, row++);
        }
    }

	private void registerViewpoint(final IWContext context)
        throws RemoteException, CreateException, FinderException {
		final ViewpointBusiness viewpointBusiness
                = getViewpointBusiness (context);
		final int subCategoryId = new Integer
                (context.getParameter (PARAM_CATEGORY)).intValue();
		final SubCategory subCategory
                = viewpointBusiness.findSubCategory (subCategoryId);
        final TopCategory topCategory = subCategory.getTopCategory ();
		final Group handlerGroup = subCategory.getHandlerGroup ();
		final int handlerGroupId
                = ((Integer) handlerGroup.getPrimaryKey()).intValue();
		final int roadId = new Integer
                (context.getParameter (PARAM_ROAD_ID)).intValue();
        final String subject = context.getParameter (PARAM_SUBJECT);
        final String message = context.getParameter (PARAM_MESSAGE);
        final User user = getCurrentUser (context);
        if (null != user) {
            viewpointBusiness.createViewpoint
                    (user, subject, message, topCategory.getName () + "/"
                     + subCategory.getName (), handlerGroupId, roadId);
        } else if (isUserIdentifiedByEmailAndName (context)) {
            final HttpSession session = context.getSession ();
            final String userName = (String) session.getAttribute (NAME_KEY);
            final String userEmail = (String) session.getAttribute (EMAIL_KEY);
            viewpointBusiness.createViewpoint
                    (userName, userEmail, subject, message,
                     topCategory.getName () + "/" + subCategory.getName (),
                     handlerGroupId, roadId);
        }
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
		table.add (getStartPageLink (context), 1, row++);
		add (table);
	}

	private void showForwardForm (final IWContext context) {
		final Form form = new Form();
		final Table table = new Table ();
		int row = 1;
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (0);
        form.maintainParameter (PARAM_VIEWPOINT_ID);
        final String forwardType = context.getParameter (FORWARDTO_KEY);       
        table.add (getLocalizedHeader
                   (FORWARDTO_KEY, FORWARDTO_DEFAULT.toUpperCase ()), 1, row++);
        table.setHeight (row++, 12);
        form.add (table);
        add (form);
        Link homeLink = null;
        try {
            homeLink = getStartPageLink (context);
            if (null != forwardType && (forwardType.equals (GROUPNAME_KEY)
                                        ||  forwardType.equals (GROUPID_KEY))) {
                Group group = null;
                String groupName = null;
                RadioButton groupRadioButton = null;
                if (forwardType.equals (GROUPNAME_KEY)) {
                    // Group name entered
                    groupName = context.getParameter (GROUPNAME_KEY);
                    group = getGroupByName (context, groupName);
                    groupRadioButton = getRadioButton (FORWARDTO_KEY,
                                                       GROUPNAME_KEY);
                    form.maintainParameter (GROUPNAME_KEY);
                } else {
                    // Group id entered
                    group = getGroupById
                            (context, context.getParameter (PARAM_GROUP_ID));
                    groupName = group.getName ();
                    groupRadioButton = getRadioButton (FORWARDTO_KEY,
                                                       GROUPID_KEY);
                    form.maintainParameter (PARAM_GROUP_ID);
                }
                final GroupBusiness groupBusiness
                        = (GroupBusiness) IBOLookup.getServiceInstance
                        (context, GroupBusiness.class);
                final RadioButton userRadioButton
                        = getRadioButton (FORWARDTO_KEY, USERID_KEY);
                userRadioButton.setSelected();
                final Collection users
                        = groupBusiness.getUsersRecursive (group);
                final DropdownMenu userDropdown = (DropdownMenu)
                        getStyledInterface (new DropdownMenu (USERID_KEY));
                for (Iterator i = users.iterator (); i.hasNext ();) {
                    final User user = (User) i.next ();
                    final String id = user.getPrimaryKey ().toString();
                    userDropdown.addMenuElement (id, user.getName ());
                }
                final Table radioTable = new Table (2, 2);
                radioTable.add (groupRadioButton, 1, 1);
                radioTable.add (new Text(groupName), 2, 1);
                radioTable.add (userRadioButton, 1, 2);
                radioTable.add (userDropdown, 2, 2);
                table.add (radioTable, 1, row++);
                table.setHeight (row++, 12);
                final SubmitButton forwardButton = getSubmitButton
                        (FORWARDVIEWPOINT_ACTION, FORWARD_KEY, FORWARD_DEFAULT);
                final SubmitButton cancelButton = getSubmitButton
                        (CANCEL_ACTION, CANCEL_KEY, CANCEL_DEFAULT);
                final Table buttonTable = new Table ();
                buttonTable.add (forwardButton, 1, 1);
                buttonTable.add (cancelButton, 2, 1);
                table.add (buttonTable, 1, row++);
            } else {
                table.add (new Text("Okänd typ av vidarebefordran"), 1, row++);
            }            
        } catch (final FinderException e) {
            table.add (new Text (e.getMessage ()), 1, row++);
            table.setHeight (row++, 12);
            table.add (homeLink, 1, row++);
        } catch (final RemoteException e) {
            e.printStackTrace ();
            table.add (new Text ("Ett fel inträffade."), 1, row++);
            table.setHeight (row++, 12);
            table.add (homeLink, 1, row++);
        }
    }

    private User getUserById (final IWContext context, final String userId)
        throws FinderException {
        User result = null;
        try {
            final UserBusiness userBusiness = (UserBusiness)
                    IBOLookup.getServiceInstance (context, UserBusiness.class);
            result = userBusiness.getUser (Integer.parseInt (userId));
        } catch (RemoteException dummy) {
            // nothing, since algorithm is in finally clause
        } finally {
            if (null == result) {
                throw new FinderException ("Hittade inte användaren med id "
                                           + userId);
            }
        }
        return result;
    }

    private User getUserByLogin (final IWContext context,
                                 final String loginName)
        throws FinderException {
        User result = null;
        try {
            final LoginTable loginTable
                    = LoginDBHandler.getUserLoginByUserName (loginName);
            final int userId = loginTable.getUserId ();
            final UserBusiness userBusiness = (UserBusiness)
                    IBOLookup.getServiceInstance (context, UserBusiness.class);
            result = userBusiness.getUser (userId);
        } catch (RemoteException dummy) {
            // nothing, since algorithm is in finally clause
        } finally {
            if (null == result) {
                throw new FinderException ("Hittade inte användarnamnet "
                                           + loginName);
            }
        }
        return result;
    }

    private User getUserBySsn (final IWContext context, final String ssn) throws FinderException{
        User result = null;
        try {
            final UserBusiness userBusiness = (UserBusiness)
                    IBOLookup.getServiceInstance (context, UserBusiness.class);
            result = userBusiness.getUser (ssn);
        } catch (RemoteException dummy) {
            // nothing, since algorithm is in finally clause
        } catch(FinderException ex){
        	log ("Hittade inte anvandaren " + ssn + " Felaktigt personnummer");
        	throw ex;
        }
        
        return result;
    }

    private Group getGroupByName (final IWContext context,
                                 final String groupName)
        throws FinderException {
        Group result = null;
        try {
            final GroupBusiness groupBusiness = (GroupBusiness)
                    IBOLookup.getServiceInstance (context, GroupBusiness.class);
            Collection col = groupBusiness.getGroupHome().findGroupsByName(groupName);
            if(col!=null && col.isEmpty()){
            		result = (Group) col.iterator().next();	
            }
        } catch (RemoteException dummy) {
            // nothing, since algorithm is in finally clause
        } finally {
            if (null == result) {
                throw new FinderException ("Hittade inte gruppen " + groupName
                                           + ". Tänk på att skillnaden på"
                                           + " versaler och gemener är"
                                           + " signifikant.");
            }
        }
        return result;
    }

    private Group getGroupById (final IWContext context, final String id)
        throws FinderException {
        Group result = null;
        try {
            final GroupBusiness groupBusiness = (GroupBusiness)
                    IBOLookup.getServiceInstance (context, GroupBusiness.class);
            result = groupBusiness.getGroupByGroupID
                    (new Integer (id).intValue ());
        } catch (RemoteException dummy) {
            // nothing, since algorithm is in finally clause
        } finally {
            if (null == result) {
                throw new FinderException ("Hittade inte gruppen med id " + id);
            }
        }
        return result;
    }

    private Link getStartPageLink (final IWContext context) {
        Link result = null;
        try {
            if (context.isLoggedOn ()) {
                final Text userHomePageText = new Text
                        (getLocalizedString (GOBACKTOMYPAGE_KEY,
                                             GOBACKTOMYPAGE_DEFAULT));
                final UserBusiness userBusiness = (UserBusiness)
                        IBOLookup.getServiceInstance (context,
                                                      UserBusiness.class);
                final User user = context.getCurrentUser ();
                final int homePageId = userBusiness.getHomePageIDForUser (user);
                result = new Link (userHomePageText);
                result.setPage (homePageId);
            }
        } catch (Exception e) {
            // nothing, since algorithm is in finally clause
        } finally {
            if (null == result) {
                result = new Link ("Tillbaka till startsidan", "/");
            }
        }
        return result;
    }

    private SubmitButton getSubmitButton (final String action, final String key,
                                          final String defaultName) {
        return (SubmitButton) getButton (new SubmitButton
                                         (action, getLocalizedString
                                          (key, defaultName)));
    }

    private Table createViewpointTable
        (final Viewpoint viewpoint, final IWContext context)
        throws RemoteException{
		final Table table = new Table();
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (getCellpadding ());
		int row = 1;
		table.add (getLocalizedSmallHeader (APPLIES_KEY, APPLIES_DEFAULT), 1,
                   row);
		table.add (new Break (), 1, row);
		table.add (new Text (viewpoint.getCategory ()), 1, row++);
		table.add (getLocalizedSmallHeader (FROMCITIZEN_KEY,
                                            FROMCITIZEN_DEFAULT), 1, row);
		table.add (new Break (), 1, row);
        final Integer userId = viewpoint.getUserId ();
        if (null != userId) {
            final UserBusiness userBusiness = (UserBusiness) IBOLookup
                    .getServiceInstance (context, UserBusiness.class);
            final User user = userBusiness.getUser (viewpoint.getUserId ());
            table.add (new Text (user.getName  ()), 1, row++);
        } else {
            table.add (new Text (viewpoint.getUserName () + " ("
                                 + viewpoint.getUserEmail () + ")"), 1, row++);
        }
		table.add (getLocalizedSmallHeader (SUBJECT_KEY, SUBJECT_DEFAULT), 1,
                   row);
		table.add (new Break (), 1, row);
		table.add (new Text (viewpoint.getSubject ()), 1, row++);
		table.add (getLocalizedSmallHeader (ROADINFO_KEY, ROADINFO_DEFAULT), 1,
                   row);
		table.add (new Break (), 1, row);
        final Integer roadInfoId = viewpoint.getRoadResponsibleId ();
        String road = "-";
        if (roadInfoId != null) {
            try {
                final ViewpointBusiness viewpointBusiness
                        = getViewpointBusiness(context);
                final RoadResponsible roadResponsible
                        = viewpointBusiness.findRoadResponsible
                        (roadInfoId.intValue ());
                road = roadResponsible.getRoad () + " ("
                        + roadResponsible.getArea () + ")";
            } catch (FinderException e) {
                e.printStackTrace ();
            }
        }
        table.add (new Text (road), 1, row++);
		table.add (getLocalizedSmallHeader (MESSAGE_KEY, MESSAGE_DEFAULT), 1,
                   row);
		table.add (new Break (), 1, row);
		table.add (new Text (viewpoint.getMessage ()), 1, row++);
        return table;
    }

	private boolean isUserIdentified (final IWContext context) {
        return context.isLoggedOn () || isUserIdentifiedBySsn (context)
                || isUserIdentifiedByEmailAndName (context);
    }

	private boolean isUserIdentifiedBySsn (final IWContext context) {
        boolean result = false;
        final HttpSession session = context.getSession ();
        try {
            if (null != session.getAttribute (SSN_KEY)) {
                result = true;
            } else {
                final String ssn = getSsn (context, PARAM_SSN);
                final UserBusiness userBusiness = (UserBusiness) IBOLookup
                        .getServiceInstance (context, UserBusiness.class);
                final User user = userBusiness.getUser (ssn);
                final Integer userId = (Integer) user.getPrimaryKey ();
                final LoginTable loginTable
                        = LoginDBHandler.getUserLogin (userId.intValue ());
                if (loginTable != null) {
                    // user has a citizen account
                    session.setAttribute (SSN_KEY, user);
                    result = true;
                }
            }
        } catch (final Exception e) {
            result = false;
        }

        return result;
    }

    private boolean isUserIdentifiedByEmailAndName (final IWContext context) {
        boolean result = false;
        final HttpSession session = context.getSession ();
        try {
            if (null != session.getAttribute (EMAIL_KEY)) {
                result = true;
            } else {
                final String email = context.getParameter (PARAM_EMAIL).trim ();
                final String name = context.getParameter (PARAM_NAME).trim ();
                if (email.length () > 0 && name.length () > 0) {
                    session.setAttribute (EMAIL_KEY, email);
                    session.setAttribute (NAME_KEY, name);
                    result = true;
                }
            }
        } catch (final Exception e) {
            result = false;
        }

        return result;
    }

    private User getCurrentUser (final IWContext context) {
        User result = null;
        
        if (context.isLoggedOn ()) {
            result = context.getCurrentUser ();
        } else if (isUserIdentifiedBySsn (context)) {
            final HttpSession session = context.getSession ();
            result = (User) session.getAttribute (SSN_KEY);
        }

        return result;
    }

    private static String getSsn(final IWContext context, final String key) {
        final String rawInput = context.getParameter(key);
        if (rawInput == null) {
            return null;
        }
        final StringBuffer digitOnlyInput = new StringBuffer();
        for (int i = 0; i < rawInput.length(); i++) {
            if (Character.isDigit(rawInput.charAt(i))) {
                digitOnlyInput.append(rawInput.charAt(i));
            }
        }
        final Calendar rightNow = Calendar.getInstance();
        final int currentYear = rightNow.get(Calendar.YEAR);
        if (digitOnlyInput.length() == 10) {
            final int inputYear
                    = new Integer(digitOnlyInput.substring(0, 2)).intValue();
            final int century = inputYear + 2000 > currentYear ? 19 : 20;
            digitOnlyInput.insert(0, century);
        }
        final PIDChecker pidChecker = PIDChecker.getInstance();
        if (digitOnlyInput.length() != 12
            || !pidChecker.isValid(digitOnlyInput.toString())) {
            return null;
        }
        final int year = new Integer(digitOnlyInput.substring(0, 4)).intValue();
        final int month
                = new Integer(digitOnlyInput.substring(4, 6)).intValue();
        final int day = new Integer(digitOnlyInput.substring(6, 8)).intValue();
        if (year < 1880 || year > currentYear || month < 1 || month > 12
            || day < 1 || day > 31) {
            return null;
        }
        return digitOnlyInput.toString();
    }

	private String getLocalizedString(final String key, final String value) {
		return getResourceBundle().getLocalizedString(key, value);
	}

	private ViewpointBusiness getViewpointBusiness (final IWContext context)
        throws RemoteException {
		return (ViewpointBusiness) IBOLookup.getServiceInstance
                (context, ViewpointBusiness.class);
	}
}
