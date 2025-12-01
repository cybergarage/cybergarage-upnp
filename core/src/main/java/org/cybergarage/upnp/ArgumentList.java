/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: ArgumentList.java
 *
 *	Revision:
 *
 *	12/05/02
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp;

import java.util.Vector;

/**
 * A list of {@link Argument} objects.
 *
 * <p>This class extends {@link Vector} to provide type-safe access to UPnP action arguments. It
 * includes methods for retrieving arguments by name or index, and for setting input/output argument
 * values.
 *
 * @see Argument
 * @see Action
 */
public class ArgumentList extends Vector {
  ////////////////////////////////////////////////
  //	Constants
  ////////////////////////////////////////////////

  /** The XML element name for argument list nodes in SCPD documents. */
  public static final String ELEM_NAME = "argumentList";

  ////////////////////////////////////////////////
  //	Constructor
  ////////////////////////////////////////////////

  /** Constructs an empty argument list. */
  public ArgumentList() {}

  ////////////////////////////////////////////////
  //	Methods
  ////////////////////////////////////////////////

  /**
   * Returns the argument at the specified index.
   *
   * @param n the index of the argument to retrieve
   * @return the argument at the specified index
   * @throws ArrayIndexOutOfBoundsException if the index is out of range
   */
  public Argument getArgument(int n) {
    return (Argument) get(n);
  }

  /**
   * Returns the argument with the specified name.
   *
   * @param name the name of the argument to retrieve
   * @return the argument with the specified name, or {@code null} if not found
   */
  public Argument getArgument(String name) {
    int nArgs = size();
    for (int n = 0; n < nArgs; n++) {
      Argument arg = getArgument(n);
      String argName = arg.getName();
      if (argName == null) continue;
      if (argName.equals(name) == true) return arg;
    }
    return null;
  }

  ////////////////////////////////////////////////
  //	Methods
  ////////////////////////////////////////////////
  /**
   * Sets argument values from the provided list.
   *
   * @param inArgList the argument list containing values to set
   * @deprecated Use {@link #setReqArgs(ArgumentList)} or {@link #setResArgs(ArgumentList)} instead
   */
  public void set(ArgumentList inArgList) {
    int nInArgs = inArgList.size();
    for (int n = 0; n < nInArgs; n++) {
      Argument inArg = inArgList.getArgument(n);
      String inArgName = inArg.getName();
      Argument arg = getArgument(inArgName);
      if (arg == null) continue;
      arg.setValue(inArg.getValue());
    }
  }

  /**
   * Sets values for all input arguments from the provided list.
   *
   * <p>Updates only arguments with direction "in" by matching names from the provided argument
   * list.
   *
   * @param inArgList the argument list containing input values
   * @throws IllegalArgumentException if a required input argument is missing from the provided list
   */
  public void setReqArgs(ArgumentList inArgList) {
    int nArgs = size();
    for (int n = 0; n < nArgs; n++) {
      Argument arg = getArgument(n);
      if (arg.isInDirection()) {
        String argName = arg.getName();
        Argument inArg = inArgList.getArgument(argName);
        if (inArg == null)
          throw new IllegalArgumentException("Argument \"" + argName + "\" missing.");
        arg.setValue(inArg.getValue());
      }
    }
  }

  /**
   * Sets values for all output arguments from the provided list.
   *
   * <p>Updates only arguments with direction "out" by matching names from the provided argument
   * list.
   *
   * @param outArgList the argument list containing output values
   * @throws IllegalArgumentException if a required output argument is missing from the provided
   *     list
   */
  public void setResArgs(ArgumentList outArgList) {
    int nArgs = size();
    for (int n = 0; n < nArgs; n++) {
      Argument arg = getArgument(n);
      if (arg.isOutDirection()) {
        String argName = arg.getName();
        Argument outArg = outArgList.getArgument(argName);
        if (outArg == null)
          throw new IllegalArgumentException("Argument \"" + argName + "\" missing.");
        arg.setValue(outArg.getValue());
      }
    }
  }
}
