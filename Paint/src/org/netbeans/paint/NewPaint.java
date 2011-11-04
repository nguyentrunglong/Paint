/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class NewPaint implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
         PaintTopComponent tc = new PaintTopComponent();
        tc.open();
        tc.requestActive();
    }
}
