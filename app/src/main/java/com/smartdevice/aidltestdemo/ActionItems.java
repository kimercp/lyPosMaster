package com.smartdevice.aidltestdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.morefun.ypossdk.emv.card.CardProc;
import com.morefun.ypossdk.pub.Api;


public class ActionItems {
	private Activity activity;
	public ProgressDialog pdialog;
	private Api mfapi;


	public Context getApplicationContext(){
		return this.activity.getApplicationContext();
	}
	public ActionItems(Activity activity, Api mfapi  )
	{
		this.activity = activity;
		this.mfapi = mfapi;
	}
	
	Object ret = null;
	Object WaitRet(Runnable uirunable){
		ret = null;
		
		activity.runOnUiThread( uirunable );
		
		while( ret == null ){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return (Integer) ret;
	}
	
	public void blockmsg(final String title, final String msg )
	{
		closewait();
		Builder dlg = new AlertDialog.Builder(activity);
		dlg.setTitle(title).setMessage(msg);

		DialogInterface.OnClickListener r = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, final int arg1) {
                CardProc.CardProcEnd();
				// TODO Auto-generated method stub
			}
		};
		dlg.setPositiveButton(  R.string.ok, r);
		dlg.show();
	}
	public int select(final String title, final String... mList){
		CharSequence[] m = new CharSequence[mList.length];
		for( int i = 0; i < mList.length;i++ )
		{
			m[i]= mList[i];
		}
		return select( title,  m);

	}

	public int select(final String title, final CharSequence[] mList){

		closewait();
		return (Integer) WaitRet(new Runnable(){
			@Override
			public void run() {

				// TODO Auto-generated method stub
				AlertDialog.Builder listDia=new AlertDialog.Builder(activity);
				listDia.setTitle(title); 
				listDia.setItems(mList, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub  						
						ret = which;
					}
				}); 				
				listDia.setCancelable(false);				
				listDia.create().show();
			}			
		});
		
	}


	public void showwait(final String title, final String msg){
		if( pdialog == null ){
			pdialog = new ProgressDialog( activity );

			pdialog.setTitle(title);
			pdialog.setMessage(msg);
			pdialog.setCancelable(true);
			pdialog.setCanceledOnTouchOutside(false);
			pdialog.setOnCancelListener( new OnCancelListener(){
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					ActionItems.this.mfapi.StopScanner();
					ActionItems.this.mfapi.Cancel();
				}
			});
			pdialog.show();
		}
		else{
			pdialog.setTitle(title);
			pdialog.setMessage( msg );
		}

	}
	
	public void closewait( ){
		// TODO Auto-generated method stub
		if( pdialog != null ){
			pdialog.dismiss();
			pdialog = null;
		}
	}

}
