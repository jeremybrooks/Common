package net.jeremybrooks.common.gui;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;


/**
 * This class makes it easy to drag and drop files from the operating
 * system to a Java program. Any <tt>java.awt.Component</tt> can be
 * dropped onto, but only <tt>javax.swing.JComponent</tt>s will indicate
 * the drop event with a changed border.
 * <p/>
 * To use this class, construct a new <tt>FileDrop</tt> by passing
 * it the target component and a <tt>Listener</tt> to receive notification
 * when file(s) have been dropped. Here is an example:
 * <p/>
 * <code><pre>
 *      JPanel myPanel = new JPanel();
 *      new FileDrop( myPanel, new FileDrop.Listener()
 *      {   public void filesDropped( java.io.File[] files )
 *          {
 *              // handle file drop
 *              ...
 *          }   // end filesDropped
 *      }); // end FileDrop.Listener
 * </pre></code>
 * <p/>
 * You can specify the border that will appear when files are being dragged by
 * calling the constructor with a <tt>javax.swing.border.Border</tt>. Only
 * <tt>JComponent</tt>s will show any indication with a border.
 * <p/>
 * You can turn on some debugging features by passing a <tt>PrintStream</tt>
 * object (such as <tt>System.out</tt>) into the full constructor. A <tt>null</tt>
 * value will result in no extra debugging information being output.
 * <p/>
 * <p/>
 * <p>I'm releasing this code into the Public Domain. Enjoy.
 * </p>
 * <p><em>Original author: Robert Harder, rharder@usa.net</em></p>
 *
 * @author Robert Harder
 * @author rharder@usa.net
 * @version 1.0
 */
public class FileDrop {

	/**
	 * Logging.
	 */
	private static Logger logger = Logger.getLogger(FileDrop.class);

	private transient javax.swing.border.Border normalBorder;
	private transient java.awt.dnd.DropTargetListener dropListener;


	/**
	 * Discover if the running JVM is modern enough to have drag and drop.
	 */
	private static Boolean supportsDnD;

	/**
	 * Default border color.
	 */
	private static Color defaultBorderColor = new Color(0f, 0f, 1f, 0.25f);


