package hwz.com.qqsharedome.uilistener;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by jan on 15/7/9.
 */
public class BaseUiListener implements IUiListener
{

    @Override
    public void onComplete(Object o)
    {
        JSONObject object = (JSONObject)o;
        doComplete(object);
    }

    protected void doComplete(JSONObject values) {
    }
    @Override
    public void onError(UiError e) {
    }
    @Override
    public void onCancel() {
    }
}

