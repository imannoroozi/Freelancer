package ir.weproject.freelance.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Iman on 12/26/2015.
 */
public class FarsiTextView extends TextView{
    String regularFont = "Fonts/DroidNaskhRegular.ttf";
    String boldFont = "Fonts/DroidNaskhBold.ttf";

    public FarsiTextView(Context context, AttributeSet attrs, boolean isBold) {
        super(context, attrs);
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), isBold ? boldFont : regularFont);
        this.setTypeface(typeFace);
    }

    public FarsiTextView(Context context, AttributeSet attrs, int defStyle, boolean isBold) {
        super(context, attrs, defStyle);
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), isBold ? boldFont : regularFont);
        this.setTypeface(typeFace);
    }

    public FarsiTextView(Context context, boolean isBold) {
        super(context);
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), isBold ? boldFont : regularFont);
        this.setTypeface(typeFace);
    }

}
