package app.tabser.pdf;


import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.Objects;

import app.tabser.rendering.AbstractSheet;
import app.tabser.rendering.RenderModel;
import app.tabser.rendering.Sheet;
import app.tabser.rendering.geometry.Rectangle;
import app.tabser.rendering.geometry.SheetMetrics;

public class PDFSheet extends AbstractSheet implements Sheet {
    private final PDDocument document;
    private PDPage currentPage;
    private PDPageContentStream currentPageContentStream;

    public PDFSheet(Rectangle viewPort, SheetMetrics sheetMetrics) throws IOException {
        super(sheetMetrics);
//        PDFBoxResourceLoader.init(context);
        document = new PDDocument();
        newPage(null);
    }

    @Override
    public boolean isMultiPage() {
        return true;
    }

    @Override
    public void drawVector(int resId, float x, float y, float width, float height) {
//        currentPageContentStream.
    }

    @Override
    public void drawLine(float xStart, float yStart, float xEnd, float yEnd) {

    }

    @Override
    public void drawRect(Rectangle bounds) {

    }

    @Override
    public void drawText(float xStart, float yStart, String text) {
        try {
            currentPageContentStream.beginText();
//            currentPageContentStream.setNonStrokingColor(15, 38, 192);
            currentPageContentStream.setFont(PDType1Font.HELVETICA, 12);
            currentPageContentStream.newLineAtOffset(xStart, yStart);
            currentPageContentStream.showText(text);
            currentPageContentStream.endText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setForegroundColor(int color) {

    }

    @Override
    public void setTextSize(float height) {

    }

    @Override
    public void setStroke(Stroke stroke) {

    }

    @Override
    public void resetParameters() {

    }

    private void exampleCode() throws IOException {
        // Write Hello World in blue text
        currentPageContentStream.beginText();
        currentPageContentStream.setNonStrokingColor(15, 38, 192);
        currentPageContentStream.setFont(PDType1Font.HELVETICA, 12);
        currentPageContentStream.newLineAtOffset(100, 700);
        currentPageContentStream.showText("Hello World");
        currentPageContentStream.endText();

        // Load in the images
//        InputStream in = assetManager.open("falcon.jpg");
//        InputStream alpha = assetManager.open("trans.png");
//        InputStream in = ViewUtils.getDrawableAsStream(getContext(), R.drawable.f_clef, 0, 0, 500, 500);
//        InputStream alpha = ViewUtils.getDrawableAsStream(getContext(), R.drawable.g_clef, 0, 0, 500, 500);

        // Draw a green rectangle
        currentPageContentStream.addRect(5, 500, 100, 100);
        currentPageContentStream.setNonStrokingColor(0f, 255f, 125f);
        currentPageContentStream.fill();

        // Draw the falcon base image
//        PDImageXObject ximage = JPEGFactory.createFromStream(document, in);
//        currentPageContentStream.drawImage(ximage, 20, 20);

        // Draw the red overlay image
//        Bitmap alphaImage = BitmapFactory.decodeStream(alpha);
//        PDImageXObject alphaXimage = LosslessFactory.createFromImage(document, alphaImage);
//        currentPageContentStream.drawImage(alphaXimage, 20, 20);

        // Make sure that the content stream is closed:
        currentPageContentStream.close();

        // Save the final pdf document to a file
//        String path = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Created.pdf";
//        document.save(path);
        document.close();
//        tv.setText("Successfully wrote PDF to " + path);
    }

    public void newPage(RenderModel.RenderIterator iterator) throws IOException {
        if (Objects.nonNull(iterator)) {
            iterator.pageOffset++;
            iterator.yPosition = 0;
        }
        if (Objects.nonNull(currentPageContentStream)) {
            currentPageContentStream.close();
        }
        currentPage = new PDPage();
        document.addPage(currentPage);
        currentPageContentStream = new PDPageContentStream(document, currentPage);
    }

    public void test() {
        try {
            exampleCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
