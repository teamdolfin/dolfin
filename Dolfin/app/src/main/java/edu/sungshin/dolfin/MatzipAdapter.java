package edu.sungshin.dolfin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MatzipAdapter extends BaseAdapter implements Filterable{
    ArrayList<Matzip> data = new ArrayList<>();
    private ArrayList<Matzip> oriData = data ;
    Context context;
    Filter listFilter;

    public MatzipAdapter(ArrayList<Matzip> data, Context context)
    {
        this.data = data;
        this.oriData = data;
        this.context = context;
    }

    public void setCheckBoxVisible() {


        for(int i =0; i<data.size() ; i++)
            data.get(i).isCheckable = true;
        notifyDataSetChanged();
    }
    public void setCheckBoxGone(){
        for(int i =0; i<data.size() ; i++)
            data.get(i).isCheckable = false;
        notifyDataSetChanged();
    }


    public void setSearchData(String s){
        if(s.length() > 0 ) {
            for (int i = 0; i < data.size(); i++) {
                if (!data.get(i).name.contains(s)) {
                    data.remove(i);
                    i--;
                }
            }
            notifyDataSetChanged();
        }
    }
    public void resetData(ArrayList<Matzip> m){
        this.data = m;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.activity_list_layout,null);

        }
        TextView t_name = (TextView)convertView.findViewById(R.id.list_name);
        TextView t_tel = (TextView)convertView.findViewById(R.id.list_tel);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_listMenu);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);

        t_name.setText(data.get(position).name);
        t_tel.setText(data.get(position).call_num);
        if(data.get(position).menu_kind == 1 ) imageView.setImageResource(R.drawable.ic_launcher_foreground);
        else if(data.get(position).menu_kind==2) imageView.setImageResource(R.drawable.ic_launcher_foreground);
        else if (data.get(position).menu_kind==3) imageView.setImageResource(R.drawable.ic_launcher_foreground);

        if(data.get(position).isCheckable == true) cb.setVisibility(View.VISIBLE);
        else cb.setVisibility(View.GONE);

        return convertView;
    }



    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter() ;
        }
        return listFilter ;


    }
    private class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint == null || constraint.length() ==0){
                results.values = oriData;
                results.count = oriData.size();
            }
            else {
                ArrayList<Matzip> itemList = new ArrayList<>();
                for(Matzip item : oriData){
                    if( item.getName().toUpperCase().contains(constraint.toString().toUpperCase()) )
                        itemList.add(item);
                }
                results.values = itemList;
                results.count = itemList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            data = (ArrayList<Matzip>) results.values;
            if(results.count>0) {
                notifyDataSetChanged();
            }
            else{
                notifyDataSetInvalidated();
            }
        }

    }


}
