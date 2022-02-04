package controller;

import controller.commands.IFactory;
import java.io.FileNotFoundException;

/**
 * Represents the image-processing application controller which serves to perform operations
 * supported by the model based on a script or interactive inputs.
 */
public interface IController {

  /**
   * Runs the application by executing the script specified by the given filepath.
   *
   * @param filename the file path of the script file
   * @param factory  the factory to be used to parse commands to
   * @throws FileNotFoundException    if the file path does not lead to a script file
   * @throws IllegalArgumentException filename is null or empty
   */
  void startApp(String filename, IFactory factory)
      throws FileNotFoundException, IllegalArgumentException;

  /**
   * Runs the application with a text view and allowing the user to type commands.
   *
   * @param filename the file path of the script file
   * @throws IllegalArgumentException filename is null or empty
   */
  void startApp(Readable filename, IFactory factory) throws IllegalArgumentException;
}