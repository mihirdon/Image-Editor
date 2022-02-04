package model.application;

import java.awt.Image;

/**
 * Interface representing a class containing methods which provide information regarding an
 * application's top-most visible layer and the ability to create a buffered image from an image
 * representation. Parameterized over the image implementation 'I'.
 */
public interface IModelObserver<I> {

  /**
   * Returns an application's top-most visible layer's image. Top-most visible layer is either the
   * current layer if it contains a non-null image, or the first layer with a non-null image derived
   * from the layer hashmap.
   *
   * @return the top-most visible layer's image
   */
  I getTopMostVisible();

  /**
   * Creates a {@code BufferedImage} from the given image.
   *
   * @param image the image to convert to a buffered image
   * @return the buffered imag version of the given image
   */
  Image createBufferedImage(I image);
}