package model.picture;

import java.util.List;
import model.pixel.IPixel;

/**
 * Represents an image with multiple layers. Has the functionality to add or remove layers, select
 * the layer to operate on, and set the visibility of a layer. Parameterized over the implementation
 * of a layer 'I'. Parameterized over the {@code IPixel} implementation of a pixel.
 */
public interface ILayeredPicture<I> extends IPicture<IPixel> {

  /**
   * Returns a list of this layered image's layers from topmost to bottommost.
   */
  List<I> getLayers();

  /**
   * Returns the currently selected layer.
   */
  I getCurrentLayer();

  /**
   * Sets the layer to operate on to the specified layer.
   *
   * @param name the name of the desired layer
   * @throws IllegalArgumentException if name is null or is not an existing layer's name
   */
  void setCurrentLayer(String name) throws IllegalArgumentException;

  /**
   * Adds an empty layer (as topmost) with the given name to this layered image.
   *
   * @param name desired name for the new layer.
   * @throws IllegalArgumentException if given name is null or matches a pre-existing layer's name
   */
  void addLayer(String name) throws IllegalArgumentException;

  /**
   * Adds the given layer (as topmost) to this layered image.
   *
   * @param layer desired layer to add
   * @throws IllegalArgumentException if given layer is null or has a matching name to a
   *                                  pre-existing layer.
   */
  void addLayer(I layer) throws IllegalArgumentException;

  /**
   * Removes the specified layer from this layered image.
   *
   * @param name the name of the layer to be removed
   * @throws IllegalArgumentException if name is null or is not an existing layer's name
   */
  void removeLayer(String name) throws IllegalArgumentException;

  /**
   * Removes the specified layer from this layered image.
   *
   * @param layer the layer to be removed
   * @throws IllegalArgumentException if layer is null or is not an existing layer
   */
  void removeLayer(I layer) throws IllegalArgumentException;

  /**
   * Sets whether this layer is visible to the given value.
   *
   * @param visibility the boolean representing whether this layer is visible
   */
  void setVisibility(boolean visibility);

  /**
   * Represents this layered image as a string.
   *
   * @return the string representing this layered image
   */
  @Override
  String toString();

  /**
   * Determines whether this layer is equal to the given object.
   *
   * @param other the object to compare
   * @return the boolean determining whether the given object is equal to this layered image
   */
  @Override
  boolean equals(Object other);

  /**
   * Generates a hashcode representing this layered image.
   *
   * @return the integer representing this layered image's hashcode
   */
  @Override
  int hashCode();

  /**
   * Sets this image's width to the given int.
   *
   * @param width desired width
   */
  void setWidth(int width);

  /**
   * Sets this image's height to the given int.
   *
   * @param height desired height
   */
  void setHeight(int height);

  /**
   * Returns a copy of the layer with the given name.
   */
  ILayer<IPicture<IPixel>> getLayer(String s) throws IllegalArgumentException;

  /**
   * Sets the current layer to the layer with the given image.
   *
   * @param image image of desired layer
   */
  void setCurrentLayerTo(IPicture<IPixel> image);
}