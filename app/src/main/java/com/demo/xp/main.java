package com.demo.xp;


import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.net.URI;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

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
                Intent intent = start(Main_context);
                param.setResult(intent);
            }
        });

    }

    Intent start(Context context) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.atp.voiceapp", "com.tencent.liteav.demo.MainActivity");
        intent.setComponent(comp);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        XposedBridge.log("call->" + "18615596666");
        intent.putExtra(Intent.EXTRA_TEXT, "16464");
        context.startActivity(intent);
        return intent;
    }
}
