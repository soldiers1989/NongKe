package com.nongke.jindao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nongke.jindao.R;
import com.nongke.jindao.base.activity.BaseActivity;
import com.nongke.jindao.base.utils.OnlineParamUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author quchao
 * @date 2018/2/11
 */

public class DaoliRechargeActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ll_pay_alipay)
    LinearLayout ll_pay_alipay;
    @BindView(R.id.ll_pay_wechat)
    LinearLayout ll_pay_wechat;
    @BindView(R.id.img_pay_alipay)
    ImageView img_pay_alipay;
    @BindView(R.id.img_pay_wechat)
    ImageView img_pay_wechat;
    @BindView(R.id.tv_daoli_desc)
    TextView tv_daoli_desc;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DaoliRechargeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_daoli_recharge;
    }

    @Override
    protected void initData(Bundle bundle) {
        title.setText(getString(R.string.my_daoli_recharge));
        iv_back.setVisibility(View.VISIBLE);
        if (OnlineParamUtil.paramResData == null || OnlineParamUtil.paramResData.rspBody == null)
            return;
        tv_daoli_desc.setText(OnlineParamUtil.paramResData.rspBody.daoli_use_desc.content);
    }


    @OnClick({R.id.ll_pay_alipay, R.id.ll_pay_wechat})
    public void click(View view) {
        switch (view.getId()) {

            case R.id.ll_pay_alipay:
                img_pay_alipay.setImageResource(R.drawable.icon_pay_select);
                img_pay_wechat.setImageResource(R.drawable.icon_pay_unselect);
                break;
            case R.id.ll_pay_wechat:
                img_pay_wechat.setImageResource(R.drawable.icon_pay_select);
                img_pay_alipay.setImageResource(R.drawable.icon_pay_unselect);
                break;
            default:
                break;
        }

    }
}