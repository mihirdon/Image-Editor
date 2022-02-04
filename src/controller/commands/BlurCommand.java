package controller.commands;

import model.application.ILayeredApplication;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.pixel.IPixel;
import view.IApplicationView;

/**
 * An ICommand that blurs the current layer.
 */
public class BlurCommand extends AbstractCommand {

  /**
   * Creates a BlurCommand object using the abstracted constructor of AbstractCommand.
   *
   * @param command the commands to be split
   * @param app the application that the methods will go to
   */
  public BlurCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(1, app);
    ILayeredPicture<ILayer<IPicture<IPixel>>> curImage = app.getCurrentImage();

    int curLayerInt = curImage.getLayers().indexOf(curImage.getCurrentLayer());

    ILayeredPicture<ILayer<IPicture<IPixel>>> result = app.blur(curLayerInt);

    result.setCurrentLayer(app.getCurrentImage().getCurrentLayer().getName());

    app.setImage(result.getCurrentLayer().getImage());

    view.renderMessage("Blurred Current Image: "
        + app.getCurrentImage().getCurrentLayer().getName());
  }
}