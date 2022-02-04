package model.application;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import model.picture.ILayer;
import model.picture.IPicture;
import model.pixel.ColorChannel;
import model.pixel.IPixel;

/**
 * {@code IModelObserver} providing information of the contained application. Parameterized over the
 * {@code IPicture<IPixel>} image implementation.
 */
public class GUIModel implements IModelObserver<IPicture<IPixel>> {

  ILayeredApplication app;

  /**
   * Constructs a {@code GUIModel} with the given application.
   *
   * @param app the desired application
   * @throws IllegalArgumentException if app is null
   */
  public GUIModel(ILayeredApplication app) {
    if (app == null) {
      throw new IllegalArgumentException("Given application is null");
    }

    this.app = app;
  }

  @Override
  public IPicture<IPixel> getTopMostVisible() {
    IPicture<IPixel> topImage = null;
    ILayer<IPicture<IPixel>> currentLayer = app.getCurrentImage().getCurrentLayer();

    if (currentLayer != null
        && currentLayer.getImage() != null
        && currentLayer.getVisibility()) {
      topImage = currentLayer.getImage();
    } else {
      for (ILayer<IPicture<IPixel>> layer : app.getCurrentImage().getLayers()) {
        if (layer != null && layer.getImage() != null && layer.getVisibility()) {
          topImage = layer.getImage();
          break;
        }
      }
    }

    return topImage;
  }

  @Override
  public Image createBufferedImage(IPicture<IPixel> image) {
    if (image == null) {
      return null;
    }

    BufferedImage bf =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

    IPixel[][] pixelSequence = image.getPixelSequence();

    for (int h = 0; h < image.getHeight(); h++) {
      for (int w = 0; w < image.getWidth(); w++) {
        IPixel pixel = pixelSequence[h][w];

        int red = pixel.getChannelValue(ColorChannel.RED);
        int green = pixel.getChannelValue(ColorChannel.GREEN);
        int blue = pixel.getChannelValue(ColorChannel.BLUE);

        Color c = new Color(red, green, blue);
        bf.setRGB(w, h, c.getRGB());
      }
    }

    return bf;
  }
}