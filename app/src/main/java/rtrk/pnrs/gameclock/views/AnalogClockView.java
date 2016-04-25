package rtrk.pnrs.gameclock.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class AnalogClockView
    extends View
{
    public AnalogClockView(Context context)
    {
        super(context);
        init();
    }


    public AnalogClockView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }


    public AnalogClockView(Context context, AttributeSet attrs,
        int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setH(int h)
    {
        this.h = h * 30;
        invalidate();
    }


    public void setM(int m)
    {
        this.m = m * (30 / 5);
        invalidate();
    }


    public void setS(int s)
    {
        this.s = s * (30 / 5);
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!isEnabled())
            return false;

        super.onTouchEvent(event);

        double xdiff = getXC() - event.getX();
        double ydiff = getYC() - event.getY();
        double d = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(ydiff, 2));

        return d <= getRadius();
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawDotsNumbers(canvas);
        drawHands(canvas);
    }


    private void drawBackground(Canvas canvas)
    {
        if (isEnabled())
        {
            canvas.drawCircle(getXC(), getYC(), getRadius(),
                circleBorderActive);
            canvas.drawCircle(getXC(), getYC(), getRadius() - border,
                circleActive);
        }
        else
        {
            canvas.drawCircle(getXC(), getYC(), getRadius(),
                circleBorderInactive);
            canvas.drawCircle(getXC(), getYC(), getRadius() - border,
                circleInactive);
        }
    }


    private void drawDotsNumbers(Canvas canvas)
    {
        canvas.save();

        for (int i = 0; i < 12; ++i)
        {
            // Hour dot
            canvas.drawCircle(getXC(), getYC() - getRadius() + border + 6, 8,
                black);

            // The number itself
            String num = i == 0? "12" : Integer.valueOf(i).toString();
            canvas.drawText(num, getXC() - 13,
                getYC() - getRadius() + border + 50, text);

            canvas.rotate(30 / 5, getXC(), getYC());

            for (int j = 0; j < 4; ++j)
            {
                // Minute dots
                canvas.drawCircle(getXC(), getYC() - getRadius() + border + 3,
                    4, black);
                canvas.rotate(30 / 5, getXC(), getYC());
            }
        }

        canvas.restore();
    }


    private void drawHands(Canvas canvas)
    {
        canvas.drawCircle(getXC(), getYC(), 15, black);
        canvas.drawCircle(getXC(), getYC(), 4, white);

        // Hours hand
        Path hours = new Path();
        canvas.save();
        canvas.rotate(h, getXC(), getYC());
        hours.moveTo(getXC() - 4, getYC() - 15);
        hours.lineTo(getXC() - 10, getYC() - getRadius() + border + 70);
        hours.lineTo(getXC(), getYC() - getRadius() + border + 55);
        hours.lineTo(getXC() + 10, getYC() - getRadius() + border + 70);
        hours.lineTo(getXC() + 4, getYC() - 15);
        hours.close();
        canvas.drawPath(hours, black);
        canvas.restore();


        // Minutes hand
        Path minutes = new Path();
        canvas.save();
        canvas.rotate(m, getXC(), getYC());
        minutes.moveTo(getXC() - 4, getYC() - 15);
        minutes.lineTo(getXC() - 10, getYC() - getRadius() + border + 40);
        minutes.lineTo(getXC(), getYC() - getRadius() + border + 15);
        minutes.lineTo(getXC() + 10, getYC() - getRadius() + border + 40);
        minutes.lineTo(getXC() + 4, getYC() - 15);
        minutes.close();
        canvas.drawPath(minutes, black);
        canvas.restore();
    }


    private void init()
    {
        text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextSize(35);
        text.setFakeBoldText(true);

        black = new Paint();
        black.setColor(Color.BLACK);

        white = new Paint();
        white.setColor(Color.WHITE);

        final int activeR = 0xFF, activeG = 0xB6, activeB = 0x53;
        final int borderR = 0xFF, borderG = 0x75, borderB = 0x38;

        circleActive = new Paint();
        circleActive.setColor(Color.argb(0xFF, activeR, activeG, activeB));

        circleBorderActive = new Paint();
        circleBorderActive.setColor(Color.argb(0xFF, borderR, borderG, borderB));

        float ci[] = new float[3];
        Color.RGBToHSV(activeR, activeG, activeB, ci);
        ci[2] /= Math.sqrt(2);
        circleInactive = new Paint();
        circleInactive.setColor(Color.HSVToColor(0xFF, ci));

        float bi[] = new float[3];
        Color.RGBToHSV(borderR, borderG, borderB, bi);
        bi[2] /= Math.sqrt(2);
        circleBorderInactive = new Paint();
        circleBorderInactive.setColor(Color.HSVToColor(0xFF, bi));
    }



    private float getXC()
    {
        return getWidth() / 2;
    }


    private float getYC()
    {
        return getHeight() / 2;
    }


    private float getRadius()
    {
        if (getHeight() > getWidth())
            return getWidth() / 2 - 15;
        else
            return getHeight() / 2 - 15;
    }



    Paint circleActive, circleInactive, circleBorderActive,
        circleBorderInactive, black, white, text;

    int h, m, s;
    final int border = 15;
}
