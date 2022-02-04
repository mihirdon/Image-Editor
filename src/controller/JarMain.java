package controller;

import controller.commands.ExtraCreditFactory;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import model.application.ILayeredApplication;
import model.application.LIMEApplication;
import view.GUIView;

/**
 * Represents the main() method used by this program's Jar file, initializing a controller which
 * allows the user to interact with a multi-layered image-processing application.
 */
public class JarMain {

  /**
   * Main method used to initialize program.
   *
   * @param args command line inputs determining how the program is run: "-text": runs the program
   *             with the text view and console "-script FilePath": runs the program by running the
   *             specified script "-interactive": runs the program with the GUI view
   */
  public static void main(String[] args) throws FileNotFoundException {
    ILayeredApplication app = new LIMEApplication();

    switch (args[0]) {
      case "-text": {
        Controller c = new Controller(app, System.out);
        c.startApp(new InputStreamReader(System.in), new ExtraCreditFactory());
        break;
      }
      case "-script": {
        Controller c = new Controller(app, System.out);
        c.startApp(args[1], new ExtraCreditFactory());
        break;
      }
      case "-interactive": {
        GUIView.setDefaultLookAndFeelDecorated(false);
        GUIView frame = new GUIView();
        new GUIController(app, frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        break;
      }
      default:
        throw new IllegalArgumentException("Missing or invalid command line inputs");
    }
  }
}