package com.smartdevice.aidltestdemo.manager;

import android.util.Log;


public class CommFunc {
	private static final String LOG_TAG = "comm.func";
	
	private static String m_title;
	private static int m_timeover = 0;


	public static int comm_func_set_comm_page (String msg , boolean quit){
		return comm_func_set_comm_page (m_title , msg , quit);	
	}
	

	private static int comm_func_set_comm_page (String title , String msg , boolean quit){
		//comm_page_set_page(m_title , msg , quit);
		Actions.ShowMsg(title , msg, quit);
		Log.v(LOG_TAG , "comm_func_set_comm_page:" + title + "-" +  msg + "-" + quit);
		return 0;
	}
	

	

	



	
}
