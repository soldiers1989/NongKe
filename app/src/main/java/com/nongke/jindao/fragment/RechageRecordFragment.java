package com.nongke.jindao.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nongke.jindao.R;
import com.nongke.jindao.adapter.InviterAdapter;
import com.nongke.jindao.adapter.PhoneRecordAdapter;
import com.nongke.jindao.adapter.divider.RecycleViewDivider;
import com.nongke.jindao.base.fragment.BaseMvpFragment;
import com.nongke.jindao.base.mmodel.PhoneRecordResData;
import com.nongke.jindao.base.utils.account.UserUtil;
import com.nongke.jindao.mcontract.RechargeRecordContract;
import com.nongke.jindao.mpresenter.RechargeRecordPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @author quchao
 * @date 2018/2/11
 */

public class RechageRecordFragment extends BaseMvpFragment<RechargeRecordPresenter> implements RechargeRecordContract.View {

    @BindView(R.id.recharge_recyclerview)
    RecyclerView recharge_recyclerview;
    @BindView(R.id.tv_recharge_hint)
    TextView tv_recharge_hint;
    private List<PhoneRecordResData.PhoneRecordBody> phoneRecordList;
    PhoneRecordAdapter phoneRecordAdapter;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public void initView() {
        initRecyclerView();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_recharge_record;
    }

    @Override
    public void reload() {
        if (!UserUtil.isLogined())
            tv_recharge_hint.setText("你还没有登录");
        mPresenter.listUserPhoneRecord();
    }


    @Override
    public RechargeRecordPresenter initPresenter() {
        return new RechargeRecordPresenter();
    }

    @Override
    protected void loadData() {
        mPresenter.listUserPhoneRecord();
    }

    @Override
    public void showRechargeRecordRes(PhoneRecordResData phoneRecordResData) {
        if (phoneRecordResData.rspBody.size() == 0) {
            tv_recharge_hint.setText("你还没有充值记录");
            tv_recharge_hint.setVisibility(View.VISIBLE);
        }
        else {
            phoneRecordAdapter.setList(phoneRecordResData.rspBody);
            tv_recharge_hint.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        phoneRecordList = new ArrayList<>();
        phoneRecordAdapter = new PhoneRecordAdapter(getActivity(), phoneRecordList);
        recharge_recyclerview.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));

        recharge_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //解决数据加载不完的问题
        recharge_recyclerview.setNestedScrollingEnabled(false);
        recharge_recyclerview.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        recharge_recyclerview.setFocusable(false);
        recharge_recyclerview.setAdapter(phoneRecordAdapter);
    }
}
