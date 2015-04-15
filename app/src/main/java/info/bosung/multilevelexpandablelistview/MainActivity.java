package info.bosung.multilevelexpandablelistview;

/**
 * Created by Eric on 2015-04-13.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    ExpandableListView explvlist;
    ParentLevel plAdapter;
    Content content = new Content();
    SearchView sv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initCountryExpandableListView();
        initSearchView();
    }

    private void initCountryExpandableListView()
    {
        explvlist = (ExpandableListView)findViewById(R.id.ParentLevel);
        plAdapter = new ParentLevel(this);
        explvlist.setAdapter(plAdapter);

        // Listview Group click listener
        explvlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Toast.makeText(getApplicationContext(),
                "Group Clicked " + content.getCountryName(groupPosition),
                Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void initSearchView()
    {
        sv = (SearchView)findViewById(R.id.searchView);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            @Override
            public boolean onQueryTextChange( String newText ) {
                if(newText.trim().length() == 0)
                {
                    content.cancelSearch();
                    if(plAdapter!=null)
                        plAdapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                content.search(query);
                if(plAdapter!=null)
                    plAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),
                        "Searching: " + query,
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                content.cancelSearch();
                if(plAdapter!=null)
                    plAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public class ParentLevel extends BaseExpandableListAdapter
    {
//        private ItemFilter mFilter = new ItemFilter();
        private Context _context;
        SecondLevelAdapter slAdapter;

        public ParentLevel(Context context)
        {
            this._context = context;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon)
        {
            return content.getProvinceName(groupPosition, childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent)
        {
            final int countryPosition = groupPosition;
            CustExpListview SecondLevelexplv = new CustExpListview(MainActivity.this);
            slAdapter = new SecondLevelAdapter(_context, countryPosition, childPosition);
            SecondLevelexplv.setAdapter(slAdapter);
            // Listview Group click listener
            SecondLevelexplv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int subGroupPosition, long id) {
                    Toast.makeText(getApplicationContext(),
                            "SubGroup Clicked " + content.getProvinceName(countryPosition, subGroupPosition),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            // Listview on child click listener
            SecondLevelexplv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int subGroupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub
                    Toast.makeText(
                            getApplicationContext(),
                            "Child Clicked " + content.getCityName(countryPosition, subGroupPosition, childPosition), Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
            });

            return SecondLevelexplv;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return content.getProvinceSize(groupPosition);
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            return content.getCountryName(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return content.getCountrySize();
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent)
        {
            String headerTitle = content.getCountryName(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }

        @Override
        public void notifyDataSetChanged()
        {
            if(slAdapter!=null)
                slAdapter.notifyDataSetChanged();
            super.notifyDataSetChanged();
        }

/*        @Override
        public Filter getFilter() {
            return mFilter;
        }

        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<String> list = originalData;

                int count = list.size();
                final ArrayList<String> nlist = new ArrayList<String>(count);

                String filterableString ;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i);
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(filterableString);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }

        }*/
    }

    public class CustExpListview extends ExpandableListView
    {

        int intGroupPosition, intChildPosition, intGroupid;

        public CustExpListview(Context context)
        {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // the value (2000) should not be fixed and be calculated
            // as follows: cell_height x root_items_count x root_items_children_count
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public class SecondLevelAdapter extends BaseExpandableListAdapter
    {

        private Context _context;
        private int _groupPosition;
        private int _childPosition;

        public SecondLevelAdapter(Context context, int groupPosition, int childPosition)
        {
            this._context = context;
            this._groupPosition = groupPosition;
            this._childPosition = childPosition;
        }

        @Override
        public Object getChild(int subGroupPosition, int childPosition)
        {
            return content.getCity(subGroupPosition, childPosition);
        }

        @Override
        public long getChildId(int subGroupPosition, int childPosition)
        {
            return childPosition;
        }

        @Override
        public View getChildView(int subGroupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent)
        {
            String headerTitle = content.getCityName(_groupPosition, subGroupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_child, null);
            }

            TextView lblListChild = (TextView) convertView.findViewById(R.id.lblListChild);
            lblListChild.setText(headerTitle);

            return convertView;
        }

        @Override
        public int getChildrenCount(int subGroupPosition)
        {
            return content.getCitySize(_groupPosition, subGroupPosition);
        }

        @Override
        public Object getGroup(int subGroupPosition)
        {
            return content.getProvinceName(_groupPosition, subGroupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return content.getProvinceSize(_groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public View getGroupView(int subGroupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent)
        {
            String headerTitle = content.getProvinceName(_groupPosition, subGroupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_subgroup, null);
            }

            TextView lblSubListHeader = (TextView) convertView.findViewById(R.id.lblSubListHeader);
            lblSubListHeader.setTypeface(null, Typeface.BOLD);
            lblSubListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
}

