package com.example.administrator.steps_count.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.administrator.steps_count.Activity.CollectionActivity;
import com.example.administrator.steps_count.R;

import java.util.ArrayList;
import java.util.List;

public class CircleFragment extends Fragment {
    private List<View> list = new ArrayList<View>();
    private List<String> listtitle = new ArrayList<String>();
    private ViewPager viewPager;
    private PagerTabStrip tabStrip;
    private SliderLayout sliderLayout;
    private PagerIndicator indicator;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.circle_layout, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabStrip = (PagerTabStrip) view.findViewById(R.id.tabstrip);
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        indicator = (PagerIndicator) view.findViewById(R.id.indicator);
        listtitle = new ArrayList<String>();
        listtitle.add("资讯推荐");
        listtitle.add("动态");
        inflater = getLayoutInflater(savedInstanceState);
        View dynamic_pager = inflater.inflate(R.layout.dynamic_layout, null);
        View consult_pager = inflater.inflate(R.layout.consult_layout, null);
        list.add(consult_pager);
        list.add(dynamic_pager);
        initImageSlider();
        MyPagerAdapter adapter = new MyPagerAdapter(list);
        viewPager.setAdapter(adapter);
        return view;
    }

    public class MyPagerAdapter extends PagerAdapter {
        //定义容纳带显示页面的集合对象
        private List<View> list;

        public MyPagerAdapter(List<View> list) {
            this.list = list;
        }

        public MyPagerAdapter() {
        }

        //viewpager组件上显示页面的总数
        @Override
        public int getCount() {
            return list.size();
        }

        //初始化，创建指定位置的页面视图
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //将list中对应该显示的页面依次添加到viewgroup中
            container.addView(list.get(position));
            return list.get(position);
        }

        //判断哪一个带显示的页面显示在viewpage中
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //移除一个给定位置的页面，滑出去的页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        //viewpager的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return listtitle.get(position);
        }
    }

    private View initImageSlider() {


        //准备好要显示的数据
        List<String> imageUrls = new ArrayList<>();
        final List<String> descriptions = new ArrayList<>();
        imageUrls.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=725404914,1689523057&fm=200&gp=0.jpg");
        imageUrls.add("https://f10.baidu.com/it/u=358462840,528327407&fm=72");
        imageUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523197262040&di=9892fe71cf1c3758bd722937c46b1a39&imgtype=0&src=http%3A%2F%2Fimgs.soufun.com%2Fnews%2F2015_03%2F13%2Fhouse%2F1426238427178_000.gif");
        descriptions.add("最新资讯");
        descriptions.add("商品推荐");
        descriptions.add("最新动态");
        for (int i = 0; i < imageUrls.size(); i++) {
            //新建三个展示View，并且添加到SliderLayout
            TextSliderView tsv = new TextSliderView(getActivity());
            tsv.image(imageUrls.get(i)).description(descriptions.get(i));
            final int finalI = i;
            tsv.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                             @Override
                                             public void onSliderClick(BaseSliderView slider) {
                                                 // Toast.makeText(getActivity(), descriptions.get(finalI), Toast.LENGTH_SHORT).show();
                                                 Intent collect=new Intent(getActivity(), CollectionActivity.class);
                                                 startActivity(collect);
                                             }
                                         }
            );
            sliderLayout.addSlider(tsv);
        }
        //对SliderLayout进行一些自定义的配置
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setDuration(3000);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomIndicator(indicator);
        return sliderLayout;
    }
}
