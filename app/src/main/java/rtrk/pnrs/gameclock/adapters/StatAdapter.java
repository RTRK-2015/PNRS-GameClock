package rtrk.pnrs.gameclock.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rtrk.pnrs.gameclock.GameClockBinder;
import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.data.Stat;


public class StatAdapter
    extends BaseAdapter
{
    public StatAdapter(Context context)
    {
        mContext = context;
        mList = new ArrayList<>();

        Resources mResources = mContext.getResources();

        mWhiteImg = mResources.getDrawable(R.drawable.white_win);
        mBlackImg = mResources.getDrawable(R.drawable.black_win);
        mDrawImg = mResources.getDrawable(R.drawable.draw);

        mWhiteTxt = mResources.getText(R.string.whitePlayerWon);
        mBlackTxt = mResources.getText(R.string.blackPlayerWon);
        mDrawTxt = mResources.getText(R.string.draw);
    }


    public void add(Stat stat)
    {
        mList.add(stat);
        notifyDataSetChanged();
    }


    @Override
    public int getCount()
    {
        return mList.size();
    }


    @Override
    public Object getItem(int pos)
    {
        Object r = null;

        try
        {
            r = mList.get(pos);
        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }

        return r;
    }


    @Override
    public long getItemId(int pos)
    {
        return pos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_stat, null);
            ViewHolder holder = new ViewHolder();
            holder.whiteLeft = (TextView)view.findViewById(R.id.txtStat_whiteLeft);
            holder.blackLeft = (TextView)view.findViewById(R.id.txtStat_blackLeft);
            holder.won = (ImageView)view.findViewById(R.id.imgStat_won);
            holder.txtWon = (TextView)view.findViewById(R.id.txtStat_won);
            view.setTag(holder);
        }

        Stat stat = (Stat)getItem(position);
        ViewHolder holder = (ViewHolder)view.getTag();

        holder.won.setImageDrawable(
            stat.won == GameClockBinder.BLACK_PLAYER_ID? mBlackImg :
            stat.won == GameClockBinder.WHITE_PLAYER_ID? mWhiteImg : mDrawImg);
        holder.txtWon.setText(
                stat.won == GameClockBinder.BLACK_PLAYER_ID? mBlackTxt :
                stat.won == GameClockBinder.WHITE_PLAYER_ID? mWhiteTxt : mDrawTxt);
        holder.whiteLeft.setText(stat.whiteLeft.toString());
        holder.blackLeft.setText(stat.blackLeft.toString());

        return view;
    }


    public void clear()
    {
        mList.clear();
        notifyDataSetChanged();
    }


    private class ViewHolder
    {
        public TextView whiteLeft, blackLeft, txtWon;
        public ImageView won;
    }


    private Drawable mBlackImg, mWhiteImg, mDrawImg;
    private CharSequence mBlackTxt, mWhiteTxt, mDrawTxt;
    private Context mContext;
    private ArrayList<Stat> mList;
}
