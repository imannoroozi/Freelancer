package ir.weproject.freelance.freelance;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.weproject.freelance.helper.ImageHelper;
import ir.weproject.freelance.helper.RoundedNetworkImageView;
import ir.weproject.freelance.ir.weproject.poem.objects.Bid;

/**
 * Created by Iman on 20160429. pasokh be porsesham fohshe rakik bood. this is for commiting
 */
public class BidsAdapter extends ArrayAdapter<Bid> {
    private final Context context;
    ArrayList<Bid> bids;
    int postID;
    Typeface typeFace;

    public enum ACTIONS {ACCEPT, REJECT, IGNORE};


    public BidsAdapter(Context context, List<Bid> bids) {
        super(context, -1, bids);
        this.context = context;
        this.bids = (ArrayList<Bid>) bids;
        typeFace = Typeface.createFromAsset(context.getAssets(), "Fonts/droidnaskh_regular.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createBidView(getItem(position), position, parent);
    }

    private View createBidView(final Bid bid, final int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.bid_item, parent, false);

        RoundedNetworkImageView bidderImage = (RoundedNetworkImageView) rowView.findViewById(R.id.bidder_image);
        TextView bidderName = (TextView) rowView.findViewById(R.id.bidder_name);
        TextView bidderRate = (TextView) rowView.findViewById(R.id.bidder_rate);
        TextView bidderReviews = (TextView) rowView.findViewById(R.id.bidder_reviews);
        RatingBar bidderRatingBar = (RatingBar) rowView.findViewById(R.id.bidder_rating_bar);
        TextView bidDate = (TextView) rowView.findViewById(R.id.bid_date);
        TextView bidCostText = (TextView) rowView.findViewById(R.id.bid_cost_text);
        TextView bidCost = (TextView) rowView.findViewById(R.id.bid_cost);
        TextView bidDurationText = (TextView) rowView.findViewById(R.id.bid_duration_text);
        TextView bidDuration = (TextView) rowView.findViewById(R.id.bid_duration);
        TextView bidDescription  = (TextView) rowView.findViewById(R.id.bid_desc);

        ImageHelper.loadNetworkImage(context, bidderImage, bid.getUser().getImageAddress());
        bidderName.setText(bid.getUser().getDisplayName());

        bidDate.setText(bid.getDate());
        bidCost.setText(String.valueOf(bid.getCost()));
        bidDuration.setText(String.valueOf(bid.getDuration()));
        bidDescription.setText(bid.getDescription());

        bidCostText.setTypeface(typeFace);
        bidDurationText.setTypeface(typeFace);

        return rowView;
    }

}
