package model.picture;

import model.pixel.IPixel;

/**
 * Represents a layer that makes up a multi-layered image. Contains a name, a visibility setting,
 * and an image. Parameterized over the type of image implementation 'I'.
 */
public interface ILayer<I> {

  /**
   * Returns the name of this layer.
   *
   * @return the string representing this layer's name.
   */
  String getName();

  /**
   * Returns whether this layer is visible.
   *
   * @return the boolean representing whether this layer is visible
   */
  boolean getVisibility();

  /**
   * Returns the image stored in this layer or null if it is empty.
   *
   * @return the image stored in this layer or null if empty
   */
  I getImage();

  /**
   * Set whether the layer is visible to the given value.
   *
   * @param visibility the desired boolean value
   */
  void setVisibility(boolean visibility);

  /**
   * Sets the stored image of this layer to the given image (including null).
   *
   * @param image the desired image or null to set this layer's image to empty
   */
  void setImage(IPicture<IPixel> image);

  /**
   * Returns a string representations of this layer.
   *
   * @return the string representing this layer
   */
  @Override
  String toString();

  /**
   * Determines whether this layer is equal to the given object.
   *
   * @param other the object to compare
   * @return boolean determining whether the given object is equal to this layer
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