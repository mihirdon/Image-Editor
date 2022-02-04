package model.picture;

import java.awt.Point;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import model.pixel.Pixel;

/**
 * An {@code ExtraCreditPictureUtil} is a Utility class used to help with making the Extra Credit
 * methods of downsizing. Its current only method is downsizing and a few helper methods. Downsizing
 * is a method that reduces the size of an image, this class returns the new 2D pixel array of the
 * downsized image.
 */
public class ExtraCreditPictureUtil {

  /**
   * Reduces an image and forms a 2D array of IPixels from it, and returns it to be used as the
   * client sees fit.
   *
   * @param image       the image that is being downsized
   * @param widthPrime  the new width of the image
   * @param heightPrime the new height of the image
   * @return a 2D array of the downsized image
   * @throws IllegalArgumentException if the width and height are either greater than or equal to
   *                                  the current width and height, or is less than or equal to
   *                                  zero
   */
  public IPixel[][] downsizing(IPicture<IPixel> image, int widthPrime, int heightPrime)
      throws IllegalArgumentException {
    int width = image.getWidth();
    int height = image.getHeight();

    if (widthPrime > width || heightPrime > height || widthPrime <= 0 || heightPrime <= 0) {
      throw new IllegalArgumentException("New height and width cannot be greater than previous"
          + " or less than or equal to zero");
    }
    IPixel[][] curPic = image.getPixelSequence();
    IPixel[][] downSize = new IPixel[heightPrime][widthPrime];

    for (int i = 0; i < heightPrime; i++) {
      for (int j = 0; j < widthPrime; j++) {

        double x = findActual(j, width, widthPrime);
        double y = findActual(i, height, heightPrime);

        int r = getRGB(x, y, curPic, ColorChannel.RED);
        int g = getRGB(x, y, curPic, ColorChannel.GREEN);
        int b = getRGB(x, y, curPic, ColorChannel.BLUE);

        downSize[i][j] = new Pixel(new Point(j, i), r, g, b);
      }
    }

    return downSize;
  }

  private int getRGB(double x, double y, IPixel[][] curPic, ColorChannel color) {
    int colorVal;

    if (!isInt(x) && !isInt(y)) {
      IPixel a = curPic[(int) Math.floor(y)][(int) Math.floor(x)];
      IPixel b = curPic[(int) Math.floor(y)][(int) Math.ceil(x)];
      IPixel c = curPic[(int) Math.ceil(y)][(int) Math.floor(x)];
      IPixel d = curPic[(int) Math.ceil(y)][(int) Math.ceil(x)];

      colorVal = calculateColorFromSurrounding(x, y, a, b, c, d, color);
    } else {
      IPixel pixel = curPic[(int) y][(int) x];
      colorVal = pixel.getChannelValue(color);
    }

    return colorVal;
  }

  private double findActual(double coordinate, int denominator, int denominatorPrime) {
    return (coordinate * denominator) / denominatorPrime;
  }

  private boolean isInt(double val) {
    double floorOfVal = Math.floor(val);
    double diff = val - floorOfVal;
    return diff == 0;
  }

  private int calculateColorFromSurrounding(double xPrime, double yPrime, IPixel a, IPixel b,
      IPixel c, IPixel d, ColorChannel color) {
    double m = calculateFormula(b.getChannelValue(color), a.getChannelValue(color), xPrime);
    double n = calculateFormula(d.getChannelValue(color), c.getChannelValue(color), xPrime);
    double newColor = calculateFormula(m, n, yPrime);
    return (int) newColor;
  }

  private double calculateFormula(double slope1, double slope2, double val) {
    return slope1 * (val - Math.floor(val)) + slope2 * (Math.ceil(val) - val);
  }
}