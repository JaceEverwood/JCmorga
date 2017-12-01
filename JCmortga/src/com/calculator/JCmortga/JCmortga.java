package com.calculator.JCmortga;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class JCmortga extends Activity {
    	/** Called when the activity is first created. */
	   // constants used when saving/restoring state

	   private static final String LOAN_AMOUNT = "LOAN_AMOUNT";
	   private static final String CUSTOM_INTEREST_RATE = "CUSTOM_INTEREST_RATE";
	   private static final String DOWN_PAYMENT = "DOWN_PAYMENT";
	   private static final String CUSTOM_YEARS = "CUSTOM_YEARS";
	   
	   private double currentLoanAmount ; // loan amount entered by the user
	   private EditText loanEditText; // accepts user input for loan amount
	   
	   private double currentdownpayment; //user downpayment input
	   private EditText downpaymentEditText; // downpayment holder.
	   
	   private double currentIR; //user interestrate input
	   private EditText iREditText; // interestrate holder.
	   
	   private double currentCustomYear; // interest rate % set with the SeekBar
	   private TextView customRateTextView; // custom rate 
	   
	   private EditText tenYearEditText; // 10 yr monthly
	   private EditText fifteenYearEditText; // 15 yr monthly
	   private EditText thirtyYearEditText;  // 30 yr monthly 
	   private EditText dshow; //show final custompayment
	   

	   // Called when the activity is first created.
	   @Override
	   public void onCreate(Bundle savedInstanceState) 
	   {
	      super.onCreate(savedInstanceState); // call superclass's version
	      setContentView(R.layout.main); // inflate the GUI

	      // check if app just started or is being restored from memory
	      if ( savedInstanceState == null ) // the app just started running
	      {
	         currentLoanAmount = 0.0; // initialize the loan amount to zero
	         currentCustomYear = 5.00; // initialize the custom year at 5years
	         currentIR = 6.5;
	         currentdownpayment=0.0;
	      } // end if
	      else // app is being restored from memory, not executed from scratch
	      {
	         // initialize the loan amount to saved amount
	    	 currentLoanAmount = savedInstanceState.getDouble(LOAN_AMOUNT); 
	    	 
	         // initialize the custom rate to saved interest rate
	    	 currentIR = savedInstanceState.getDouble(CUSTOM_INTEREST_RATE);
	    	 
	    	 //initialize the downpayment to saved downpayment
	    	 currentdownpayment =savedInstanceState.getDouble(DOWN_PAYMENT);
	         
	         // initialize the custom year to saved custom year 
	         currentCustomYear = 
	            savedInstanceState.getDouble(CUSTOM_YEARS); 
	      } // end else
	      
	      // get the TextView displaying the custom interest rate
	      customRateTextView = (TextView) findViewById(R.id.customRateTextView);
	      
	      // get the downpaymentEditText 
	      downpaymentEditText = (EditText) findViewById(R.id.downpaymentEditText);
	      downpaymentEditText.addTextChangedListener(downpayEditTextWatcher);
	      
	      //get the interest rate
	      iREditText = (EditText) findViewById(R.id.interestRateEditText);
	      iREditText.addTextChangedListener(iREditTextWatcher);
	      
	      // get the loanEditText 
	      loanEditText = (EditText) findViewById(R.id.loanEditText);
	      // loanEditTextWatcher handles loanEditText's onTextChanged event
	      loanEditText.addTextChangedListener(loanEditTextWatcher);
	
	      // get the SeekBar used to set the custom interest rate
	      SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
	      customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
	      
	      // get references to the 10yr, 15yr and 30yr EditTexts
	      tenYearEditText = (EditText) findViewById(R.id.tenYearEditText);
	      fifteenYearEditText = (EditText) findViewById(R.id.fifteenYearEditText);
	      thirtyYearEditText = (EditText) findViewById(R.id.thirtyYearEditText);
	      dshow = (EditText)findViewById(R.id.dShowEditText);
	      
	      //get downpayment and interest rate
	      
	   } 

	   // calculate monthly payment
	   private double formula(double loan, double downpayment, double rate, double term)
	   {
		  double c = rate/100/12.;
		  double n = term *12 ;
		  return (loan-downpayment) * (c * Math.pow(1 + c, n )) / ( Math.pow(1 + c,n)-1);
	   }
	   
	   // updates 10, 15 and 30 yr EditTexts
	   private void updateMonthlyPayment() 
	   {
	      // calculate monthly payment
	      double tenYearMonthly = 
	    		  formula(currentLoanAmount,currentdownpayment,currentIR, 10 );
	      double fifteenYearMonthly = 
	    		  formula(currentLoanAmount,currentdownpayment,currentIR, 20 );
	      double thirtyYearMonthly = 
	    		  formula(currentLoanAmount,currentdownpayment,currentIR, 30 );
	      double customYearMonthly = 
	    		  formula(currentLoanAmount,currentdownpayment,currentIR, currentCustomYear);
	      

	      // 10, 15 and 30 yr monthly payment EditTexts
	      tenYearEditText.setText("$" + String.format("%.0f", tenYearMonthly));
	      fifteenYearEditText.setText("$" + String.format("%.0f", fifteenYearMonthly));
	      thirtyYearEditText.setText("$" + String.format("%.0f", thirtyYearMonthly));
	      dshow.setText("$" + String.format("%.0f", customYearMonthly));
	   } 

	   // updates the custom rate and monthly payment EditTexts
	   private void updateCustomRate() 
	   {
	      // set customRateTextView's text to match the position of the SeekBar
		  customRateTextView.setText(String.format("%.02f", currentCustomYear) + "years");
	      updateMonthlyPayment(); // update the 10, 15 and 30 yr EditTexts

	   } 

	   // save values of loanEditText and customSeekBar
	   @Override
	   protected void onSaveInstanceState(Bundle outState)
	   {
	      super.onSaveInstanceState(outState);
	      
	      outState.putDouble(LOAN_AMOUNT, currentLoanAmount);
	      outState.putDouble(CUSTOM_YEARS, currentCustomYear);
	      outState.putDouble(CUSTOM_INTEREST_RATE, currentIR);
	      outState.putDouble(DOWN_PAYMENT, currentdownpayment);
	      
	   } 
	   
	   // called when the user changes the position of SeekBar
	   private OnSeekBarChangeListener customSeekBarListener = 
	      new OnSeekBarChangeListener() 
	   {
	      // update currentCustomYear, then call updateCustomRate
	      public void onProgressChanged(SeekBar seekBar, int progress,
	         boolean fromUser) 
	      {
	         // get currentCustomYear from the position of the SeekBar's thumb
	         currentCustomYear = seekBar.getProgress() / 40.0;
	      // update EditTexts for custom rate and monthly
	         updateCustomRate(); 
	      } 

	      public void onStartTrackingTouch(SeekBar seekBar) 
	      {} 

	      public void onStopTrackingTouch(SeekBar seekBar) 
	      {} 
	   }; 

	   // event-handling object that responds to loanEditText's events
	   private TextWatcher loanEditTextWatcher = new TextWatcher() 
	   {
	      // called when the user enters a number

	      public void onTextChanged(CharSequence s, int start, int before, int count) 
	      {         
	         // convert loanEditText's text to a double
	         try
	         {
	            currentLoanAmount = Double.parseDouble(s.toString());
	         } 
	         catch (NumberFormatException e)
	         {
	            currentLoanAmount = 0.0; // default if an exception occurs
	         } 

	         // update the Monthly Payment
	         updateMonthlyPayment(); // update the 10, 15 and 30 yr EditTexts
	      } 

	      public void afterTextChanged(Editable s) 
	      {} 

	      public void beforeTextChanged(CharSequence s, int start, int count,
	         int after) 
	      {} 
	   }; 
	   
	   // event-handling object that responds to iREditText's events
	   private TextWatcher iREditTextWatcher = new TextWatcher() 
	   {
	      // called when the user enters a number

	      public void onTextChanged(CharSequence s, int start, int before, int count) 
	      {         
	         // convert loanEditText's text to a double
	         try
	         {
	            currentIR = Double.parseDouble(s.toString());
	         } 
	         catch (NumberFormatException e)
	         {
	            currentIR = 6.5; // default if an exception occurs
	         } 

	         // update the Monthly Payment
	         updateMonthlyPayment(); // update the 10, 15 and 30 yr EditTexts
	      } 

	      public void afterTextChanged(Editable s) 
	      {} 

	      public void beforeTextChanged(CharSequence s, int start, int count,
	         int after) 
	      {} 
	   }; 
	   
	   //downpayment watcher
	   private TextWatcher downpayEditTextWatcher = new TextWatcher() 
	   {
	      // called when the user enters a number

	      public void onTextChanged(CharSequence s, int start, int before, int count) 
	      {         
	         // convert loanEditText's text to a double
	         try
	         {
	        	 currentdownpayment = Double.parseDouble(s.toString());
	         } 
	         catch (NumberFormatException e)
	         {
	        	 currentdownpayment = 0.0; // default if an exception occurs
	         } 

	         // update the Monthly Payment
	         updateMonthlyPayment(); // update the 10, 15 and 30 yr EditTexts
	      } 

	      public void afterTextChanged(Editable s) 
	      {} 

	      public void beforeTextChanged(CharSequence s, int start, int count,
	         int after) 
	      {} 
	   }; 	    
	} // end
	   