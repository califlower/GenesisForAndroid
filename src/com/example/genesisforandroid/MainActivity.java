package com.example.genesisforandroid;

import java.util.ArrayList;
import org.jsoup.nodes.Document;

import com.fima.cardsui.views.CardUI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;





public class MainActivity extends Activity 
{

	
	ArrayList<String> classes=new ArrayList<String>();
	ArrayList<String> teachers=new ArrayList<String>();
	GetNH highlands;
	Button login;
	EditText email;
	EditText pass;
	AlertDialog.Builder alertBuilder;
	AlertDialog alert;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Creation of items for login
		login = (Button) findViewById(R.id.btnLogin);
		email=(EditText)findViewById(R.id.inputEmail);
		pass=(EditText)findViewById(R.id.inputPass);
		
		

		
		
		
		//Action on clicking login
        login.setOnClickListener(new View.OnClickListener() 
         {
             @Override
			public void onClick(View v) 
             {
         		new GetGenesis().execute(email.getText().toString(),pass.getText().toString());	
             }
         });
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	//Background networking thread
	private class GetGenesis extends AsyncTask<String, Void, Document> 
    {
    	@Override
		protected Document doInBackground(String... UserPass) 
    	{
    		highlands=new GetNH(UserPass[0],UserPass[1]);
    		
    		if (highlands.getGradeDoc()!=null)
    		{
    			classes=highlands.getClasses();
    			teachers=highlands.getTeachers();
    		}
    		return highlands.getGradeDoc();

    	}


        protected void onPreExecute()
        {
        	alertBuilder=new AlertDialog.Builder(MainActivity.this);
        	alertBuilder.setMessage("    Loading");
        	alertBuilder.setCancelable(false);
        	
        	alert=alertBuilder.create();
        	alert.show();
        	
        	
        	
        }
    	
    	//After executing main thread in background
		protected void onPostExecute(Document doc) 
        {
			alert.dismiss();
			
          	if (doc==null)
          	{
      			//Creation of a Alert dialog for wrong password or username
                
                alertBuilder.setMessage(highlands.getErrorType());
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() 
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    	alert.dismiss();
                    	
                    }
                });
                
                alert=alertBuilder.create();
                alert.show();
                
        		email.setText("");
        		pass.setText("");
          	}
  			else
  			{
  				
  				alert.dismiss();
  				setContentView(R.layout.card_sheet);
  				CardUI mCardView = (CardUI) findViewById(R.id.cardsview);
  				
  				for(int i=0;i<classes.size();i++)
  				{
  					mCardView.addCard(new MyGradeCard(classes.get(i),teachers.get(i), "Period 6", "A+",false));

  				}
  						
  				mCardView.refresh();



  			}	
 
        
        }


    }

}
