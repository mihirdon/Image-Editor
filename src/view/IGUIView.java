package view;

import java.awt.Image;
import java.awt.event.ActionListener;

/**
 * Interface representing a {@code IApplicationView} which represents an application through a GUI.
 * Contains methods performing operations on the Java Swing components.
 */
public interface IGUIView extends IApplicationView {

  /**
   * Sets all the JFrame component's listener to the given action listener.
   *
   * @param listener the desired listener
   */
  void setActionListener(ActionListener listener);

  /**
   * Determines which text areas are displayed based on the currently-selected layer creation
   * method. These text areas are used to determine the inputs for layer creation.
   */
  void switchCurrentCreate();

  /**
   * Provides a {@code Readable} containing the "create layer" command with the relevant arguments
   * from the appropriate Java Swing components.
   *
   * @return the create layer command
   */
  Readable createLayer();

  /**
   * Provides a {@code Readable} containing one of the filter or color transformation operations.
   *
   * @return the operation command
   */
  Readable applyFilter();

  /**
   * Provides a {@code Readable} containing the "current" command with the relevant arguments from
   * the appropriate Java Swing components.
   *
   * @return the current layer command
   */
  Readable getCurrentLayer();

  /**
   * Uses the {@code JFileChooser} to select a jpeg, png, or ppm image to potentially load into the
   * current layer.
   */
  void openFiles();

  /**
   * Provides a {@code Readable} containing the "load" command with the relevant arguments from the
   * appropriate Java Swing components.
   *
   * @return the load layer command
   */
  Readable loadImage();

  /**
   * Displays the given {@code Image} to the GUI.
   *
   * @param bf the desired image to display
   */
  void displayImage(Image bf);

  /**
   * Provides a {@code Readable} containing the "save" or "save all" command with the relevant
   * arguments from the appropriate Java Swing components.
   *
   * @return the save or save all layer command
   */
  Readable openSaveFile();

  /**
   * Provides the filepath to a selected script file to execute in the GUI.
   *
   * @return the filepath of the script file.
   */
  String getScript();

  /**
   * Determines which text areas are displayed based on the currently-selected extra credit method.
   * These text areas are used to determine the inputs for downsize or mosaic.
   */
  void switchCurrentExtraCredit();

  /**
   * Provides a {@code Readable} containing the "mosaic" or "downsize" command with the relevant
   * arguments from the appropriate Java Swing components.
   *
   * @return the mosaic or downsize command
   */
  Readable applyExtraCredit();
}
