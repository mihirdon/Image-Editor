package model.picture;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import model.pixel.ColorChannel;
import model.pixel.IPixel;

/**
 * Represents an image made up of several layers which each represent their own image. Offers the
 * same functionality as an {@code IPicture<IPixel>} in addition to the ability to add or remove
 * layers, choose a layer to operate on, or change the visibility of a layer. Parameterized over the
 * {@code ILayer<IPicture<IPixel>>} implementation of a layer. Invariant: Each layer can only
 * contain images that match this {@code LayeredPicture}'s width and height.
 */
public class LayeredPicture implements ILayeredPicture<ILayer<IPicture<IPixel>>> {

  private final Map<String, ILayer<IPicture<IPixel>>> layers;
  private String currentLayer;
  private int width;
  private int height;
  private final int maxVal;

  /**
   * Constructs a {@code LayeredPicture} from the given {@code ILayeredPicture}. Current layer is
   * initialized as null.
   *
   * @param other the layered image to copy
   * @throws IllegalArgumentException if given image is null
   */
  public LayeredPicture(ILayeredPicture<ILayer<IPicture<IPixel>>> other)
      throws IllegalArgumentException {
    if (other == null) {
      throw new IllegalArgumentException("Other cannot be null");
    }

    layers = new HashMap<>();

    for (ILayer<IPicture<IPixel>> layer : other.getLayers()) {
      layers.put(layer.getName(), new Layer(layer));
    }

    width = other.getWidth();
    height = other.getHeight();
    maxVal = other.getMaxVal();

    currentLayer = null;
  }

  /**
   * Constructs a {@code LayeredPicture} with no layers. Current layer is initialized as null.
   *
   * @param width  the width each image must match
   * @param height the height each image must match
   * @param maxVal the maximum value that each image's pixels can have for their color channel
   *               values.
   * @throws IllegalArgumentException width or height are not positive, or maxVal is negative
   */
  public LayeredPicture(int width, int height, int maxVal) throws IllegalArgumentException {
    this.layers = new HashMap<>();

    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Width and height must be positive");
    } else if (maxVal < 0) {
      throw new IllegalArgumentException("Maximum value cannot be negative");
    }

    this.width = width;
    this.height = height;
    this.maxVal = maxVal;

