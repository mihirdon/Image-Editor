package model.pixel;

import java.awt.Point;

/**
 * Represents an image's pixel which contains its coordinates, maximum value, and color channels.
 * Contains operations to retrieve these values and set its color channel values. Color channel
 * values are represented as integers.
 */
public interface IPixel {

  /**
   * Returns the pixel's position as a {@link Point}.
   *
   * @return a Point representing the pixel's row and column
   */
  Point getCoordinates();

  /**
   * Returns the maximum value that the red, green, and blue channels can have.
   *
   * @return an int representing the maximum value
   */
  int getMaxVal();

  /**
   * Returns this pixel's color channel value depending on the given {@code ColorChannel} type.
   *
   * @param channel the desired color channel
   * @return an int representing the specified color channel's value
   * @throws IllegalArgumentException if given {@code ColorChannel} is null
   */
  int getChannelValue(ColorChannel channel) throws IllegalArgumentException;

  /**
   * Sets the pixel's specified color channel to the provided value. Automatically clamps value
   * between 0 and this pixel's maximum value.
   *
   * @param channel desired color channel value to change
   * @param value   the desired red channel value
   * @throws IllegalArgumentException if given {@code ColorChannel} is null
   */
  void setChannelValue(ColorChannel channel, int value) throws IllegalArgumentException;

  /**
   * Represents this pixel as a string, detailing its coordinates, maximum value, and color channel
   * values.
   *
   * @return the string representing this pixel
   */
  @Override
  String toString();

  /**
   * Returns true if the given object is an {@code IPixel} and contains the same values for each of
   * its fields.
   *
   * @return boolean determining whether the given object is equal to this pixel
   */
  @Override
  boolean equals(Object other);

  /**
   * Generates a hash code for this pixel based on its fields.
   *
   * @return int representing this pixel's hash code
   */
  @Override
  int hashCode();
}