/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003-2004
 *
 *	File : SearchAction
 *
 *	Revision:
 *
 *	08/16/04
 *		- Changed getObjectID() to return the string value.
 *
 ******************************************************************/

package org.cybergarage.upnp.std.av.server.action;

import org.cybergarage.upnp.*;

public class SearchAction extends Action {
  ////////////////////////////////////////////////
  // Constants
  ////////////////////////////////////////////////

  public static final String CONTAINER_ID = "ContainerID";
  public static final String SEARCH_CRITERIA = "SearchCriteria";
  public static final String FILTER = "Filter";
  public static final String STARTING_INDEX = "StartingIndex";
  public static final String REQUESTED_COUNT = "RequestedCount";
  public static final String SORT_CRITERIA = "SortCriteria";

  public static final String RESULT = "Result";
  public static final String NUMBER_RETURNED = "NumberReturned";
  public static final String TOTAL_MACHES = "TotalMatches";
  public static final String UPDATE_ID = "UpdateID";

  ////////////////////////////////////////////////
  // Constrictor
  ////////////////////////////////////////////////

  public SearchAction(Action action) {
    super(action);
  }

  ////////////////////////////////////////////////
  // Request
  ////////////////////////////////////////////////

  public String getContainerID() {
    return getArgumentValue(CONTAINER_ID);
  }

  public String getSearchCriteria() {
    return getArgumentValue(SEARCH_CRITERIA);
  }

  public int getStartingIndex() {
    return getArgumentIntegerValue(STARTING_INDEX);
  }

  public int getRequestedCount() {
    return getArgumentIntegerValue(REQUESTED_COUNT);
  }

  public String getSortCriteria() {
    return getArgumentValue(SORT_CRITERIA);
  }

  public String getFilter() {
    return getArgumentValue(FILTER);
  }

  ////////////////////////////////////////////////
  // Result
  ////////////////////////////////////////////////

  public void setResult(String value) {
    setArgumentValue(RESULT, value);
  }

  public void setNumberReturned(int value) {
    setArgumentValue(NUMBER_RETURNED, value);
  }

  public void setTotalMaches(int value) {
    setArgumentValue(TOTAL_MACHES, value);
  }

  public void setUpdateID(int value) {
    setArgumentValue(UPDATE_ID, value);
  }
}
