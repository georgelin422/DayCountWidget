package mmpud.project.daycountwidget;

import java.util.Calendar;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DayCountDetailDialog extends Activity {
	
	private static final String PREFS_NAME = "mmpud.project.daycountwidget.DayCountWidget";
	private static final String TAG_NAME = "mmpud";
	private TextView txtDetailDiffDays;
	private TextView txtDetailTargetDay;
	private TextView txtDetailTitle;
	private Button btnEdit;
	private int mAppWidgetId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_count_detail_dialog);
		
        // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        
        }
        Log.d(TAG_NAME, "mAppWidgetId: " + mAppWidgetId);
        txtDetailDiffDays = (TextView)findViewById(R.id.txt_detail_diffdays);
        txtDetailTargetDay = (TextView)findViewById(R.id.txt_detail_targetday);
        txtDetailTitle = (TextView)findViewById(R.id.txt_detail_title);
   
		btnEdit = (Button)findViewById(R.id.btn_detail_edit);
		btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Click on the widget for edit
				Intent intent = new Intent(DayCountDetailDialog.this , DayCountConfigure.class);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId); 
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
				startActivity(intent);
			}
		});
		
		// Get target YYYY/MM/DD from shared preferences according to different appWidgetId
		SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME,0);
		int targetYear = prefs.getInt("year"+mAppWidgetId, 0);
		int targetMonth = prefs.getInt("month"+mAppWidgetId, 0);
		int targetDate = prefs.getInt("date"+mAppWidgetId, 0);
		String targetTitle = prefs.getString("title"+mAppWidgetId, "");
		
		Calendar calToday = Calendar.getInstance();
		Calendar calTarget = Calendar.getInstance();
		
		// Update the bays difference
		calTarget.set(targetYear, targetMonth, targetDate);
		long diffDays = daysBetween(calToday, calTarget);
		if(diffDays > 0) {
			txtDetailDiffDays.setText(diffDays + " day(s) left until");
			txtDetailTargetDay.setText(targetYear + "/" + targetMonth + "/" + targetDate);
			txtDetailTitle.setText(targetTitle);
		} else {
			txtDetailDiffDays.setText(-diffDays + " day(s) since");
			txtDetailTargetDay.setText(targetYear + "/" + targetMonth + "/" + targetDate);
			txtDetailTitle.setText(targetTitle);
		}
	}
		
	
	public long daysBetween(Calendar startDay, Calendar endDate) {
		long startTime = startDay.getTime().getTime();
		long endTime = endDate.getTime().getTime();
		long diffTime = endTime - startTime;
		return (diffTime / (1000 * 60 * 60 * 24));
	}
}
