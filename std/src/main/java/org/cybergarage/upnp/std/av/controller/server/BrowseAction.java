/******************************************************************
 *
 *	MediaPlayer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2005
 *
 *	File : BrowseAction.java
 *
 *	09/26/05
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.std.av.controller.server;

import org.cybergarage.upnp.*;

public class BrowseAction {
  ////////////////////////////////////////////////
  // Constants
  ////////////////////////////////////////////////

  public static final String OBJECT_ID = "ObjectID";
  public static final String BROWSE_FLAG = "BrowseFlag";
  public static final String FILTER = "Filter";
  public static final String STARTING_INDEX = "StartingIndex";
  public static final String REQUESTED_COUNT = "RequestedCount";
  public static final String SORT_CRITERIA = "SortCriteria";

  public static final String BROWSE_METADATA = "BrowseMetadata";
  public static final String BROWSE_DIRECT_CHILDREN = "BrowseDirectChildren";

  public static final String RESULT = "Result";
  public static final String NUMBER_RETURNED = "NumberReturned";
  public static final String TOTAL_MACHES = "TotalMatches";
  public static final String UPDATE_ID = "UpdateID";

  ////////////////////////////////////////////////
  // Member
  ////////////////////////////////////////////////

  private Action action;

  ////////////////////////////////////////////////
  // Constrictor
  ////////////////////////////////////////////////

  public BrowseAction(Action action) {
    this.action = action;
  }

  ////////////////////////////////////////////////
  // getArgument
  ////////////////////////////////////////////////

  public Argument getArgument(String name) {
    return action.getArgument(name);
  }

  ////////////////////////////////////////////////
  // Request
  ////////////////////////////////////////////////

  public String getBrowseFlag() {
    return action.getArgumentValue(BROWSE_FLAG);
  }

  public boolean isMetadata() {
    return BROWSE_METADATA.equals(getBrowseFlag());
  }

  public boolean isDirectChildren() {
    return BROWSE_DIRECT_CHILDREN.equals(getBrowseFlag());
  }

  public String getObjectID() {
    return action.getArgumentValue(OBJECT_ID);
  }

  public int getStartingIndex() {
    return action.getArgumentIntegerValue(STARTING_INDEX);
  }

  public int getRequestedCount() {
    return action.getArgumentIntegerValue(REQUESTED_COUNT);
  }

  public String getSortCriteria() {
    return action.getArgumentValue(SORT_CRITERIA);
  }

  public String getFilter() {
    return action.getArgumentValue(FILTER);
  }

  ////////////////////////////////////////////////
  // Request
  ////////////////////////////////////////////////

  public void setBrowseFlag(String browseFlag) {
    action.setArgumentValue(BROWSE_FLAG, browseFlag);
  }

  public void setObjectID(String objectID) {
    action.setArgumentValue(OBJECT_ID, objectID);
  }

  public void setStartingIndex(int idx) {
    action.setArgumentValue(STARTING_INDEX, idx);
  }

  public void setRequestedCount(int count) {
    action.setArgumentValue(REQUESTED_COUNT, count);
  }

  public void setFilter(String filter) {
    action.setArgumentValue(FILTER, filter);
  }

  public void setSortCriteria(String sortCaiteria) {
    action.setArgumentValue(SORT_CRITERIA, sortCaiteria);
  }

  ////////////////////////////////////////////////
  // Result
  ////////////////////////////////////////////////

  public void setResult(String value) {
    action.setArgumentValue(RESULT, value);
  }

  public void setNumberReturned(int value) {
    action.setArgumentValue(NUMBER_RETURNED, value);
  }

  public void setTotalMaches(int value) {
    action.setArgumentValue(TOTAL_MACHES, value);
  }

  public void setUpdateID(int value) {
    action.setArgumentValue(UPDATE_ID, value);
  }

  ////////////////////////////////////////////////
  // post
  ////////////////////////////////////////////////

  public boolean postControlAction() {
    return action.postControlAction();
  }
}
