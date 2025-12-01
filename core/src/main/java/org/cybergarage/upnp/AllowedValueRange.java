/******************************************************************
 *
 *	CyberLink for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2004
 *
 *	File: AllowedValueRange.java
 *
 *	Revision:
 *
 *	03/27/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import org.cybergarage.xml.Node;

/**
 * Represents an allowed value range for a UPnP state variable.
 *
 * <p>This class defines a numeric range constraint for state variables, including minimum, maximum,
 * and optional step values. It is used when a state variable can take any numeric value within a
 * specified range.
 *
 * @see StateVariable
 * @see AllowedValue
 */
public class AllowedValueRange {
  ////////////////////////////////////////////////
  //	Constants
  ////////////////////////////////////////////////

  /** The XML element name for allowed value range nodes in SCPD documents. */
  public static final String ELEM_NAME = "allowedValueRange";

  ////////////////////////////////////////////////
  //	Member
  ////////////////////////////////////////////////

  private Node allowedValueRangeNode;

  /**
   * Returns the underlying XML node representing this allowed value range.
   *
   * @return the allowed value range XML node
   */
  public Node getAllowedValueRangeNode() {
    return allowedValueRangeNode;
  }

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /**
   * Constructs an allowed value range from an existing XML node.
   *
   * @param node the XML node representing the allowed value range
   */
  public AllowedValueRange(Node node) {
    allowedValueRangeNode = node;
  }

  /** Constructs an empty allowed value range. */
  public AllowedValueRange() {
    // TODO Test
    allowedValueRangeNode = new Node(ELEM_NAME);
  }

  ////////////////////////////////////////////////
  //	isAllowedValueRangeNode
  ////////////////////////////////////////////////

  /**
   * Constructs an allowed value range with specified bounds and step.
   *
   * @param max the maximum allowed value, or {@code null} to omit
   * @param min the minimum allowed value, or {@code null} to omit
   * @param step the step increment, or {@code null} to omit
   */
  public AllowedValueRange(Number max, Number min, Number step) {
    // TODO Test
    allowedValueRangeNode = new Node(ELEM_NAME);
    if (max != null) setMaximum(max.toString());
    if (min != null) setMinimum(min.toString());
    if (step != null) setStep(step.toString());
  }

  /**
   * Checks if the given XML node represents an allowed value range element.
   *
   * @param node the XML node to check
   * @return {@code true} if the node's name matches the allowed value range element name, {@code
   *     false} otherwise
   */
  public static boolean isAllowedValueRangeNode(Node node) {
    return ELEM_NAME.equals(node.getName());
  }

  ////////////////////////////////////////////////
  //	minimum
  ////////////////////////////////////////////////

  private static final String MINIMUM = "minimum";

  /**
   * Sets the minimum allowed value.
   *
   * @param value the minimum value as a string
   */
  public void setMinimum(String value) {
    getAllowedValueRangeNode().setNode(MINIMUM, value);
  }

  /**
   * Returns the minimum allowed value.
   *
   * @return the minimum value as a string
   */
  public String getMinimum() {
    return getAllowedValueRangeNode().getNodeValue(MINIMUM);
  }

  ////////////////////////////////////////////////
  //	maximum
  ////////////////////////////////////////////////

  private static final String MAXIMUM = "maximum";

  /**
   * Sets the maximum allowed value.
   *
   * @param value the maximum value as a string
   */
  public void setMaximum(String value) {
    getAllowedValueRangeNode().setNode(MAXIMUM, value);
  }

  /**
   * Returns the maximum allowed value.
   *
   * @return the maximum value as a string
   */
  public String getMaximum() {
    return getAllowedValueRangeNode().getNodeValue(MAXIMUM);
  }

  ////////////////////////////////////////////////
  //	width
  ////////////////////////////////////////////////

  private static final String STEP = "step";

  /**
   * Sets the step increment for the range.
   *
   * <p>The step defines the granularity of allowed values within the range.
   *
   * @param value the step increment as a string
   */
  public void setStep(String value) {
    getAllowedValueRangeNode().setNode(STEP, value);
  }

  /**
   * Returns the step increment for the range.
   *
   * @return the step increment as a string
   */
  public String getStep() {
    return getAllowedValueRangeNode().getNodeValue(STEP);
  }
}
