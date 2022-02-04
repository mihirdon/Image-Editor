package controller.commands;

import model.application.ILayeredApplication;

/**
 * Provides the ability to create {@code ICommand} objects from the given string to operate
 * on the given application.
 */
public interface IFactory {

  /**
   * Creates the appropriate {@code ICommand} based on the given string with the given application.
   *
   * @param command string representing the desired command
   * @param app application used as part of the command
   * @return the appropriate command object.
   */
  ICommand create(String command, ILayeredApplication app);
}
