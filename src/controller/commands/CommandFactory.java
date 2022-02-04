package controller.commands;

import model.application.ILayeredApplication;

/**
 * A {@code FactoryCommand} creates ICommands capable of interpreting
 * the string command it is given.
 */
public class CommandFactory implements IFactory {

  /**
   * Creates an ICommand based off of the first string it is given.
   *
   * @param command the string that needs to be interpreted
   * @param app     the application to send to the ICommands so that they can call the appropriate
   *                method
   * @return an ICommand capable of interpreting and calling the appropriate method as dictated by
   *         the string
   * @throws IllegalArgumentException if their is no ICommands that can interpret this command, or
   *                                  if the command does not fit the format of the ICommands
   */
  public ICommand create(String command, ILayeredApplication app)
      throws IllegalArgumentException {
    String[] commandsInParts = command.split(" ");
    String com = commandsInParts[0];

    switch (com) {
      case "create":
        return new CreateCommand(command, app);
      case "current":
        return new SetCurrentCommand(command, app);
      case "load":
        return new LoadCommand(command, app);
      case "blur":
        return new BlurCommand(command, app);
      case "sharpen":
        return new SharpenCommand(command, app);
      case "sepia":
        return new SepiaCommand(command, app);
      case "monochrome":
        return new MonochromeCommand(command, app);
      case "checkerboard":
        return new CheckerboardCommand(command, app);
      case "save":
        return new SaveCommand(command, app);
      case "saveAll":
        return new SaveAllCommand(command, app);
      case "invisible":
        return new InvisibleCommand(command, app);
      case "visible":
        return new VisibleCommand(command, app);
      case "q":
      case "Q":
        return null;
      default:
        throw new IllegalArgumentException("Not a valid command");
    }
  }
}