package model.picture;

import java.awt.Point;
import model.pixel.ColorChannel;

/**
 * Represents an image which consists of a width and height, a maximum value for each pixel's color
 * channel, and a sequence of pixels. Parameterized over the type of pixel implementation 'P'.
 */
public interface IPicture<P> {

  /**
   * Returns the width of this image in terms of the number of pixels.
   *
   * @return an int representing the number of pixels.
   */
  int getWidth();

  /**
   * Returns the height of this image in terms of the number of pixels.
   *
   * @return an int representing the number of pixels.
   */
  int getHeight();

  /**
   * Returns the maximum value that each color channel value can have in each of this image's
   * pixels.
   *
   * @return an int representing the maximum value
   */
  int getMaxVal();

  /**
   * Returns a 2D array representing the pixel sequence forming this image. The 2D array type is
   * parameterized over the pixel implementation 'P'.
   *
   * @return a 2D array of this image's pixels
   */
  P[][] getPixelSequence();

  /**
   * Returns a 2D array of integers representing the specified color channel values of each pixel
   * forming this image.
   *
   * @param channel the {@code ColorChannel} type representing the desired color channel
   * @return a 2D array of integers
   * @throws IllegalArgumentException if given {@code ColorChannel} is null
   */
  int[][] getChannelValues(ColorChannel channel) throws IllegalArgumentException;

  /**
   * Returns a 2D array representing a matrix consisting of the specified color channel values from
   * this image's sequence of pixels. Its size is determined by selecting a pixel to be its center
   * and specifying an odd width/height which are always the same, meaning the dimensions of the
   * matrix are always square and odd (e.g. 3x3, 5x5, etc...).
   *
   * @param dimension the length of this 2D array's width and height
   * @param center    the coordinates of the center pixel represented as a {@code Point}
   * @param channel   the {@code ColorChannel} type specifying which color value this 2D array
   *                  holds
   * @throws IllegalArgumentException if the dimension is even or not positive, and if the specified
   *                                  center pixel coordinates are out of bounds for this image.
   */
  int[][] getImageSubset(int dimension, Point center, ColorChannel channel)
      throws IllegalArgumentException;

  /**
   * Returns a copy of this image after applying the provided kernel to every pixel of this image.
   *
   * @param kernel double[][] serving as the desired kernel with which to update all the pixel's
   *               color channel values
   * @throws IllegalArgumentException if given kernel is not odd and square
   */
  IPicture<P> filter(double[][] kernel) throws IllegalArgumentException;

  /**
   * Returns a copy of this image after applying the provided matrix to every pixel of this image.
   *
   * @param matrix double[][] serving as the desired matrix with which to update all the pixel's
   *               color channel values
   * @throws IllegalArgumentException if the given matrix does not have the same dimensions as the
   *                                  number of color channels of this image's pixel implementation
   */
  IPicture<P> colorTransform(double[][] matrix) throws IllegalArgumentException;

  /**
   * Represents this image as a string, detailing its width and height, maximum value, and each
   * pixel forming this image.
   *
   * @return the string representing this pixel's color channel values
   */
  @Override
  String toString();

  /**
   * Returns true if the given object is an {@code IPicture} and contains the same values for each
   * of its fields.
   *
   * @return boolean determining whether the given object is equal to this pixel
   */
  @Override
  boolean equals(Object other);

  /**
   * Generates a hash code for this image based on its fields.
   *
   * @return int representing this image's hash code
   */
  @Override
  int hashCode();
}