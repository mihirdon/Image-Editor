package controller.commands;

import model.application.ILayeredApplication;
import model.application.LIMEApplicationWithExtraCredit;

/**
 * Factory class used to create the downsize and mosaic
 * commands in addition to all previous commands.
 */
public class ExtraCreditFactory implements IFactory {
  private final IFactory factory;

  /**
   * Constructs an {@code ExtraCreditFactory} with the original command factory.
   */
  public ExtraCreditFactory() {
    this.factory = new CommandFactory();
  }

  /**
   * Constructs an {@code ExtraCreditFactory} with the given command factory.
   *
   * @param factory the desired command factory
   */
  public ExtraCreditFactory(IFactory factory) {
    this.factory = factory;
  }

  @Override
  public ICommand create(String command, ILayeredApplication application) {
    if (command.contains("mosaic")) {
      return new MosaicCommand(command, new LIMEApplicationWithExtraCredit(application));
    }
    else if (command.contains("downsize")) {
      return new DownsizeCommand(command, new LIMEApplicationWithExtraCredit(application));
    }
    else {
      return factory.create(command, application);
    }
  }
}