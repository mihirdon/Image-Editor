package view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringReader;
import java.util.Locale;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Represents a {@code IGUIView} using {@code JFrame} to represent an application as a GUI.
 */
public class GUIView extends JFrame implements IGUIView {

  // Main window of application.
  protected final JPanel mainPanel;

  // Left and right sides of the application.
  JPanel leftMainPanel;
  protected JPanel rightMainPanel;

  // Left side containing the layer creation, layer operations, and save features.
  protected JPanel layerCreationPanel;

  // Left sub-panels for layer creation and saving.
  protected JPanel currentCreate;

  // Left side buttons.
  protected JButton createLayerButton;
  protected JButton applyOperationButton;
  protected JButton saveButton;
  protected JButton scriptButton;

  // Right side buttons.
  protected JButton openButton;
  protected JButton loadButton;

  // Left drop downs.
  protected JComboBox<String> createOptions;
  protected JComboBox<String> operationOptions;
  protected JComboBox<String> saveOptions;

  // Right drop downs.
  protected JComboBox<String> layerOptions;

  // Left text areas.
  protected JTextArea layerCreationName;
  protected JTextArea tileSize;
  protected JTextArea numTiles;

  // Right text area.
  protected JTextArea display;

  // Right labels.
  protected JLabel image;
  protected JLabel openedFilePath;

  // File type filters
  protected FileNameExtensionFilter jpeg;
  protected FileNameExtensionFilter png;
  protected FileNameExtensionFilter ppm;

  //Extra Credit Panel
  protected JPanel extraCreditPanel;
  protected JComboBox<String> extraCreditOptions;
  protected JTextArea mosaicTextArea;
  protected JPanel currentExtraCredit;
  protected JTextArea newWidth;
  protected JTextArea newHeight;
  protected JButton extraCreditButton;
  protected JPanel downsizePanel;

  /**
   * Constructs a {@code GUIView} forming an image-processing application GUI.
   */
  public GUIView() {
    super();
    setTitle("LIME Application");
    setSize(1200, 800);

    mainPanel = new JPanel();
    mainPanel.setLayout(new FlowLayout());

    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    initializeMainSubPanels();
    initializeLeftPanel();
    initializeRightPanel();
  }

  protected void initializeMainSubPanels() {
    leftMainPanel = new JPanel();
    leftMainPanel.setLayout(new BoxLayout(leftMainPanel, BoxLayout.Y_AXIS));

    rightMainPanel = new JPanel();
    rightMainPanel.setLayout(new BoxLayout(rightMainPanel, BoxLayout.Y_AXIS));

    mainPanel.add(leftMainPanel);
    mainPanel.add(rightMainPanel);
  }

  protected void initializeLeftPanel() {
    layerCreationPanel = new JPanel();
    layerCreationPanel.setBorder(BorderFactory.createTitledBorder("Layer Creation"));
    layerCreationPanel.setLayout(new BoxLayout(layerCreationPanel, BoxLayout.Y_AXIS));
    leftMainPanel.add(layerCreationPanel);

    initializeCreateDropdown();
    initializeCreateLayers();
    initializeCreateButton();
    initializeLayerOperationsPanel();
    initializeSaveAndSaveAll();
    initializeScriptOperation();
    initializeExtraCredit();
    initializeExtraCreditPanels();
    initializeExtraCreditButton();
  }

  protected void initializeRightPanel() {
    initializeDisplay();
    layerSelection();
    imageDisplay();
    loadPanel();
  }

  protected void initializeCreateDropdown() {
    createOptions = new JComboBox<>();
    layerCreationPanel.add(createOptions);
    createOptions.setActionCommand("Create Options");

    createOptions.addItem("Regular Layer");
    createOptions.addItem("Checkerboard Layer");
    createOptions.setEditable(false);
  }

  protected void initializeExtraCredit() {
    extraCreditPanel = new JPanel();
    extraCreditPanel.setBorder(BorderFactory.createTitledBorder("Extra Credit Options"));
    extraCreditPanel.setLayout(new BoxLayout(extraCreditPanel, BoxLayout.Y_AXIS));
    leftMainPanel.add(extraCreditPanel);

    extraCreditOptions = new JComboBox<>();
    extraCreditPanel.add(extraCreditOptions);
    extraCreditOptions.setActionCommand("Extra Credit Options");

    extraCreditOptions.addItem("Mosaic Panel");
    extraCreditOptions.addItem("Downsize Panel");
    extraCreditOptions.setEditable(false);
  }

