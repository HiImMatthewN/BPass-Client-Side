package com.spcba.bpass.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class CustomTicketView extends View {
    private Path shapePath;
    private float cornerRadius;
    public CustomTicketView(Context context,float cornerRadius) {
        super(context);
        shapePath = getPath(120, 240);
        this.cornerRadius = cornerRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shapePath != null)
            canvas.clipPath(shapePath);

        super.onDraw(canvas);
    }

    public Path getPath(int width, int height) {
        Path path = new Path();

        float cutoutRadius = 50f;
        float cutoutWidth = 0f;
        if (cutoutRadius != 0f)
            cutoutWidth = cutoutRadius;
        else
            cutoutWidth = 8f;

        float cutOutHeight = 3*cutoutWidth/4;



        float padding = 0f;
        float pathLeft = padding;
        float pathRight = width - padding;
        float pathTop = padding;
        float pathBottom = (float)height - padding;


        float cutoutXStart = width/2 - cutoutWidth;
        float cutoutXEnd = cutoutXStart + cutoutWidth*2;
        float topCutoutYStart = pathTop -cutOutHeight;
        float topCutoutYEnd = pathTop + cutOutHeight;

        float botCutoutYStart = pathBottom - cutOutHeight;
        float botCutoutYEnd = pathBottom + cutOutHeight;

        path.setFillType(Path.FillType.WINDING);
        path.moveTo(pathLeft + cornerRadius, pathTop);
        path.lineTo(cutoutXStart, pathTop);

        path.arcTo(new RectF(cutoutXStart, topCutoutYStart, cutoutXEnd, topCutoutYEnd), 180f, -180f);
        path.lineTo(pathRight - cornerRadius, pathTop);

        path.arcTo(new RectF(pathRight - cornerRadius * 2, pathTop, pathRight, pathTop + cornerRadius * 2), 270f, 90f);
        path.lineTo(pathRight, pathBottom - cornerRadius);

        //bottom right corner
        path.arcTo(new RectF(pathRight - cornerRadius * 2, pathBottom - cornerRadius * 2, pathRight, pathBottom), 0f, 90f);
        path.lineTo(cutoutXEnd, pathBottom);

        // bottom cutour
        path.arcTo(new RectF(cutoutXStart, botCutoutYStart, cutoutXEnd, botCutoutYEnd), 0f, -180f);
        path.lineTo(pathLeft + cornerRadius, pathBottom);

        //bottom left corner
        path.arcTo(new RectF(pathLeft, pathBottom - cornerRadius * 2, pathLeft + cornerRadius * 2, pathBottom), 90f, 90f);
        path.lineTo(pathLeft, pathTop - cornerRadius);

        //top left corner
        path.arcTo(new RectF(pathLeft, pathTop, pathLeft + cornerRadius * 2, pathTop + cornerRadius * 2), 180f, 90f);
        path.close();


        return path;

    }
}
