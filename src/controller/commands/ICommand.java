package controller.commands;

import view.IApplicationView;

/**
 * An {@code ICommands} is an object that takes in a command and performs the given action as
 * dictated by the model. Takes in a string that it breaks apart into a set amount of pieces using
 * each part of the string as a different part of the method, with some parts representing the name
 * and some parts representing the parameters.
 */
public interface ICommand {

  /**
   * A method that applies the job of the ICommand, calling a given model to perform the actual
   * method, and making the Command interpret the string array into the method name and parameters.
   *
   * @param view the visualization after it is done performing its action
   * @throws IllegalArgumentException if the model throws an exception or if the strings is not in
   *                                  the specified format of the object, or if any of its
   *                                  conditions are not met
   */
  void apply(IApplicationView view) throws IllegalArgumentException;
}