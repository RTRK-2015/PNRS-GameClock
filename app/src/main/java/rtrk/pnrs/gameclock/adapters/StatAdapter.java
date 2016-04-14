package rtrk.pnrs.gameclock.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.data.Stat;


public class StatAdapter
    extends BaseAdapter
{
    public StatAdapter(Context context)
    {
        mContext = context;
        mList = new ArrayList<>();
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

        Resources res = mContext.getResources();
        holder.won.setImageDrawable(
            stat.getWon() == Stat.Won.BLACK? res.getDrawable(R.drawable.black_win) :
            stat.getWon() == Stat.Won.WHITE? res.getDrawable(R.drawable.white_win) :
            res.getDrawable(R.drawable.draw));
        holder.txtWon.setText(
                stat.getWon() == Stat.Won.BLACK? res.getText(R.string.blackPlayerWon) :
                stat.getWon() == Stat.Won.WHITE? res.getText(R.string.whitePlayerWon) :
                res.getText(R.string.draw));
        holder.whiteLeft.setText(stat.getWhiteLeft().toString());
        holder.blackLeft.setText(stat.getBlackLeft().toString());

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


    private Context mContext;
    private ArrayList<Stat> mList;
}
