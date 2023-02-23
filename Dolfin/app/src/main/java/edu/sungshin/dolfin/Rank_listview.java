package edu.sungshin.dolfin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.NameList;

import java.util.ArrayList;

public class Rank_listview extends BaseAdapter {
    interface Listener {
        void onItemClickedAT(int position);
    }

    private chal_listview.Listener listener;

    public void setListener(chal_listview.Listener listener){
        this.listener = listener;
    }

    private ArrayList<ListViewItem> listViewItemList ;

    // Rank_listview Adapter의 생성자
    public Rank_listview(ArrayList<ListViewItem> itemList) {
        if (itemList == null) {
            listViewItemList = new ArrayList<ListViewItem>() ;
        } else {
            listViewItemList = itemList ;
        }
    }

    public int getCount() {
        return listViewItemList.size() ;
    }
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }
    public long getItemId(int position) {
        return position ;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "rank_listview" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rank_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView rankTextView = (TextView) convertView.findViewById((R.id.textViewRank));
        TextView pointTextView = (TextView) convertView.findViewById(R.id.textViewPoint) ;
        TextView textTextView = (TextView) convertView.findViewById(R.id.textViewText) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        rankTextView.setText(String.valueOf(position+1));
        pointTextView.setText(Integer.toString(listViewItem.getPoint()));
        textTextView.setText(listViewItem.getText());

        return convertView;
    }
    public void addItem(int rank, int point, String text) {
        ListViewItem item = new ListViewItem();

        item.setRank(rank);
        item.setPoint(point);
        item.setText(text);

        listViewItemList.add(item);
    }
    public ArrayList<ListViewItem> getItemList() {
        return listViewItemList ;
    }

    public ArrayList<String> getRankList(){
        ArrayList<ListViewItem> temp = getItemList();
        ArrayList<String> rankList = new ArrayList<>();
        for(int i=0; i<temp.size(); i++){
            rankList.add(temp.get(i).getText());
        }
        return rankList;
    }

    public static class ListViewItem {
        private static int rank;
        private int point ;
        private String text ;

        public void setRank(int rank) {
            ListViewItem.rank = rank ;
        }
        public void setPoint(int point) {
            this.point = point ;
        }
        public void setText(String text) {
            this.text = text ;
        }

        public static int getRank() { return rank ; }
        public int getPoint() {
            return point ;
        }
        public String getText() {
            return text ;
        }
    }
}