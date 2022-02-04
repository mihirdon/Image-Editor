package model.application;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Represents an image processing application which can manipulate several images through filtering
 * and color processing. Application can also import and export images. When manipulating images, a
 * new image is returned rather than mutating original. Parameterized over the type of image
 * implementation 'I'.
 */
public interface IApplication<I> {

  /**
   * Returns a a copy of this application's list of images.
   *
   * @return the copy of this application's list of images
   */
  List<I> getImages();

  /**
   * Returns the specified image that this application contains.
   *
   * @param index the index of the desired image
   * @return the desired image
   * @throws IllegalArgumentException if index is negative or out of bounds
   */
  I getImage(int index) throws IllegalArgumentException;

  /**
   * Adds the given image to this application's collection of images.
   *
   * @param image the image you  wish to add
   * @throws IllegalArgumentException if given image is null
   */
  void addImage(I image) throws IllegalArgumentException;

  /**
   * Removes the image at the specified index from this application's list of images.
   *
   * @param index the index of the desired image
   * @throws IllegalArgumentException if index is negative or out of bounds
   */
  void removeImage(int index) throws IllegalArgumentException;

  /**
   * Blurs the specified image. Currently blurs with a fixed kernel.
   *
   * @param index the index of the desired image
   * @return a blurred version of the specified picture
   * @throws IllegalArgumentException if index is negative or out of bounds
   */
  I blur(int index) throws IllegalArgumentException;

  /**
   * Sharpens the specified image. Currently sharpens with a fixed kernel.
   *
   * @param index the index of the desired image
   * @return a sharpened version of the specified picture
   * @throws IllegalArgumentException if index is negative or out of bounds
   */
  I sharpen(int index) throws IllegalArgumentException;

  /**
   * Converts the specified image to monochrome (greyscale). Currently uses a fixed matrix.
   *
   * @param index the index of the desired image
   * @return a monochrome version of the specified picture
   * @throws IllegalArgumentException if index is negative or out of bounds
   */
  I monochrome(int index) throws IllegalArgumentException;

  /**
   * Converts the specified image to sepia tone. Currently uses a fixed matrix.
   *
   * @param index the index of the desired image
   * @return a sepia tone version of the specified picture
   * @throws IllegalArgumentException if index is negative or out of bounds
   */
  I sepiaTone(int index) throws IllegalArgumentException;

  /**
   * Creates a square checkerboard with the given arguments.
   *
   * @param tileSize the number of pixels forming each side of a tile
   * @param numTiles the number of tiles forming each side of the checkerboard
   * @return the checkerboard image
   * @throws IllegalArgumentException if either argument is not positive
   */
  I createCheckerBoard(int tileSize, int numTiles) throws IllegalArgumentException;

  /**
   * Creates an image from the given file path. When giving an image to import, please place in the
   * res/ folder and give the path as the string, for example "res/Koala.ppm". Only supports PPM
   * images.
   *
   * @param filename the path of the file
   * @return the image that was imported
   * @throws IllegalArgumentException if given file path is null, or given file path does not
   *                                  retrieve a file
   */
  I importImage(String filename) throws IllegalArgumentException;

  /**
   * Makes a PPM file from the specified image with the given name. Can either include or exclude
   * ".ppm" from the name (both are supported). Only supports PPM images.
   *
   * @param index the index of the desired image
   * @param name  what the client wishes to name the new file (without .ppm)
   * @throws IllegalArgumentException if index is negative or out of bounds, given name is null, or
   *                                  I/O exception occurs.
   * @throws FileNotFoundException    if file cannot be created
   */
  void exportImage(int index, String name) throws IllegalArgumentException, FileNotFoundException;
}