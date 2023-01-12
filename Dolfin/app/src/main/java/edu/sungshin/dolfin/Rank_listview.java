package edu.sungshin.dolfin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Rank_listview extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemList ;

    // Rank_listview Adapter의 생성자
    public Rank_listview(ArrayList<ListViewItem> itemList) {
        if (itemList == null) {
            listViewItemList = new ArrayList<ListViewItem>() ;
        } else {
            listViewItemList = itemList ;
        }
    }
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    public int getCount() {
        return listViewItemList.size() ;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    public long getItemId(int position) {
        return position ;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "rank_listview" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rank_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView noTextView = (TextView) convertView.findViewById(R.id.textViewNo) ;
        TextView textTextView = (TextView) convertView.findViewById(R.id.textViewText) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        noTextView.setText(Integer.toString(listViewItem.getNo()));
        textTextView.setText(listViewItem.getText());

        return convertView;
    }
    public void addItem(int no, String text) {
        ListViewItem item = new ListViewItem();

        item.setNo(no);
        item.setText(text);

        listViewItemList.add(item);
    }
    public ArrayList<ListViewItem> getItemList() {
        return listViewItemList ;
    }

    public class ListViewItem {
        private int no ;
        private String text ;

        public void setNo(int no) {
            this.no = no ;
        }
        public void setText(String text) {
            this.text = text ;
        }

        public int getNo() {
            return no ;
        }
        public String getText() {
            return text ;
        }
    }
}
