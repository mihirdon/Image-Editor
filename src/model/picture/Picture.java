package model.picture;

import java.awt.Point;
import java.util.Arrays;
import java.util.Objects;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import model.pixel.Pixel;

/**
 * Represents an image which contains a width and height, a maximum value for each pixel's color
 * channel value, and a sequence of pixels which form the image. Uses the {@code IPixel}
 * implementation.
 */
public class Picture implements IPicture<IPixel> {

  protected final int width;
  protected final int height;
  protected final int maxVal;
  protected final IPixel[][] pixels;

  /**
   * Constructs a {@code Picture} by setting its fields to those of the provided {@code IPicture}.
   * Throws an exception if the given image is null.
   *
   * @param other the image whose fields are copied over
   */
  public Picture(IPicture<IPixel> other) {
    if (other == null) {
      width = 0;
      height = 0;
      maxVal = 0;
      pixels = null;
    } else {
      width = other.getWidth();
      height = other.getHeight();
      maxVal = other.getMaxVal();
      pixels = other.getPixelSequence();
    }
  }

  /**
   * Constructs a {@code Pixel} object with the given arguments.
   *
   * @param width  number of columns in terms of pixels
   * @param height number of rows in terms of pixels
   * @param maxVal maximum value for each pixel's color channel values (inclusive)
   * @param pixels a 2D array representing this image's sequence of pixels
   * @throws IllegalArgumentException if width or height are not positive, the maximum value is
   *                                  negative, the pixel sequence is null, or the dimensions of the
   *                                  pixel sequence do not match this image's width and height.
   */
  public Picture(int width, int height, int maxVal, IPixel[][] pixels)
      throws IllegalArgumentException {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width and height must be positive");
    } else if (maxVal < 0) {
      throw new IllegalArgumentException("Cannot have negative maximum value");
    }

    this.width = width;
    this.height = height;
    this.maxVal = maxVal;

    if (pixels == null) {
      throw new IllegalArgumentException("Pixel sequence cannot be null");
    } else if (pixels.length != height || pixels[0].length != width) {
      throw new IllegalArgumentException("Pixel sequence does not match image's width and height");
    }

    this.pixels = pixels;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getMaxVal() {
    return maxVal;
  }

  @Override
  public IPixel[][] getPixelSequence() {
    IPixel[][] result = new IPixel[height][width];

    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        result[h][w] = new Pixel(pixels[h][w]);
      }
    }

    return result;
  }

  @Override
  public int[][] getChannelValues(ColorChannel channel) throws IllegalArgumentException {
    int[][] result = new int[height][width];

    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        result[h][w] = pixels[h][w].getChannelValue(channel);
      }
    }

    return result;
  }

  @Override
  public int[][] getImageSubset(int dimension, Point center, ColorChannel channel)
      throws IllegalArgumentException {
    if (dimension % 2 != 1) {
      throw new IllegalArgumentException("Dimension is even or not positive");
    } else if (center == null) {
      throw new IllegalArgumentException("Given center cannot be null");
    } else if (channel == null) {
      throw new IllegalArgumentException("Given channel cannot be null");
    }

    int[][] subset = new int[dimension][dimension];

    if (center.getX() >= width || center.getY() >= height
        || center.getX() < 0 || center.getY() < 0) {
      throw new IllegalArgumentException("Specified center is out of bounds");
    }

    int subsetH = 0;

    for (int h = (int) (center.getY() - (dimension / 2));
        h <= (int) (center.getY() + (dimension / 2)); h++) {

      int subsetW = 0;

      for (int w = (int) (center.getX() - (dimension / 2));
          w <= (int) (center.getX() + (dimension / 2)); w++) {

        if (w >= 0 && h >= 0 && w < width && h < height) {
          subset[subsetH][subsetW] = pixels[h][w].getChannelValue(channel);
        }

        subsetW++;
      }

      subsetH++;
    }

    return subset;
  }

  @Override
  public IPicture<IPixel> filter(double[][] kernel) throws IllegalArgumentException {
    if (kernel.length != kernel[0].length || kernel.length % 2 == 0) {
      throw new IllegalArgumentException("Given kernel is not odd and square");
    }

    IPixel[][] newSequence = new IPixel[height][width];

    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        int red = applyKernel(kernel,
            getImageSubset(kernel.length, pixels[h][w].getCoordinates(), ColorChannel.RED));

        int green = applyKernel(kernel,
            getImageSubset(kernel.length, pixels[h][w].getCoordinates(), ColorChannel.GREEN));

        int blue = applyKernel(kernel,
            getImageSubset(kernel.length, pixels[h][w].getCoordinates(), ColorChannel.BLUE));

        newSequence[h][w] = new Pixel(pixels[h][w].getCoordinates(), maxVal, red, green, blue);
      }
    }

    return new Picture(width, height, maxVal, newSequence);
  }

  // Multiplies each value of the given kernel with the respective value from the given subset
  // of this image's pixels, adding these products together and returning the sum.
  protected int applyKernel(double[][] kernel, int[][] subset) {
    int sum = 0;

    for (int h = 0; h < kernel.length; h++) {
      for (int w = 0; w < kernel[0].length; w++) {
        sum += kernel[h][w] * subset[h][w];
      }
    }

    return sum;
  }

  @Override
  public IPicture<IPixel> colorTransform(double[][] matrix) throws IllegalArgumentException {
    if (matrix.length != matrix[0].length || matrix.length != ColorChannel.values().length) {
      throw new IllegalArgumentException("Given matrix is not the same dimensions"
          + " as the number of color channels");
    }

    IPixel[][] newSequence = getPixelSequence();

    for (IPixel[] pixelSequence : newSequence) {
      for (IPixel pixel : pixelSequence) {
        int[] newChannelValues = this.applyMatrix(matrix, pixel);

        pixel.setChannelValue(ColorChannel.RED, newChannelValues[0]);
        pixel.setChannelValue(ColorChannel.GREEN, newChannelValues[1]);
        pixel.setChannelValue(ColorChannel.BLUE, newChannelValues[2]);
      }
    }

    return new Picture(width, height, maxVal, newSequence);
  }

  // Returns an array representing the new values of each color channel
  // (in the order of red, green, and blue) by multiplying each column of values from the
  // given matrix with the corresponding color channel value (same order specified above)
  // and summing the products of each row to determine the new value for each channel
  // (Each row represents a different channel value).
  protected int[] applyMatrix(double[][] kernel, IPixel pixel) {
    int[] sums = new int[kernel.length];

    for (int i = 0; i < sums.length; i++) {
      sums[i] = (int) (kernel[i][0] * pixel.getChannelValue(ColorChannel.RED)
          + kernel[i][1] * pixel.getChannelValue(ColorChannel.GREEN)
          + kernel[i][2] * pixel.getChannelValue(ColorChannel.BLUE));
    }

    return sums;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    result.append(String.format("Dimensions: %dx%d", width, height)).append("\n");
    result.append(String.format("Maximum Value: %d", maxVal)).append("\n");

    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        result.append(pixels[h][w].toString()).append("\n");
      }
    }

    return result.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (!(other instanceof Picture)) {
      return false;
    }

    return width == ((Picture) other).getWidth()
        && height == ((Picture) other).getHeight()
        && maxVal == ((Picture) other).getMaxVal()
        && Arrays.deepEquals(pixels, ((Picture) other).getPixelSequence());
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height, maxVal, Arrays.deepHashCode(pixels));
  }
}