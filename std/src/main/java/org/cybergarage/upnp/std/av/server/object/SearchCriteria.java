/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003-2004
 *
 *	File : SearchCriteria.java
 *
 *	Revision:
 *
 *	08/16/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.std.av.server.object;

public class SearchCriteria {
  ////////////////////////////////////////////////
  // Constants
  ////////////////////////////////////////////////

  public static final String ID = "@id";
  public static final String PARENT_ID = "@parentID";
  public static final String TITLE = "dc:title";
  public static final String CREATOR = "dc:creator";
  public static final String CLASS = "upnp:class";
  public static final String DATE = "dc:date";

  public static final String AND = "and";
  public static final String OR = "or";

  public static final String EQ = "=";
  public static final String NEQ = "!=";
  public static final String LT = "<";
  public static final String LE = "<=";
  public static final String GT = ">";
  public static final String GE = ">=";

  public static final String CONTAINS = "contains";
  public static final String DOESNOTCONTAIN = "doesNotContain";
  public static final String DERIVEDFROM = "derivedfrom";
  public static final String EXISTS = "exists";

  public static final String TRUE = "true";
  public static final String FALSE = "false";

  public static final String WCHARS = " \t\n\f\r"; // \v couldn't be added.

  ////////////////////////////////////////////////
  // Constroctor
  ////////////////////////////////////////////////

  public SearchCriteria() {
    setProperty("");
    setOperation("");
    setValue("");
    setLogic("");
  }

  public SearchCriteria(SearchCriteria searchCri) {
    setProperty(searchCri.getProperty());
    setOperation(searchCri.getOperation());
    setValue(searchCri.getValue());
    setLogic(searchCri.getLogic());
    setResult(searchCri.getResult());
  }

  ////////////////////////////////////////////////
  // property
  ////////////////////////////////////////////////

  private String property;

  public void setProperty(String val) {
    property = val;
  }

  public String getProperty() {
    return property;
  }

  ////////////////////////////////////////////////
  // property
  ////////////////////////////////////////////////

  private String operation;

  public void setOperation(String val) {
    operation = val;
  }

  public String getOperation() {
    return operation;
  }

  public boolean isEQ() {
    return (operation.compareTo(EQ) == 0) ? true : false;
  }

  public boolean isNEQ() {
    return (operation.compareTo(NEQ) == 0) ? true : false;
  }

  public boolean isLT() {
    return (operation.compareTo(LT) == 0) ? true : false;
  }

  public boolean isLE() {
    return (operation.compareTo(LE) == 0) ? true : false;
  }

  public boolean isGT() {
    return (operation.compareTo(GT) == 0) ? true : false;
  }

  public boolean isGE() {
    return (operation.compareTo(GE) == 0) ? true : false;
  }

  public boolean isContains() {
    return (operation.compareTo(CONTAINS) == 0) ? true : false;
  }

  public boolean isDoesNotContain() {
    return (operation.compareTo(DOESNOTCONTAIN) == 0) ? true : false;
  }

  public boolean isDerivedFrom() {
    return (operation.compareTo(DERIVEDFROM) == 0) ? true : false;
  }

  public boolean isExists() {
    return (operation.compareTo(EXISTS) == 0) ? true : false;
  }

  ////////////////////////////////////////////////
  // value
  ////////////////////////////////////////////////

  private String value;

  public void setValue(String val) {
    value = val;
  }

  public String getValue() {
    return value;
  }

  boolean isTrueValue() {
    return (value.compareTo(TRUE) == 0) ? true : false;
  }

  boolean isFalseValue() {
    return (value.compareTo(FALSE) == 0) ? true : false;
  }

  ////////////////////////////////////////////////
  // Logic
  ////////////////////////////////////////////////

  private String logic;

  public void setLogic(String val) {
    logic = val;
  }

  public String getLogic() {
    return logic;
  }

  public boolean isLogicalAND() {
    return (logic.compareTo(AND) == 0) ? true : false;
  }

  public boolean isLogicalOR() {
    return (logic.compareTo(OR) == 0) ? true : false;
  }

  ////////////////////////////////////////////////
  // Result
  ////////////////////////////////////////////////

  private boolean result;

  public void setResult(boolean value) {
    result = value;
  }

  public boolean getResult() {
    return result;
  }
}
