package controller;


import controller.commands.ICommand;
import controller.commands.IFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import model.application.ILayeredApplication;
import view.ApplicationView;
import view.IApplicationView;

/**
 * A controller for an image-processing application that deals with muti-layered images.
 */
public class Controller implements IController {

  private final ILayeredApplication app;
  private final IApplicationView view;

  /**
   * Creates a controller with a given view and application.
   *
   * @param app  the application model that recieves commands
   * @param view the visual to display the application
   * @throws IllegalArgumentException if either argument is null
   */
  public Controller(ILayeredApplication app, IApplicationView view)
      throws IllegalArgumentException {
    if (app == null || view == null) {
      throw new IllegalArgumentException("Null arguments are not allowed.");
    }

    this.app = app;
    this.view = view;
  }

  /**
   * Creates a controller which outputs any results to the provided appendable.
   *
   * @param app the model application to be used and send commands to
   * @param ap  the {@code Appendable} used by the view
   */
  public Controller(ILayeredApplication app, Appendable ap) {
    this(app, new ApplicationView(app, ap));
  }

  @Override
  public void startApp(String filename, IFactory factory)
      throws FileNotFoundException, IllegalArgumentException {
    if (filename == null || filename.isEmpty()) {
      throw new IllegalArgumentException("Filename is null and/or empty");
    }

    useApp(new Scanner(new FileInputStream(filename)), factory);
  }

  @Override
  public void startApp(Readable rd, IFactory factory) throws IllegalArgumentException {
    if (rd == null) {
      throw new IllegalArgumentException("Readable is null");
    }
    useApp(new Scanner(rd), factory);
  }

  /**
   * Uses the given scanner to read any commands and perform the appropriate operations, displaying
   * the results through the view.
   *
   * @param scan    the scanner reading inputs
   * @param factory the factory for knowing where to parse commands
   */
  private void useApp(Scanner scan, IFactory factory) {
    view.visualizeCommands();

    while (true) {
      ICommand comObj = this.createCommandObject(scan, factory);

      if (comObj == null) {
        return;
      }

      try {
        comObj.apply(view);
      } catch (IllegalArgumentException e) {
        view.renderMessage(e.getMessage());
      }
    }
  }

  /**
   * Creates a command object capable of interpreting the command string it is given.
   *
   * @param scan    a scanner to read the inputs
   * @param factory the factory used to determine where to parse commands to
   * @return an {@code ICommand} capable of interpreting the command string
   */
  private ICommand createCommandObject(Scanner scan, IFactory factory) {
    while (true) {
      String command;

      if (scan.hasNextLine()) {
        command = scan.nextLine();
      } else {
        return null;
      }

      try {
        return factory.create(command, app);
      } catch (IllegalArgumentException i) {
        view.renderMessage("Not a valid command, please try again");
      }
    }
  }
}