package model.application;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import model.application.ImportExportUtil.FileType;
import model.picture.ExtraCreditPictureUtil;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.picture.Picture;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import model.pixel.Pixel;

/**
 * A LIMEApplicationWithExtraCredit is a type of application that allows for the concrete use of the
 * mosaic method, while having all of the same functionality as an {@link ILayeredApplication}
 * through the use of its ILayeredApplication object.
 */
public class LIMEApplicationWithExtraCredit implements IExtraCredit {

  private final Random rand;
  private final List<List<IPixel>> randSeeds;
  private final List<Point> randomSeedsCoords;
  private ILayeredApplication app;

  /**
   * Creates an object of LIMEApplicationWithExtraCredit with a list of images already initialized
   * within the object.
   *
   * @param images the list of images to be initialized within the object
   */
  public LIMEApplicationWithExtraCredit(List<ILayeredPicture<ILayer<IPicture<IPixel>>>> images) {
    if (images == null || images.isEmpty()) {
      throw new IllegalArgumentException("Please initialize images to a valid value");
    }
    this.rand = new Random();
    this.randSeeds = new ArrayList<>();
    this.randomSeedsCoords = new ArrayList<>();
    this.app = new LIMEApplication();
    for (ILayeredPicture<ILayer<IPicture<IPixel>>> image : images) {
      app.addImage(image);
    }
  }

  /**
   * Creates an object of LIMEApplicationWithExtraCredit with the default values, with a random
   * variable initialized to the default seed(1), a newly created arraylist for the random seeds
   * within the class, a newly created arraylist for the points of the random seeds, and a default
   * application initialized to the LIMEApplication.
   */
  public LIMEApplicationWithExtraCredit() {

    this.rand = new Random();
    this.randSeeds = new ArrayList<>();
    this.randomSeedsCoords = new ArrayList<>();
    this.app = new LIMEApplication();
  }

  /**
   * Takes in an ILayeredApplication to be used as the object that all of the methods within this
   * call, allowing it to have all of the functionality of an ILayeredApplication.
   *
   * @param app the application that will be used for the ILayeredApplication methods.
   */
  public LIMEApplicationWithExtraCredit(ILayeredApplication app) {
    this();
    this.app = app;
  }

  @Override
  public void downsizing(int widthPrime, int heightPrime) {
    ILayeredPicture<ILayer<IPicture<IPixel>>> curPic = app.getCurrentImage();
    ILayer<IPicture<IPixel>> curLayer = curPic.getCurrentLayer();

    for (ILayer<IPicture<IPixel>> layer : curPic.getLayers()) {
      IPixel[][] newImage = new ExtraCreditPictureUtil().downsizing(
          layer.getImage(), widthPrime, heightPrime);
      app.setCurrentLayer(layer);
      app.setImage(new Picture(widthPrime, heightPrime, 255, newImage));
    }

    app.setCurrentLayer(curLayer);
  }

  @Override
  public void mosaic(int numOfSeeds) throws IllegalArgumentException {
    IPicture<IPixel> curPic = app.getCurrentImage().getCurrentLayer().getImage();

    if (numOfSeeds > curPic.getHeight() * curPic.getWidth()) {
      throw new IllegalArgumentException("Cannot be more seeds then there are pixels");
    }
    IPixel[][] imageSubset = curPic.getPixelSequence();

    populateRandLists(numOfSeeds, imageSubset);
    formTiles(imageSubset);
    IPixel[][] newImage = makeNewMosaicImage(imageSubset);

    app.setImage(new Picture(curPic.getWidth(), curPic.getHeight(), curPic.getMaxVal(),
        newImage));
  }

  //populates the lists of seeds and coordinates with randomly chosen points
  private void populateRandLists(int numOfSeeds, IPixel[][] imageSubset) {
    while (randomSeedsCoords.size() < numOfSeeds) {
      int width = rand.nextInt(imageSubset[0].length);
      int height = rand.nextInt(imageSubset.length);
      if (!randomSeedsCoords.contains(new Point(width, height))) {
        randSeeds.add(new ArrayList<>(Collections.singletonList(imageSubset[height][width])));
        randomSeedsCoords.add(new Point(width, height));
      }
    }
  }

  //forms the actual tiles by adding pixels closest to each seed to their specific array list
  private void formTiles(IPixel[][] imageSubset) {
    for (IPixel[] row : imageSubset) {
      for (IPixel pixel : row) {
        int nearestRandInt =
            findClosestSeedInt(pixel.getCoordinates(), randomSeedsCoords);
        randSeeds.get(nearestRandInt).add(pixel);
      }
    }
  }