    currentLayer = null;
  }

  /**
   * Constructs a {@code LayeredPicture} with empty layers from the given list of names. Current
   * layer is initialized as null.
   *
   * @param width  the width each image must match
   * @param height the height each image must match
   * @param maxVal the maximum value that each image's pixels can have for their color channel
   *               values.
   * @param layers the list of layers to add
   * @throws IllegalArgumentException if layers is null or any layer in the list is null, width or
   *                                  height are not positive, or maxVal is negative
   */
  public LayeredPicture(int width, int height, int maxVal, List<ILayer<IPicture<IPixel>>> layers)
      throws IllegalArgumentException {
    this(width, height, maxVal);

    if (layers == null) {
      throw new IllegalArgumentException("Given list of layers cannot be null");
    }

    for (ILayer<IPicture<IPixel>> layer : layers) {
      addLayer(layer);
    }
  }

  /**
   * Constructs a {@code LayeredPicture} with empty layers from the given list of names.
   * Automatically sets the visibility of each layer to 'true'. Current layer is initialized as
   * null.
   *
   * @param width  the width each image must match
   * @param height the height each image must match
   * @param maxVal the maximum value that each image's pixels can have for their color channel
   *               values.
   * @param names  the list of names which are used to create empty layers
   * @throws IllegalArgumentException if any of the given names is null, width or height are not
   *                                  positive, or maxVal is negative
   */
  public LayeredPicture(int width, int height, int maxVal, String... names)
      throws IllegalArgumentException {
    this(width, height, maxVal);

    for (String name : names) {
      if (name == null) {
        throw new IllegalArgumentException("Cannot be given a null name");
      } else {
        layers.put(name, new Layer(name));
      }
    }
  }

  /**
   * Constructs a {@code LayeredPicture} with no layers, a width and height of 0, and maximum value
   * of 255. Current layer is initialized as null.
   */
  public LayeredPicture() {
    this(0, 0, 255);
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

  // Returns the pixel sequence of the currently-selected layer's image.
  @Override
  public IPixel[][] getPixelSequence() {
    return getCurrentImage().getPixelSequence();
  }

  // Returns the currently-selected layer's image.
  // Throws an IllegalStateException if currentLayer is set to null.
  private IPicture<IPixel> getCurrentImage() {
    if (currentLayer == null) {
      throw new IllegalStateException("No layer has been selected");
    }

    return getCurrentLayer().getImage();
  }

  @Override
  public int[][] getChannelValues(ColorChannel channel) throws IllegalArgumentException {
    return getCurrentImage().getChannelValues(channel);
  }

  @Override
  public int[][] getImageSubset(int dimension, Point center, ColorChannel channel)
      throws IllegalArgumentException {
    return getCurrentImage().getImageSubset(dimension, center, channel);
  }

  @Override
  public List<ILayer<IPicture<IPixel>>> getLayers() {
    List<ILayer<IPicture<IPixel>>> result = new ArrayList<>();

    for (ILayer<IPicture<IPixel>> layer : layers.values()) {
      if (layer.getImage() != null) {
        result.add(
            new Layer(layer.getName(), layer.getVisibility(), new Picture(layer.getImage())));
      } else {
        result.add(new Layer(layer.getName(), layer.getVisibility(), layer.getImage()));
      }
    }

    return result;
  }

  @Override
  public ILayer<IPicture<IPixel>> getCurrentLayer() {
    return layers.get(currentLayer);
  }

  @Override
  public IPicture<IPixel> filter(double[][] kernel) throws IllegalArgumentException {
    return getCurrentImage().filter(kernel);
  }

  @Override
  public IPicture<IPixel> colorTransform(double[][] matrix) throws IllegalArgumentException {
    return getCurrentImage().colorTransform(matrix);
  }

  @Override
  public void setCurrentLayer(String name) throws IllegalArgumentException {
    if (name == null || !layers.containsKey(name)) {
      throw new IllegalArgumentException("Layer name is null or doesn't exist");
    }

    if (width == 0 && height == 0 && layers.get(name).getImage() != null) {
      IPicture<IPixel> picture = layers.get(name).getImage();
      this.width = picture.getWidth();
      this.height = picture.getHeight();
    }

    currentLayer = name;
  }

  @Override
  public void addLayer(String name) throws IllegalArgumentException {
    if (name == null || layers.containsKey(name)) {
      throw new IllegalArgumentException("Layer name is null or already exists");
    }

    layers.put(name, new Layer(name));
  }

  @Override
  public void addLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException {
    if (layer == null || layers.containsKey(layer.getName())) {
      throw new IllegalArgumentException("Layer is null or has pre-existing name");
    }

    if (layer.getImage() != null && this.width == 0 && this.height == 0) {
      this.width = layer.getImage().getWidth();
      this.height = layer.getImage().getHeight();
    }

    layers.put(layer.getName(), layer);
  }

  @Override
  public void removeLayer(String name) throws IllegalArgumentException {
    if (name == null || !layers.containsKey(name)) {
      throw new IllegalArgumentException("Layer name is null or doesn't exist");
    }

    layers.remove(name);
  }

  @Override
  public void removeLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException {
    if (layer == null || !layers.containsKey(layer.getName())) {
      throw new IllegalArgumentException("Layer is null or is not in layered image");
    }

    layers.remove(layer.getName());
  }

  @Override
  public void setVisibility(boolean visibility) {
    if (currentLayer == null) {
      throw new IllegalStateException("Layer has not been selected");
    }

    layers.get(currentLayer).setVisibility(visibility);
  }

  @Override
  public String toString() {
    StringBuilder result;
    result = new StringBuilder();

    result.append(String.format("Width: %d\n", width));
    result.append(String.format("Height: %d\n", height));
    result.append(String.format("Maximum value: %d\n", maxVal));
    result.append(String.format("Current layer: %s\n", currentLayer));

    for (ILayer<IPicture<IPixel>> layer : layers.values()) {
      result.append(layer.toString()).append("\n");
    }

    return result.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (!(other instanceof LayeredPicture)) {
      return false;
    }

    boolean sameCurrentLayer;

    if (currentLayer == null) {
      sameCurrentLayer = ((LayeredPicture) other).getCurrentLayer() == null;
    } else {
      sameCurrentLayer = getCurrentLayer().equals(((LayeredPicture) other).getCurrentLayer());
    }

    return width == ((LayeredPicture) other).getWidth()
        && height == ((LayeredPicture) other).getHeight()
        && maxVal == ((LayeredPicture) other).getMaxVal()
        && sameCurrentLayer
        && Arrays.deepEquals(getLayers().toArray(), ((LayeredPicture) other).getLayers().toArray());
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height, maxVal, currentLayer, layers);
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public ILayer<IPicture<IPixel>> getLayer(String s) throws IllegalArgumentException {
    if (s == null || s.isEmpty() || !layers.containsKey(s)) {
      throw new IllegalArgumentException("String given does not exist in table or is "
          + "empty or null");
    }

    return new Layer(layers.get(s));
  }

  @Override
  public void setCurrentLayerTo(IPicture<IPixel> image) {
    if (image == null) {
      throw new IllegalArgumentException("Image is null");
    }

    layers.get(currentLayer).setImage(image);
  }
}