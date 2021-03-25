package de.cas.experts.software.maze.app;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

class ImagePanel extends JLabel {

	private static final long serialVersionUID = 95929926672038540L;
	
	private ScaleParameters scaleParameters = null;
	
	private List<ImageMouseEventListener> imageClickListeners = new ArrayList<>();
	
	ImagePanel() {
		super();
		enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}
	
	@Override
	protected void processMouseEvent(MouseEvent e) {
		ImageIcon icon = (ImageIcon) getIcon();
		if(icon != null && scaleParameters != null) {
			Point point = new Point(e.getX(), e.getY());
			Optional<Point> imagePixelCoordinates = scaleParameters.canvasToImageCoordinates(point);
			if(imagePixelCoordinates.isPresent()) {
				imageClickListeners.forEach(listener -> listener.mouseEventOccurred(new ImageMouseEvent(ImagePanel.this, imagePixelCoordinates.get(), e)));
			}
		}
	}
	
	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		processMouseEvent(e);
	}
	
	public void addImageClickListener(ImageMouseEventListener listener) {
		imageClickListeners.add(listener);
	}

	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = (ImageIcon) getIcon();
		if (icon != null) {
			drawScaledImage(icon.getImage(), this, g);
		}
	}

	private void drawScaledImage(Image image, Component canvas, Graphics g) {
		scaleParameters = new ScaleParameters(image.getWidth(null), image.getHeight(null), canvas.getWidth(), canvas.getHeight());
		
		g.drawImage(image, scaleParameters.getTopLeftX(), scaleParameters.getTopLeftY(), scaleParameters.getBottomRightX(), scaleParameters.getBottomRightY(), 0, 0, image.getWidth(null),
				image.getHeight(null), null);
	}
	
	public interface ImageMouseEventListener extends EventListener {
		void mouseEventOccurred(ImageMouseEvent event);
	}
	
	public static class ImageMouseEvent extends EventObject {
		private static final long serialVersionUID = 563693651961047148L;

		private final Point imagePoint;
		
		private final MouseEvent nativeEvent;
		
		private ImageMouseEvent(Object source, Point imagePoint, MouseEvent nativeEvent) {
			super(source);
			this.imagePoint = imagePoint;
			this.nativeEvent = nativeEvent;
		}

		public Point getImagePoint() {
			return imagePoint;
		}

		public MouseEvent getNativeEvent() {
			return nativeEvent;
		}
	}
	
	class AnswerEvent extends EventObject {
		private static final long serialVersionUID = -564171116815801359L;

		public static final int YES = 0, NO = 1, CANCEL = 2; // Button constants

		  protected int id; // Which button was pressed?

		  public AnswerEvent(Object source, int id) {
		    super(source);
		    this.id = id;
		  }

		  public int getID() {
		    return id;
		  } // Return the button
		}

	private static class ScaleParameters {

		private Point topLeft;
		private Point bottomRight;
		
		private Dimension imageDimension;

		public ScaleParameters(int imageWidth, int imageHeight, int canvasWidth, int canvasHeight) {
			topLeft = new Point(0, 0);
			imageDimension = new Dimension(imageWidth, imageHeight);
			final double aspectRatio = (double) imageHeight / imageWidth;

			final double canvasAspectRatio = (double) canvasHeight / canvasWidth;

			if (imageWidth < canvasWidth && imageHeight < canvasHeight) {
				if (canvasAspectRatio > aspectRatio) {
					imageWidth = canvasWidth;
				} else {
					imageHeight = canvasHeight;
				}
			}
			if (canvasAspectRatio > aspectRatio) {
				imageHeight = (int) (canvasWidth * aspectRatio);
				topLeft.y = (canvasHeight - imageHeight) / 2;
			} else {
				imageWidth = (int) (canvasHeight / aspectRatio);
				topLeft.x = (canvasWidth - imageWidth) / 2;
			}

			bottomRight = new Point(topLeft.x + imageWidth, topLeft.y + imageHeight);
		}
		
		
		public Optional<Point> canvasToImageCoordinates(Point canvasPoint) {
			int x = (int)(((double)(canvasPoint.x - topLeft.x)) / (bottomRight.x - topLeft.x) * imageDimension.width);
			int y = (int)(((double)(canvasPoint.y - topLeft.y)) / (bottomRight.y - topLeft.y) * imageDimension.height);
			if (0 <= x && x < imageDimension.width && 0 <= y && y < imageDimension.height) {
				return Optional.of(new Point(x, y));
			} else {
				return Optional.empty();
			}
		}
		

		public int getTopLeftX() {
			return topLeft.x;
		}

		public int getTopLeftY() {
			return topLeft.y;
		}

		public int getBottomRightX() {
			return bottomRight.x;
		}

		public int getBottomRightY() {
			return bottomRight.y;
		}
	}
}

