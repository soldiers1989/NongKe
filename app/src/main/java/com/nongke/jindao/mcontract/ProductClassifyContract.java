package com.nongke.jindao.mcontract;

import com.nongke.jindao.base.mmodel.BannerResData;
import com.nongke.jindao.base.mmodel.ProductResData;

/**
 * Created by gaolei on 2018/6/18.
 */

public class ProductClassifyContract {

    public interface Presenter {


        void pageProduct(int pageSize, String orderType, String orderBy);


        void onLoadMore(int pageSize, String orderType, String orderBy);
    }

    public interface View {

        void showProduct(ProductResData productResData, boolean isRefresh);

    }
}
