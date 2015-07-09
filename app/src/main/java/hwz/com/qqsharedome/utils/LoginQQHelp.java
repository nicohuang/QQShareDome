package hwz.com.qqsharedome.utils;

import android.content.Context;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import hwz.com.qqsharedome.MainActivity;

/**
 * Created by jan on 15/7/9.
 */

public class LoginQQHelp
{

    /**
     * 获取qq登陆用户信息
     */
    public static void updateUserInfo(Context context) {
        if (MainActivity.mTencent != null && MainActivity.mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    //获取不到这个信息
                }
                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject)response;
                    //因为open qq没有提供这样的接口。
                    String nickName = "";//比如QQ昵称
                    try {
                        nickName= json.getString("nickname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancel() {
                    //获取用户信息被取消
                }
            };
            UserInfo mInfo = new UserInfo(context, MainActivity.mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
        }
    }
}
