package model.pixel;

import java.awt.Point;
import java.util.Objects;

/**
 * A {@code Pixel} is the base unit for an image, containing coordinates for its row and column, the
 * maximum value each color channel can have, and the three integer values for each of its red,
 * green, and blue color channels. This implementation disallows pixels with negative coordinates.
 */
public class Pixel implements IPixel {

  protected final Point position;
  protected final int maxVal;

  protected int red;
  protected int green;
  protected int blue;

  /**
   * Constructs a {@code Pixel} by setting its fields to those of the provided {@code IPixel}.
   * Throws an exception if given pixel is null.
   *
   * @param other the pixel whose fields are copied over
   * @throws IllegalArgumentException if given {@code IPixel} is null
   */
  public Pixel(IPixel other) throws IllegalArgumentException {
    if (other == null) {
      throw new IllegalArgumentException("Given pixel is null");
    }

    this.position = other.getCoordinates();
    this.maxVal = other.getMaxVal();

    this.red = other.getChannelValue(ColorChannel.RED);
    this.green = other.getChannelValue(ColorChannel.GREEN);
    this.blue = other.getChannelValue(ColorChannel.BLUE);
  }

  /**
   * Constructs a {@code Pixel} object with the given arguments. Each color channel value is
   * automatically clamped between 0 and the provided maximum value. Throws an exception if given
   * point contains negative values or maximum value is negative.
   *
   * @param position the position of the pixel as a {@code Point}
   * @param maxVal   the maximum value of each color channel (inclusive)
   * @param red      the integer value of the red channel
   * @param green    the integer value of the green channel
   * @param blue     the integer value of the blue channel
   * @throws IllegalArgumentException if the given point is null, the given point contains negative
   *                                  values, or maximum value is negative
   */
  public Pixel(Point position, int maxVal, int red, int green, int blue)
      throws IllegalArgumentException {
    if (position == null) {
      throw new IllegalArgumentException("Position cannot be null");
    } else if (position.getX() < 0 || position.getY() < 0) {
      throw new IllegalArgumentException("Cannot have coordinates with negative values");
    } else if (maxVal < 0) {
      throw new IllegalArgumentException("Cannot have negative maximum value");
    }

    this.position = position;
    this.maxVal = maxVal;

    this.red = clamp(red);
    this.green = clamp(green);
    this.blue = clamp(blue);
  }

  /**
   * Constructs a {@code Pixel} object with the given arguments. Default maximum value is set to
   * 255. Throws an exception if given point contains negative values.
   *
   * @param position the position of the pixel as a {@code Point}
   * @param red      the integer value of the red channel
   * @param green    the integer value of the green channel
   * @param blue     the integer value of the blue channel
   * @throws IllegalArgumentException if given point contains negative values
   */
  public Pixel(Point position, int red, int green, int blue) throws IllegalArgumentException {
    this(position, 255, red, green, blue);
  }

  /**
   * Clamps the given integer by setting any negative value to 0 and any value above this pixel's
   * maximum value to said maximum value. Any other value is left unchanged.
   *
   * @param value the integer value that is being clamped
   * @return the new integer clamped between 0 and this pixel's maximum value
   */
  protected int clamp(int value) {
    if (value < 0) {
      value = 0;
    } else if (value > maxVal) {
      value = maxVal;
    }

    return value;
  }

  // Returns a copy of this pixel's position in order to prevent mutation of its field.
  @Override
  public Point getCoordinates() {
    return new Point(position);
  }

  @Override
  public int getMaxVal() {
    return this.maxVal;
  }

  @Override
  public int getChannelValue(ColorChannel channel) throws IllegalArgumentException {
    if (channel == null) {
      throw new IllegalArgumentException("Given channel cannot be null");
    }

    switch (channel) {
      case RED:
        return red;
      case GREEN:
        return green;
      case BLUE:
        return blue;
      default:
        throw new IllegalArgumentException("Invalid color channel");
    }
  }

  @Override
  public void setChannelValue(ColorChannel channel, int value) throws IllegalArgumentException {
    if (channel == null) {
      throw new IllegalArgumentException("Given channel cannot be null");
    }

    switch (channel) {
      case RED:
        red = clamp(value);
        break;
      case GREEN:
        green = clamp(value);
        break;
      case BLUE:
        blue = clamp(value);
        break;
      default:
        throw new IllegalArgumentException("Invalid color channel");
    }
  }

  @Override
  public String toString() {
    StringBuilder result;
    result = new StringBuilder();

    result.append(String.format("Coordinates: %s\n", position.toString()));
    result.append(String.format("Maximum Value: %d\n", maxVal));
    result.append(String.format("Red Channel Value: %d\n", red));
    result.append(String.format("Green Channel Value: %d\n", green));
    result.append(String.format("Blue Channel Value: %d\n", blue));

    return result.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (!(other instanceof Pixel)) {
      return false;
    }

    return this.position.equals(((Pixel) other).getCoordinates())
        && this.maxVal == ((Pixel) other).getMaxVal()
        && this.red == ((Pixel) other).getChannelValue(ColorChannel.RED)
        && this.green == ((Pixel) other).getChannelValue(ColorChannel.GREEN)
        && this.blue == ((Pixel) other).getChannelValue(ColorChannel.BLUE);
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, maxVal, red, green, blue);
  }
}