package com.smartdevice.aidltestdemo.manager;

/**
 * Created by Administrator on 2016-10-14.
 */
public class WaitUIRet implements IWaitUIRets {
    static Object m_Result = null;

    @Override
    public void InitResult() {
        SetResult(null);
    }

    @Override
    public void SetResult(Object ret) {
        synchronized (this) {
            m_Result = ret;
        }
    }

    public Object GetResult() {
        synchronized (this) {
            return m_Result;
        }
    }

    @Override
    public Object WaitForResult() {
        while (GetResult() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }
        }
        return GetResult();
    }
}
