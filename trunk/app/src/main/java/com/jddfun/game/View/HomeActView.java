package com.jddfun.game.View;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MACHINE on 2017/5/9.
 */

public interface HomeActView extends BaseView {
    ImageView getIvLaunch();

    void setPopViewVisib(int type);

    void setivEidtVisib(int type);

    Bundle getBundle();

    TextView getTabMain();
    TextView getTabShare();
    TextView getTabMine();

}
