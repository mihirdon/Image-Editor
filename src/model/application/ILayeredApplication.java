package model.application;

import model.application.ImportExportUtil.FileType;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.pixel.IPixel;

/**
 * Represents a {@code IApplication} which can support multiple file types for import and export as
 * well. Parameterized over the {@code ILayeredPicture<ILayer<IPicture<IPixel>>>} implementation of
 * an image, meaning only layered images are supported.
 */
public interface ILayeredApplication
    extends IMultipleFormatApplication<ILayeredPicture<ILayer<IPicture<IPixel>>>> {

  /**
   * Returns the currently selected image.
   *
   * @return the current image.
   */
  ILayeredPicture<ILayer<IPicture<IPixel>>> getCurrentImage();

  /**
   * Updates the current image's current layer's image to the given image.
   *
   * @param image updated image
   * @throws IllegalArgumentException if given image is null
   */
  void setImage(IPicture<IPixel> image) throws IllegalArgumentException;

  /**
   * Adds a layer to the current image's collection of layers by creating a new layer with the given
   * name.
   *
   * @param name name for layer to be added
   * @throws IllegalArgumentException if name is null, empty, starts with '.', or already exists
   *                                  within the current layer
   */
  void createLayer(String name) throws IllegalArgumentException;

  /**
   * Adds the given layer to the current image's collection of layers.
   *
   * @param layer layer to be added
   * @throws IllegalArgumentException if layer is null or already exists in current image
   */
  void createLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException;

  /**
   * Sets the image to operate on to the image at the specified index.
   *
   * @param i index of desired image
   * @throws IllegalArgumentException if index is negative or out of bounds
   */
  void setCurrentPicture(int i) throws IllegalArgumentException;

  /**
   * Sets the image to operate on to the given image.
   *
   * @param image desired image
   * @throws IllegalArgumentException if image is null or is not part of this application's list of
   *                                  images.]
   */
  void setCurrentPicture(ILayeredPicture<ILayer<IPicture<IPixel>>> image)
      throws IllegalArgumentException;

  /**
   * Sets the current image's layer to the layer with the given name.
   *
   * @param name name of desired layer to operate on
   * @throws IllegalArgumentException if name is null or doesn't exist in current image
   */
  void setCurrentLayer(String name) throws IllegalArgumentException;

  /**
   * Sets the current image's layer to operate on to the given layer.
   *
   * @param layer desired layer
   * @throws IllegalArgumentException if layer is null or doesn't exist in current image
   */
  void setCurrentLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException;

  /**
   * Removes the current image's layer of the given name.
   *
   * @param name the name of the desired layer
   * @throws IllegalArgumentException if name is null or doesn't exist in current image
   */
  void removeLayer(String name) throws IllegalArgumentException;

  /**
   * Removes the given layer from the current image's collection of layers.
   *
   * @param layer the desired layer to remove
   * @throws IllegalArgumentException if layer is null or doesn't exist in current image
   */
  void removeLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException;

  /**
   * Sets the current image's current layer's visibility to the given value.
   *
   * @param visible boolean determining whether the image's layer is visible
   */
  void setVisibility(boolean visible);

  /**
   * Exports the top-most visible layer of the current image as the given type to the given
   * location. Top-most visible layer is either the current layer if it contains a non-null image,
   * or the first layer with a non-null image derived from the layer hashmap.
   *
   * @param filepath the desired filepath
   * @param type     type supported file type in {@code FileType} enumeration
   * @throws IllegalArgumentException if filepath or type is null
   */
  void exportTopMostVisibleLayer(String filepath, FileType type) throws IllegalArgumentException;

  /**
   * Imports the image at the given file path into the current image's current layer.
   */
  void load(String filename);
}