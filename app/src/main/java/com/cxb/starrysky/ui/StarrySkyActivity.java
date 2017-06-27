package com.cxb.starrysky.ui;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.cxb.starrysky.R;
import com.cxb.starrysky.app.APP;
import com.cxb.starrysky.app.BaseActivity;
import com.cxb.starrysky.model.PersonInfo;
import com.cxb.starrysky.utils.AssetsUtil;
import com.cxb.starrysky.widget.starrysky.StarrySkyView;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 星空
 */

public class StarrySkyActivity extends BaseActivity {

    private StarrySkyView ssvStar;
    private List<PersonInfo> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starry_sky);

        initView();
        setData();

    }

    private void initView() {
        ssvStar = (StarrySkyView) findViewById(R.id.ssv_star);
        ssvStar.setOnClickListener(click);
    }

    private void setData() {
        Observable.create(new ObservableOnSubscribe<List<PersonInfo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<PersonInfo>> e) throws Exception {
                String json = AssetsUtil.getAssetsTxtByName(StarrySkyActivity.this, "family_tree.txt");
                mList = JSONObject.parseArray(json, PersonInfo.class);
                if (!e.isDisposed()) {
                    e.onNext(mList);
                    e.onComplete();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PersonInfo>>() {
                    @Override
                    public void accept(@NonNull List<PersonInfo> psersonList) throws Exception {
                        if (psersonList != null) {
                            ssvStar.setPersonList(psersonList);
                        }
                    }
                });
    }

    //点击监听
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ssv_star:
                    if (mList != null) {
                        Glide.get(APP.getInstance()).clearMemory();
                        Collections.shuffle(mList);
                        ssvStar.setPersonList(mList);
                    }
                    break;
            }
        }
    };

}
