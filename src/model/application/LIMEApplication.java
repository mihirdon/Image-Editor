package model.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.application.ImportExportUtil.FileType;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.picture.Layer;
import model.picture.LayeredPicture;
import model.pixel.IPixel;

/**
 * Represents an Image Processing Application which can operate on several multi-layered images,
 * offering the functionality of an {@code IApplication}. Parameterized over the {@code
 * ILayeredPicture} implementation.
 */
public class LIMEApplication implements ILayeredApplication {

  protected final List<ILayeredPicture<ILayer<IPicture<IPixel>>>> images;
  protected int currentImage;

  /**
   * Constructs a {@code LIMEApplication} from the given list of multi-layered images.
   *
   * @param images the list of images to work on
   * @throws IllegalArgumentException if given list of images is null
   */
  public LIMEApplication(List<ILayeredPicture<ILayer<IPicture<IPixel>>>> images)
      throws IllegalArgumentException {
    if (images == null) {
      throw new IllegalArgumentException("List of images cannot be null");
    }

    this.images = images;
    this.currentImage = 0;
  }

  /**
   * Constructs a {@code LIMEApplication} with an empty list of images.
   */
  public LIMEApplication() {
    this(new ArrayList<>());
  }

  @Override
  public List<ILayeredPicture<ILayer<IPicture<IPixel>>>> getImages() {
    List<ILayeredPicture<ILayer<IPicture<IPixel>>>> newImages = new ArrayList<>();

    for (ILayeredPicture<ILayer<IPicture<IPixel>>> image : images) {
      ILayeredPicture<ILayer<IPicture<IPixel>>> layeredPic =
          new LayeredPicture(image.getWidth(), image.getHeight(),
              image.getMaxVal());

      for (int i = 0; i < image.getLayers().size(); i++) {
        layeredPic.addLayer(new Layer(image.getLayers().get(i)));
      }

      newImages.add(layeredPic);
    }

    return newImages;
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> getImage(int index)
      throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index is negative or out of bounds");
    }

    ILayeredPicture<ILayer<IPicture<IPixel>>> oldPic = images.get(index);
    ILayeredPicture<ILayer<IPicture<IPixel>>> layPic = new LayeredPicture(oldPic.getWidth(),
        oldPic.getHeight(), oldPic.getMaxVal());

    for (int i = 0; i < oldPic.getLayers().size(); i++) {
      layPic.addLayer(new Layer(oldPic.getLayers().get(i)));
    }

    return layPic;
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> getCurrentImage() {
    try {
      return images.get(currentImage);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  @Override
  public void addImage(ILayeredPicture<ILayer<IPicture<IPixel>>> image)
      throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Given image cannot be null");
    }

    images.add(image);
  }

