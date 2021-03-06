package com.nongke.jindao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nongke.jindao.base.pay.PayResult;
import com.nongke.jindao.R;
import com.nongke.jindao.activity.RegisterLoginActivity;
import com.nongke.jindao.activity.VipRechargeActivity;
import com.nongke.jindao.base.event.LoginAccountEvent;
import com.nongke.jindao.base.fragment.BaseMvpFragment;
import com.nongke.jindao.base.mmodel.LoginResData;
import com.nongke.jindao.base.mmodel.RechargeResData;
import com.nongke.jindao.base.pay.alipay.AliPayUtil;
import com.nongke.jindao.base.utils.account.OnlineParamUtil;
import com.nongke.jindao.base.utils.account.UserUtil;
import com.nongke.jindao.base.utils.Utils;
import com.nongke.jindao.mcontract.RechargeContract;
import com.nongke.jindao.mpresenter.RechargePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author quchao
 * @date 2018/2/11
 */

public class RechageDetailFragment extends BaseMvpFragment<RechargePresenter> implements RechargeContract.View  {

    @BindView(R.id.tv_recharge_50)
    TextView tv_recharge_50;
    @BindView(R.id.tv_recharge_100)
    TextView tv_recharge_100;
    @BindView(R.id.tv_vip_recharge)
    TextView tv_vip_recharge;
    @BindView(R.id.tv_recharge_phone_num)
    TextView tv_recharge_phone_num;
    @BindView(R.id.tv_recharge_immediate)
    TextView tv_recharge_immediate;
    @BindView(R.id.tv_money_recharge)
    TextView tv_money_recharge;
    @BindView(R.id.tv_money_pay)
    TextView tv_money_pay;
    @BindView(R.id.tv_phone_recharge_desc)
    TextView tv_phone_recharge_desc;

    @BindView(R.id.ll_money_recharge)
    LinearLayout ll_money_recharge;
    @BindView(R.id.ll_money_pay)
    LinearLayout ll_money_pay;
    @BindView(R.id.ll_pay_alipay)
    LinearLayout ll_pay_alipay;
    @BindView(R.id.ll_pay_wechat)
    LinearLayout ll_pay_wechat;
    @BindView(R.id.img_pay_alipay)
    ImageView img_pay_alipay;
    @BindView(R.id.img_pay_wechat)
    ImageView img_pay_wechat;

    public final int SDK_PAY_FLAG = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;


                }
            }
        }

        ;
    };
    @Override
    public void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_recharge_detail;
    }

    @Override
    public void reload() {
    }

    @Override
    public RechargePresenter initPresenter() {
        return new RechargePresenter();
    }

    @Override
    public void loadData() {
    }

    @OnClick({R.id.tv_vip_recharge, R.id.tv_recharge_50, R.id.tv_recharge_100, R.id.ll_pay_alipay, R.id.ll_pay_wechat, R.id.tv_recharge_immediate})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_vip_recharge:
                VipRechargeActivity.startActivity(getActivity());
                break;
            case R.id.tv_recharge_50:
                tv_recharge_50.setBackgroundResource(R.drawable.shape_recharge_ammount_bg_select);
                tv_recharge_50.setTextColor(getResources().getColor(R.color.white));
                tv_recharge_100.setBackgroundResource(R.drawable.shape_recharge_ammount_bg);
                tv_recharge_100.setTextColor(getResources().getColor(R.color.color_black));
//                showRechargeMoney(50);
                break;
            case R.id.tv_recharge_100:
                tv_recharge_100.setBackgroundResource(R.drawable.shape_recharge_ammount_bg_select);
                tv_recharge_100.setTextColor(getResources().getColor(R.color.white));
                tv_recharge_50.setBackgroundResource(R.drawable.shape_recharge_ammount_bg);
                tv_recharge_50.setTextColor(getResources().getColor(R.color.color_black));
