package com.example.utils.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;

public final class ComponentsHelper {

	private static Float scale;
	
	private ComponentsHelper() {
		
	}
	
	public static int dpToPixel(int dp, Context context) {
	    if (scale == null) {
			scale = context.getResources().getDisplayMetrics().density;
		}
	    return (int) ((float) dp * scale);
	}
	
	public static void setButtonDefaults(Button btn){
		btn.setPadding(6, 1, 6, 1);
		btn.setTextSize(14f);
	}
	
	public static void setRoundCorners(View v){
		GradientDrawable shape =  new GradientDrawable();
		shape.setCornerRadius(8);
		shape.setColor(Color.RED);
		v.setBackgroundDrawable(shape);
	}
}
