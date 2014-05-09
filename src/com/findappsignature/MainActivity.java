package com.findappsignature;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	private static String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			final Context mContext = PlaceholderFragment.this.getActivity();
			
			final Button button = (Button) rootView.findViewById(android.R.id.button1);
			final EditText editText = (EditText) rootView.findViewById(android.R.id.text1);
			final EditText results = (EditText) rootView.findViewById(R.id.results);
			results.setSelectAllOnFocus(true);
			
			button.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					results.setText("");
					PackageManager packageManager = mContext.getPackageManager();
				    try {
				        Signature[] signs = packageManager.getPackageInfo(editText.getEditableText().toString(), PackageManager.GET_SIGNATURES).signatures;
				        for (Signature signature : signs) {
				            Log.d(TAG, "sign = " + signature.toCharsString());
				            results.append(signature.toCharsString());
				        }
				        
				        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
				            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
				            clipboard.setText(results.getEditableText().toString());
				        } else {
				            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
				            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", results.getEditableText().toString());
				                    clipboard.setPrimaryClip(clip);
				        }
				        
				        Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_LONG).show();
				        
				    } catch (NameNotFoundException e) {
				        e.printStackTrace();
				    }
				}
			});
			
			
			
			return rootView;
		}
	}

}
