package com.example.genesisforandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class MyGradeCard extends Card 
{
	public MyGradeCard(String className, String teacherName, String period,
			String grade,Boolean isClickable) {
		super(className, teacherName, period, grade,
				isClickable);
	}

	public View getCardContent(Context context) 
	{
		View v = LayoutInflater.from(context).inflate(R.layout.grades_card, null);
		
		((TextView) v.findViewById(R.id.className)).setText(className);
		((TextView) v.findViewById(R.id.period)).setText(period);
		((TextView) v.findViewById(R.id.teacherName)).setText(teacherName);
		((TextView) v.findViewById(R.id.grade)).setText(grade);
		
		
		


		if (isClickable == true)
			((LinearLayout) v.findViewById(R.id.gradeLayout))
					.setBackgroundResource(R.drawable.selectable_background_cardbank);



		return v;
	}
}


