package com.clverpanda.nfshare.Widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by clverpanda on 2017/3/30 0030.
 * It's the file for NFShare.
 */

public class AlwaysMarqueeTextView extends AppCompatTextView
{
    public AlwaysMarqueeTextView(Context context)
    {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
