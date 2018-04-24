package com.smartdevice.aidltestdemo.manager;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.smartdevice.aidltestdemo.base.BaseActivity;
import com.morefun.ypossdk.misc.Misc;
import com.smartdevice.aidltestdemo.manager.WaitUIRet;

import java.util.ArrayList;

public class ActionService implements Runnable {
	private static ActionService  instance = null;
	private static String LOG_TAG = "ActionService";

	public static ActionService Instance(){
		if(instance == null){
			instance = new ActionService();
		}
		return instance;
	}

	public ActionService() {
		new Thread(this).start();
	}
	/*
        List<ActionActivity> items = new ArrayList<ActionActivity>();

        boolean isSendComplete;
        public void AddNewItem(ActionActivity item){
            if(item.isBase()){
                items.add(item);
            }
            iActionBaseItemlast = item;
        }

        public void RemoveItem(ActionActivity item){
            if(item.isBase()){
                items.remove(item);
            }
        }

        public void SetSendComplete(){
            isSendComplete = true;
        }
        */
	public boolean Action(BaseActivity startActivity, String item){
		return Action( startActivity, item, item);
	}
	public boolean Action(BaseActivity startActivity, String item , String title){
		return  Action(startActivity,item, title, null );
	}

	Runnable actionend = null;
	public boolean Action(BaseActivity startActivity, String item , String title, Runnable actionend){
		if(this.startActivity != null) {
			Misc.setTipShow(startActivity, "Transaction was not completed，Please complete and try again");
			return  false;
		}
		this.startActivity = startActivity;
		this.actionend = actionend;
		Message msg = new Message();
		msg.what = 0;
		Bundle data = new Bundle();
		data.putString("itemid", item);
		data.putString("itemtitle", title);
		msg.setData(data);
		return mHandler.sendMessage(msg);
	}

	Runnable action = null;
	public boolean Action(BaseActivity startActivity, Runnable action, Runnable actionend){
		this.startActivity = startActivity;
		Message msg = new Message();
		msg.what = 1;
		this.action = action;
		this.actionend = actionend;

		return mHandler.sendMessage(msg);
	}

	//ActionActivity iActionBaseItemlast ;
	//String itemid;
	//String itemtitle;

	Object m_Result;
	boolean hasResult;
	BaseActivity startActivity = null;
	String itemtitle;

	private ArrayList<ItemListener> m_list_func = new ArrayList<ItemListener>();

	public interface ItemListener {
		public int actionitem(String itemname);
	}

	public void addFunc(ItemListener func){
		m_list_func.add(func);
	}

	boolean mustclose = false;
	public IWaitUIRets RunAndWaitUI(String s, Bundle param )  {

		WaitUIRet await = new WaitUIRet();

		Bundle data = new Bundle();
		if( param == null )	{
			param = new Bundle();
		}
		String title = param.getString("title");
		if( title == null )	{//默认标题
			param.putString("title", itemtitle);
		}
		int timeout = param.getInt("timeout");
		if( timeout == 0)	{
			//param.putInt("timeout", 60);
		}

		data.putString("classname", s);
		data.putBundle("param", param);

		//isSendComplete = false;
		isCancel = false;
		await.InitResult();

		param.putSerializable( "iWaitUIRet" ,await);
		mustclose = true;
		startActivity.action_startActivity( s, param );
		return await;
	}

	boolean isCancel = false;
	public void SetCancel(){
		isCancel = true;
	}

	public boolean IsCancel() {
		// TODO Auto-generated method stub
		return isCancel;
	}


	public void SetResult(Object Result){
		m_Result = Result;
		hasResult = true;
	}

	void finish(){
		if( startActivity != null )
		{
			startActivity.action_finish();
			mustclose = false;
		}
	}
	/*
	public void finishall(){

		List<ActionActivity> itemsfinish = new ArrayList<ActionActivity>();
		for( ActionActivity i : items )	{
			itemsfinish.add(i);
		}

		for( ActionActivity i : itemsfinish ){
			RunAndWaitUI(i,"finishall",null);
		}
		itemsfinish.clear();
	}*/

	public  void actionitem(String itemid){
		try {
			for( ItemListener i : m_list_func  )	{
				i.actionitem( itemid );
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	Handler mHandler;

	@SuppressLint("HandlerLeak")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch( msg.what )	{
					case 0:	{
						Bundle b;
						b = msg.getData();
						String id = b.getString("itemid");
						itemtitle = b.getString("itemtitle");
						try{
							actionitem(id);
						}
						catch ( Exception e)
						{
							e.printStackTrace();
						}
						if(mustclose)
						{
							finish();
						}
						if( actionend != null ) {
							startActivity.runOnUiThread( actionend );
						}
					}
					break;
					case 1:	{
						try{
							if( action != null ) {
								action.run();
							}
						}
						catch ( Exception e)
						{
							e.printStackTrace();
						}
						if(mustclose)
						{
							finish();
						}
						if( actionend != null ) {
							startActivity.runOnUiThread( actionend );
						}

					}
					break;


				}
				startActivity = null;


			}
		};
		Looper.loop();
	}
}
