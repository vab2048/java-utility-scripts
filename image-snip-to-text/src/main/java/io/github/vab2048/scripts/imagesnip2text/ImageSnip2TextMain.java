package io.github.vab2048.scripts.imagesnip2text;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import javax.imageio.ImageIO;

import lombok.SneakyThrows;
import net.sourceforge.tess4j.*;

public class ImageSnip2TextMain {
    private static final String DEFAULT_TESSERACT_PATH = "C:/Program Files/Tesseract-OCR/tessdata/";
    private static final ImageSnip2TextMain SCRIPT = new ImageSnip2TextMain();

    public static void main(String[] args) throws Exception {
        // Point to default install location for Tesseract
        System.setProperty("TESSDATA_PREFIX", DEFAULT_TESSERACT_PATH);

        // Step 1: Launch Snipping Tool (Windows 10/11)
        Runtime.getRuntime().exec("cmd /c start ms-screenclip:");
        System.out.println("Snipping Tool launched. Please make a snip...");

        // Step 2: Wait for user to snip and copy to clipboard
        BufferedImage originalSnip = SCRIPT.waitForClipboardImage(30);
        if (originalSnip == null) {
            System.err.println("No snip detected in clipboard.");
            return;
        }
        BufferedImage snip = SCRIPT.scaleUp(2, SCRIPT.toGrayscale(originalSnip));

        // Step 3: Save image
        File outputFile = Files.createTempFile("snip_" + System.currentTimeMillis(), ".png").toFile();
        ImageIO.write(snip, "png", outputFile);
        System.out.println("Image saved to: " + outputFile.getAbsolutePath());

        // Step 4: Run OCR on saved image
        String ocrText = SCRIPT.extractTextFromImage(outputFile);
        System.out.println("Extracted text:\n" + ocrText);

        // Step 5: Copy text to clipboard
        SCRIPT.setClipboardText(ocrText);
        System.out.println("Text copied to clipboard!");

    }

    @SneakyThrows
    private BufferedImage waitForClipboardImage(int timeoutSeconds)  {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        long startTime = System.currentTimeMillis();

        while ((System.currentTimeMillis() - startTime) < timeoutSeconds * 1000) {
            if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                return (BufferedImage) clipboard.getData(DataFlavor.imageFlavor);
            }
            Thread.sleep(500); // check every 0.5 sec
        }
        return null;
    }

    @SneakyThrows
    private String extractTextFromImage(File imageFile)  {
        ITesseract instance = new Tesseract();

        // If Tesseract is installed in a custom location:
        instance.setDatapath(DEFAULT_TESSERACT_PATH);

        // Optional: improve OCR results for code
        instance.setVariable("user_defined_dpi", "300");
        instance.setPageSegMode(6);  // Assume uniform block of text

        // Modes: (0) legacy engine only (original Tesseract),
        // (1) Neural nets "long short-term memory" (LSTM) engine (modern, recommended)
        // (2) Legacy + LSTM engines (combined), (3) Default, based on what’s available
        instance.setOcrEngineMode(1); // LSTM only

        return instance.doOCR(imageFile);
    }

    private void setClipboardText(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private BufferedImage toGrayscale(BufferedImage original) {
        BufferedImage gray = new BufferedImage(
                original.getWidth(), original.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );
        Graphics g = gray.getGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return gray;
    }

    private BufferedImage scaleUp(int ratioToScaleUp, BufferedImage original) {
        int newWidth = ratioToScaleUp * original.getWidth();
        int newHeight = ratioToScaleUp * original.getHeight();

        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g2d = resized.createGraphics();

        // Use rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resized;
    }

}
