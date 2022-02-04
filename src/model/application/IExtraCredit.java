package model.application;


/**
 * An {@code IExtraCredit} interface is an interface that handles layers in the same way as the
 * {@link ILayeredApplication}, but with the added method of mosaic. This works functionally the
 * same as a ILayeredApplication.
 */
public interface IExtraCredit extends ILayeredApplication {

  /**
   * Mosaic is a method that splits the image into a bunch of "tiles" that form together to create
   * the image. The "tiles" are clusters of pixels that have had all of their rgb values averaged to
   * make it feel like a mosaic image.
   *
   * @param numOfSeeds the number of "tiles" the user wishes to create
   */
  void mosaic(int numOfSeeds);

  /**
   * Downsizing is a method that reduces the width and height of all of the images to the specified
   * width and height.
   *
   * @param width  the new width of the image
   * @param height the new height of the image
   * @throws IllegalArgumentException if the width and height are less than or equal to zero or
   *                                  greater than the current images width and height
   */
  void downsizing(int width, int height) throws IllegalArgumentException;
}
