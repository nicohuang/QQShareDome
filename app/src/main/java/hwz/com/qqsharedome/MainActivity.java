package hwz.com.qqsharedome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hwz.com.qqsharedome.uilistener.BaseUiListener;


public class MainActivity extends Activity
{
    public static Tencent mTencent;
    private static String APP_ID = "1104720510";
    private static String qqOpenId="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        //点击按钮分享
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginByQQ();
                shareToQQzone();
            }
        });
    }

    /**
     * 使用qq登陆
     */
    private void loginByQQ(){
        if (!mTencent.isSessionValid()) {
            IUiListener listener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
                    try {
                        //在这里我们可以去到唯一QQ互联可以给我们识别不同QQ用户的openid
                        qqOpenId= values.getString("openid");
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }finally{
                        if(qqOpenId==null){
                            qqOpenId= "";
                        }
                    }
                    updateUserInfo();
                }
            };
            mTencent.login(this,"all", listener);
        } else {
            mTencent.logout(this);
        }
    }

    /**
     * 获取qq登陆用户信息
     */
    private void updateUserInfo() {
        if (mTencent != null &&mTencent.isSessionValid()) {
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
            UserInfo mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
        }
    }

    /**
     * 设置分享内容
     * 参数说明
     * QzoneShare.SHARE_TO_QQ_KEY_TYPE	选填	Int	SHARE_TO_QZONE_TYPE_IMAGE_TEXT（图文）
     * QzoneShare.SHARE_TO_QQ_TITLE	必填	Int	分享的标题，最多200个字符。
     * QzoneShare.SHARE_TO_QQ_SUMMARY	选填	String	分享的摘要，最多600字符。
     * QzoneShare.SHARE_TO_QQ_TARGET_URL	必填	String	需要跳转的链接，URL字符串。
     * QzoneShare.SHARE_TO_QQ_IMAGE_URL	选填	String	分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
     */
    private void shareToQQzone() {
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "Test");
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  "content infro");
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  "http://www.hicsg.com");
        ArrayList imageUrls = new ArrayList();
        imageUrls.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        Tencent mTencent = Tencent.createInstance("",MainActivity.this);//申请的id
        mTencent.shareToQzone(MainActivity.this, params, new BaseUiListener());
    }

    /**
     * 处理分享返回继续操作
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }

}




