package com.idega.block.forum.presentation;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.idega.block.IWBlock;
import com.idega.block.forum.business.ForumBusiness;
import com.idega.block.forum.business.ForumTree;
import com.idega.block.forum.data.ForumData;
import com.idega.block.presentation.CategoryBlock;
import com.idega.block.text.business.TextFormatter;
import com.idega.builder.data.IBPage;
import com.idega.core.business.CategoryBusiness;
import com.idega.core.business.CategoryFinder;
import com.idega.core.data.Email;
import com.idega.core.data.ICCategory;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.event.IWPresentationState;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.StatefullPresentation;
import com.idega.presentation.StatefullPresentationImplHandler;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;

/**
 * Title:        Forum block<p>
 * Description:  <p>
 * Copyright:    Copyright (c) idega margmi?lun hf.<p>
 * Company:      idega margmi?lun hf.<p>
 * @author idega 2002 - idega team - <a href="mailto:laddi@idega.is">laddi@idega.is</a>
 * @version 1.2
 */

public class Forum extends CategoryBlock implements IWBlock, StatefullPresentation {

  private int _objectID = -1;
  private int _selectedObjectID = -1;
  private int _topicID = -1;
  private int _threadID = -1;
  private boolean _isAdmin = false;
  private boolean _hasAddPermission = true;
  private String _attribute;
  private int _iLocaleID;

  private int _firstThread = 1;
  private int _lastThread = 10;
  private int _numberOfThreads = 10;
  private String _spaceBetween = "8";
  private boolean _showOverviewLink = true;
  private boolean _showTopicName = true;
  private boolean _showResponses = true;

  private int _state = ForumBusiness.FORUM_TOPICS;
  private int _initialState = ForumBusiness.FORUM_TOPICS;
  private int _openLevel = 0;
  private Table _myTable;

  private boolean _styles = true;
  private String _headerStyle;
  private String _linkStyle;
  private String _linkHoverStyle;
  private String _topicLinkStyle;
  private String _topicLinkHoverStyle;
  private String _threadLinkStyle;
  private String _threadLinkHoverStyle;
  private String _textStyle;
  private String _headingStyle;
  private String _informationStyle;

  private String _headingColor;

  private String _width;
  private IBPage _page;
  private Image _threadImage;

  private String _topicName;
  private String _threadName;
  private String _linkName;

  private static String AddPermisson = "add";
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.forum";
  protected IWResourceBundle _iwrb;
  protected IWBundle _iwb;
  protected IWBundle _iwcb;
  private ForumBusiness forumBusiness;

  private StatefullPresentationImplHandler stateHandler = null;

  private String _authorWidth = "160";
	private String _replyWidth = "60";
	private String _dateWidth = "100";
	
	public Forum() {

    /**
     * @todo implement Statehandling for Forum
     */
    stateHandler = new ForumTree().getStateHandler();

    setDefaultValues();
  }

  /**
   * Temporary implementation
   * returning stateClass for the ForumTree Object
   */
  public Class getPresentationStateClass(){
    return stateHandler.getPresentationStateClass();
  }

  public IWPresentationState getPresentationState(IWUserContext iwuc){
    return stateHandler.getPresentationState(this,iwuc);
  }



  public void main(IWContext iwc) throws Exception {
    _iwrb = getResourceBundle(iwc);
    _iwb = getBundle(iwc);
    _iwcb = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    _hasAddPermission = iwc.hasPermission(AddPermisson,this);

    _isAdmin = iwc.hasEditPermission(this);
    _iLocaleID = ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale());
    _objectID = getICObjectInstanceID();

    getParameters(iwc);
    forumBusiness = new ForumBusiness();

    _myTable = new Table();
    _myTable.setCellpadding(0);
    _myTable.setCellspacing(0);
    _myTable.setBorder(0);
    _myTable.setWidth(_width);

    int row = 1;
    if(_isAdmin){
      _myTable.add(getAdminPart(iwc),1,row);
      row++;
    }