	/**
	 * Constructs a {@link FileDrop} with a default light-blue border
	 * and, if <var>c</var> is a {@link java.awt.Container}, recursively
	 * sets all elements contained within as drop targets, though only
	 * the top level container will change borders.
	 *
	 * @param c        Component on which files will be dropped.
	 * @param listener Listens for <tt>filesDropped</tt>.
	 * @since 1.0
	 */
	public FileDrop(final Component c, final Listener listener) {
		this(c,
				BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor),
				true,
				listener);
	}


	/**
	 * Constructor with a default border and the option to recursively set drop targets.
	 * If your component is a <tt>java.awt.Container</tt>, then each of its children
	 * components will also listen for drops, though only the parent will change borders.
	 *
	 * @param c         Component on which files will be dropped.
	 * @param recursive Recursively set children as drop targets.
	 * @param listener  Listens for <tt>filesDropped</tt>.
	 * @since 1.0
	 */
	public FileDrop(final Component c, final boolean recursive, final Listener listener) {
		this(c,
				BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor),
				recursive,
				listener);
	}


	/**
	 * Constructor with a specified border.
	 * If your component is a <tt>java.awt.Container</tt>, then each of its children
	 * components will also listen for drops, though only the parent will change borders.
	 *
	 * @param c          Component on which files will be dropped.
	 * @param dragBorder Border to use on <tt>JComponent</tt> when dragging occurs.
	 * @param listener   Listens for <tt>filesDropped</tt>.
	 * @since 1.0
	 */
	public FileDrop(final Component c, final Border dragBorder, final Listener listener) {
		this(c,
				dragBorder,
				true,
				listener);
	}   // end constructor


	/**
	 * Full constructor with a specified border.
	 *
	 * @param c          Component on which files will be dropped.
	 * @param dragBorder Border to use on <tt>JComponent</tt> when dragging occurs.
	 * @param recursive  Recursively set children as drop targets.
	 * @param listener   Listens for <tt>filesDropped</tt>.
	 * @since 1.0
	 */
	public FileDrop(final Component c, final Border dragBorder, final boolean recursive, final Listener listener) {
		if (supportsDnD()) {
			dropListener = new java.awt.dnd.DropTargetListener() {
				@Override
				public void dragEnter(java.awt.dnd.DropTargetDragEvent evt) {
					logger.debug("dragEnter event.");

					// Is this an acceptable drag event?
					if (isDragOk(evt)) {
						// If it's a Swing component, set its border
						if (c instanceof JComponent) {
							JComponent jc = (JComponent) c;
							normalBorder = jc.getBorder();
							logger.debug("normal border saved.");
							jc.setBorder(dragBorder);
							logger.debug("drag border set.");
						}

						// Acknowledge that it's okay to enter
						//evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
						evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
						logger.debug("event accepted.");
					} else {   // Reject the drag event
						evt.rejectDrag();
						logger.debug("event rejected.");
					}   // end else: drag not ok
				}   // end dragEnter

				@Override
				public void dragOver(DropTargetDragEvent evt) {
					// This is called continually as long as the mouse is
					// over the drag target.
				}   // end dragOver

				@Override
				public void drop(DropTargetDropEvent evt) {
					logger.debug("drop event.");
					try {   // Get whatever was dropped
						Transferable tr = evt.getTransferable();

						// Is it a file list?
						if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
							// Say we'll take it.
							//evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
							evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
							logger.debug("file list accepted.");

							if (listener != null) {
								// Pass the listener a List of File objects.
								// The javaFileListFlavor guarantees that the object is a List of File objects.
								listener.filesDropped((List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor));
							}

							// Mark that drop is completed.
							evt.getDropTargetContext().dropComplete(true);
							logger.debug("drop complete.");
						} else {
							logger.debug("Not a file list - abort.");
							evt.rejectDrop();
						}
					} catch (IOException ioe) {
						logger.error("Aborting drag and drop.", ioe);
						evt.rejectDrop();
					} catch (UnsupportedFlavorException ufe) {
						logger.error("Aborting drag and drop.", ufe);
						evt.rejectDrop();
					} finally {
						// If it's a Swing component, reset its border
						if (c instanceof JComponent) {
							((JComponent) c).setBorder(normalBorder);
							logger.debug("normal border restored.");
						}
					}
				}

				public void dragExit(DropTargetEvent evt) {
					logger.debug("dragExit event.");
					// If it's a Swing component, reset its border
					if (c instanceof JComponent) {
						((JComponent) c).setBorder(normalBorder);
						logger.debug("normal border restored.");
					}
				}

				public void dropActionChanged(DropTargetDragEvent evt) {
					logger.debug("dropActionChanged event.");
					// Is this an acceptable drag event?
					if (isDragOk(evt)) {   //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
						evt.acceptDrag(DnDConstants.ACTION_COPY);
						logger.debug("event accepted.");
					} else {
						evt.rejectDrag();
						logger.debug("event rejected.");
					}
				}
			};

			// Make the component (and possibly children) drop targets
			makeDropTarget(c, recursive);
		} else {
			logger.debug("Drag and drop is not supported with this JVM.");
		}
	}


	private static boolean supportsDnD() {
		if (supportsDnD == null) {
			boolean support = false;
			try {
				Class arbitraryDndClass = Class.forName("java.awt.dnd.DnDConstants");
				support = true;
			} catch (Exception e) {
				support = false;
			}

			supportsDnD = new Boolean(support);
		}
		System.out.println(supportsDnD.booleanValue());
		return supportsDnD;
	}


	private void makeDropTarget(final Component c, boolean recursive) {
		// Make drop target
		final DropTarget dt = new DropTarget();
		try {
			dt.addDropTargetListener(dropListener);
		} catch (TooManyListenersException tmle) {
			logger.error("Drop will not work due to previous error. Do you have another listener attached?", tmle);
		}

		// Listen for hierarchy changes and remove the drop target when the parent gets cleared out.
		c.addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent evt) {
				logger.debug("Hierarchy changed.");
				Component parent = c.getParent();
				if (parent == null) {
					c.setDropTarget(null);
					logger.debug("Drop target cleared from component.");
				} else {
					new DropTarget(c, dropListener);
					logger.debug("Drop target added to component.");
				}
			}
		});

		if (c.getParent() != null) {
			new DropTarget(c, dropListener);
		}

		if (recursive && (c instanceof Container)) {
			// Get it's components
			Component[] comps = ((Container) c).getComponents();

			// Set it's components as listeners also
			for (Component child : comps) {
				makeDropTarget(child, recursive);
			}
		}
	}


	/**
	 * Determine if the dragged data is a file list.
	 */
	private boolean isDragOk(final DropTargetDragEvent evt) {
		boolean ok = false;
		int i = 0;

		// Get data flavors being dragged
		DataFlavor[] flavors = evt.getCurrentDataFlavors();

		// See if any of the flavors are a file list
		while (!ok && i < flavors.length) {
			if (flavors[i].equals(DataFlavor.javaFileListFlavor)) {
				ok = true;
			}
			i++;
		}

		// If debug logging is enabled, show data flavors
		if (logger.isDebugEnabled()) {
			if (flavors.length == 0) {
				logger.debug("No data flavors.");
			} else {
				for (DataFlavor flavor : flavors) {
					logger.debug(flavor);
				}
			}
		}

		return ok;
	}


	/**
	 * Removes the drag-and-drop hooks from the component and optionally
	 * from the all children. You should call this if you add and remove
	 * components after you've set up the drag-and-drop.
	 * This will recursively unregister all components contained within
	 * <var>c</var> if <var>c</var> is a {@link java.awt.Container}.
	 *
	 * @param c The component to unregister as a drop target
	 * @since 1.0
	 */
	public static boolean remove(Component c) {
		return remove(c, true);
	}


	/**
	 * Removes the drag-and-drop hooks from the component and optionally
	 * from the all children. You should call this if you add and remove
	 * components after you've set up the drag-and-drop.
	 *
	 * @param c         The component to unregister
	 * @param recursive Recursively unregister components within a container
	 * @since 1.0
	 */
	public static boolean remove(Component c, boolean recursive) {
		boolean result;

		if (supportsDnD()) {
			logger.debug("Removing drag-and-drop hooks.");
			c.setDropTarget(null);
			if (recursive && (c instanceof Container)) {
				Component[] comps = ((Container) c).getComponents();
				for (Component child : comps) {
					remove(child, recursive);
				}
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}

		return result;
	}


	/**
	 * Runs a sample program that shows dropped files
	 */
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame("FileDrop");
		//javax.swing.border.TitledBorder dragBorder = new javax.swing.border.TitledBorder( "Drop 'em" );
		final javax.swing.JTextArea text = new javax.swing.JTextArea();
		frame.getContentPane().add(
				new javax.swing.JScrollPane(text),
				java.awt.BorderLayout.CENTER);

		new FileDrop(text, /*dragBorder,*/ new FileDrop.Listener() {
			public void filesDropped(List<File> fileList) {

				for (File file : fileList) {
					try {
						text.append(file.getCanonicalPath() + "\n");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		frame.setBounds(100, 100, 300, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}


	/* ********  I N N E R   I N T E R F A C E   L I S T E N E R  ******** */


	/**
	 * Implement this inner interface to listen for when files are dropped. For example
	 * your class declaration may begin like this:
	 * <code><pre>
	 *      public class MyClass implements FileDrop.Listener
	 *      ...
	 *      public void filesDropped( List<File> fileList )
	 *      {
	 *          ...
	 *      }   // end filesDropped
	 *      ...
	 * </pre></code>
	 *
	 * @since 1.0
	 */
	public interface Listener {
		/**
		 * This method is called when files have been successfully dropped.
		 *
		 * @param fileList A list of <tt>File</tt>s that were dropped.
		 * @since 1.0
		 */
		public abstract void filesDropped(List<File> fileList);
	}   // end inner-interface Listener

}   // end class FileDrop