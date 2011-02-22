package com.cs546group1.assignment3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class MySpeech extends Activity implements OnInitListener{
private int MY_DATA_CHECK_CODE = 0;
	
	private TextToSpeech tts;
	private String mStr;
    
	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
        mStr = extra.getString("data");
        Intent checkIntent = new Intent();
        
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				tts = new TextToSpeech(this, this);
			} 
			else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	public void onInit(int status) 
	{
		if (status == TextToSpeech.SUCCESS) {
			System.out.println("trying to do text to speech");
			Speak();
		}
        else if (status == TextToSpeech.ERROR) {
        	System.out.println("text to speech error");
        }
	}
	
	public void Speak()
	{
		tts.speak(mStr, TextToSpeech.QUEUE_ADD, null);
		System.out.println("after call to Speak");
	}
	
}
