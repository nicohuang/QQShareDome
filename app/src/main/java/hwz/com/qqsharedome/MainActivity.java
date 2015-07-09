package hwz.com.qqsharedome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hwz.com.qqsharedome.uilistener.BaseUiListener;
import hwz.com.qqsharedome.utils.LoginQQHelp;


public class MainActivity extends Activity
{
    public static Tencent mTencent;
    //appid
    private static String APP_ID = "1104720510";
    //qq用户id
    private static String qqOpenId = "";
    //标题
    private EditText edt_title;
    //内容
    private EditText edt_message;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_message = (EditText) findViewById(R.id.edt_message);
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        loginByQQ();
        //点击按钮分享
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String title = edt_title.getText().toString().trim();
                String message = edt_message.getText().toString().trim();
                shareToQQzone(title, message);
            }
        });
        findViewById(R.id.btn_client_send).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String title = edt_title.getText().toString().trim();
                String message = edt_message.getText().toString().trim();
                onClickShare(title, message);
            }
        });
    }

    /**
     * 使用qq登陆
     */
    private void loginByQQ()
    {
        if (!mTencent.isSessionValid())
        {
            IUiListener listener = new BaseUiListener()
            {
                @Override
                protected void doComplete(JSONObject values)
                {
                    try
                    {
                        //在这里我们可以去到唯一QQ互联可以给我们识别不同QQ用户的openid
                        qqOpenId = values.getString("openid");
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (qqOpenId == null)
                        {
                            qqOpenId = "";
                        }
                    }
                    LoginQQHelp.updateUserInfo(MainActivity.this);
                }
            };
            mTencent.login(this, "all", listener);
        } else
        {
            mTencent.logout(this);
        }
    }

    /**
     * qq个人分享
     * 设置分享内容
     */
    private void onClickShare(String title,String messame) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, messame);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
        mTencent.shareToQQ(MainActivity.this, params, new BaseUiListener());
    }
    /**qq空间分享
     * 设置分享内容
     * 参数说明
     * QzoneShare.SHARE_TO_QQ_KEY_TYPE	选填	Int	SHARE_TO_QZONE_TYPE_IMAGE_TEXT（图文）
     * QzoneShare.SHARE_TO_QQ_TITLE	必填	Int	分享的标题，最多200个字符。
     * QzoneShare.SHARE_TO_QQ_SUMMARY	选填	String	分享的摘要，最多600字符。
     * QzoneShare.SHARE_TO_QQ_TARGET_URL	必填	String	需要跳转的链接，URL字符串。
     * QzoneShare.SHARE_TO_QQ_IMAGE_URL	选填	String	分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
     */
    private void shareToQQzone(String title, String message)
    {
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, message);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.hicsg.com");
        ArrayList imageUrls = new ArrayList();
        imageUrls.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        Tencent mTencent = Tencent.createInstance("", MainActivity.this);//申请的id
        mTencent.shareToQzone(MainActivity.this, params, new BaseUiListener());
    }

    /**
     * 处理分享返回继续操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }

}




