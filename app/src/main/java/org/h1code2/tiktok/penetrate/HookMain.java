package org.h1code2.tiktok.penetrate;

import android.app.Application;
import android.content.Context;

import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String packageName = loadPackageParam.packageName;
        if (packageName.equals("com.ss.android.ugc.trill") || packageName.equals("com.zhiliaoapp.musically")) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    final Context context = (Context) param.args[0];
                    ClassLoader classLoader = context.getClassLoader();
                    setCountryCode(classLoader);
                }
            });
        }
    }

    /**
     * hook android.telephony.TelephonyManager getSimCountryIso方法 实现替换系统获取的国家代码
     */
    private void setCountryCode(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", classLoader, "getSimCountryIso", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult(getCountryCode());
            }
        });
    }

    /**
     * 随机生成一个国家代码
     *
     * @return 国家代码
     */
    public static String getCountryCode() {
        String[] countryCode = new String[]{
                "us", "jp", "ca", "aq", "eu", "br", "cu", "dk", "eg", "fr", "de", "hk", "id",
                "it", "kp", "kr", "mo", "my", "mx", "mm", "nl", "nz", "pk", "pt", "sg", "za",
                "se", "ch", "tw", "th", "tr", "cb"
        };
        Random random = new Random();
        return countryCode[random.nextInt(countryCode.length)];
    }

}
