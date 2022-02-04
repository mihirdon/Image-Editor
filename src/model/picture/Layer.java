package model.picture;

import java.util.Objects;
import model.pixel.IPixel;

/**
 * Represents a layer which makes up a multi-layered image. Contains a name, visibility, and image
 * field. Parameterized over the {@code IPicture<IPixel} implementation of an image.
 */
public class Layer implements ILayer<IPicture<IPixel>> {

  private final String name;
  private boolean visibility;
  private IPicture<IPixel> image;

  /**
   * Constructs a {@code Layer} from the given {@code ILayer<IPicture<IPixel>>}.
   *
   * @param other the layer to copy from
   * @throws IllegalArgumentException if given layer is null
   */
  public Layer(ILayer<IPicture<IPixel>> other) throws IllegalArgumentException {
    if (other == null) {
      throw new IllegalArgumentException("Other cannot be null");
    }

    name = other.getName();
    visibility = other.getVisibility();
    image = new Picture(other.getImage());
  }

  /**
   * Constructs a {@code Layer} with the given name, visibility and image.
   *
   * @param name       the desired name for this layer
   * @param visibility whether or not this layer is visible
   * @param image      the image this layer stores
   * @throws IllegalArgumentException if given name is null or empty
   */
  public Layer(String name, boolean visibility, IPicture<IPixel> image)
      throws IllegalArgumentException {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty or null");
    }

    this.name = name;
    this.visibility = visibility;
    this.image = image;
  }

  /**
   * Constructs a {@code Layer} with the given name and image. Visibility is automatically set to
   * true.
   *
   * @param name  the desired name for this layer
   * @param image the image this layer stores
   * @throws IllegalArgumentException if given name is null or empty
   */
  public Layer(String name, IPicture<IPixel> image) throws IllegalArgumentException {
    this(name, true, image);
  }

  /**
   * Constructs a {@code Layer} with the given name and null as its image (to represent that it is
   * empty). Visibility is automatically set to true.
   *
   * @param name the desired name for this layer
   * @throws IllegalArgumentException if given name is null or empty
   */
  public Layer(String name) throws IllegalArgumentException {
    this(name, null);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean getVisibility() {
    return visibility;
  }

  @Override
  public IPicture<IPixel> getImage() {
    if (image == null) {
      return null;
    }

    return new Picture(image);
  }

  @Override
  public void setVisibility(boolean visibility) {
    this.visibility = visibility;
  }

  @Override
  public void setImage(IPicture<IPixel> image) {
    this.image = image;
  }

  @Override
  public String toString() {
    StringBuilder result;
    result = new StringBuilder();

    result.append(String.format("Name: %s\n", getName()));
    result.append(String.format("Visible: %b\n", getVisibility()));

    if (image != null) {
      result.append(getImage().toString()).append("\n");
    }

    return result.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (!(other instanceof Layer)) {
      return false;
    }

    boolean sameImage;

    if (image == null) {
      sameImage = null == ((Layer) other).getImage();
    } else {
      sameImage = getImage().equals(((Layer) other).getImage());
    }

    return getName().equals(((Layer) other).getName())
        && getVisibility() == ((Layer) other).getVisibility()
        && sameImage;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, visibility, image);
  }
}