  protected void initializeExtraCreditPanels() {
    JPanel mosaicPanel = new JPanel();
    mosaicPanel.setLayout(new BoxLayout(mosaicPanel, BoxLayout.Y_AXIS));

    mosaicTextArea = new JTextArea(1, 8);
    JScrollPane mosaicScrollPane = new JScrollPane(mosaicTextArea);
    mosaicTextArea.setLineWrap(true);
    mosaicTextArea.setBorder(BorderFactory.createTitledBorder("Number of Tiles"));
    mosaicPanel.add(mosaicScrollPane);

    downsizePanel = new JPanel();
    downsizePanel.setLayout(new BoxLayout(downsizePanel, BoxLayout.X_AXIS));

    currentExtraCredit = new JPanel();
    currentExtraCredit.setLayout(new CardLayout());
    extraCreditPanel.add(currentExtraCredit);
    currentExtraCredit.add(mosaicPanel, "Mosaic Panel");
    currentExtraCredit.add(downsizePanel, "Downsize Panel");

    newWidth = new JTextArea(1, 8);
    JScrollPane newWidthScrollPane = new JScrollPane(newWidth);
    newWidthScrollPane.setBorder(BorderFactory.createTitledBorder("Image Width in pixels"));
    newWidth.setLineWrap(true);
    downsizePanel.add(newWidthScrollPane);

    newHeight = new JTextArea(1, 8);
    JScrollPane newHeightScrollPane = new JScrollPane(newHeight);
    newHeight.setLineWrap(true);
    newHeightScrollPane.setBorder(BorderFactory.createTitledBorder("Image Height in pixels"));
    downsizePanel.add(newHeightScrollPane);
  }

  protected void initializeCreateLayers() {
    JPanel createRegularLayer = new JPanel();
    createRegularLayer.setLayout(new BoxLayout(createRegularLayer, BoxLayout.X_AXIS));

    layerCreationName = new JTextArea(1, 8);
    JScrollPane layerScrollPane = new JScrollPane(layerCreationName);
    layerCreationName.setLineWrap(true);
    layerCreationName.setBorder(BorderFactory.createTitledBorder("Layer name"));
    createRegularLayer.add(layerScrollPane);

    JPanel createCheckerboardLayer = new JPanel();
    createCheckerboardLayer.setLayout(new BoxLayout(createCheckerboardLayer, BoxLayout.X_AXIS));

    currentCreate = new JPanel();
    currentCreate.setLayout(new CardLayout());
    layerCreationPanel.add(currentCreate);
    currentCreate.add(createRegularLayer, "Regular Layer");
    currentCreate.add(createCheckerboardLayer, "Checkerboard Layer");

    tileSize = new JTextArea(1, 8);
    JScrollPane tileSizeScrollPane = new JScrollPane(tileSize);
    tileSizeScrollPane.setBorder(BorderFactory.createTitledBorder("Tile Side Length in pixels"));
    tileSize.setLineWrap(true);
    createCheckerboardLayer.add(tileSizeScrollPane);

    numTiles = new JTextArea(1, 8);
    JScrollPane numTilesScrollPane = new JScrollPane(numTiles);
    numTiles.setLineWrap(true);
    numTilesScrollPane.setBorder(BorderFactory.createTitledBorder("Board Side Length in tiles"));
    createCheckerboardLayer.add(numTilesScrollPane);
  }

  protected void initializeExtraCreditButton() {
    extraCreditButton = new JButton("Apply Function");
    extraCreditButton.setActionCommand("Apply Function");

    extraCreditPanel.add(extraCreditButton);
  }

  protected void initializeCreateButton() {
    createLayerButton = new JButton("Create Layer");
    createLayerButton.setActionCommand("Create Button");

    layerCreationPanel.add(createLayerButton);
  }

  protected void initializeLayerOperationsPanel() {
    JPanel layerOperationPanel = new JPanel();
    layerOperationPanel.setBorder(BorderFactory.createTitledBorder("Layer Operations"));
    layerOperationPanel.setLayout(new BoxLayout(layerOperationPanel, BoxLayout.Y_AXIS));
    leftMainPanel.add(layerOperationPanel);

    operationOptions = new JComboBox<>();
    layerOperationPanel.add(operationOptions);

    String[] namesOfOperations = {"Blur", "Sharpen", "Monochrome", "Sepia", "Invisible",
        "Visible"};

    for (String namesOfOperation : namesOfOperations) {
      operationOptions.addItem(namesOfOperation);
    }
    operationOptions.setSelectedItem("Blur");

    applyOperationButton = new JButton("Apply");
    applyOperationButton.setActionCommand("Apply Button");

    layerOperationPanel.add(applyOperationButton);
  }

