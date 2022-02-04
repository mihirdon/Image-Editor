package controller.commands;

import model.application.ILayeredApplication;
import view.IApplicationView;

/**
 * A {@code LoadCommand} sets a given image to a layer's image using the image's filepath.
 */
public class LoadCommand extends AbstractCommand {

  /**
   * Creates a LoadCommand using the constructor provided by AbstractCommand.
   *
   * @param command a string for the commands to be parsed out of
   * @param app     a model to call the methods from
   */
  public LoadCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(command.length, app);
    StringBuilder filepath = new StringBuilder();

    for (int i = 1; i < command.length; i++) {
      if (i != command.length - 1) {
        filepath.append(String.format("%s ", command[i]));
      } else {
        filepath.append(command[i]);
      }
    }

    app.load(filepath.toString());
    view.renderMessage("Loaded " + filepath);
  }
}