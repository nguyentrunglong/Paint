/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.paint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.java.dev.colorchooser.ColorChooser;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author common
 */
public class PaintTopComponent extends TopComponent implements ActionListener, ChangeListener {

    private PaintCanvas canvas = new PaintCanvas(); //The component the user draws on
    private JComponent preview; //A component in the toolbar that shows the paintbrush size
    private static int ct = 0; //A counter you use to provide names for new images

    public PaintTopComponent() {

        initComponents();

        String displayName = NbBundle.getMessage(
                PaintTopComponent.class,
                "UnsavedImageNameFormat",
                new Object[]{new Integer(ct++)});

        setDisplayName(displayName);
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

    @Override
    public String preferredID() {
        return "Image";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
           canvas.clear();
        } else if (e.getSource() instanceof ColorChooser) {
           ColorChooser cc = (ColorChooser) e.getSource();
           canvas.setPaint (cc.getColor());
        }

        preview.paintImmediately(0, 0, preview.getWidth(), preview.getHeight());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider js = (JSlider) e.getSource();
        canvas.setDiam (js.getValue());
        preview.paintImmediately(0, 0, preview.getWidth(), preview.getHeight());
        
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JToolBar bar = new JToolBar();

        ColorChooser fg = new ColorChooser();
        preview = canvas.createBrushSizeView();

        //Now build our toolbar:

        //Make sure components don't get squished:
        Dimension min = new Dimension(32, 32);
        preview.setMaximumSize(min);
        fg.setPreferredSize(new Dimension(16, 16));
        fg.setMinimumSize(min);
        fg.setMaximumSize(min);

        JButton clear = new JButton(
                NbBundle.getMessage(PaintTopComponent.class, "LBL_Clear"));

        JLabel fore = new JLabel(
                NbBundle.getMessage(PaintTopComponent.class, "LBL_Foreground"));

        fg.addActionListener(this);
        clear.addActionListener(this);

        JSlider js = new JSlider();
        js.setMinimum(1);
        js.setMaximum(24);
        js.setValue(canvas.getDiam());
        js.addChangeListener(this);

        fg.setColor(canvas.getColor());

        bar.add(clear);
        bar.add(fore);
        bar.add(fg);
        JLabel bsize = new JLabel(
                NbBundle.getMessage(PaintTopComponent.class, "LBL_BrushSize"));

        bar.add(bsize);
        bar.add(js);
        bar.add(preview);

        JLabel spacer = new JLabel("   "); //Just a spacer so the brush preview
        //isn't stretched to the end of the
        //toolbar

        spacer.setPreferredSize(new Dimension(400, 24));
        bar.add(spacer);

        //And install the toolbar and the painting component:
        add(bar, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

    }

     public void save() throws IOException {

        if (getDisplayName().endsWith(".png")) {
	    doSave(new File(getDisplayName()));
        } else {
	    saveAs();
        }

    }

    public void saveAs() throws IOException {

        JFileChooser ch = new JFileChooser();
        if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION && ch.getSelectedFile() != null) {

	    File f = ch.getSelectedFile();

	    if (!f.getPath().endsWith(".png")) {
	        f = new File(f.getPath() + ".png");
	    }

	    if (!f.exists()) {

	        if (!f.createNewFile()) {
		    String failMsg = NbBundle.getMessage(
		             PaintTopComponent.class,
			    "MSG_SaveFailed", new Object[] { f.getPath() }
	            );
		    JOptionPane.showMessageDialog(this, failMsg);
		    return;
	        }

	    } else {
	        String overwriteMsg = NbBundle.getMessage(
		    PaintTopComponent.class,
                    "MSG_Overwrite", new Object[] { f.getPath() }
	        );

	        if (JOptionPane.showConfirmDialog(this, overwriteMsg)
	        != JOptionPane.OK_OPTION) {
		    return;
	        }

	    }

	    doSave(f);

        }

    }

    private void doSave(File f) throws IOException {

        BufferedImage img = canvas.getImage();
        ImageIO.write(img, "png", f);
        String statusMsg = NbBundle.getMessage(PaintTopComponent.class,
            "MSG_Saved", new Object[] { f.getPath() });
        StatusDisplayer.getDefault().setStatusText(statusMsg);
        setDisplayName(f.getName());

    }
}
