/*
 * Copyright (c) 2013-2021, Jeremy Brooks
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.jeremybrooks.common.gui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.TooManyListenersException;


/**
 * <p>This class makes it easy to drag and drop files from the operating
 * system to a Java program. Any java.awt.Component can be
 * dropped onto, but only javax.swing.JComponents will indicate
 * the drop event with a changed border.</p>
 *
 * <p>To use this class, construct a new FileDrop by passing
 * it the target component and a Listener to receive notification
 * when file(s) have been dropped. Here is an example:</p>
 *
 * <pre>
 * {@code
 *      JPanel myPanel = new JPanel();
 *      new FileDrop( myPanel, new FileDrop.Listener()
 *      {   public void filesDropped( java.io.File[] files )
 *          {
 *              // handle file drop
 *              ...
 *          }   // end filesDropped
 *      }); // end FileDrop.Listener
 * }
 * </pre>
 * <p>You can specify the border that will appear when files are being dragged by
 * calling the constructor with a javax.swing.border.Border. Only
 * JComponents will show any indication with a border.</p>
 *
 * <p>You can turn on some debugging features by passing a PrintStream
 * object (such as System.out) into the full constructor. A null
 * value will result in no extra debugging information being output.</p>
 *
 * <p><em>Adapted from public domain code written by Robert Harder, rharder@usa.net</em></p>
 *
 * <p>Here is a simple test program that will show dropped files.</p>
 * <pre>
 *   {@code
 * public static void main(String[] args) {
 *   javax.swing.JFrame frame = new javax.swing.JFrame("FileDrop");
 *   final javax.swing.JTextArea text = new javax.swing.JTextArea();
 *   frame.getContentPane().add(
 *     new javax.swing.JScrollPane(text), java.awt.BorderLayout.CENTER);
 *
 *   new FileDrop(text, new FileDrop.Listener() {
 *     public void filesDropped(List<File> fileList) {
 *
 *       for (File file : fileList) {
 *         try {
 *           text.append(file.getCanonicalPath() + "\n");
 *         } catch (IOException ex) {
 *           ex.printStackTrace();
 *         }
 *       }
 *     }
 *   });
 *
 *   frame.setBounds(100, 100, 300, 400);
 *   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 *   frame.setVisible(true);
 *   }
 * }
 * </pre>
 */
public class FileDrop {

  private transient javax.swing.border.Border normalBorder;
  private transient java.awt.dnd.DropTargetListener dropListener;


  /* Discover if the running JVM is modern enough to have drag and drop. */
  private static Boolean supportsDnD;

  /* Default border color. */
  private static final Color defaultBorderColor = new Color(0f, 0f, 1f, 0.25f);

  private PrintStream printStream;

