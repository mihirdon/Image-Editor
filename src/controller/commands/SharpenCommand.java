package controller.commands;

import model.application.ILayeredApplication;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.pixel.IPixel;
import view.IApplicationView;

/**
 * {@code ICommands} object in charge of sharpening the application's current image's current
 * layer's image.
 */
public class SharpenCommand extends AbstractCommand {

  /**
   * Constructs a {@code SharpenCommand} with the given arguments.
   *
   * @param command the string determining the outcome of this command's behavior
   * @param app     the application which the command object operates on.
   */
  public SharpenCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(1, app);
    ILayeredPicture<ILayer<IPicture<IPixel>>> curImage = app.getCurrentImage();

    int curLayerInt = curImage.getLayers().indexOf(curImage.getCurrentLayer());

    ILayeredPicture<ILayer<IPicture<IPixel>>> result = app.sharpen(curLayerInt);

    result.setCurrentLayer(app.getCurrentImage().getCurrentLayer().getName());

    app.setImage(result.getCurrentLayer().getImage());

    view.renderMessage("Sharpened Current Image: "
        + app.getCurrentImage().getCurrentLayer().getName());
  }
}