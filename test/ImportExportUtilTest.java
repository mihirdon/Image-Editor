import static org.junit.Assert.assertEquals;

import java.io.IOException;
import model.application.ILayeredApplication;
import model.application.IMultipleFormatApplication;
import model.application.ImportExportUtil;
import model.application.ImportExportUtil.FileType;
import model.application.LIMEApplication;
import model.application.MultipleFormatApplication;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.pixel.IPixel;
import org.junit.Test;

/**
 * Test class for {@code ImportExportUtilTest} testing the import and export functionality.
 */
public class ImportExportUtilTest {
  // Tests the FileType enumerations gets the correct extensions for each type.
  @Test
  public void testFileType() {
    assertEquals(".jpeg", FileType.JPEG.getExtension());
    assertEquals(".png", FileType.PNG.getExtension());
    assertEquals(".ppm", FileType.PPM.getExtension());
  }

  // Tests importImageRegular() method correctly imports each file type.
  @Test
  public void testImportImageRegular() throws IOException {
    IMultipleFormatApplication<IPicture<IPixel>> application = new MultipleFormatApplication();

    IPicture<IPixel> expected = application.createCheckerBoard(1, 2);

    assertEquals(expected,
        ImportExportUtil.importImageRegular("res/SmallBoard.ppm", FileType.PPM));

    assertEquals(expected,
        ImportExportUtil.importImageRegular("res/SmallBoard.png", FileType.PNG));

    // Compression slightly changes RGB values, making JPEG tests inaccurate.
  }

  // Tests importImageRegular() method throws exception when given a null file path.
  @Test(expected = IllegalArgumentException.class)
  public void testImportRegularNullName() throws IOException {
    ImportExportUtil.importImageRegular(null, FileType.PPM);
  }

  // Tests importImageRegular() method throws exception when given a null file type.
  @Test(expected = IllegalArgumentException.class)
  public void testImportRegularNullType() throws IOException {
    ImportExportUtil.importImageRegular("res/SmallBoard.ppm", null);
  }

  // Tests exportImageRegular() correctly provides the given string in PPM format.
  // Only testing PPM format since JPEG and PNG rely on ImageIO.
  @Test
  public void testExportImageRegular() throws IOException {
    StringBuilder output = new StringBuilder();

    MockImportExportUtil mock = new MockImportExportUtil(output);

    mock.exportRegularImage(new MultipleFormatApplication().createCheckerBoard(1, 2),
        "TestExport");

    String expected = "P3\n"
        + "2 2\n"
        + "255\n"
        + "255\n"
        + "255\n"
        + "255\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "255\n"
        + "255\n"
        + "255\n";

    assertEquals(expected, output.toString());
  }

  // Tests exportImageRegular() method throws an exception when given a null image.
  @Test(expected = IllegalArgumentException.class)
  public void testExportRegularNullImage() throws IOException {
    ImportExportUtil.exportImageRegular(null, "test", FileType.PPM);
  }

