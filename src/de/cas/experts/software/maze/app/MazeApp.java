package de.cas.experts.software.maze.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.cas.experts.software.maze.Location;
import de.cas.experts.software.maze.MazeSolver;
import de.cas.experts.software.maze.app.ImagePanel.ImageMouseEvent;
import de.cas.experts.software.maze.app.ImagePanel.ImageMouseEventListener;

public class MazeApp extends JPanel implements ActionListener, ImageMouseEventListener {

	private static final long serialVersionUID = -235483830573698544L;
	private JButton loadButton;
	private JLabel statusBar;
	private JFileChooser fileChooser;
	private ImagePanel imagePanel;

	private BufferedImage image;
	private MazeSolver mazeSolver;

	private Point origin = null;
	private Point destination = null;
	
	Executor latestTaskExecutor = new ThreadPoolExecutor(1, 1, // Single threaded 
	        30L, TimeUnit.SECONDS, // Keep alive, not really important here
	        new ArrayBlockingQueue<>(1), // Single element queue
	        new ThreadPoolExecutor.DiscardOldestPolicy()); // When new work is submitted discard oldest

	public MazeApp() {
		super(new BorderLayout());

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
		fileChooser.setFileFilter(imageFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		loadButton = new JButton("Open image");
		loadButton.addActionListener(this);
		topPanel.add(loadButton);
		statusBar = new JLabel();
		topPanel.add(statusBar);

		imagePanel = new ImagePanel();
		imagePanel.setPreferredSize(new Dimension(400, 300));
		imagePanel.addImageClickListener(this);

		add(topPanel, BorderLayout.PAGE_START);
		add(imagePanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == loadButton) {
			int returnVal = fileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				initializeWithImage(file);
			}
		}
	}

	@Override
	public void mouseEventOccurred(ImageMouseEvent event) {
		MouseEvent mouseEvent = event.getNativeEvent();
		boolean isLeftMouseButton = SwingUtilities.isLeftMouseButton(mouseEvent);
		boolean isRightMouseButton = SwingUtilities.isRightMouseButton(mouseEvent);
		boolean isMouseButton = isLeftMouseButton || isRightMouseButton;
		if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED
				|| (mouseEvent.getID() == MouseEvent.MOUSE_DRAGGED && isMouseButton)) {
			boolean compute = false;
			if (isLeftMouseButton) {
				Point newOrigin = event.getImagePoint();
				if (!Objects.equals(origin, newOrigin)) {
					origin = newOrigin;
					compute = true;
				}
			} else {
				Point newDestination = event.getImagePoint();
				if (!Objects.equals(destination, newDestination)) {
					destination = newDestination;
					compute = true;
				}
			}
			if (origin != null && destination != null && compute) {
//				drawPath(computePath(toLocation(origin), toLocation(destination)));
				CompletableFuture//
						.supplyAsync(() -> computePath(toLocation(origin), toLocation(destination)), latestTaskExecutor)//
						.thenAccept(this::drawPath);
			}
		}
	}

	private List<Location> computePath(Location origin, Location destination) {
		statusBar.setText("Computing..");
		long startTime = System.currentTimeMillis();
		List<Location> result = mazeSolver.findPath(origin, destination);
		long duration = System.currentTimeMillis() - startTime;
		statusBar.setText("Finished computation in " + duration + " ms.");
		return result;
	}

	private void drawPath(List<Location> path) {
		BufferedImage imageWithPath = ImageOperations.addPathToImage(image, path);
		imagePanel.setIcon(new ImageIcon(imageWithPath));
	}

	private void initializeWithImage(File file) {
		try {
			image = ImageIO.read(file);
			imagePanel.setIcon(new ImageIcon(image));
			mazeSolver = new MazeSolver(image);
			origin = null;
			destination = null;
		} catch (IOException e) {
			System.out.println("Could not load file " + file.getName());
		}
	}

	private Location toLocation(Point point) {
		return new Location(point.y, point.x);
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("MazeApp");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new MazeApp());

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}
