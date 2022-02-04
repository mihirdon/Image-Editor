package controller.commands;

import model.application.ILayeredApplication;

/**
 * An abstract class that is used for convenience allowing for protected methods that can be used in
 * all of the ICommands.
 */
abstract class AbstractCommand implements ICommand {

  public final String[] command;
  public final ILayeredApplication app;

  /**
   * A convenience constructor so that any ICommands do not have to repeat this command and can
   * simply call super.
   *
   * @param command A String of commands to be separated into the different parameters.
   * @param app     An application that the object uses to perform methods.
   */
  public AbstractCommand(String command, ILayeredApplication app) {
    this.command = command.split(" ");
    this.app = app;
  }

  /**
   * A convenience method that is abstracted to allow for checking a lot of ICommands conditions
   * using only one method.
   *
   * @param numOfString the number of strings that the command object will need.
   * @param app         the application in order to get images and check size and layer conditions
   * @throws IllegalArgumentException if the command length is too short, if the picture or layer
   *                                  was not created, or if there is not a current layer.
   */
  protected void conditionsMet(int numOfString, ILayeredApplication app)
      throws IllegalArgumentException {
    if (command.length != numOfString) {
      throw new IllegalArgumentException("Command length is invalid");
    }
    if (app.getImages().size() == 0) {
      throw new IllegalArgumentException("Must first create a layer");
    }
    if (app.getCurrentImage().getCurrentLayer() == null) {
      throw new IllegalArgumentException("Set a current layer first");
    }
  }
}