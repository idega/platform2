/*
 * $Id: ExtendedDropdownDouble.java,v 1.3 2003/09/08 08:10:07 laddi Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.presentation;

import java.util.*;
import java.lang.reflect.*;

import com.idega.presentation.*;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.InterfaceObjectContainer;
import com.idega.presentation.ui.SelectOption;
import com.idega.data.GenericEntity;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * @author originally Laddis (SelectDropDownDouble) 
 * @author Kelly 
 * 
 * Thanks Anders for helping me with this :-)
 *  
 * The purpose with this is to present a double drop down menu that gets populated 
 * from many different beans, business collections etc. 
 *
 * Modifications as follows:
 * 
 * Can serve many selectors on one web page ;-) (hehe Laddi) 
 * Checkout getKey and getValue. I use reflection to make this
 * functional for ConditionHolders (Pointing to either Generic entities or 
 * Business methods)
 * Supports localize on values = getLocalizationKey
 * 
 * @see se.idega.idegaweb.commune.accounting.business.ConditionHolder#
 * @see se.idega.idegaweb.commune.accounting.business.RegulationsBusiness#
 * 
 * If you interested in how to use this. See findAllOperations in RegulationsBusiness and 
 * RegulationListEditor
 * 
 * Kelly
 * 
 */ 
public class ExtendedDropdownDouble extends InterfaceObjectContainer {
	private String _styleClass;
	private String _primarySelected;
	private String _secondarySelected;
	private String primaryName;
	private String secondaryName;
	private DropdownMenu primary;
	private DropdownMenu secondary;
	private Collection _primaryCollection;
	protected Map _secondaryMap;
	protected Map _methodNameMap;
	private int _spaceBetween;
	private ExtendedDropdownDouble _objectToDisable;
	private String _disableValue;
	private boolean _disabled;
	static int _nameCounter = 0;
	private CommuneBlock _parent = null;
		
    public ExtendedDropdownDouble(CommuneBlock parent)
    {
        primaryName = "primary";
        secondaryName = "secondary";
        primary = null;
        secondary = null;
        _spaceBetween = 3;
        _disabled = false;
        _parent = parent;
    }

    public ExtendedDropdownDouble(CommuneBlock parent, String primaryName, String secondaryName)
    {
        this.primaryName = "primary";
        this.secondaryName = "secondary";
        primary = null;
        secondary = null;
        _spaceBetween = 3;
        _disabled = false;
        this.primaryName = primaryName;
        this.secondaryName = secondaryName;
		_parent = parent;
    }

    public void main(IWContext iwc)
        throws Exception
    {
        if(getStyleAttribute() != null)
        {
            getPrimaryDropdown().setStyleAttribute(getStyleAttribute());
            getSecondaryDropdown().setStyleAttribute(getStyleAttribute());
        }
        addElementsToPrimary();
        getPrimaryDropdown().setOnChange("setDropdownOptions"+_nameCounter+"(this, findObj('" + secondaryName + "'), -1);");
        if(_objectToDisable != null)
        {
            getSecondaryDropdown().setToDisableWhenSelected(_objectToDisable.getPrimaryName(), _disableValue);
            getSecondaryDropdown().setToDisableWhenSelected(_objectToDisable.getSecondaryName(), _disableValue);
        }
        getPrimaryDropdown().setDisabled(_disabled);
        getSecondaryDropdown().setDisabled(_disabled);
        Table table = new Table();
        table.setCellpadding(0);
        table.setCellspacing(0);
        add(table);
        int column = 1;
        table.add(getPrimaryDropdown(), column++, 1);
        if(_spaceBetween > 0)
            table.setWidth(column++, _spaceBetween);
        table.add(getSecondaryDropdown(), column, 1);
        if(_styleClass != null)
        {
            getPrimaryDropdown().setStyleClass(_styleClass);
            getSecondaryDropdown().setStyleClass(_styleClass);
        }
        Script script = getParentPage().getAssociatedScript();
        script.addFunction("setDropdownOptions"+_nameCounter, getSelectorScript());
        if(_secondarySelected == null)
            _secondarySelected = "-1";
        getParentPage().setOnLoad("setDropdownOptions"+_nameCounter+"(findObj('" + primaryName + "'),findObj('" + secondaryName + "'), '" + _secondarySelected + "')");
		_nameCounter++;
    }

    private void addElementsToPrimary()
    {
        if(_primaryCollection != null)
        {
            Iterator iter = _primaryCollection.iterator();
            boolean hasSelected = false;
            while(iter.hasNext()) 
            {
                SelectOption option = (SelectOption)iter.next();
                getPrimaryDropdown().addOption(option);
                if(!hasSelected)
                {
                    getPrimaryDropdown().setSelectedOption(option.getValue());
                    hasSelected = true;
                }
            }
            if(_primarySelected != null)
                getPrimaryDropdown().setSelectedElement(_primarySelected);
        }
    }

