package it.unict.dieei.semm.trackmyself.dialogs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.unict.dieei.semm.trackmyself.MapActivity;
import it.unict.dieei.semm.trackmyself.R;
import it.unict.dieei.semm.trackmyself.R.string;
import it.unict.dieei.semm.trackmyself.util.Utils;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class SelectTimeRangeDialogFragment extends DialogFragment implements OnClickListener {

	private Button btnFromTime, btnFromDate, btnToTime, btnToDate;
	private EditText txtFromTime, txtFromDate, txtToTime, txtToDate;
	private GregorianCalendar from;
	private GregorianCalendar to;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -2);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		if(from==null || to==null){
			from = new GregorianCalendar();
			to = new GregorianCalendar();
			from.setTimeInMillis(from.getTimeInMillis()-Long.parseLong(Utils.getStringPreference("ore_ultime_attivita", getActivity())) * 1000);
		}
		
		View selectRangeDialog = inflater.inflate(R.layout.select_range_dialog, null);
		btnFromTime = (Button) selectRangeDialog.findViewById(R.id.browseDateFromTime);
		btnFromDate = (Button) selectRangeDialog.findViewById(R.id.browseDateFromDate);
		btnToTime = (Button) selectRangeDialog.findViewById(R.id.browseDateToTime);
		btnToDate = (Button) selectRangeDialog.findViewById(R.id.browseDateToDate);

		txtFromTime = (EditText) selectRangeDialog.findViewById(R.id.fromTime);
		txtFromDate = (EditText) selectRangeDialog.findViewById(R.id.fromDate);
		txtToTime = (EditText) selectRangeDialog.findViewById(R.id.toTime);
		txtToDate = (EditText) selectRangeDialog.findViewById(R.id.toDate);

		btnFromTime.setOnClickListener(this);
		btnFromDate.setOnClickListener(this);
		btnToTime.setOnClickListener(this);
		btnToDate.setOnClickListener(this);
		
		txtFromDate.setText(String.format("%02d",from.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d",(from.get(Calendar.MONTH) + 1)) + "/" + String.format("%04d",from.get(Calendar.YEAR)));
		txtToDate.setText(String.format("%02d",to.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d",(to.get(Calendar.MONTH) + 1)) + "/" + String.format("%04d",to.get(Calendar.YEAR)));
		txtFromTime.setText(String.format("%02d", from.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", from.get(Calendar.MINUTE)));
		txtToTime.setText(String.format("%02d", to.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", to.get(Calendar.MINUTE)));

		builder.setTitle("Seleziona un intervallo").setMessage("Seleziona un intervallo per visualizzare un percorso")
				.setView(selectRangeDialog).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						MapActivity act = (MapActivity)getActivity();
						act.computaPercorso(from.getTimeInMillis(),to.getTimeInMillis());
					}
				}).setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		// Create the AlertDialog object and return it
		
		
		return builder.create();
	}

	@Override
	public void onClick(View v) {
		if (v == btnFromTime) {
			setTimeField(txtFromTime,from);
		} else if (v == btnFromDate) {
			setDateField(txtFromDate,from);
		} else if (v == btnToTime) {
			setTimeField(txtToTime,to);
		} else if (v == btnToDate) {
			setDateField(txtToDate,to);
		}

	}
	public void setDateField(final EditText txtField, final GregorianCalendar date){
		final Calendar c = date;
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				// Display Selected date in textbox
				txtField.setText(String.format("%02d",dayOfMonth) + "/" + String.format("%02d",(monthOfYear + 1)) + "/" + String.format("%04d",year));
				date.set(Calendar.YEAR,year);
				date.set(Calendar.MONTH, monthOfYear);
				date.set(Calendar.DAY_OF_MONTH,dayOfMonth);
			}
		}, mYear, mMonth, mDay);
		dpd.show();
		
	}
	public void setTimeField(final EditText txtField, final GregorianCalendar date){
		 // Process to get Current Time
        final Calendar c = date;
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                            int minute) {
                        // Display Selected time in textbox
                    	
                    	txtField.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                    	date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    	date.set(Calendar.MINUTE, minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
	}
}