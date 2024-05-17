package app.tabser.view.model.pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Environment;

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import app.tabser.R;
import app.tabser.view.ViewUtils;
import app.tabser.view.model.definition.Sheet;
import app.tabser.view.model.geometry.SheetMetrics;
import app.tabser.view.render.AbstractSheet;
import app.tabser.view.render.RenderIterator;

public class PDFSheet extends AbstractSheet implements Sheet {
    private final PDDocument document;
    private PDPage currentPage;
    private PDPageContentStream currentPageContentStream;

    public PDFSheet(Context context, Rect viewPort, SheetMetrics sheetMetrics) throws IOException {
        super(context, viewPort, sheetMetrics);
        PDFBoxResourceLoader.init(context);
        document = new PDDocument();
        newPage(null);
    }

    @Override
    public boolean isMultiPage() {
        return true;
    }

    @Override
    public void drawVector(int resId, float x, float y, float width, float height) {

    }

    @Override
    public void drawLine(float xStart, float yStart, float xEnd, float yEnd) {

    }

    @Override
    public void drawRect(Rect bounds) {

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
        InputStream in = ViewUtils.getDrawableAsStream(getContext(), R.drawable.f_clef, 0, 0, 500, 500);
        InputStream alpha = ViewUtils.getDrawableAsStream(getContext(), R.drawable.g_clef, 0, 0, 500, 500);

        // Draw a green rectangle
        currentPageContentStream.addRect(5, 500, 100, 100);
        currentPageContentStream.setNonStrokingColor(0, 255, 125);
        currentPageContentStream.fill();

        // Draw the falcon base image
        PDImageXObject ximage = JPEGFactory.createFromStream(document, in);
        currentPageContentStream.drawImage(ximage, 20, 20);

        // Draw the red overlay image
        Bitmap alphaImage = BitmapFactory.decodeStream(alpha);
        PDImageXObject alphaXimage = LosslessFactory.createFromImage(document, alphaImage);
        currentPageContentStream.drawImage(alphaXimage, 20, 20);

        // Make sure that the content stream is closed:
        currentPageContentStream.close();

        // Save the final pdf document to a file
        String path = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Created.pdf";
        document.save(path);
        document.close();
//        tv.setText("Successfully wrote PDF to " + path);
    }

    public void newPage(RenderIterator iterator) throws IOException {
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
