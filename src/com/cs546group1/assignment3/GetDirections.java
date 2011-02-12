package com.cs546group1.assignment3;

import com.cs546group1.assignment3.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * UI for getting directions.
 * @author Jay
 *
 */
public class GetDirections extends Activity {
	
	public static final int BUTTON_TEXT = 0;
	public static final int BUTTON_VISUAL = 1;
	public static final int BUTTON_AUDIO = 2;
	public static final int BUTTON_CANCEL = 3;
	
	
	public static final String CODE_NAME = "code";
	
	private static String codeText;

    /**
     * onCreate() - make a GUI that supports the user making requests for text or visual directions (or cancelling).
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_directions);
        setTitle(R.string.get_directions);
        
        codeText = savedInstanceState.getString(TypeList.TYPE_NAME);


        Button getTextButton = (Button) findViewById(R.id.getText);
        Button getVisualButton = (Button) findViewById(R.id.getVisual);
        Button getAudioButton = (Button) findViewById(R.id.getAudio);
        Button getCancel = (Button) findViewById(R.id.cancel);

        getTextButton.setOnClickListener(new View.OnClickListener() {

        	/**
        	 * onClick() - when the user clicks text direction button, start process of showing list.
        	 */
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(GetDirections.CODE_NAME, codeText.toUpperCase());
                bundle.putInt("BUTTON", BUTTON_TEXT);
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
        
        getVisualButton.setOnClickListener(new View.OnClickListener() {

        	/**
        	 * onClick() - when the user clicks visual confirm button, tell the map to show the path.
        	 */
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(GetDirections.CODE_NAME, codeText.toUpperCase());
                bundle.putInt("BUTTON", BUTTON_VISUAL);
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
        
        getAudioButton.setOnClickListener(new View.OnClickListener() {

        	/**
        	 * onClick() - when the user clicks visual confirm button, tell the map to show the path.
        	 * This will have audio directions spoken to the user as well.
        	 */
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(GetDirections.CODE_NAME, codeText.toUpperCase());
                bundle.putInt("BUTTON", BUTTON_AUDIO);
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
        
        getCancel.setOnClickListener(new View.OnClickListener() {

        	/**
        	 * onClick() - when the user clicks cancel, return back to the map without drawing new paths.
        	 */
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent mIntent = new Intent();
                bundle.putInt("BUTTON", BUTTON_CANCEL);
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
    }
}
