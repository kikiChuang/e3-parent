package com.jddfun.game.Act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.Adapter.PictureAdapter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.ToastUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 发布
 * Created by MACHINE on 2017/4/12.
 */

public class PublishAct extends JDDBaseActivity implements View.OnClickListener, TextWatcher {

    @BindView(R.id.iv_back_rl)
    RelativeLayout iv_back_rl;
    @BindView(R.id.tv_activity_title)
    TextView tv_activity_title;
    @BindView(R.id.pulish_ed)
    EditText pulish_ed;
    @BindView(R.id.pulish_tx)
    TextView pulish_tx;
    @BindView(R.id.publish_publish)
    TextView publish_publish;
    @BindView(R.id.publicsh_GridView)
    GridView publicsh_GridView;


    private int REQUEST_IMAGE = 1;


    CharSequence temp;
    private int selectionStart;
    private int selectionEnd;
    private PictureAdapter mMPictureAdapter;
    private ArrayList<String> mPath = new ArrayList<>();
    private List<MultipartBody.Part> mParts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_act);
        ButterKnife.bind(this);
        mContext = PublishAct.this;
        tv_activity_title.setText("分享");
        iv_back_rl.setOnClickListener(this);
        publish_publish.setOnClickListener(this);

        pulish_ed.addTextChangedListener(this);
        mMPictureAdapter = new PictureAdapter(this, mPath);
        publicsh_GridView.setAdapter(mMPictureAdapter);
        publicsh_GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mPath.size()) {
                    StartAlbum();
                } else {
                    mPath.remove(position);
                    mMPictureAdapter.updata(mPath);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_rl:
                finish();
                break;
            case R.id.publish_publish:
                MobclickAgent.onEvent(this, "share_0006");

                if (pulish_ed.getText().length() < 15) {
                    UtilsTools.showToast("字数不能少于15字", mContext);
                    return;
                }
                onPostMoreFile(mPath);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPath = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                ArrayList<String> ImagList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mPath.addAll(ImagList);
                mMPictureAdapter = new PictureAdapter(this, mPath);
                publicsh_GridView.setAdapter(mMPictureAdapter);
            }
        }
    }

    public void StartAlbum() {
        MultiImageSelector.create(this)
                .showCamera(false)
                .count(3 - mPath.size())// 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .multi() // 多选模式, 默认模式;
                .start(this, REQUEST_IMAGE);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        pulish_tx.setText(s.length() + "/140");
        temp = s;
    }

    @Override
    public void afterTextChanged(Editable s) {
        selectionStart = pulish_ed.getSelectionStart();
        selectionEnd = pulish_ed.getSelectionEnd();
        if (temp.length() > 140) {
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionEnd;
            pulish_ed.setText(s);
            pulish_ed.setSelection(tempSelection);
        }
        if (temp.length() > 14) {
            publish_publish.setBackgroundResource(R.mipmap.btn_click);
        } else {
            publish_publish.setBackgroundResource(R.mipmap.btn_bg);
        }
    }

    int type = 0;

    public void onPostMoreFile(final List<String> ListImag) {
        showLoading(false, "发布中.....");
        mParts = new ArrayList<>();
        final MultipartBody.Part params = MultipartBody.Part.createFormData("content", pulish_ed.getText().toString());

        if (ListImag.size() != 0 && type <= ListImag.size() - 1) {
            recude(ListImag, params);
        }else{
            mParts.add(params);
            setUpdata();
        }
    }

    public void recude(final List<String> ListImag, final MultipartBody.Part params) {
        Luban.get(PublishAct.this)
                .load(new File(ListImag.get(type)))
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", "", requestBody);
                        mParts.add(part);
                        type++;
                        if (ListImag.size() == type) {
                            mParts.add(params);
                            setUpdata();
                        } else {
                            recude(ListImag, params);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }

    public void setUpdata() {
        ServiceFactory.getInstance()
                .createService(JDDApiService.class)
                .publish(mParts)
                .compose(RxUtils.<HttpResult<Object>>defaultSchedulers())
                .compose(this.<HttpResult<Object>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object list) {
                        dismissLoading();
                        ToastUtils.show(PublishAct.this, "发表成功 正在审核");
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_NOTFIY_PUBLIC_SHARE_SUCCESS));
                        finish();

                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        dismissLoading();
                        ToastUtils.show(PublishAct.this, "发布失败");
                    }
                });
    }

}
