package controller.commands;

import model.application.IExtraCredit;
import view.IApplicationView;

/**
 * A {@code MosaicCommand} makes the given image monochrome.
 */
public class MosaicCommand extends AbstractCommand {

  private final IExtraCredit app;

  /**
   * Creates a MosaicCommand using the constructor given by the AbstractCommand.
   *
   * @param command     the command to be searched for parameter to call the model's method with
   * @param application the model of which to call the appropriate method
   */
  public MosaicCommand(String command, IExtraCredit application) {
    super(command, application);
    this.app = application;
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(2, app);

    app.mosaic(Integer.parseInt(command[1]));
  }
}