  //sets all of the pixel values to the appropriate average value
  private IPixel[][] makeNewMosaicImage(IPixel[][] imageSubset) {
    for (List<IPixel> tile : randSeeds) {
      int r = getAverageValue(tile, ColorChannel.RED);
      int g = getAverageValue(tile, ColorChannel.GREEN);
      int b = getAverageValue(tile, ColorChannel.BLUE);

      for (IPixel pix : tile) {
        imageSubset[(int) pix.getCoordinates().getY()][(int) pix.getCoordinates().getX()]
            = new Pixel(pix.getCoordinates(), 255, r, g, b);
      }
    }

    return imageSubset;
  }

  //finds the closest seed to the given point using the coordinates of those random seeds, returns
  //where in the coordinate list the given seed is
  private int findClosestSeedInt(Point pixPoint, List<Point> randSeedsCoords) {
    Point current = randSeedsCoords.get(0);
    int winner = 0;

    for (int i = 0; i < randSeedsCoords.size(); i++) {
      if (Math.abs(pixPoint.distance(randSeedsCoords.get(i)))
          < Math.abs(pixPoint.distance(current))) {
        current = randSeedsCoords.get(i);
        winner = i;
      }
    }

    return winner;
  }

  //gets the average pixel value of the given list of the specific type of color specified
  private int getAverageValue(List<IPixel> tile, ColorChannel color) {
    int average = 0;
    for (IPixel pix : tile) {
      average += pix.getChannelValue(color);
    }

    average = average / tile.size();
    return average;
  }

  @Override
  public List<ILayeredPicture<ILayer<IPicture<IPixel>>>> getImages() {
    return app.getImages();
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> getImage(int index)
      throws IllegalArgumentException {
    return app.getImage(index);
  }

  @Override
  public void addImage(ILayeredPicture<ILayer<IPicture<IPixel>>> image)
      throws IllegalArgumentException {
    app.addImage(image);
  }

  @Override
  public void removeImage(int index) throws IllegalArgumentException {
    app.removeImage(index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> blur(int index) throws IllegalArgumentException {
    return app.blur(index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> sharpen(int index)
      throws IllegalArgumentException {
    return app.sharpen(index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> monochrome(int index)
      throws IllegalArgumentException {
    return app.monochrome(index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> sepiaTone(int index)
      throws IllegalArgumentException {
    return app.sepiaTone(index);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> createCheckerBoard(int tileSize, int numTiles)
      throws IllegalArgumentException {
    return app.createCheckerBoard(tileSize, numTiles);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> importImage(String filename)
      throws IllegalArgumentException {
    return importImage(filename, FileType.PPM);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> importImage(String fileName, FileType type)
      throws IllegalArgumentException {
    return app.importImage(fileName, type);
  }

  @Override
  public void exportImage(int index, String name)
      throws IllegalArgumentException, FileNotFoundException {
    exportImage(getImages().get(index), name, FileType.PPM);
  }

  @Override
  public void exportImage(ILayeredPicture<ILayer<IPicture<IPixel>>> image, String name,
      FileType type) throws IllegalArgumentException {
    app.exportImage(image, name, type);
  }

  @Override
  public ILayeredPicture<ILayer<IPicture<IPixel>>> getCurrentImage() {
    return app.getCurrentImage();
  }

  @Override
  public void setImage(IPicture<IPixel> image) throws IllegalArgumentException {
    app.setImage(image);
  }

  @Override
  public void createLayer(String name) throws IllegalArgumentException {
    app.createLayer(name);
  }

  @Override
  public void createLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException {
    app.createLayer(layer);
  }

  @Override
  public void setCurrentPicture(int i) throws IllegalArgumentException {
    app.setCurrentPicture(i);
  }

  @Override
  public void setCurrentPicture(ILayeredPicture<ILayer<IPicture<IPixel>>> image)
      throws IllegalArgumentException {
    app.setCurrentPicture(image);
  }

  @Override
  public void setCurrentLayer(String name) throws IllegalArgumentException {
    app.setCurrentLayer(name);
  }

  @Override
  public void setCurrentLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException {
    app.setCurrentLayer(layer);
  }

  @Override
  public void removeLayer(String name) throws IllegalArgumentException {
    app.removeLayer(name);
  }

  @Override
  public void removeLayer(ILayer<IPicture<IPixel>> layer) throws IllegalArgumentException {
    app.removeLayer(layer);
  }

  @Override
  public void setVisibility(boolean visible) {
    app.setVisibility(visible);
  }

  @Override
  public void exportTopMostVisibleLayer(String filepath, FileType type)
      throws IllegalArgumentException {
    app.exportTopMostVisibleLayer(filepath, type);
  }

  @Override
  public void load(String filename) {
    app.load(filename);
  }
}
