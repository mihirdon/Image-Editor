package model.application;

import model.application.ImportExportUtil.FileType;

/**
 * Interface representing an improved version of an application which supports importing and
 * exporting multiple file formats.
 */
public interface IMultipleFormatApplication<I> extends IApplication<I> {

  /**
   * Imports the image of the given type at the given file path.
   *
   * @param fileName file path of the image to import with extension
   * @param type     type supported file type in {@code FileType} enumeration
   * @return the 'I' image representation of desired image
   * @throws IllegalArgumentException if filename is null or type is null
   */
  I importImage(String fileName, FileType type) throws IllegalArgumentException;

  /**
   * Exports the given image with the given name as the given file type. Exports to the "res"
   * folder.
   *
   * @param image desired image to export
   * @param name  name of exported image without extension
   * @param type  type supported file type in {@code FileType} enumeration
   * @throws IllegalArgumentException if image is null, name is null, empty, or starts with '.', or
   *                                  type is null
   */
  void exportImage(I image, String name, FileType type) throws IllegalArgumentException;
}
