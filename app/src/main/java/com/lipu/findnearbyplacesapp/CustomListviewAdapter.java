package com.lipu.findnearbyplacesapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lipu.findnearbyplacesapp.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class CustomListviewAdapter extends BaseAdapter{
	List<HashMap<String, String>>  names ;
    Context ctxt;
    LayoutInflater inflater;
    private OnClickListenerAdvanceListRow m_onClickListenerBookingListRow;


    public CustomListviewAdapter(List<HashMap<String,String>> newsArrayList, Context c,OnClickListenerAdvanceListRow m_onClickListenerBookingListRow) {
        names = newsArrayList;
        ctxt = c;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.m_onClickListenerBookingListRow = m_onClickListenerBookingListRow;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return names.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        // TODO Create the cell (View) and populate it with an element of the array
        if (view == null) {
//          view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            view = inflater.inflate(R.layout.list_row, viewGroup, false);
        }

        TextView name = (TextView) view.findViewById(R.id.textView1);
        TextView address = (TextView) view.findViewById(R.id.textView2);
        TextView other = (TextView) view.findViewById(R.id.textView3);
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar1);
        Button btnshow = (Button) view.findViewById(R.id.btnShow);
        btnshow.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				m_onClickListenerBookingListRow.OnClickRowAction(names.get(position), 1, position);
			}
		});

        name.setText(names.get(position).get("place_name"));
        address.setText(names.get(position).get("vicinity"));
        other.setText(names.get(position).get("rating"));
        float rating = 0;
        if(names.get(position).get("rating").equals("-NA-")){
        	
        }else
        	rating = Float.parseFloat(names.get(position).get("rating"));
        ratingBar.setRating(rating);
        return view;
    }
    
    
    public interface OnClickListenerAdvanceListRow {

    	public void OnClickRowAction(HashMap<String, String> hashMap, long id, int position);

    }

    }





