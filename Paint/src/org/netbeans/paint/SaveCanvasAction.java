/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.paint;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import org.openide.ErrorManager;
import org.openide.util.HelpCtx;
import org.openide.util.WeakListeners;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;

public final class SaveCanvasAction extends CallableSystemAction implements ActionListener, PropertyChangeListener {

    public void actionPerformed(ActionEvent e) {
       TopComponent tc = TopComponent.getRegistry().getActivated();

        if (tc instanceof PaintTopComponent) {

            try {
                ((PaintTopComponent) tc).saveAs();
            } catch (IOException ioe) {
                ErrorManager.getDefault().notify(ioe);
            }

        } else {

            //Theoretically the active component could have changed
            //between the time the menu item or toolbar button was
            //pressed and when the action was invoked.  Not likely,
            //but theoretically possible
            Toolkit.getDefaultToolkit().beep();

        }
    }

    @Override
    public void performAction() {
    }

    @Override
    public String getName() {
        return "Save Canvas";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
       if (TopComponent.Registry.PROP_ACTIVATED.equals(evt.getPropertyName())){
	    updateEnablement();
        }
    }

    private void updateEnablement() {
          setEnabled(TopComponent.getRegistry().getActivated()
        instanceof PaintTopComponent);
    }

      public SaveCanvasAction() {

        TopComponent.getRegistry().addPropertyChangeListener (
	    WeakListeners.propertyChange(this,
	    TopComponent.getRegistry()));

        updateEnablement();

    }
}