    private String getSelectorScript()
    {
        StringBuffer s = new StringBuffer();
        s.append("function setDropdownOptions"+_nameCounter+"(input, inputToChange, selected) {").append("\n\t");
        s.append("var dropdownValues = new Array();").append("\n\t");
        int column = 0;
        if(_secondaryMap != null)
        {
            for(Iterator iter = _secondaryMap.keySet().iterator(); iter.hasNext();)
            {
                column = 0;
                String key = (String)iter.next();
                Map map = (Map)_secondaryMap.get(key);
				String methodName = (String)_methodNameMap.get(key);
                s.append("\n\t").append("dropdownValues[\"" + key + "\"] = new Array();").append("\n\t");
                String secondKey;
                String value;
                for(Iterator iterator = map.keySet().iterator(); iterator.hasNext(); s.append("dropdownValues[\"" + key + "\"][" + column++ + "] = new Option('" + value + "','" + secondKey + "');").append("\n\t"))
                {
                    Object element = iterator.next();
                    secondKey = getKey(element);
                    value = getValue(map.get(element), methodName);
                }

            }

        }
        s.append("\n\t");
        s.append("var chosen = input.options[input.selectedIndex].value;").append("\n\t");
        s.append("inputToChange.options.length = 0;").append("\n\n\t");
        s.append("var array = dropdownValues[chosen];").append("\n\t");
        s.append("for (var a=0; a < array.length; a++)").append("{\n\t\t");
        s.append("var index = inputToChange.options.length;").append("\n\t\t");
        s.append("inputToChange.options[index] = array[a];").append("\n\t\t");
        s.append("var option = inputToChange.options[index];").append("\n\t\t");
        s.append("if (option.value == selected)").append("\n\t\t\t");
        s.append("option.selected = true;").append("\n\t\t");
        s.append("else").append("\n\t\t\t");
        s.append("option.selected = false;").append("\n\t");
        s.append("}").append("\n").append("}");
        return s.toString();
    }

    protected String getKey(Object key)
    {
		if (key instanceof GenericEntity) {
			GenericEntity ge = (GenericEntity) key;
			return ge.getPrimaryKey().toString();
		} else if (key instanceof Object []){
			Object [] o = (Object []) key;
			return o[0].toString();
		} else {
			return key.toString();
		}
    }

    protected String getValue(Object value, String methodName)
    {
		if (value instanceof GenericEntity) {
			// Bean OBJ
			Object o = value;
			Class c = o.getClass();
			String s = "";
			try {
				Method m = c.getMethod(methodName, null);					
				if (methodName.compareTo("getLocalizationKey") == 0) {
					s  = localize((String)m.invoke(o, null), (String)m.invoke(o, null));
				} else {
					s  = localize((String)m.invoke(o, null), (String)m.invoke(o, null));
				}
				
			} catch (Exception e) {
			}
			return s;
		} else if (value instanceof Object []){
			// Direct call to Business
			Object [] o = (Object []) value;
			return o[1].toString();
		} else { 
			// Some dummy data
			return value.toString();
    	}
    }

    public void addMenuElement(String value, String name, Map values, String dataMethodName)
    {
        if(_primaryCollection == null)
            _primaryCollection = new Vector();
        if(_secondaryMap == null) {
            _secondaryMap = new HashMap();
			_methodNameMap = new HashMap();
		}
        _primaryCollection.add(new SelectOption(name, value));
        
        _secondaryMap.put(value, values);
		_methodNameMap.put(value, dataMethodName);
    }

    public void addEmptyElement(String primaryDisplayString, String secondaryDisplayString)
    {
        Map map = new HashMap();
        map.put("-1", secondaryDisplayString);
        addMenuElement("-1", primaryDisplayString, map, "");
    }

    public DropdownMenu getPrimaryDropdown()
    {
        if(primary == null)
            primary = new DropdownMenu(primaryName);
        return primary;
    }

    public DropdownMenu getSecondaryDropdown()
    {
        if(secondary == null)
            secondary = new DropdownMenu(secondaryName);
        return secondary;
    }

    public String getPrimaryName()
    {
        return primaryName;
    }

    public String getSecondaryName()
    {
        return secondaryName;
    }


    public void setSpaceBetween(int spaceBetween)
    {
        _spaceBetween = spaceBetween;
    }

    public void setPrimaryName(String string)
    {
        primaryName = string;
    }

    public void setSecondaryName(String string)
    {
        secondaryName = string;
    }

    public void setSelectedValues(String primaryValue, String secondaryValue)
    {
        _primarySelected = primaryValue;
        _secondarySelected = secondaryValue;
    }

    public void setToEnableWhenNotSelected(ExtendedDropdownDouble doubleDropdown, String disableValue)
    {
        _objectToDisable = doubleDropdown;
        _disableValue = disableValue;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    protected Map getSecondaryMap()
    {
        return _secondaryMap;
    }

	public String localize(String textKey, String defaultText) {
		if (_parent != null) {
			return _parent.localize(textKey, defaultText);
		} else {
			return defaultText;
		}
	}
 
}