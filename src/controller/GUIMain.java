package controller;

import javax.swing.JFrame;
import model.application.ILayeredApplication;
import model.application.LIMEApplication;
import view.GUIView;

/**
 * Represents the main() method initializing a controller which allows the user * to interact with a
 * multi-layered image-processing application through a GUI.
 */
public class GUIMain {

  /**
   * Main method used to initialize program.
   *
   * @param args is not used
   */
  public static void main(String[] args) {
    ILayeredApplication app = new LIMEApplication();

    GUIView.setDefaultLookAndFeelDecorated(false);
    GUIView frame = new GUIView();
    new GUIController(app, frame);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}