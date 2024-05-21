package app.tabser.view.render;

/*
public class TabOldCode {
    void drawSheet(Canvas canvas, Paint paint, boolean dragging) {
        renderer.setUp(canvas, paint, context.getApplicationContext());
        RenderIterator iterator = new RenderIterator();
        iterator.yPosition = deltaY;
        iterator.lineOffset = 0;
        iterator.cursorPositions = new ArrayList<>();
//        lineCount = 0;
        //lineHeight = 0;
        renderer.renderDocument();
        /*
        while (!iterator.endReached) {

            RenderedLine renderedLine;
            if (renderedLines.size() > lineCount && renderedLines.get(lineCount).invalid == false) {
                renderedLine = renderedLines.get(lineCount);
                canvas.drawBitmap(renderedLine.bitmap, design.getxStart(), renderedLine.lineMetrics.yOffset, paint);
            } else {
                renderedLine = drawLine(canvas, paint, iterator);
            }
        }
        lineCount = iterator.lineOffset;
        displayedCursorPositions = iterator.cursorPositions.toArray(new ModelCursor[0]);
        ySheetEnd = (int) iterator.yPosition;
        paint.setColor(Color.MAGENTA);
        canvas.drawLine(0f, ySheetEnd, viewPort.right, ySheetEnd, paint);

         */
        /*
         * Draw active cursor position
         */
        /*
        int foregroundColorActive = design.getForegroundColorActiveSheet();
        Color rgbForegroundActive = Color.valueOf(foregroundColorActive);
        paint.setARGB(32, (int) rgbForegroundActive.red(),
                (int) rgbForegroundActive.green(), (int) rgbForegroundActive.blue());
        if (modelCursor.selectionArea.left == modelCursor.selectionArea.right) {
            PathEffect paintEffect = paint.getPathEffect();
            paint.setPathEffect(new DashPathEffect(new float[]{15f, 5f}, 0f));
            canvas.drawLine(
                    modelCursor.selectionArea.left,
                    modelCursor.selectionArea.top,
                    modelCursor.selectionArea.left,
                    modelCursor.selectionArea.bottom, paint);
            paint.setPathEffect(paintEffect);
        } else {
            //paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(modelCursor.selectionArea, paint);
            //paint.setStyle(Paint.Style.FILL);
        }
        if (searchCursor && (Objects.isNull(nav.animator) || !nav.animator.isRunning()) && !viewPort.contains(modelCursor.selectionArea)) {
            searchCursor = false;
            nav.scrollToLine(modelCursor.lineIndex, design.getAnimationDuration());
        } else if (searchCursor) {
            searchCursor = false;
        }
        *
    }

    private void drawLine(Canvas canvasOriginal, Paint paintOriginal, RenderIterator renderResult) {
        // float yStart = this.yStart;
        // Rect all = new Rect(0, 0, tabView.getTabViewWidth(), tabView.getTabViewHeight());
        /*
        Canvas canvas = canvasOriginal;
        Paint paint = paintOriginal;
        int lineIndex = renderResult.lineOffset;
        RenderedLine renderedLine;
        if (renderedLines.size() > lineIndex) {
            renderedLine = renderedLines.get(lineIndex);
        } else {
            renderedLine = new RenderedLine(renderResult);
        }

        if (renderedLine.invalid) {
            //  renderedLine.render()
        } else {

        }

        int stringCount = model.getTuning().getStringCount();
        paint.setStrokeWidth(design.getStrokeWidth());
        int a = 80;
        int foregroundColor = design.getForegroundColorInactiveSheet();
        Color rgbForeground = Color.valueOf(foregroundColor);
        paint.setARGB(a, (int) rgbForeground.red(), (int) rgbForeground.green(), (int) rgbForeground.blue());
        float yIncrement = design.getYIncrement();
        final float xStart = design.getxStart();
        paint.setTextSize(yIncrement);
        float yCursor = renderResult.yPosition + (design.getYIncrement() * 3);
        final float yStart = yCursor;
        float xCursor = 0;
        //Rect renderDim = getRenderDimensions();
        if (!settings.isCompact()) {
            float[] xyCursor = drawStaffLines(canvas, paint, yStart, yCursor);
            xCursor = xyCursor[0];
            yCursor = xyCursor[1];
        }
        /*
         * Draw tabulature lines
         *
        float yTabStart = yCursor;
        yCursor = drawTabulatureLines(canvas, paint, yCursor);
        final float yEnd = yCursor;
        /*
         * Draw Tab Clef
         *
        float tabClefWidth = drawClef(canvas, paint, Song.Clef.TAB, yTabStart, stringCount);
        /*
         * Draw beginning bar line
         *
        canvas.drawLine(xStart, yStart, xStart, yEnd, paint);
        paint.setColor(foregroundColor);
        /*
         * Notes
         *
        xCursor = Math.max(xCursor, xStart * 2 + tabClefWidth + xStart);
        /*
        if (renderResult.barOffset == 0) {
            lineHeight = yEnd - renderResult.yPosition;
        }
         */
        /*
        drawNotes(canvas, paint, xCursor, yEnd, a, rgbForeground, yTabStart, renderResult);
        renderResult.yPosition = yEnd;
        renderResult.lineOffset++;
         *
//        return renderedLine;
    }

    private float[] drawStaffLines(Canvas canvas, Paint paint, float yStart, float yCursor) {
        float xStart = design.getxStart();
        float yIncrement = design.getYIncrement();
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(xStart, yCursor, tabView.getTabViewWidth() - xStart, yCursor, paint);
            yCursor += yIncrement;
        }
        int clefStart = drawClef(canvas, paint, model.getClef(), yStart);
        yCursor += 3 * yIncrement;
        float xCursor = 2 * xStart + clefStart + xStart;
        return new float[]{xCursor, yCursor};
    }

    private float drawTabulatureLines(Canvas canvas, Paint paint, float yCursor) {
        float xStart = design.getxStart();
        float yIncrement = design.getYIncrement();
        int stringCount = model.getTuning().getStringCount();
        for (int i = 0; i < stringCount; i++) {
            canvas.drawLine(xStart, yCursor, tabView.getTabViewWidth() - xStart, yCursor, paint);
            if (i + 1 < stringCount) {
                yCursor += yIncrement;
            }
        }
        return yCursor;
    }

    private void drawNotes(Canvas canvas, Paint paint, float xCursor, float yEnd, int a,
                           Color rgbForeground, float yTabStart, RenderIterator renderResult) {
        //float yStart = renderResult.yPosition;
        float yStart = renderResult.yPosition + (design.getYIncrement() * 3);
        int barIndex = renderResult.barOffset;
        int beatIndex = renderResult.beatOffset;
        int stringCount = model.getTuning().getStringCount();
        /*
         * Set initial cursor position
         *
        // this.modelCursor.x = xCursor;
        if (model.getBars(modelCursor.sequenceKey).size() == 0) {
            modelCursor.setRect((int) xCursor, (int) yStart, (int) xCursor, (int) yEnd);
        }
        ArrayList<Bar> bars = model.getBars(modelCursor.sequenceKey);
        LineMetrics lineDimensions = LineMetrics.calculateLine(
                tabView.getTabViewWidth() - design.getxStart() * 2,
                renderResult, design, model);
        float renderWidth = lineDimensions.xIncrement;
        //float defaultWidth = design.getYIncrement() * 1.5f;
        int calculatedBars = renderResult.calculatedBarCount;
        /*
         * the max index + 1 for bars in sequence for this line
         *
        int lineBars = barIndex + calculatedBars;
        for (int i = barIndex; i < lineBars && i < bars.size(); i++) {
            Bar bar = bars.get(i);
            int foregroundColor = design.getForegroundColorInactiveSheet();
            float yIncrement = design.getYIncrement();
            int backgroundColor = design.getBackgroundColorSheet();
            if (beatIndex == 0) {
                paint.setARGB(a, (int) rgbForeground.red(), (int) rgbForeground.green(), (int) rgbForeground.blue());
                // Bar Number
                canvas.drawText(String.valueOf(barIndex + 1), xCursor, (int) (yStart - yIncrement * 0.666), paint);
                paint.setColor(foregroundColor);
            }
            /*
             * loop through notes (per string) of this beat
             *
            for (Map<Integer, Note> notes : bar.getNotes()) {
                for (Integer key : notes.keySet()) {
                    Note n = notes.get(key);
                    if (Objects.nonNull(n)) {
                        int string = n.getString();
                        String fret = n.getFret() > -1 ? String.valueOf(n.getFret()) : "X";
                        Rect fretRect = new Rect();
                        paint.getTextBounds(fret, 0, fret.length(), fretRect);
                        float y = yTabStart + yIncrement * (stringCount - 1 - string) + yIncrement / 3;
                        paint.setColor(backgroundColor);
                        float textX = xCursor + renderWidth / 2 - fretRect.right / 2f;
                        Rect blankRect = new Rect((int) textX, (int) y + fretRect.top, (int) (textX + fretRect.right), (int) (y));
                        canvas.drawRect(blankRect, paint);
                        paint.setColor(foregroundColor);
                        canvas.drawText(fret, textX, y, paint);
                    }
                }
                if (modelCursor.barIndex == barIndex && modelCursor.beatIndex == beatIndex) {
                    modelCursor.setRect((int) xCursor, (int) yStart, (int) (xCursor + renderWidth), (int) yEnd);
                    renderResult.cursorPositions.add(modelCursor);
                    modelCursor.lineIndex = renderResult.lineOffset;
                } else {
                    SheetView.ModelCursor newPos = new SheetView.ModelCursor();
                    newPos.barIndex = barIndex;
                    newPos.beatIndex = beatIndex;
                    newPos.setRect((int) xCursor, (int) yStart, (int) (xCursor + renderWidth), (int) yEnd);
                    newPos.lineIndex = renderResult.lineOffset;
                    renderResult.cursorPositions.add(newPos);
                }
                //float actualWidth = Math.max(maxWidth, defaultWidth);
                xCursor += renderWidth;
                beatIndex++;
                renderResult.beatOffset = beatIndex;
            }
            /*
             * Draw Bar Line
             *
            if (Objects.nonNull(bar.getSeparator())) {
                paint.setARGB(a, (int) rgbForeground.red(), (int) rgbForeground.green(), (int) rgbForeground.blue());
                canvas.drawLine(
                        xCursor,
                        yStart,
                        xCursor,
                        yTabStart + yIncrement * (model.getTuning().getStringCount() - 1), paint);
                paint.setColor(foregroundColor);
            }
            barIndex++;
            beatIndex = 0;
            renderResult.barOffset = barIndex;
            renderResult.beatOffset = 0;
        }
        /*
         * set trailing cursor
         *
        if (modelCursor.isTrailing()) {
            //this.xCursor = xCursor;
            modelCursor.lineIndex = renderResult.lineOffset;
            modelCursor.setRect(xCursor + design.getxStart(), yStart, xCursor + design.getxStart(), yEnd);
        }
    }

    private int drawClef(Canvas canvas, Paint paint, Song.Clef clef, float yStart) {
        return drawClef(canvas, paint, clef, yStart, -1);
    }

    private int drawClef(Canvas canvas, Paint paint, Song.Clef clef, float yStart, int stringCount) {
        int width;
        float xStart = design.getxStart();
        int x = (int) (xStart * 2);
        float yIncrement = design.getYIncrement();
        if (clef == Song.Clef.BASS) {
            width = (int) (yIncrement * (3.3 / 20 * 18));
            Drawable bassClef = ViewUtils.getDrawable(context, R.drawable.f_clef, x, (int) yStart,
                    width, (int) (yIncrement * 3.3));
            bassClef.setTint(design.getBackgroundColorSheet());
            bassClef.draw(canvas);
            bassClef.setTint(paint.getColor());
            bassClef.draw(canvas);
        } else if (clef == Song.Clef.TREBLE) {
            width = (int) (yIncrement * (7 / 165.6 * 58.6));
            Drawable trebleClef = ViewUtils.getDrawable(context, R.drawable.g_clef, x,
                    (int) (yStart - yIncrement * 1.33), width, (int) (yIncrement * 7));
//            trebleClef.setTint(design.getBackgroundColorSheet());
//            trebleClef.draw(canvas);
            trebleClef.setTint(paint.getColor());
            trebleClef.draw(canvas);
        } else {
            int tabClefHeight = (int) ((stringCount - 1) * yIncrement);
            int tabClefWidth = (int) (tabClefHeight / 112.3f * 27.7f);
            Drawable tabClef = ViewUtils.getDrawable(context, R.drawable.tab_clef, (int) (xStart * 2),
                    (int) (yStart + tabClefHeight * 0.05), tabClefWidth, (int) (tabClefHeight * 0.9));
//            tabClef.setTint(design.getBackgroundColorSheet());
//            tabClef.draw(canvas);
            tabClef.setTint(paint.getColor());
            tabClef.draw(canvas);
            width = tabClefWidth;
        }
        return width;
    }
}

         */
