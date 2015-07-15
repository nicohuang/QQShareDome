package hwz.com.qqsharedome.share;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;

import hwz.com.qqsharedome.MainActivity;

/**
 * Created by jan on 15/7/13.
 */
public class DoShareHelp
{
    /**
     * qq个人分享
     * 设置分享内容
     */
    public static void onClickShare(Activity activity ,String title,String messame,String path) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, messame);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
        //QQShare.SHARE_TO_QQ_IMAGE_URL可以是本地图片路径获取网络图片路径  "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif"
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
        MainActivity.mTencent.shareToQQ(activity, params, new BaseUiListener());
    }
}
