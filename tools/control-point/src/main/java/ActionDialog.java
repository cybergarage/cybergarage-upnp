/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File : ActionDialog.java
 *
 ******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.cybergarage.upnp.*;
import org.cybergarage.upnp.Action;

public class ActionDialog extends JDialog implements ActionListener {
  private Action action;
  private JButton okButton;
  private JButton cancelButton;
  private boolean result;
  private ArgumentList inArgList;
  private Vector inArgFieldList;

  public ActionDialog(Frame frame, Action action) {
    super(frame, true);
    getContentPane().setLayout(new BorderLayout());

    this.action = action;

    inArgList = new ArgumentList();
    inArgFieldList = new Vector();

    JPanel argListPane = new JPanel();
    // argListPane.setLayout(new BoxLayout(argListPane,  BoxLayout.Y_AXIS));
    argListPane.setLayout(new GridLayout(0, 2));
    getContentPane().add(argListPane, BorderLayout.CENTER);

    ArgumentList argList = action.getArgumentList();
    int nArgs = argList.size();
    for (int n = 0; n < nArgs; n++) {
      Argument arg = argList.getArgument(n);
      if (arg.isInDirection() == false) continue;

      JLabel argLabel = new JLabel(arg.getName());
      JTextField argField = new JTextField();

      inArgFieldList.add(argField);
      argListPane.add(argLabel);
      argListPane.add(argField);

      Argument inArg = new Argument();
      inArg.setName(arg.getName());
      inArgList.add(inArg);
    }

    okButton = new JButton("OK");
    okButton.addActionListener(this);
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this);
    JPanel buttonPane = new JPanel();
    buttonPane.add(okButton);
    buttonPane.add(cancelButton);
    getContentPane().add(buttonPane, BorderLayout.SOUTH);

    pack();

    Dimension size = getSize();
    Point fpos = frame.getLocationOnScreen();
    Dimension fsize = frame.getSize();
    setLocation(fpos.x + (fsize.width - size.width) / 2, fpos.y + (fsize.height - size.height) / 2);
  }

  ////////////////////////////////////////////////
  //	actionPerformed
  ////////////////////////////////////////////////

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == okButton) {
      result = true;
      int fieldCnt = inArgFieldList.size();
      for (int n = 0; n < fieldCnt; n++) {
        JTextField field = (JTextField) inArgFieldList.get(n);
        String value = field.getText();
        Argument arg = inArgList.getArgument(n);
        arg.setValue(value);
      }
      action.setInArgumentValues(inArgList);
      dispose();
    }
    if (e.getSource() == cancelButton) {
      result = false;
      dispose();
    }
  }

  ////////////////////////////////////////////////
  //	actionPerformed
  ////////////////////////////////////////////////

  public boolean doModal() {
    show();
    return result;
  }
}
