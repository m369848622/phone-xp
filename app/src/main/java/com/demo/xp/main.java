package com.demo.xp;


import static com.demo.xp.common.readFileString;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import external.org.apache.commons.lang3.StringUtils;

public class main implements IXposedHookLoadPackage {
    public Context Main_context;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpp) throws Throwable {
        String packageName = lpp.packageName;
        ClassLoader classLoader = lpp.classLoader;
        if (Main_context == null) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Main_context = (Context) param.args[0];
                }
            });
        }


        XposedBridge.log("start->" + packageName);
        XposedHelpers.findAndHookConstructor("android.content.Intent", classLoader, String.class, Uri.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String sdcardPath = Environment.getExternalStorageDirectory().toString();
                File file = new File(sdcardPath + "/phone.txt");
                String aphone = readFileString(file);
                if (aphone != null && aphone.length() > 0) {
                    Log.i("sldfdsf", "aphone1");
                    Intent intent = start(Main_context, String.valueOf(param.args[1]), aphone);
                    param.setResult(intent);
                } else {
                    Log.i("sldfdsf", "aphone2");
                    super.beforeHookedMethod(param);
                }
            }
        });

        XposedHelpers.findAndHookMethod(Toast.class, "show", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(null);
            }
        });

//        XposedHelpers.findAndHookConstructor("com.android.dialer.main.impl.MainActivity.onKeyDown", classLoader, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                int keyCode = Integer.parseInt(String.valueOf(param.args[0]));
//                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//                    Log.i("sdfljksdlfk", "DOWN");
//                }
//                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//                    Log.i("sdfljksdlfk", "UP");
//                }
//            }
//
//        });


    }

    Intent start(Context context, String phone, String aphone) {
        XposedBridge.log("start->" + aphone);
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.atp.voiceapp", "com.tencent.liteav.demo.MainActivity");
        intent.setComponent(comp);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        XposedBridge.log("call->" + phone.split("tel:")[1]);
        intent.putExtra(Intent.EXTRA_TEXT, aphone + "," + phone.split("tel:")[1].replace("%20", ""));
        context.startActivity(intent);
        return intent;
    }
}