//                showRechargeMoney(100);
                break;
            case R.id.ll_pay_alipay:
                img_pay_alipay.setImageResource(R.drawable.icon_pay_select);
                img_pay_wechat.setImageResource(R.drawable.icon_pay_unselect);
                break;
            case R.id.ll_pay_wechat:
                img_pay_wechat.setImageResource(R.drawable.icon_pay_select);
                img_pay_alipay.setImageResource(R.drawable.icon_pay_unselect);
                break;
            case R.id.tv_recharge_immediate:
                if (!UserUtil.isLogined()) {
                    RegisterLoginActivity.startActivity(getActivity());
                    Utils.showToast(getString(R.string.user_not_login), true);
                    return;
                }
                String supportPhoneRecharge = OnlineParamUtil.paramResData.rspBody.support_phone_recharge.content;
                if ("false".equals(supportPhoneRecharge.trim())) {
                    Utils.showToast("抱歉，暂时不支持话费充值业务", false);
                    return;
                }
                if (UserUtil.getUserInfo().rspBody.isVip == 0) {
                    Utils.showToast("你不是VIP会员，不能使用话费充值业务", false);
                    return;
                }
                mPresenter.recharge(3, 3, 1, (float)0.88);

                break;
            default:
                break;
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginAccountEvent accountEvent) {
        judgeLoginAndVip();
    }

    public void judgeLoginAndVip() {
        if (UserUtil.isLogined()) {
            if (OnlineParamUtil.paramResData == null || OnlineParamUtil.paramResData.rspBody == null || UserUtil.getUserInfo().rspBody == null)
                return;
//            ll_money_recharge.setVisibility(View.VISIBLE);
//            ll_money_pay.setVisibility(View.VISIBLE);
            tv_recharge_phone_num.setText(UserUtil.getUserInfo().rspBody.phone);

//            showRechargeMoney(50);
            int phoneDiscount = Utils.stringToInt(OnlineParamUtil.paramResData.rspBody.vip_phone_discount.content);
            if (UserUtil.getUserInfo().rspBody.isVip == 1 && phoneDiscount < 100) {
                tv_vip_recharge.setVisibility(View.GONE);
                String recharge_50 = getResources().getString(R.string.recharge_50);
                String recharge_50_format = String.format(recharge_50, phoneDiscount * 50 / 100);
                tv_recharge_50.setText(recharge_50_format);
                String recharge_100 = getResources().getString(R.string.recharge_100);
                String recharge_100_format = String.format(recharge_100, phoneDiscount * 100 / 100);
                tv_recharge_100.setText(recharge_100_format);

                tv_recharge_50.setTextSize(16);
                tv_recharge_100.setTextSize(16);

            } else {
                tv_vip_recharge.setVisibility(View.VISIBLE);
                tv_recharge_50.setText("50元");
                tv_recharge_100.setText("100元");
                tv_recharge_50.setTextSize(18);
                tv_recharge_100.setTextSize(18);

            }

        } else {
            tv_recharge_phone_num.setText("");
            ll_money_recharge.setVisibility(View.GONE);
            ll_money_pay.setVisibility(View.GONE);
            tv_recharge_50.setText("50元");
            tv_recharge_100.setText("100元");
            tv_vip_recharge.setVisibility(View.GONE);
        }
        tv_phone_recharge_desc.setText(OnlineParamUtil.paramResData.rspBody.phone_recharge_desc.content);
    }

    private void showRechargeMoney(int rechargeMoney) {
        int phoneDiscount = Utils.stringToInt(OnlineParamUtil.paramResData.rspBody.vip_phone_discount.content);
        tv_money_recharge.setText(rechargeMoney + "");
        if (UserUtil.getUserInfo().rspBody.isVip == 1) {
            tv_money_pay.setText(rechargeMoney * phoneDiscount / 100 + "");
        } else tv_money_pay.setText(rechargeMoney + "");

    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showRechargeRes(RechargeResData rechargeResData) {
        final String paySign = rechargeResData.rspBody.paySign;

        AliPayUtil.pay(mHandler,getActivity(),paySign);
    }

    @Override
    public void showUserInfo(LoginResData loginResData) {

    }
}