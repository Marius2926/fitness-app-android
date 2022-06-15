package eu.unibuc.ro.database.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eu.unibuc.ro.R;

public class ChartDraw extends View {


    private Paint paint;
    private List<String> labels;
    private Map<String, Float> chartData;

    public ChartDraw(Context context, Map<String,Float> chartData){
        super(context);

        this.labels = new ArrayList<>(chartData.keySet());
        this.chartData = chartData;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG); //pentru ca linia sa fie smooth
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(chartData == null || chartData.size() == 0){
            return;
        }

        paint.setStrokeWidth(4);

        float paddWidth = 100;
        float paddHeight = 270;

        float availableWidth = getWidth() - 2 * paddWidth;
        float availableHeight = getHeight() - 2 * paddHeight;

        paint.setTextSize(80);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawText(getContext().getString(R.string.chart_draw_title),100,120,paint);

        paint.setTextSize(40);
        paint.setColor(Color.BLACK);
        //axa ox
        canvas.drawLine(paddWidth, paddHeight + availableHeight,
                paddWidth + availableWidth, paddHeight + availableHeight,paint );

        //axa oy
        canvas.drawLine(paddWidth, paddHeight, paddWidth, paddHeight + availableHeight,paint);

        float maximumHydrationLevel = Collections.max(chartData.values());
        float widthOfElement = 55;
        paint.setColor(getResources().getColor(R.color.waterProcessBarProgressColor));

        if(labels.size() >= 2){
            for(int i =0;i<labels.size()-1; i++){

                float x1 = paddWidth + widthOfElement *i ;
                float y1 = (float) ((1 - chartData.get(labels.get(i)) / maximumHydrationLevel)
                        * availableHeight + paddHeight);
                float x2 = paddWidth + (i+1) * widthOfElement;
                float y2 = (float) ((1 - chartData.get(labels.get(i+1)) / maximumHydrationLevel)
                        * availableHeight + paddHeight);

                canvas.drawLine(x1,y1,x2,y2,paint);
                paint.setColor(Color.BLUE);
                canvas.drawCircle(x1,y1,10,paint);
                drawLabel(canvas,x1,paddHeight,availableHeight,labels.get(i).substring(0,10));
                canvas.drawText(chartData.get(labels.get(i)).toString(),paddWidth-85,
                        (float) ((1 - chartData.get(labels.get(i)) / maximumHydrationLevel)
                                * availableHeight + paddHeight),paint);
            }
            canvas.drawCircle(paddWidth + widthOfElement * (labels.size()-1),
                    (float) ((1 - chartData.get(labels.get(labels.size()-1)) / maximumHydrationLevel)
                    * availableHeight + paddHeight),10,paint);
            drawLabel(canvas,paddWidth + widthOfElement * (labels.size()-1),paddHeight
                    ,availableHeight,labels.get(labels.size()-1).substring(0,10));
            canvas.drawText(chartData.get(labels.get(labels.size()-1)).toString(),paddWidth-85,
                    (float) ((1 - chartData.get(labels.get(labels.size()-1)) / maximumHydrationLevel)
                            * availableHeight + paddHeight),paint);
        } else{
            paint.setColor(Color.BLUE);
            canvas.drawCircle(paddWidth,(float) ((1 - chartData.get(labels.get(0)) / maximumHydrationLevel)
                    * availableHeight + paddHeight),10,paint);
            canvas.drawText(chartData.get(labels.get(0)).toString(),paddWidth-85,
                    (float) ((1 - chartData.get(labels.get(0)) / maximumHydrationLevel)
                            * availableHeight + paddHeight),paint);
            drawLabel(canvas,paddWidth ,paddHeight,availableHeight,labels.get(0).substring(0,10));
        }
    }

    private void drawLabel(Canvas canvas, float x1, float paddHeight, float availableHeight, String text){
      paint.setTextSize(40);
      float x = x1 + 20;
      float y = paddHeight + availableHeight + 200;
      canvas.rotate(270, x, y);
      canvas.drawText(text,x,y,paint);
      canvas.rotate(-270, x, y);
    }
}
