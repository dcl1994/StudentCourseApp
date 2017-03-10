package util;

import android.app.Application;
import android.content.Context;

/**
 * 全局获取Context的技巧
 */
public class MyApplication extends Application {
    private static Context context;

    /**
     * 重写父类的oncreat方法
     * 通过调用getApplicationContext 方法得到了一个应用程序级别的Context
     * 然后又提供了一个静态的getContext方法，将刚才获得Context返回
     */
    @Override
    public void onCreate() {
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