  @Override
  public void removeImage(int index) throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index is negative or out of bounds");
    }

    images.remove(index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> blur(int index) throws IllegalArgumentException {
    if (index < 0 || index >= images.get(currentImage).getLayers().size()) {
      throw new IllegalArgumentException("Index is negative or out of bounds");
    }

    return returnLayeredImage(addLayersToApp().blur(index), index);
  }

  // Returns an application containing all the images that each layer of the current image contains.
  private IApplication<IPicture<IPixel>> addLayersToApp() {
    IApplication<IPicture<IPixel>> app = new Application();
    ILayeredPicture<ILayer<IPicture<IPixel>>> curImage = getCurrentImage();

    for (int i = 0; i < curImage.getLayers().size(); i++) {
      app.addImage(curImage.getLayers().get(i).getImage());
    }

    return app;
  }

  // Returns a new layered image with the image of the layer specified by the index replaced
  // with the given image.
  private ILayeredPicture<ILayer<IPicture<IPixel>>> returnLayeredImage(IPicture<IPixel> image,
      int index) {
    ILayeredPicture<ILayer<IPicture<IPixel>>> curImage = getCurrentImage();

    ILayeredPicture<ILayer<IPicture<IPixel>>> returnImage = new LayeredPicture(curImage.getWidth(),
        curImage.getHeight(),
        curImage.getMaxVal());

    for (int i = 0; i < curImage.getLayers().size(); i++) {
      if (i == index) {
        returnImage.addLayer(new Layer(curImage.getLayers().get(i).getName(), image));
      } else {
        returnImage.addLayer(curImage.getLayers().get(i));
      }
    }

    return returnImage;
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> sharpen(int index)
      throws IllegalArgumentException {
    if (index < 0 || index >= images.get(currentImage).getLayers().size()) {
      throw new IllegalArgumentException("Index is negative or out of bounds");
    }

    return returnLayeredImage(addLayersToApp().sharpen(index), index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> monochrome(int index)
      throws IllegalArgumentException {
    if (index < 0 || index >= images.get(currentImage).getLayers().size()) {
      throw new IllegalArgumentException("Index is negative or out of bounds");
    }

    return returnLayeredImage(addLayersToApp().monochrome(index), index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> sepiaTone(int index)
      throws IllegalArgumentException {
    if (index < 0 || index >= images.get(currentImage).getLayers().size()) {
      throw new IllegalArgumentException("Index is negative or out of bounds");
    }

    return returnLayeredImage(addLayersToApp().sepiaTone(index), index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> createCheckerBoard(int tileSize, int numTiles)
      throws IllegalArgumentException {
    if (tileSize <= 0 || numTiles <= 0) {
      throw new IllegalArgumentException("Number of pixels and tiles must be positive");
    }

    ILayeredPicture<ILayer<IPicture<IPixel>>> returnImage =
        new LayeredPicture(tileSize * numTiles, tileSize * numTiles, 255);

    returnImage.addLayer(new Layer("Checkerboard",
        new Application().createCheckerBoard(tileSize, numTiles)));

    return returnImage;
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> importImage(String filename)
      throws IllegalArgumentException {
    if (filename == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    return importImage(filename, FileType.PPM);
  }

  // Filename represents the name of the text file containing the desired layered image data.
  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> importImage(String filename, FileType type)
      throws IllegalArgumentException {
    if (filename == null) {
      throw new IllegalArgumentException("File path cannot be null");
    } else if (type == null) {
      throw new IllegalArgumentException("File type cannot be null");
    }

    return ImportExportUtil.importImageLayered(filename, type);
  }

  @Override
  public void exportImage(int index, String name)
      throws IllegalArgumentException {
    if (index < 0 || index >= images.get(currentImage).getLayers().size()) {
      throw new IllegalArgumentException("Index is negative or out of bounds");
    }

    exportImage(images.get(index), name, FileType.PPM);
  }

  @Override
  public void exportImage(ILayeredPicture<ILayer<IPicture<IPixel>>> image, String filename,
      FileType type) throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Given image cannot be null");
    } else if (filename == null || filename.isEmpty() || filename.charAt(0) == '.') {
      throw new IllegalArgumentException("File name is invalid");
    } else if (type == null) {
      throw new IllegalArgumentException("File type cannot be null");
    }

    ImportExportUtil.exportImageLayered(image, filename, type);
  }

  @Override
  public void exportTopMostVisibleLayer(String filename, FileType type)
      throws IllegalArgumentException {
    if (filename == null || type == null) {
      throw new IllegalArgumentException("File path and type cannot be null");
    }

    ILayer<IPicture<IPixel>> exportLayer = null;
    ILayer<IPicture<IPixel>> currentLayer = getCurrentImage().getCurrentLayer();

    if (currentLayer.getImage() != null
        && currentLayer.getVisibility()) {
      exportLayer = currentLayer;
    } else {
      for (ILayer<IPicture<IPixel>> layer : getCurrentImage().getLayers()) {
        if (layer.getVisibility()) {
          exportLayer = layer;
          break;
        }
      }
    }

    if (exportLayer == null) {
      throw new IllegalStateException("Current image has no visible layers");
    }

    try {
      ImportExportUtil.exportImageRegular(
          exportLayer.getImage(), filename, type);
    } catch (IOException e) {
      throw new IllegalArgumentException("IO exception occurred");
    }
  }

  @Override
  public void setImage(IPicture<IPixel> image) throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Given image is null");
    }

    getCurrentImage().getCurrentLayer().setImage(image);
  }

  @Override
  public void createLayer(String name) {
    if (name == null || name.isEmpty() || name.charAt(0) == '.') {
      throw new IllegalArgumentException("File name cannot be null, empty, or start with '.'");
    }

    getCurrentImage().addLayer(name);
  }

  @Override
  public void createLayer(ILayer<IPicture<IPixel>> layer) {
    getCurrentImage().addLayer(layer);
  }

  @Override
  public void setCurrentPicture(int i) {
    if (i < 0 | i >= images.size()) {
      throw new IllegalArgumentException("Index is negative or out of bounds");
    }

    this.currentImage = i;
  }

  @Override
  public void setCurrentPicture(ILayeredPicture<ILayer<IPicture<IPixel>>> picture)
      throws IllegalArgumentException {
    if (!images.contains(picture)) {
      throw new IllegalArgumentException("Picture is not within images");
    }

    this.currentImage = images.indexOf(picture);
  }

  @Override
  public void setCurrentLayer(String name) throws IllegalArgumentException {
    getCurrentImage().setCurrentLayer(name);
  }

  @Override
  public void setCurrentLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException {
    if (layer == null) {
      throw new IllegalArgumentException("Layer cannot be null");
    }

    getCurrentImage().setCurrentLayer(layer.getName());
  }

  @Override
  public void removeLayer(String name) throws IllegalArgumentException {
    getCurrentImage().removeLayer(name);
  }

  @Override
  public void removeLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException {
    getCurrentImage().removeLayer(layer);
  }

  @Override
  public void setVisibility(boolean visible) {
    getCurrentImage().setVisibility(visible);
  }

  @Override
  public void load(String filename) throws IllegalArgumentException {
    if (filename == null || filename.isEmpty()) {
      throw new IllegalArgumentException("Filename cannot be null or empty");
    }

    ILayeredPicture<ILayer<IPicture<IPixel>>> curPic = getCurrentImage();
    IPicture<IPixel> pic;

    if (filename.contains(".ppm")) {
      try {
        pic = ImportExportUtil.importImageRegular(filename, FileType.PPM);
      } catch (IOException i) {
        throw new IllegalArgumentException("IOException occurred");
      }
    } else if (filename.contains(".jpeg")) {
      try {
        pic = ImportExportUtil.importImageRegular(filename, FileType.JPEG);
      } catch (IOException i) {
        throw new IllegalArgumentException("IOException occurred");
      }
    } else if (filename.contains(".png")) {
      try {
        pic = ImportExportUtil.importImageRegular(filename, FileType.PNG);
      } catch (IOException i) {
        throw new IllegalArgumentException("IOException occurred");
      }
    } else {
      throw new IllegalArgumentException("Invalid ending");
    }

    curPic.setCurrentLayerTo(pic);

    if (curPic.getHeight() == 0 && curPic.getWidth() == 0) {
      curPic.setWidth(pic.getWidth());
      curPic.setHeight(pic.getHeight());
    }
  }
}