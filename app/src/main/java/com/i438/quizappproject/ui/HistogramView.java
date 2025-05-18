package com.i438.quizappproject.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.i438.quizappproject.R;
import com.i438.quizappproject.model.QuizHistoryEntry;

import java.util.ArrayList;
import java.util.List;

public class HistogramView extends View {
    private List<QuizHistoryEntry> data = new ArrayList<>();
    private Paint correctPaint, incorrectPaint, textPaint;
    private float barWidth = 40f;
    private float groupSpacing = 60f;
    private float barSpacing = 10f;

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        correctPaint = new Paint();
        correctPaint.setColor(Color.parseColor("#4CAF50"));
        correctPaint.setStyle(Paint.Style.FILL);

        incorrectPaint = new Paint();
        incorrectPaint.setColor(Color.parseColor("#F44336"));
        incorrectPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<QuizHistoryEntry> data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data.isEmpty()) return;

        float maxTotal = getMaxTotalQuestions();
        float chartHeight = getHeight() - 100f;
        float baseY = chartHeight;

        float x = groupSpacing;

        for (int i = 0; i < data.size(); i++) {
            QuizHistoryEntry entry = data.get(i);
            float correctHeight = (entry.getCorrect() / maxTotal) * chartHeight;
            float incorrectHeight = (entry.getIncorrect() / maxTotal) * chartHeight;

            float correctLeft = x;
            float correctRight = x + barWidth;
            float incorrectLeft = correctRight + barSpacing;
            float incorrectRight = incorrectLeft + barWidth;

            // Draw correct bar
            canvas.drawRect(correctLeft, baseY - correctHeight, correctRight, baseY, correctPaint);
            canvas.drawText(String.valueOf(entry.getCorrect()), (correctLeft + correctRight) / 2, baseY - correctHeight - 10, textPaint);

            // Draw incorrect bar
            canvas.drawRect(incorrectLeft, baseY - incorrectHeight, incorrectRight, baseY, incorrectPaint);
            canvas.drawText(String.valueOf(entry.getIncorrect()), (incorrectLeft + incorrectRight) / 2, baseY - incorrectHeight - 10, textPaint);

            // Draw Test label under bars
            float groupCenter = (correctLeft + incorrectRight) / 2;
            canvas.drawText("Test " + (i + 1), groupCenter, baseY + 30, textPaint);

            x += 2 * barWidth + barSpacing + groupSpacing;
        }
    }

    private float getMaxTotalQuestions() {
        float max = 0;
        for (QuizHistoryEntry entry : data) {
            float total = entry.getCorrect() + entry.getIncorrect();
            if (total > max) max = total;
        }
        return max;
    }
}
