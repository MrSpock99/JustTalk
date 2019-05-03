package itis.ru.justtalk.adapters.people;

/**
 * Created by KottlandPro TET on 3/3/2018.
 */

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import itis.ru.justtalk.R;
import itis.ru.justtalk.models.User;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class CardPagerAdapterS extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<User> mData;
    private float mBaseElevation;

    public CardPagerAdapterS() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItemS(User user) {
        mViews.add(null);
        mData.add(user);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.card_view_tinder_like, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardview);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * Companion.getMAX_ELEVATION_FACTOR());
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(User user, View view) {
        TextView tvName = view.findViewById(R.id.tv_name_age);
        TextView tvDistance = view.findViewById(R.id.tv_distance);
        ImageView ivAvatar = view.findViewById(R.id.iv_user_avatar);

        tvName.setText(user.getName());
        tvDistance.setText(user.getLocation().toString());
        Transformation transformation = new RoundedCornersTransformation(20, 1);

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transforms(transformation);

        RequestBuilder<Drawable> thumbnail = Glide.with(view)
                .load(R.drawable.ic_launcher_background)
                .apply(requestOptions);

        Glide.with(view)
                .load(user.getAvatarUrl())
                .apply(requestOptions)
                .thumbnail(thumbnail)
                .into(ivAvatar);
    }
}
