package com.smartdevice.aidltestdemo.print;

import com.smartdevice.aidltestdemo.SignActivity;
import com.morefun.ypossdk.misc.Handware;
import com.morefun.ypossdk.misc.Misc;

import static com.smartdevice.aidltestdemo.print.PrintFunc.PrintWriteWithPage;
import static com.smartdevice.aidltestdemo.print.PrintFunc.getPrintItem;
import static com.smartdevice.aidltestdemo.print.PrintFunc.getPrintStytle;
import static com.smartdevice.aidltestdemo.print.PrintFunc.osl_print_add;

public class PrintTransaction {


    //    public static String PRINT_LINE_SEPARATE = ("==============================\r\n");
    private static String PRINT_LINE_SEPARATE = ("-------------------------------\r\n");


    public static void testPrint(String tradeType, String amount) {
        StringBuilder sbprint = new StringBuilder();
        sbprint.append( Handware.Manager.Print_heat_factor(15) );
        sbprint.append( getPrintStytle(4) );
        sbprint.append( Handware.Manager.Print_align(1) ) ;
        sbprint.append( "China Unionpay\r\n");

        sbprint.append( Handware.Manager.Print_row_space(5));
        if (SignActivity.mBitmap != null)
            sbprint.append(SignActivity.mBitmap);

        sbprint.append(Handware.Manager.Print_row_space(108));
        osl_print_add(sbprint.toString());
        PrintWriteWithPage();
    }
}

