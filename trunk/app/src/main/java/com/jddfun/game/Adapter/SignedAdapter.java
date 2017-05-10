package com.jddfun.game.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.bean.SignInfo;

import java.util.ArrayList;



/**
 * Created by MACHINE on 2017/3/23.
 */

public class SignedAdapter extends RecyclerView.Adapter {

    private SignInfo signInfo;


    public SignedAdapter(SignInfo signInfo) {
        ArrayList<String> moneyList = signInfo.getMoneyList();
        moneyList.add("1000");
        this.signInfo = signInfo;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sign_recycele_item, parent, false);
        SignViewHolder holder = new SignViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SignViewHolder) holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return signInfo.getMoneyList().size();
    }

    private class SignViewHolder extends RecyclerView.ViewHolder {

        private ImageView gold_num;
        private TextView gold_money;
        private ImageView sign_ok;
        private TextView sign_tv;
        public SignViewHolder(View view) {
            super(view);
            initView();
        }

        private void initView() {
            gold_num= (ImageView) itemView.findViewById(R.id.gold_num);
            gold_money= (TextView) itemView.findViewById(R.id.gold_money);
            sign_ok= (ImageView) itemView.findViewById(R.id.sign_ok);
            sign_tv= (TextView) itemView.findViewById(R.id.sign_tv);
        }


        public void bindView(int position) {
            String model = signInfo.getMoneyList().get(position);
            if (position == 7) {
                gold_money.setText("随机");
            } else {
                gold_money.setText("x" + model);
            }
            String dayNum = "第一天";
            switch (position) {
                case 0:
                    dayNum = "第一天";
                    break;
                case 1:
                    dayNum = "第二天";
                    break;
                case 2:
                    dayNum = "第三天";
                    break;
                case 3:
                    dayNum = "第四天";
                    break;
                case 4:
                    dayNum = "第五天";
                    break;
                case 5:
                    dayNum = "第六天";
                    break;
                case 6:
                    dayNum = "第七天";
                    break;
                case 7:
                    dayNum = "七天以上";
                    break;
            }
            Integer num = Integer.valueOf(model);

            if (num < 488) {
                gold_num.setImageResource( R.mipmap.one_leaf);
            } else if (num > 488 && num < 688) {
                gold_num.setImageResource( R.mipmap.two_leaf);
            } else {
                gold_num.setImageResource( R.mipmap.three_leaf);
            }
            sign_tv.setText(dayNum);
            int day = 0;
            if (signInfo != null) {
                day = signInfo.getSignedDay();
            }
            if (position < day && position != 7) {
                sign_ok.setVisibility(View.VISIBLE);
            } else {
                sign_ok.setVisibility(View.INVISIBLE);
            }

        }
    }
}
