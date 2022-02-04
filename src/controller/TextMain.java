package controller;

import controller.commands.ExtraCreditFactory;
import java.io.InputStreamReader;
import model.application.ILayeredApplication;
import model.application.LIMEApplication;

/**
 * Represents the main() method initializing a controller which allows the user to interact with a
 * multi-layered image-processing application through a text view.
 */
public class TextMain {

  /**
   * Main method used to initialize program.
   *
   * @param args is not used
   */
  public static void main(String[] args) {
    ILayeredApplication app = new LIMEApplication();

    Controller c = new Controller(app, System.out);
    c.startApp(new InputStreamReader(System.in), new ExtraCreditFactory());
  }
}