  /**
   * Constructs a {@link FileDrop} with a default light-blue border
   * and, if <var>c</var> is a {@link java.awt.Container}, recursively
   * sets all elements contained within as drop targets, though only
   * the top level container will change borders.
   *
   * @param c        Component on which files will be dropped.
   * @param listener Listens for filesDropped.
   * @since 1.0
   */
  public FileDrop(final Component c, final Listener listener) {
    this(c,
        BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor),
        true,
        listener,
        null);
  }


  /**
   * Constructor with a default border and the option to recursively set drop targets.
   * If your component is a java.awt.Container, then each of its children
   * components will also listen for drops, though only the parent will change borders.
   *
   * @param c         Component on which files will be dropped.
   * @param recursive Recursively set children as drop targets.
   * @param listener  Listens for filesDropped.
   * @since 1.0
   */
  public FileDrop(final Component c, final boolean recursive, final Listener listener) {
    this(c,
        BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor),
        recursive,
        listener,
        null);
  }


  /**
   * Constructor with a specified border.
   * If your component is a java.awt.Container, then each of its children
   * components will also listen for drops, though only the parent will change borders.
   *
   * @param c          Component on which files will be dropped.
   * @param dragBorder Border to use on JComponent when dragging occurs.
   * @param listener   Listens for filesDropped.
   * @since 1.0
   */
  public FileDrop(final Component c, final Border dragBorder, final Listener listener) {
    this(c,
        dragBorder,
        true,
        listener,
        null);
  }


  /**
   * Full constructor with a specified border.
   *
   * @param c          Component on which files will be dropped.
   * @param dragBorder Border to use on JComponent when dragging occurs.
   * @param recursive  Recursively set children as drop targets.
   * @param listener   Listens for filesDropped.
   *                   @param printStream an optional print stream for logging messages.
   * @since 1.0
   */
  public FileDrop(final Component c, final Border dragBorder, final boolean recursive, final Listener listener,
                  final PrintStream printStream) {
    if (supportsDnD()) {
      this.printStream = printStream;
      dropListener = new java.awt.dnd.DropTargetListener() {
        @Override
        public void dragEnter(java.awt.dnd.DropTargetDragEvent evt) {
          log("dragEnter event.", null);

          // Is this an acceptable drag event?
          if (isDragOk(evt)) {
            // If it's a Swing component, set its border
            if (c instanceof JComponent) {
              JComponent jc = (JComponent) c;
              normalBorder = jc.getBorder();
              log("normal border saved.", null);
              jc.setBorder(dragBorder);
              log("drag border set.", null);
            }

            // Acknowledge that it's okay to enter
            //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
            evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
            log("event accepted.", null);
          } else {   // Reject the drag event
            evt.rejectDrag();
            log("event rejected.", null);
          }   // end else: drag not ok
        }   // end dragEnter

        @Override
        public void dragOver(DropTargetDragEvent evt) {
          // This is called continually as long as the mouse is
          // over the drag target.
        }   // end dragOver

        @Override
        public void drop(DropTargetDropEvent evt) {
          log("drop event.", null);
          try {   // Get whatever was dropped
            Transferable tr = evt.getTransferable();

            // Is it a file list?
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
              // Say we'll take it.
              //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
              evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
              log("file list accepted.", null);

              if (listener != null) {
                // Pass the listener a List of File objects.
                // The javaFileListFlavor guarantees that the object is a List of File objects.
                listener.filesDropped((List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor));
              }

              // Mark that drop is completed.
              evt.getDropTargetContext().dropComplete(true);
              log("drop complete.", null);
            } else {
              log("Not a file list - abort.", null);
              evt.rejectDrop();
            }
          } catch (IOException | UnsupportedFlavorException ioe) {
            log("Aborting drag and drop.", ioe);
            evt.rejectDrop();
          } finally {
            // If it's a Swing component, reset its border
            if (c instanceof JComponent) {
              ((JComponent) c).setBorder(normalBorder);
              log("normal border restored.", null);
            }
          }
        }

        public void dragExit(DropTargetEvent evt) {
          log("dragExit event.", null);
          // If it's a Swing component, reset its border
          if (c instanceof JComponent) {
            ((JComponent) c).setBorder(normalBorder);
            log("normal border restored.", null);
          }
        }

        public void dropActionChanged(DropTargetDragEvent evt) {
          log("dropActionChanged event.", null);
          // Is this an acceptable drag event?
          if (isDragOk(evt)) {   //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
            evt.acceptDrag(DnDConstants.ACTION_COPY);
            log("event accepted.", null);
          } else {
            evt.rejectDrag();
            log("event rejected.", null);
          }
        }
      };

      // Make the component (and possibly children) drop targets
      makeDropTarget(c, recursive);
    } else {
      log("Drag and drop is not supported with this JVM.", null);
    }
  }

  private void log(String message, Exception exception) {
    if (this.printStream != null && message != null) {
      try {
        this.printStream.println(message);
        if (exception != null) {
          exception.printStackTrace(this.printStream);
        }
        this.printStream.flush();
      } catch (Exception e) {
        System.err.println("Error writing message '" + message + "; to printStream.");
        e.printStackTrace();
      }
    }
  }

  private static boolean supportsDnD() {
    if (supportsDnD == null) {
      try {
        Class.forName("java.awt.dnd.DnDConstants");
        supportsDnD = true;
      } catch (Exception e) {
        supportsDnD = false;
      }
    }
    return supportsDnD;
  }


  private void makeDropTarget(final Component c, boolean recursive) {
    // Make drop target
    final DropTarget dt = new DropTarget();
    try {
      dt.addDropTargetListener(dropListener);
    } catch (TooManyListenersException tmle) {
      log("Drop will not work due to previous error. Do you have another listener attached?", tmle);
    }

    // Listen for hierarchy changes and remove the drop target when the parent gets cleared out.
    c.addHierarchyListener(new HierarchyListener() {
      public void hierarchyChanged(HierarchyEvent evt) {
        log("Hierarchy changed.", null);
        Component parent = c.getParent();
        if (parent == null) {
          c.setDropTarget(null);
          log("Drop target cleared from component.", null);
        } else {
          new DropTarget(c, dropListener);
          log("Drop target added to component.", null);
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
    if (this.printStream != null) {
      if (flavors.length == 0) {
        log("No data flavors.", null);
      } else {
        for (DataFlavor flavor : flavors) {
          log(flavor.toString(), null);
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
   * @return true if the hooks are removed successfully.
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
   * @return true if the hooks are removed successfully.
   * @since 1.0
   */
  public static boolean remove(Component c, boolean recursive) {
    boolean result;

    if (supportsDnD()) {
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

	/* ********  I N N E R   I N T E R F A C E   L I S T E N E R  ******** */
  /**
   * Implement this inner interface to listen for when files are dropped. For example
   * your class declaration may begin like this:
   * <pre>
   * {@code
   *      public class MyClass implements FileDrop.Listener
   *      ...
   *      public void filesDropped( List<File> fileList )
   *      {
   *          ...
   *      }   // end filesDropped
   *      ...
   * }
   * </pre>
   *
   * @since 1.0
   */
  public interface Listener {
    /**
     * This method is called when files have been successfully dropped.
     *
     * @param fileList A list of Files that were dropped.
     * @since 1.0
     */
    void filesDropped(List<File> fileList);
  }
}