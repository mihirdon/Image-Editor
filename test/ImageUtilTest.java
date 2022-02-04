import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import model.application.ImageUtil;
import model.application.Application;
import model.application.IApplication;
import model.picture.IPicture;
import model.pixel.IPixel;
import org.junit.Test;

/**
 * Test class for the {@code ImageUtils} class.
 * Ensures PPM images are imported correctly with all valid restrictions.
 */
public class ImageUtilTest {
  // Tests importPPM() method in the ImageUtil() class.
  @Test
  public void testImportPPM() throws FileNotFoundException {
    IApplication<IPicture<IPixel>> application = new Application();

    assertEquals(application.createCheckerBoard(1, 2),
        ImageUtil.importPPM("res/SmallBoard.ppm"));
  }

  // Tests importImage() method throws an exception when given a null path.
  @Test(expected = IllegalArgumentException.class)
  public void testImportImageNullPath() throws FileNotFoundException {
    ImageUtil.importPPM(null);
  }

  // Tests importImage() method throws an exception when given an incorrect path.
  @Test(expected = FileNotFoundException.class)
  public void testImportImageIncorrectPath() throws FileNotFoundException {
    ImageUtil.importPPM("res/Doesn'tExist.ppm");
  }
}