  // Tests exportImageRegular() method throws an exception when given a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testExportRegularNullName() throws IOException {
    ImportExportUtil.exportImageRegular(
        new MultipleFormatApplication().createCheckerBoard(1, 2),
        null, FileType.PPM);
  }

  // Tests exportImageRegular() method throws an exception when given a empty name.
  @Test(expected = IllegalArgumentException.class)
  public void testExportRegularEmptyName() throws IOException {
    ImportExportUtil.exportImageRegular(
        new MultipleFormatApplication().createCheckerBoard(1, 2),
        "", FileType.PPM);
  }

  // Tests exportImageRegular() method throws an exception when given a name starting with '.'.
  @Test(expected = IllegalArgumentException.class)
  public void testExportRegularDotName() throws IOException {
    ImportExportUtil.exportImageRegular(
        new MultipleFormatApplication().createCheckerBoard(1, 2),
        ".test", FileType.PPM);
  }

  // Tests exportImageRegular() method throws an exception when given a null type.
  @Test(expected = IllegalArgumentException.class)
  public void testExportRegularNullType() throws IOException {
    ImportExportUtil.exportImageRegular(
        new MultipleFormatApplication().createCheckerBoard(1, 2),
        "test", null);
  }

  // Tests importImageLayered() method correctly imports the specified layered image.
  @Test
  public void testImportImageLayered() {
    ILayeredApplication application = new LIMEApplication();

    ILayeredPicture<ILayer<IPicture<IPixel>>> image =
        application.createCheckerBoard(1, 2);

    assertEquals(image,
        ImportExportUtil.importImageLayered("res/PPMLayeredBoard.txt", FileType.PPM));

    assertEquals(image,
        ImportExportUtil.importImageLayered("res/PNGLayeredBoard.txt", FileType.PNG));

    // Compression slightly changes RGB values, making JPEG tests inaccurate.
  }

  // Tests importImageLayered() method throws exception when given a null file path.
  @Test(expected = IllegalArgumentException.class)
  public void testImportLayeredNullName() {
    ImportExportUtil.importImageLayered(null, FileType.PPM);
  }

  // Tests importImageLayered() method throws exception when given a null type.
  @Test(expected = IllegalArgumentException.class)
  public void testImportLayeredNullType() {
    ImportExportUtil.importImageLayered("res/PPMLayeredBoard.txt", null);
  }

  // Tests exportImageLayered() method correctly provides the string representing the text file
  // storing the layered image data for each supported file type.
  @Test
  public void testExportImageLayered() {
    StringBuilder ppmOutput = new StringBuilder();

    new MockImportExportUtil(ppmOutput).exportLayeredImage(
        new LIMEApplication().createCheckerBoard(1, 2),
        "PPMLayeredBoard", FileType.PPM);

    String ppmExpected = "PPMLayeredBoard.txt\n"
        + "2 2 255\n"
        + "Checkerboard true res/Checkerboard.ppm\n";

    assertEquals(ppmExpected, ppmOutput.toString());

    StringBuilder pngOutput = new StringBuilder();

    new MockImportExportUtil(pngOutput).exportLayeredImage(
        new LIMEApplication().createCheckerBoard(1, 2),
        "PNGLayeredBoard", FileType.PNG);

    String pngExpected = "PNGLayeredBoard.txt\n"
        + "2 2 255\n"
        + "Checkerboard true res/Checkerboard.png\n";

    assertEquals(pngExpected, pngOutput.toString());

    StringBuilder jpegOutput = new StringBuilder();

    new MockImportExportUtil(jpegOutput).exportLayeredImage(
        new LIMEApplication().createCheckerBoard(1, 2),
        "JPEGLayeredBoard", FileType.JPEG);

    String jpegExpected = "JPEGLayeredBoard.txt\n"
        + "2 2 255\n"
        + "Checkerboard true res/Checkerboard.jpeg\n";

    assertEquals(jpegExpected, jpegOutput.toString());
  }

  // Tests exportImageLayered() method throws an exception when given a null image.
  @Test(expected = IllegalArgumentException.class)
  public void testExportLayeredNullImage() {
    ImportExportUtil.exportImageLayered(null, "test", FileType.PPM);
  }

  // Tests exportImageLayered() method throws an exception when given a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testExportLayeredNullName() {
    ImportExportUtil.exportImageLayered(
        new LIMEApplication().createCheckerBoard(1, 2), null, FileType.PPM);
  }

  // Tests exportImageLayered() method throws an exception when given an empty name.
  @Test(expected = IllegalArgumentException.class)
  public void testExportLayeredEmptyName() {
    ImportExportUtil.exportImageLayered(
        new LIMEApplication().createCheckerBoard(1, 2), "", FileType.PPM);
  }

  // Tests exportImageLayered() method throws an exception when given a name starting with '.'.
  @Test(expected = IllegalArgumentException.class)
  public void testExportLayeredDotName() {
    ImportExportUtil.exportImageLayered(
        new LIMEApplication().createCheckerBoard(1, 2), ".", FileType.PPM);
  }

  // Tests exportImageLayered() method throws an exception when given a null type.
  @Test(expected = IllegalArgumentException.class)
  public void testExportLayeredNullType() {
    ImportExportUtil.exportImageLayered(
        new LIMEApplication().createCheckerBoard(1, 2), "test", null);
  }
}