    Table table = getForum(iwc);
    if ( table != null )
      _myTable.add(table,1,row);
    add(_myTable);
  }

  private Table getForum(IWContext iwc) {
    Table table = new Table();
      table.setCellspacing(2);
      table.setCellpadding(0);
    table.setWidth(Table.HUNDRED_PERCENT);
    setStyles();

    switch (_state) {
    case ForumBusiness.FORUM_TOPICS:
      getForumTopics(iwc,table);
      break;
    case ForumBusiness.FORUM_THREADS:
      getForumThreads(iwc,table);
      break;
    case ForumBusiness.FORUM_COLLECTION:
      getForumCollection(iwc,table);
      break;
    case ForumBusiness.TOPIC_COLLECTION:
      getTopicCollection(iwc,table);
      break;
    }

    return table;
  }

  private void getForumTopics(IWContext iwc,Table table) {
    iwc.removeSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD+"_"+_objectID);
    iwc.removeSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD+"_"+_objectID);

    Text topicText = new Text(_iwrb.getLocalizedString("topics","Topics"));
    topicText.setFontStyle(_headingStyle);
    Text threadsText = new Text(_iwrb.getLocalizedString("threads","Threads"));
    threadsText.setFontStyle(_headingStyle);
    Text updatedText = new Text(_iwrb.getLocalizedString("last_updated","Last updated"));
    updatedText.setFontStyle(_headingStyle);

    table.setWidth(2,"60");
    table.setWidth(3,"90");

    table.add(topicText,1,1);
    table.add(threadsText,2,1);
    table.add(updatedText,3,1);

    Table topicTable = null;

    Vector list = new Vector();
    list.addAll(this.getCategories());

    if ( list != null ) {
      Link topicLink = null;
      Text numberOfThreadsText = null;
      Text lastUpdatedText = null;
      ICCategory topic;
      int row = 2;

      for ( int a = 0; a < list.size(); a++ ) {
	topic = (ICCategory) list.get(a);

	if ( topic != null ) {
	  topicLink = new Link(topic.getName());
	  topicLink.setStyle(_topicName);
	  topicLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,topic.getID());
	  topicLink.addParameter(ForumBusiness.PARAMETER_STATE,ForumBusiness.FORUM_THREADS);
	  topicLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);

	  int numberOfThreads = forumBusiness.getNumberOfThreads(topic);
	  numberOfThreadsText = formatText(String.valueOf(numberOfThreads),_textStyle);

	  ForumData newestThread = forumBusiness.getNewestThreads(topic);
	  if ( newestThread != null ) {
	    lastUpdatedText = getThreadDate(iwc,newestThread);
	  }
	  else
	    lastUpdatedText = null;

	  table.add(topicLink,1,row);
	  table.add(numberOfThreadsText,2,row);
	  if ( lastUpdatedText != null )
	    table.add(lastUpdatedText,3,row);

	  row++;
	}
      }
    }

    table.setColumnAlignment(2,"center");
    table.setColumnAlignment(3,"center");
  }

  private void getForumThreads(IWContext iwc,Table table) {
    int row = 1;

    ICCategory topic = null;
    if ( _topicID != -1 )
      topic = CategoryFinder.getInstance().getCategory(_topicID);

    ForumData thread = null;
    try {
      thread = forumBusiness.getForumData(Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID)));
      _threadID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID));
    }
    catch (NumberFormatException e) {
      thread = null;
    }

    if ( topic != null ) {
      if ( _showTopicName ) {
	Text topicText = formatText(topic.getName(),_headerStyle);
	table.add(topicText,1,row++);
	table.setBackgroundImage(1,row++,_iwb.getImage("shared/dotted.gif"));
      }

      if ( thread != null && thread.isValid() ) {
	table.add(getThreadHeaderTable(thread,iwc),1,row++);
	table.setBackgroundImage(1,row++,_iwb.getImage("shared/dotted.gif"));
	table.add(getThreadBodyTable(thread),1,row++);
      }

      table.setHeight(1,row++,"16");
      table.add(getForumLinks(),1,row++);

      updateThreadCount(iwc);

      ForumData[] threads = forumBusiness.getThreads(topic);
      ForumData[] someThreads = forumBusiness.getThreads(threads,_firstThread,_lastThread);
      boolean hasNextThreads = forumBusiness.hasNextThreads(threads,_lastThread);
      boolean hasPreviousThreads = forumBusiness.hasPreviousThreads(_firstThread);

      table.add(getForumTree(someThreads),1,row);

      if ( hasNextThreads || hasPreviousThreads )
	table.add(getNextPreviousTable(hasNextThreads,hasPreviousThreads),1,++row);
    }
  }

  private void getForumCollection(IWContext iwc,Table table) {
    table.setCellpaddingAndCellspacing(0);
    int row = 1;

    Collection categories = this.getCategories();
    if ( categories != null && categories.size() == 1 ) {
      _topicID = ((ICCategory)new Vector(categories).get(0)).getID();
    }

    List list = forumBusiness.getThreadsInCategories(categories,_numberOfThreads);
    if ( list != null ) {
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
	ForumData thread = (ForumData) iter.next();

	table.add(getUser(thread),1,row);
	table.add(formatText(","+Text.NON_BREAKING_SPACE),1,row);
	table.add(getThreadDate(iwc,thread),1,row);
	table.add(Text.getBreak(),1,row);
	table.add(getThreadLink(thread,_topicName),1,row);
	if ( _showResponses ) {
	  table.add(formatText(Text.NON_BREAKING_SPACE),1,row);
	  table.add(getThreadResponses(thread),1,row++);
	}
	else {
	  row++;
	}
	table.setHeight(1,row++,_spaceBetween);
      }
    }

    table.setAlignment(1,row,Table.HORIZONTAL_ALIGN_RIGHT);
    table.add(getForumLinks(),1,row);
  }

  private void getTopicCollection(IWContext iwc,Table table) {
    int row = 1;
    table.setCellpaddingAndCellspacing(0);
    Table threadTable;

    Collection categories = getCategories();
    if ( categories != null ) {
      Iterator iter = categories.iterator();
      while (iter.hasNext()) {
	ICCategory category = (ICCategory) iter.next();
	table.add(getTopicLink(category),1,row);
	table.add(Text.getNonBrakingSpace(),1,row);
	table.add(getTopicThreadsText(category),1,row);

	List threads = forumBusiness.getThreads(category,_numberOfThreads);
	if ( threads != null ) {
	  threadTable = new Table();
	  threadTable.setCellpaddingAndCellspacing(0);
	  threadTable.setWidth(Table.HUNDRED_PERCENT);
	  int threadRow = 1;

	  Iterator iter2 = threads.iterator();
	  while (iter2.hasNext()) {
	    ForumData thread = (ForumData) iter2.next();
	    if ( _threadImage != null ) {
	      _threadImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
	      _threadImage.setHorizontalSpacing(2);
	      threadTable.add(_threadImage,1,threadRow);
	    }
	    threadTable.add(getThreadLink(thread,_threadName),1,threadRow);
	    threadTable.add(formatText(Text.NON_BREAKING_SPACE),1,threadRow);
	    threadTable.add(getThreadResponses(thread),1,threadRow++);
	  }

	  table.add(threadTable,1,row++);
	  table.setHeight(1,row++,_spaceBetween);
	}
      }
    }
  }

  private Link getTopicLink(ICCategory category) {
    Link link = new Link(category.getName());
      link.setStyle(_topicName);
    if ( _page != null )
      link.setPage(_page);
    link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,category.getID());
    link.addParameter(ForumBusiness.PARAMETER_STATE,ForumBusiness.FORUM_THREADS);
    link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);

    return link;
  }

  private Text getTopicThreadsText(ICCategory topic) {
    int numberOfThreads = forumBusiness.getNumberOfThreads(topic);
    return formatText("("+String.valueOf(numberOfThreads)+Text.NON_BREAKING_SPACE+_iwrb.getLocalizedString("threads_lc","threads")+")",_informationStyle);
  }

  private Link getThreadLink(ForumData thread,String styleName) {
    String headline = thread.getThreadSubject();
    if ( headline == null ) headline = "";

    Link link = new Link(headline);
      link.setStyle(styleName);
    if ( _page != null )
      link.setPage(_page);
    link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,thread.getTopicID());
    link.addParameter(ForumBusiness.PARAMETER_STATE,ForumBusiness.FORUM_THREADS);
    link.addParameter(ForumBusiness.PARAMETER_THREAD_ID,thread.getID());
    //link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);

    return link;
  }

  private Text getThreadResponses(ForumData thread) {
    return formatText("("+String.valueOf(thread.getNumberOfResponses())+Text.NON_BREAKING_SPACE+_iwrb.getLocalizedString("replies_lc","replies")+")",_informationStyle);
  }

  private PresentationObject getUser(ForumData thread) {
    Text text = formatText(_iwrb.getLocalizedString("unknown","Unknown"));
    if ( thread.getUserID() != -1 ) {
      User user = UserBusiness.getUser(thread.getUserID());
      Email mail = UserBusiness.getUserMail(thread.getUserID());
      if ( user != null && mail != null ) {
	String name = user.getName();
	if ( user.getDisplayName() != null && user.getDisplayName().length() > 0 )
	  name = user.getDisplayName();
	Link link = new Link(name);
	  link.setStyle(_threadName);
	link.setURL("mailto:"+mail.getEmailAddress());
	return link;
      }
      else if ( user != null ) {
	Text userText = formatText(user.getName(),_textStyle);
	return userText;
      }
    }
    else if ( thread.getUserName() != null && thread.getUserEMail() != null ) {
      Link link = new Link(thread.getUserName());
	link.setStyle(_threadName);
      if ( thread.getUserEMail() != null )
	link.setURL("mailto:"+thread.getUserEMail());
      return link;
    }
    else if ( thread.getUserName() != null ) {
      Text userText = formatText(thread.getUserName(),_textStyle);
      return userText;
    }
    return text;
  }

  private Text getThreadDate(IWContext iwc,ForumData thread) {
    IWTimestamp stamp = new IWTimestamp(thread.getThreadDate());
    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
    Date date = new Date(stamp.getTimestamp().getTime());
    return formatText(format.format(date));
  }

  private ForumTree getForumTree(ForumData[] threads) {
    ForumTree tree = new ForumTree();
    /**
     * @todo chage later: legacy-fix
     */
     tree.setICObjectInstanceID(this.getICObjectInstanceID());
     System.out.println("Forum: tree.setICObjectInstanceID("+this.getICObjectInstanceID()+");");

      tree.setHeadingColor(_headingColor);
      tree.setExtraHeadingColor(1,_headingColor);
      tree.setExtraHeadingColor(2,_headingColor);
      tree.setExtraHeadingColor(3,_headingColor);
      tree.setColumns(2);
      tree.setParallelExtraColumns(3);
      tree.setFirstLevelNodes(threads);
      tree.setNestLevelAtOpen(_openLevel);
      tree.setToShowTreeIcons(false);
      tree.setWidth("100%");
      tree.setExtraColumnWidth(1,_authorWidth);
      tree.setExtraColumnWidth(2,_replyWidth);
      tree.setExtraColumnWidth(3,_dateWidth);
      tree.setIconDimensions("15","12");
      tree.setTreeHeading(1,formatText(_iwrb.getLocalizedString("thread","Thread"),_headingStyle));
      tree.setExtraColumnHeading(1,formatText(_iwrb.getLocalizedString("author","Author"),_headingStyle));
      tree.setExtraColumnHeading(2,formatText(_iwrb.getLocalizedString("replies","Replies"),_headingStyle));
      tree.setExtraColumnHeading(3,formatText(_iwrb.getLocalizedString("date","Date"),_headingStyle));
      tree.setExtraColumnHorizontalAlignment(2,"center");
      tree.setToShowHeaderRow(true);
      tree.setTreePadding(2);
      tree.setTextStyle(_textStyle);
      tree.setLinkStyleName(_threadName);
      tree.setLinkPage(_page);
      tree.setObjectInstanceID(_objectID);
      tree.setThreadImage(_threadImage);
      tree.setResourceBundle(_iwrb);
    return tree;
  }

  private Table getThreadHeaderTable(ForumData thread,IWContext iwc) {
    Table table = new Table(1,3);
    table.setWidth("100%");
    table.setCellpadding(2);
    table.setCellspacing(0);

    Image image;
    if ( _threadImage == null ) {
      image = _iwb.getImage("shared/thread.gif");
    }
    else {
      image = (Image) _threadImage.clone();
    }
      image.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);

    String headlineString = thread.getThreadSubject();
    if ( headlineString == null ) headlineString = "";
    Text headline = formatText(headlineString,_headingStyle);

    IWTimestamp stamp = new IWTimestamp(thread.getThreadDate());
    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
    Date date = new Date(stamp.getTimestamp().getTime());
    Text dateText = formatText(format.format(date));

    table.add(image,1,1);
    table.add(headline,1,1);
    table.add(getUser(thread),1,2);
    table.add(formatText(","+Text.NON_BREAKING_SPACE),1,2);
    table.add(dateText,1,2);

    table.add(getThreadLinks(iwc,thread),1,3);

    return table;
  }

  private Table getThreadBodyTable(ForumData thread) {
    Table table = new Table(1,1);
    table.setWidth("100%");
    table.setCellpadding(2);
    table.setCellspacing(0);

    String bodyString = thread.getThreadBody();
    if ( bodyString == null ) bodyString = "";
    Text body = formatText(TextFormatter.formatText(bodyString,1,"100%"));

    table.add(body,1,1);
    return table;
  }

  private Table getNextPreviousTable(boolean hasNext,boolean hasPrevious) {
    Table table = new Table();
    if ( hasPrevious ) {
      Link link = new Link(_iwb.getImage("shared/previous.gif"));
      link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,_topicID);
      link.addParameter(ForumBusiness.PARAMETER_STATE,ForumBusiness.FORUM_THREADS);
      link.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD,(_firstThread - _numberOfThreads));
      link.addParameter(ForumBusiness.PARAMETER_LAST_THREAD,(_lastThread - _numberOfThreads));
      link.addParameter(ForumBusiness.PARAMETER_THREAD_ID,_threadID);
      link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);
      table.add(link,1,1);
      table.add(Text.NON_BREAKING_SPACE, 1, 1);

			Link previousLink = new Link(_iwrb.getLocalizedString("previous_threads","Previous"));
			previousLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,_topicID);
			previousLink.addParameter(ForumBusiness.PARAMETER_STATE,ForumBusiness.FORUM_THREADS);
			previousLink.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD,(_firstThread - _numberOfThreads));
			previousLink.addParameter(ForumBusiness.PARAMETER_LAST_THREAD,(_lastThread - _numberOfThreads));
			previousLink.addParameter(ForumBusiness.PARAMETER_THREAD_ID,_threadID);
			previousLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);
			table.add(previousLink,1,1);
    }
    if ( hasNext ) {
      Link link = new Link(_iwb.getImage("shared/next.gif","Next"));
      link.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,_topicID);
      link.addParameter(ForumBusiness.PARAMETER_STATE,ForumBusiness.FORUM_THREADS);
      link.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD,(_firstThread + _numberOfThreads));
      link.addParameter(ForumBusiness.PARAMETER_LAST_THREAD,(_lastThread + _numberOfThreads));
      link.addParameter(ForumBusiness.PARAMETER_THREAD_ID,_threadID);
      link.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);
      table.add(link,2,1);
			table.add(Text.NON_BREAKING_SPACE, 2, 1);

			Link nextLink = new Link(_iwrb.getLocalizedString("next_threads"));
			nextLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,_topicID);
			nextLink.addParameter(ForumBusiness.PARAMETER_STATE,ForumBusiness.FORUM_THREADS);
			nextLink.addParameter(ForumBusiness.PARAMETER_FIRST_THREAD,(_firstThread + _numberOfThreads));
			nextLink.addParameter(ForumBusiness.PARAMETER_LAST_THREAD,(_lastThread + _numberOfThreads));
			nextLink.addParameter(ForumBusiness.PARAMETER_THREAD_ID,_threadID);
			nextLink.addParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID,_objectID);
			table.add(nextLink,2,1);
    }

    return table;
  }

  private Table getThreadLinks(IWContext iwc,ForumData thread) {
    Table table = new Table();
    int column = 1;

    Image replyImage = _iwb.getImage("shared/reply.gif");
      replyImage.setHorizontalSpacing(2);
      replyImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
    table.add(replyImage,column,1);

    if ( _hasAddPermission ) {
      Link reply = new Link(_iwrb.getLocalizedString("reply","Reply"));
	reply.setStyle(_linkName);
	reply.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,_topicID);
	reply.addParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID,thread.getID());
	reply.setWindowToOpen(ForumThreadEditor.class);
      table.add(reply,column++,1);
    }
    else {
      Text text = new Text(_iwrb.getLocalizedString("reply","Reply"));
	text.setFontStyle(_linkStyle);
      table.add(text,column++,1);
    }

    if ( thread.getUserID() != -1 && iwc.getUserId() == thread.getUserID() && thread.getChildCount() == 0 ) {
      Image editImage = _iwb.getImage("shared/edit.gif");
	editImage.setHorizontalSpacing(2);
	editImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
      table.add(editImage,column,1);

      Link edit = new Link(_iwrb.getLocalizedString("edit","Edit"));
	edit.setStyle(_linkName);
	edit.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,_topicID);
	edit.addParameter(ForumBusiness.PARAMETER_THREAD_ID,thread.getID());
	edit.addParameter(ForumBusiness.PARAMETER_PARENT_THREAD_ID,thread.getParentThreadID());
	edit.setWindowToOpen(ForumThreadEditor.class);
      table.add(edit,column++,1);
    }

    if ( _isAdmin ) {
      Image deleteImage = _iwb.getImage("shared/delete.gif");
	deleteImage.setHorizontalSpacing(2);
	deleteImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
      table.add(deleteImage,column,1);

      Link delete = new Link(_iwrb.getLocalizedString("delete","Delete"));
	delete.setStyle(_linkName);
	delete.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,_topicID);
	delete.addParameter(ForumBusiness.PARAMETER_THREAD_ID,thread.getID());
	delete.addParameter(ForumBusiness.PARAMETER_MODE,ForumBusiness.PARAMETER_DELETE);
	delete.setWindowToOpen(ForumThreadEditor.class);
      table.add(delete,column++,1);
    }

    return table;
  }

  private Table getForumLinks() {
    Table table = new Table();
    int column = 1;

    if ( _topicID != -1 ) {
      Image newImage = _iwb.getImage("shared/new.gif");
	newImage.setHorizontalSpacing(2);
	newImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
      table.add(newImage,column,1);

      if ( _hasAddPermission ) {
	Link newLink = new Link(_iwrb.getLocalizedString("new_thread"));
	  newLink.setStyle(_linkName);
	  newLink.setWindowToOpen(ForumThreadEditor.class);
	  newLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID,_topicID);
	table.add(newLink,column++,1);
      }
      else {
	Text newText = new Text(_iwrb.getLocalizedString("new_thread"));
	  newText.setFontStyle(_linkStyle);
	table.add(newText,column++,1);
      }
    }

    if ( _showOverviewLink ) {
      Image overviewImage = _iwb.getImage("shared/forum.gif");
	overviewImage.setHorizontalSpacing(2);
	overviewImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
      table.add(overviewImage,column,1);
      table.add(getOverviewLink(),column,1);
    }

    return table;
  }

  private Link getOverviewLink() {
    Link overView = new Link(_iwrb.getLocalizedString("topic_overview","Topic overview"));
      overView.setStyle(_linkName);
      overView.addParameter(ForumBusiness.PARAMETER_STATE,ForumBusiness.FORUM_TOPICS);
      if ( _page != null )
	overView.setPage(_page);

    return overView;
  }

  private Table getAdminPart(IWContext iwc) {
    Table adminTable = new Table(2,1);
    adminTable.setCellpadding(0);
    adminTable.setCellspacing(0);

    Link categoryLink = this.getCategoryLink();
      categoryLink.setPresentationObject(_iwcb.getImage("shared/edit.gif"));
    adminTable.add(categoryLink);

    return adminTable;
  }

  private void updateThreadCount(IWContext iwc) {
    try {
      _firstThread = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_FIRST_THREAD));
    }
    catch (Exception e) {
      try {
	_firstThread = Integer.parseInt((String)iwc.getSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD+"_"+_objectID));
      }
      catch (Exception ex) {
	_firstThread = 1;
      }
    }
    iwc.setSessionAttribute(ForumBusiness.PARAMETER_FIRST_THREAD+"_"+_objectID,Integer.toString(_firstThread));

    try {
      _lastThread = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_LAST_THREAD));
    }
    catch (Exception e) {
      try {
	_lastThread = Integer.parseInt((String)iwc.getSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD+"_"+_objectID));
      }
      catch (Exception ex) {
	_lastThread = _numberOfThreads;
      }
    }
    iwc.setSessionAttribute(ForumBusiness.PARAMETER_LAST_THREAD+"_"+_objectID,Integer.toString(_lastThread));
  }

  private void setStyles() {
    if ( _topicName == null ) {
      _topicName = "forumTopic_"+_objectID;
    }

    if ( _threadName == null ) {
      _threadName = "forumThread_"+_objectID;
    }

    if ( _linkName == null ) {
      _linkName = "forumLink_"+_objectID;
    }

    if ( getParentPage() != null ) {
      getParentPage().setStyleDefinition("A."+_topicName,_topicLinkStyle);
      getParentPage().setStyleDefinition("A."+_topicName+":hover",_topicLinkHoverStyle);

      getParentPage().setStyleDefinition("A."+_threadName,_threadLinkStyle);
      getParentPage().setStyleDefinition("A."+_threadName+":hover",_threadLinkHoverStyle);

      getParentPage().setStyleDefinition("A."+_linkName,_linkStyle);
      getParentPage().setStyleDefinition("A."+_linkName+":hover",_linkHoverStyle);
    }
    else {
      _styles = false;
    }
  }

  private void getParameters(IWContext iwc) {
    try {
      _selectedObjectID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_OBJECT_INSTANCE_ID));
    }
    catch (NumberFormatException e) {
      _selectedObjectID = -1;
    }

    try {
      _state = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_STATE));
    }
    catch (NumberFormatException e) {
    }

    try {
      _topicID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID));
    }
    catch (NumberFormatException e) {
      _topicID = -1;
    }

    if ( _selectedObjectID != -1 && _selectedObjectID != _objectID )
      _state = _initialState;
  }

  private Text formatText(String textString) {
    return formatText(textString,_textStyle);
  }

  private Text formatText(String textString,String style) {
    Text text = new Text(textString);
      text.setFontStyle(style);
    return text;
  }

  private void setDefaultValues() {
    _width = Table.HUNDRED_PERCENT;
    _headingColor = "#eeeeee";

    _headerStyle = "font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold";
    _linkStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #000000; text-decoration: none;";
    _linkHoverStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #000000; text-decoration: underline;";
    _topicLinkStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline; font-weight:bold;";
    _topicLinkHoverStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline; font-weight:bold;";
    _threadLinkStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline;";
    _threadLinkHoverStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: underline;";
    _headingStyle = "font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold";
    _textStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 11px; color: #000000; text-decoration: none;";
    _informationStyle = "font-family: Arial, Helvetica,sans-serif; font-size: 10px; color: #999999;";
  }

  public void setNumberOfDisplayedThreads(int numberOfThreads) {
    _numberOfThreads = numberOfThreads;
  }

  public void setWidth(String width) {
    _width = width;
  }

  public void setPage(IBPage page) {
    _page = page;
  }

  public void setSpaceBetween(int spaceBetween) {
    _spaceBetween = String.valueOf(spaceBetween);
  }

  public void setShowOverviewLink(boolean showOverview) {
    _showOverviewLink = showOverview;
  }

  public void setShowTopicName(boolean showTopic) {
    _showTopicName = showTopic;
  }

  public void setShowResponses(boolean showResponses) {
    _showResponses = showResponses;
  }

  public void setLayout(int layout) {
    _state = layout;
    _initialState = layout;
  }

  public void setThreadImage(Image image) {
    _threadImage = image;
  }

  public void setHeaderStyle(String style) {
    _headerStyle = style;
  }

  public void setHeadingStyle(String style) {
    _headingStyle = style;
  }

  public void setInformationStyle(String style) {
    _informationStyle = style;
  }

  public void setTextStyle(String style) {
    _textStyle = style;
  }

	public void setDefaultOpenLevel(int openLevel) {
		_openLevel = openLevel;
	}

  public void setLinkStyles(String style,String hoverStyle) {
    _linkStyle = style;
    _linkHoverStyle = hoverStyle;
  }

  public void setThreadLinkStyles(String style,String hoverStyle) {
    _threadLinkStyle = style;
    _threadLinkHoverStyle = hoverStyle;
  }

  public void setTopicLinkStyles(String style,String hoverStyle) {
    _topicLinkStyle = style;
    _topicLinkHoverStyle = hoverStyle;
  }

  public void setHeadingColor(String color) {
    _headingColor = color;
  }

  public boolean deleteBlock(int ICObjectInstanceID) {
    return CategoryBusiness.getInstance().disconnectBlock(ICObjectInstanceID);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public String getCategoryType(){
    return "forum";
  }

  public void registerPermissionKeys(){
    registerPermissionKey(AddPermisson);
  }

  public boolean getMultible(){ return true; }

	/**
	 * Sets the authorWidth.
	 * @param authorWidth The authorWidth to set
	 */
	public void setAuthorWidth(String authorWidth) {
		_authorWidth = authorWidth;
	}

	/**
	 * Sets the dateWidth.
	 * @param dateWidth The dateWidth to set
	 */
	public void setDateWidth(String dateWidth) {
		_dateWidth = dateWidth;
	}

	/**
	 * Sets the replyWidth.
	 * @param replyWidth The replyWidth to set
	 */
	public void setReplyWidth(String replyWidth) {
		_replyWidth = replyWidth;
	}

} // Class Forum