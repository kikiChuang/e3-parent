package com.jddfun.game.Act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.Presenter.BasePresenter;
import com.jddfun.game.Utils.BaseUtil;
import com.jddfun.game.Utils.ToastUtil;

import butterknife.ButterKnife;


/**
 * 所有Activity的积累
 */
public abstract class BaseActivity<V,T extends BasePresenter<V>> extends JDDBaseActivity {
	protected Activity context = null;//上下文
	public T presenter;
    private ToastUtil mtoast;
	protected Bundle mSavedInstanceState = null;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setLayoutView());
		this.context = this;
		this.mSavedInstanceState = savedInstanceState;
		ButterKnife.bind(this);
        mtoast = new ToastUtil();
		presenter = initPresenter();
		presenter.attach((V)context);
		init();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (BaseUtil.isShouldHideKeyboard(v, ev)) {
				BaseUtil.hideKeyboard(v.getWindowToken(), this);
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.dettach();
	}

	//设置布局
	protected abstract int setLayoutView();

	//初始化成员
	protected abstract void init();

	//实例化presenter
	public abstract T initPresenter();

	/**************公共方法***************/

	//页面跳转
	public void startActivity(Class<?> className){
		Intent intent = new Intent(this,className);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	//页面带参数跳转
	public void startActivity(Class<?> className,String[] flags,String[] values){
		Intent intent = new Intent(this,className);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if(flags != null && flags.length > 0){
			for(int i=0;i<flags.length;i++){
				intent.putExtra(flags[i], values[i]);
			}
		}
		startActivity(intent);
	}
}
