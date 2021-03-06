package com.bitdubai.reference_niche_wallet.fermat_wallet.common.navigation_drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.bitdubai.android_fermat_ccp_wallet_fermat.R;
import com.bitdubai.fermat_android_api.ui.Views.BadgeDrawable;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.MenuItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Matias Furszyfer on 2015.09.30..
 */
public class NavigationViewAdapter extends FermatAdapter<MenuItem, NavigationItemMenuViewHolder> {


    Typeface tf;
    protected NavigationViewAdapter(Context context) {
        super(context);
    }

    public NavigationViewAdapter(Context context, List<MenuItem> dataSet) {
        super(context, dataSet);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
    }
    /**
     * Create a new holder instance
     *
     * @param itemView View object
     * @param type     int type
     * @return ViewHolder
     */
    @Override
    protected NavigationItemMenuViewHolder createHolder(View itemView, int type) {
        return new NavigationItemMenuViewHolder(itemView);
    }

    /**
     * Get custom layout to use it.
     *
     * @return int Layout Resource id: Example: R.layout.row_item
     */
    @Override
    protected int getCardViewResource() {
        return R.layout.navigation_row;
    }

    /**
     * Bind ViewHolder
     *
     * @param holder   ViewHolder object
     * @param data     Object data to render
     * @param position position to render
     */
    @Override
    protected void bindHolder(NavigationItemMenuViewHolder holder, MenuItem data, int position) {

        try {

            holder.getLabel().setText(data.getLabel());

                //holder.getRow_container().setBackgroundColor(Color.parseColor("#80000000"));
            if(data.isSelected())
                holder.getRow_container().setBackgroundResource(R.color.black_overlay_2);

                switch (position) {
                    case 0:
                        Picasso.with(context).load((data.isSelected()) ? R.drawable.btn_drawer_icon_home_fluor : R.drawable.btn_drawer_home_normal).into(holder.getIcon());
                        break;
                    case 1:
                        Picasso.with(context).load((data.isSelected()) ? R.drawable.btn_drawer_icon_profile_fluor : R.drawable.btn_drawer_profile_normal).into(holder.getIcon());
                        break;
                    case 2:
                        Picasso.with(context).load((data.isSelected()) ? R.drawable.btn_drawer_icon_request_fluor : R.drawable.btn_drawer_request_normal).into(holder.getIcon());
                        if(data.getNotifications()!=0){
                            holder.getBadge().setBackground(new BadgeDrawable.BadgeDrawableBuilder(context).setCount(data.getNotifications()).setTextSize(32).build());
                        }
                        break;
                    case 3:
                        Picasso.with(context).load((data.isSelected()) ? R.drawable.icon_settings : R.drawable.btn_drawer_settings_normal).into(holder.getIcon());
                        break;
                    case 4:
                        Picasso.with(context).load((data.isSelected()) ? R.drawable.btn_drawer_icon_logout_fluor : R.drawable.btn_drawer_logout_normal).into(holder.getIcon());
                        break;
                    default:
                        break;
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
