package kriti.somevalue.com.iom;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class History extends Fragment {

    private ListView listView;
    private List<String> list=new ArrayList();
    String REF_PREF="prefs";

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);

//        SharedPreferences sharedPreferences=getContext().getSharedPreferences(REF_PREF, Context.MODE_PRIVATE);
//        String restoredText=sharedPreferences.getString("VendorId",null);
//
//        list.add(restoredText);
//
//        listView=view.findViewById(R.id.listView);
//        ArrayAdapter adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
//        listView.setAdapter(adapter);

        listView=view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if(isVisibleToUser){
            SharedPreferences sharedPreferences=getContext().getSharedPreferences(REF_PREF, Context.MODE_PRIVATE);
            String restoredText=sharedPreferences.getString("VendorId",null);

            list.add(restoredText);


            ArrayAdapter adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
            listView.setAdapter(adapter);
        }
    }
}