  protected void initializeSaveAndSaveAll() {
    JPanel savePanel = new JPanel();
    savePanel.setBorder((BorderFactory.createTitledBorder("Save")));
    savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS));

    saveOptions = new JComboBox<>();
    savePanel.add(saveOptions);

    saveOptions.addItem("Save top-most visible layer");
    saveOptions.addItem("Save all layers");
    saveOptions.setSelectedItem("Save top-most visible layer");

    saveButton = new JButton("Save");
    saveButton.setActionCommand("Save Button");

    savePanel.add(saveButton);
    leftMainPanel.add(savePanel);
  }

  protected void initializeScriptOperation() {
    scriptButton = new JButton("Execute Script");
    scriptButton.setActionCommand("Script Button");
    leftMainPanel.add(scriptButton);
  }

  protected void initializeDisplay() {
    display = new JTextArea(10, 20);
    JScrollPane displayScrollPane = new JScrollPane(display);
    display.setLineWrap(true);
    display.setEditable(false);
    displayScrollPane.setBorder(BorderFactory.createTitledBorder("Display"));
    rightMainPanel.add(displayScrollPane);
  }

  protected void layerSelection() {
    layerOptions = new JComboBox<>();
    layerOptions.setBorder(BorderFactory.createTitledBorder("Select Your Layer:"));
    layerOptions.setActionCommand("Layer Options");

    rightMainPanel.add(layerOptions);
  }

  protected void imageDisplay() {
    JPanel imageDisplayPanel = new JPanel();
    imageDisplayPanel.setBorder(BorderFactory.createTitledBorder("Top-most visible layer"));
    imageDisplayPanel.setLayout(new GridLayout(1, 0, 10, 10));
    rightMainPanel.add(imageDisplayPanel);

    image = new JLabel();

    JScrollPane imageScrollPane = new JScrollPane(image);
    imageScrollPane.setPreferredSize(new Dimension(580, 380));
    imageDisplayPanel.add(imageScrollPane);
  }

  protected void loadPanel() {
    jpeg = new FileNameExtensionFilter("JPEG Images", "jpeg");
    png = new FileNameExtensionFilter("PNG Images", "png");
    ppm = new FileNameExtensionFilter("PPM Images", "ppm");

    JPanel loadLayerPanel = new JPanel();
    loadLayerPanel.setLayout(new BoxLayout(loadLayerPanel, BoxLayout.X_AXIS));
    rightMainPanel.add(loadLayerPanel);

    openButton = new JButton("Open an image");
    openButton.setActionCommand("Open Button");

    loadLayerPanel.add(openButton);

    openedFilePath = new JLabel("File path will appear here");
    loadLayerPanel.add(openedFilePath);

    loadButton = new JButton("Load image into layer");
    loadButton.setActionCommand("Load Button");

    loadLayerPanel.add(loadButton);
  }

  protected void setButtonListener(JButton button, ActionListener listener) {
    button.addActionListener(listener);
  }

  protected void setComboBoxListener(JComboBox<?> combobox, ActionListener listener) {
    combobox.addActionListener(listener);
  }

  @Override
  public void setActionListener(ActionListener listener) {
    setComboBoxListener(createOptions, listener);
    setButtonListener(createLayerButton, listener);
    setButtonListener(applyOperationButton, listener);
    setButtonListener(saveButton, listener);
    setComboBoxListener(layerOptions, listener);
    setButtonListener(openButton, listener);
    setButtonListener(loadButton, listener);
    setButtonListener(scriptButton, listener);
    setButtonListener(extraCreditButton, listener);
    setComboBoxListener(extraCreditOptions, listener);
  }

  @Override
  public void renderMessage(String s) {
    if (display != null) {
      display.append(String.format("%s\n", s));
      checkIfLayerHasBeenCreated(s);
    }
  }

  protected void checkIfLayerHasBeenCreated(String s) {
    String[] words = s.split(" ");
    if (s.contains("Created") && !(s.contains("board"))) {
      layerOptions.addItem(words[2]);
    } else if (s.contains("created") && s.contains("board")) {
      layerOptions.addItem("Checkerboard");
    }
  }

  @Override
  public void displayImage(Image bf) {
    if (bf == null) {
      image.setIcon(null);
    } else {
      image.setIcon(new ImageIcon(bf));
    }
  }

  @Override
  public void visualizeCommands() {
    // Left empty since command instructions are visualized through GUI itself.
  }

  @Override
  public void switchCurrentCreate() {
    String options = Objects.requireNonNull(createOptions.getSelectedItem()).toString();
    CardLayout cl = (CardLayout) currentCreate.getLayout();

    if (layerCreationPanel != null && currentCreate != null) {
      cl.show(currentCreate, options);
    }
  }

  @Override
  public void switchCurrentExtraCredit() {
    String options = Objects.requireNonNull(extraCreditOptions.getSelectedItem()).toString();
    CardLayout cl = (CardLayout) currentExtraCredit.getLayout();

    if (extraCreditPanel != null && currentCreate != null) {
      cl.show(currentExtraCredit, options);
    }
  }

  @Override
  public Readable createLayer() {
    Readable r;
    String s = Objects.requireNonNull(createOptions.getSelectedItem()).toString();

    if (s.equals("Regular Layer")) {
      r = new StringReader("create layer " + layerCreationName.getText());
      layerCreationName.setText("");
    } else if (s.equals("Checkerboard Layer")) {
      r = new StringReader("checkerboard " + tileSize.getText() + " " + numTiles.getText());
      tileSize.setText("");
      numTiles.setText("");
    } else {
      throw new IllegalArgumentException("Option Selected Is Not Possible");
    }
    return r;
  }

  @Override
  public Readable applyFilter() {
    String s = Objects.requireNonNull(operationOptions.getSelectedItem()).toString()
        .toLowerCase(Locale.ROOT);
    return new StringReader(s);
  }

  protected Readable saveOrSaveAll(String filepath,
      FileNameExtensionFilter filetype) {
    String s = Objects.requireNonNull(saveOptions.getSelectedItem()).toString()
        .toLowerCase(Locale.ROOT);
    Readable r;
    String type;

    if (filetype.equals(jpeg)) {
      type = ".jpeg";
    } else if (filetype.equals(png)) {
      type = ".png";
    } else if (filetype.equals(ppm)) {
      type = ".ppm";
    } else {
      throw new IllegalArgumentException("Given filetype is invalid");
    }

    if (s.contains("all")) {
      r = new StringReader(
          String.format("saveAll %s %s", filepath, type));
    } else if (s.contains("top-most")) {
      r = new StringReader(String.format("save %s %s", filepath, type));
    } else {
      throw new IllegalArgumentException("Invalid drop-down selection");
    }

    return r;
  }

  @Override
  public Readable getCurrentLayer() {
    if (layerOptions.getSelectedItem() == null) {
      return null;
    }

    String s = layerOptions.getSelectedItem().toString();
    return new StringReader(String.format("current %s", s));
  }

  @Override
  public void openFiles() {
    final JFileChooser fileChooser = new JFileChooser(".");

    fileChooser.addChoosableFileFilter(jpeg);
    fileChooser.addChoosableFileFilter(png);
    fileChooser.addChoosableFileFilter(ppm);

    int filePath = fileChooser.showOpenDialog(GUIView.this);

    if (filePath == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      openedFilePath.setText(f.getAbsolutePath());
    }
  }

  @Override
  public Readable openSaveFile() {
    Readable r = null;

    final JFileChooser fileChooser = new JFileChooser(".");

    fileChooser.addChoosableFileFilter(jpeg);
    fileChooser.addChoosableFileFilter(png);
    fileChooser.addChoosableFileFilter(ppm);

    int filePath = fileChooser.showSaveDialog(this);

    if (filePath == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();

      r = saveOrSaveAll(f.getPath(), (FileNameExtensionFilter) fileChooser.getFileFilter());
    }

    return r;
  }

  @Override
  public Readable loadImage() {
    String s = openedFilePath.getText();
    return new StringReader(String.format("load %s", s));
  }

  @Override
  public String getScript() {
    final JFileChooser fileChooser = new JFileChooser(".");

    FileNameExtensionFilter text =
        new FileNameExtensionFilter("Text files", "txt");

    fileChooser.addChoosableFileFilter(text);

    int filePath = fileChooser.showSaveDialog(this);

    if (filePath == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();

      return f.getPath();
    }

    return null;
  }

  @Override
  public Readable applyExtraCredit() {
    Readable r;
    String s = Objects.requireNonNull(extraCreditOptions.getSelectedItem()).toString();

    if (s.contains("Mosaic")) {
      r = new StringReader("mosaic " + mosaicTextArea.getText());
      mosaicTextArea.setText("");
    } else if (s.contains("Downsize")) {
      r = new StringReader("downsize " + newWidth.getText() + " " + newHeight.getText());
      newWidth.setText("");
      newHeight.setText("");
    } else {
      throw new IllegalArgumentException("Option Selected Is Not Possible");
    }
    return r;
  }
}