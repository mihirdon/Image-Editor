package controller.commands;

import model.application.ILayeredApplication;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.pixel.IPixel;
import view.IApplicationView;

/**
 * A {@code MonochromeCommand} makes the given image monochrome.
 */
public class MonochromeCommand extends AbstractCommand {

  /**
   * Creates a MonochromeCommand using the constructor given by the AbstractCommand.
   *
   * @param command the command to be searched for parameter to call the model's method with
   * @param app     the model of which to call the appropriate method
   */
  public MonochromeCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(1, app);
    ILayeredPicture<ILayer<IPicture<IPixel>>> curImage = app.getCurrentImage();

    int curLayerInt = curImage.getLayers().indexOf(curImage.getCurrentLayer());

    ILayeredPicture<ILayer<IPicture<IPixel>>> result = app.monochrome(curLayerInt);

    result.setCurrentLayer(app.getCurrentImage().getCurrentLayer().getName());

    app.setImage(result.getCurrentLayer().getImage());

    view.renderMessage("Monochrome Current Image: "
        + app.getCurrentImage().getCurrentLayer().getName());
  }
}