package controller.commands;

import model.application.IExtraCredit;
import view.IApplicationView;

/**
 * A {@code DownsizeCommand} reduces the size of the application's current image and layers.
 */
public class DownsizeCommand extends AbstractCommand {

  private final IExtraCredit app;

  /**
   * Creates a DownsizeCommand using the constructor given by the AbstractCommand.
   *
   * @param command the command to be searched for parameter to call the model's method with
   * @param app     the model of which to call the appropriate method
   */
  public DownsizeCommand(String command, IExtraCredit app) {
    super(command, app);
    this.app = app;
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(3, app);

    app.downsizing(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
  }
}
