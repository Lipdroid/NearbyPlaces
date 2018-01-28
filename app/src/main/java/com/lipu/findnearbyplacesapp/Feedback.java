package com.lipu.findnearbyplacesapp;

import com.lipu.findnearbyplacesapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends Activity
{
	/**
	 * input the subject of the email sender
	 */
	EditText edtSenderSubject;

	/**
	 * text of the feedback to input
	 */
	EditText edtFeedbackInput;
	/**
	 * by clicking the button the email has sent.
	 */
	Button btnEmailSend;
	/**
	 * by clicking the button the email has sent.
	 */
	EditText edtEmailAddress;

	/**
	 * String to store the data from input and spinner;
	 */
	String strSenderSubject, strHaditNumber, strfeedbackInput, strQualityName,
			strEmailAddress;
	String strDuaNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feedback);
		/**
		 * text of the feedback to input
		 */
		final EditText edtFeedbackInput;
		// edtSenderSubject=(EditText)
		// findViewById(R.id.editTextFeedbackSubject);
		btnEmailSend = (Button) findViewById(R.id.buttonFeedBack);
		edtFeedbackInput = (EditText) findViewById(R.id.editTextFeedbackText);
		strDuaNumber = getIntent().getStringExtra("idPass");

		btnEmailSend.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				// strSenderSubject = edtSenderSubject.getText().toString();
				strfeedbackInput = edtFeedbackInput.getText().toString();
				// strEmailAddress = edtEmailAddress.getText().toString();
				Intent itnSendEmailIntent = new Intent(
						Intent.ACTION_SEND);
				itnSendEmailIntent.setType("text/plain");
				String shareBody = strfeedbackInput;

				itnSendEmailIntent.putExtra(
						Intent.EXTRA_SUBJECT,
						"User feedback from Nearby Places");
				itnSendEmailIntent.putExtra(Intent.EXTRA_TEXT,
						strfeedbackInput);
				itnSendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
				{ "lipuhossain67@gmail.com" });
				// sendEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,"nazmulcse25@gmail.com");

				try
				{
					startActivity(Intent.createChooser(itnSendEmailIntent,
							"Send Via"));
				} catch (android.content.ActivityNotFoundException ex)
				{
					Toast.makeText(getApplicationContext(),
							"There are no email clients installed.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
