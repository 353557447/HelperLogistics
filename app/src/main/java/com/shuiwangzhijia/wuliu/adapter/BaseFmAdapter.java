package com.shuiwangzhijia.wuliu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

/**
 * Created by wangsuli on 2017/12/12.
 */
public class BaseFmAdapter<T> extends FragmentPagerAdapter {
    public void setList(List<Fragment> list,List<T> titles) {
        this.list = list;
        this.titles = titles;
        notifyDataSetChanged();
    }

    private List<Fragment> list;

    public List<T> getTitles() {
        return titles;
    }

    public void setTitles(List<T> titles) {
        this.titles = titles;
        notifyDataSetChanged();
    }

    private List<T> titles;

    public BaseFmAdapter(FragmentManager fm, List<Fragment> list, List<T> titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return (list == null) ? 0 : list.size();
    }

    //重写这个方法，将设置每个Tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        T title = titles.get(position);
        if (title instanceof String) {
            return (CharSequence) title;
        }
        return "";
    }
}
