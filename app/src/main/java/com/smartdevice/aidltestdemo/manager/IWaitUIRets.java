package com.smartdevice.aidltestdemo.manager;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-10-14.
 */
public interface IWaitUIRets extends Serializable {
    void InitResult();
    void SetResult(Object ret);
    Object WaitForResult();
}
