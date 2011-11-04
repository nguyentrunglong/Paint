/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.paint;

/**
 *
 * @author common
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author Timothy Boudreau
 */
public class PaintCanvas extends JComponent implements MouseListener, MouseMotionListener {

    public PaintCanvas() {
	addMouseListener (this);
	addMouseMotionListener (this);
	setBackground (Color.WHITE);
    }

    private int diam = 10;
    public void setBrush (int diam) {
	this.diam = diam;
    }

    public void setDiam (int val) {
	this.diam = val;
    }

    public int getDiam() {
	return diam;
    }

    private Paint paint = Color.BLUE;
    public void setPaint (Paint c) {
	this.paint = c;
    }

    public Paint getPaint() {
	return paint;
    }

    public Color getColor() {
	if (paint instanceof Color) {
	    return (Color) paint;
	} else {
	    return Color.BLACK;
	}
    }

    private BufferedImage backingImage = null;
    public void clear () {
	backingImage = null;
	repaint();
    }

    public BufferedImage getImage() {
	if (backingImage == null || backingImage.getWidth() != getWidth() || backingImage.getHeight() != getHeight()) {
	    BufferedImage old = backingImage;
	    backingImage = new BufferedImage (getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
	    Graphics g = backingImage.getGraphics();
	    g.setColor (Color.WHITE);
	    g.fillRect (0, 0, getWidth(), getHeight());
	    if (old != null) {
		((Graphics2D) backingImage.getGraphics()).drawRenderedImage(old,
			AffineTransform.getTranslateInstance(0, 0));
	    }
	}
	return backingImage;
    }

    public void paint (Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.drawRenderedImage(getImage(), AffineTransform.getTranslateInstance(0,0));
    }

    public void mouseClicked(MouseEvent e) {
	Point p = e.getPoint();
	int half = diam / 2;
	Graphics g = getImage().getGraphics();
	((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	((Graphics2D) g).setPaint (getPaint());
	g.fillOval(p.x - half, p.y - half, diam, diam);
	repaint (p.x - half, p.y - half, diam, diam);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
	mouseClicked (e);
    }

    public void mouseMoved(MouseEvent e) {
    }

    JComponent createBrushSizeView() {
        return new BrushSizeView();
    }


    private class BrushSizeView extends JComponent {

	public boolean isOpaque() {
	    return true;
	}

	public void paint (Graphics g) {
	    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setColor (getBackground());
	    g.fillRect (0,0,getWidth(),getHeight());
	    Point p = new Point (getWidth() / 2, getHeight() / 2);
	    int half = getDiam() / 2;
	    int diam = getDiam();
	    g.setColor (getColor());
	    g.fillOval(p.x - half, p.y - half, diam, diam);
	}

	public Dimension getPreferredSize() {
	    return new Dimension (32, 32);
	}

	public Dimension getMinimumSize() {
	    return getPreferredSize();
	}
    }
}