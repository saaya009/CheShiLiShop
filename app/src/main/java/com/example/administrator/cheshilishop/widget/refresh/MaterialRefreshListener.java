package com.example.administrator.cheshilishop.widget.refresh;

public abstract class MaterialRefreshListener {
    public void onfinish(){}

    public abstract void onRefresh(MaterialRefreshLayout materialRefreshLayout);
    public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout){}
}
