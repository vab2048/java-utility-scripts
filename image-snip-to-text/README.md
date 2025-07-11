# Image Snip (Screenshot) To Text (Windows)

This library:
1) Triggers the snipping tool
2) Takes the image snipped and writes it as a temp file.
3) Feeds the temp file into OCR and converts to text
4) Copies the text to the clipboard

# Usage

Setup prerequisites (see Prerequisites section).

1. Run CodeImage2TextMain
2. Select the text using the snipping tool
3. Wait - if it is a large amount it can take time for the OCR process to run.
4. Text will become available on the clipboard.

# Prerequisites

1. Install Tesseract for Windows
    - Download the installer from the [wiki](https://github.com/UB-Mannheim/tesseract/wiki)
    - Install to the default location: `C:\Program Files\Tesseract-OCR`
    - This should result in a folder `C:\Program Files\Tesseract-OCR\tessdata\ contains eng.traineddata` being installed.
