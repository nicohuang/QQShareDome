package hwz.com.qqsharedome;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import hwz.com.qqsharedome.uilistener.BaseUiListener;


public class MainActivity extends Activity
{
    public static Tencent mTencent;
    //appid
    private static String APP_ID = "1104720510";
    //标题
    private EditText edt_title;
    //内容
    private EditText edt_message;
    //显示图片
    private ImageView img_local;
    //图片返回码
    private final int IMAGE_CODE = 0;
    //本地图片路径
    private static String path="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_message = (EditText) findViewById(R.id.edt_message);
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        img_local = (ImageView)findViewById(R.id.img_local);

        //qq空间分享
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String title = edt_title.getText().toString().trim();
                String message = edt_message.getText().toString().trim();
                if(doEdt(title,message))
                {
                    shareToQQzone(title, message);
                }
            }
        });
        //分享给qq好友
        findViewById(R.id.btn_client_send).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String title = edt_title.getText().toString().trim();
                String message = edt_message.getText().toString().trim();

                if(doEdt(title,message))
                {
                    onClickShare(title, message);
                }
            }
        });
        //获取本地图片
        findViewById(R.id.btn_get_image).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,IMAGE_CODE);
            }
        });
    }
    //判断输入框是否为空
    private boolean doEdt(String title,String message)
    {
        if(title.equals(""))
        {
            Toast.makeText(MainActivity.this,"标题不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(message.equals(""))
        {
            Toast.makeText(MainActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
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
        //QQShare.SHARE_TO_QQ_IMAGE_URL可以是本地图片路径获取网络图片路径//"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif"
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,path);
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
        //图片集合
        ArrayList imageUrls = new ArrayList();
        imageUrls.add(path);
        imageUrls.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");

        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
//        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
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
        //获取本地图片路径
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case IMAGE_CODE:
                    Bitmap logoBitmap;

                    //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                    ContentResolver resolver = getContentResolver();
                    //此处的用于判断接收的Activity是不是你想要的那个
                    try
                    {
                        //获得图片的uri
                        Uri originalUri = data.getData();
                        //显得到bitmap图片
                        logoBitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        img_local.setImageBitmap(logoBitmap);

                        //获取图片的路径：
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        String path1 = cursor.getString(column_index);
                        path =path1;
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}




