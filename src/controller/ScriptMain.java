package controller;

import controller.commands.ExtraCreditFactory;
import java.io.FileNotFoundException;
import model.application.ILayeredApplication;
import model.application.LIMEApplication;

/**
 * Represents the main() method initializing a controller which allows the user * to interact with a
 * multi-layered image-processing application by executing a script.
 */
public class ScriptMain {

  /**
   * Main method used to initialize program.
   *
   * @param args is not used
   */
  public static void main(String[] args) throws FileNotFoundException {
    ILayeredApplication app = new LIMEApplication();

    Controller c = new Controller(app, System.out);
    c.startApp("res/ScriptFile1.txt", new ExtraCreditFactory());
  }
}