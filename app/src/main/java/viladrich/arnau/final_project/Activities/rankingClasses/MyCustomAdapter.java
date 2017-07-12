package viladrich.arnau.final_project.Activities.rankingClasses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import viladrich.arnau.final_project.R;


public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.AdapterViewHolder> {

    ArrayList<Contact> contactos;

    public MyCustomAdapter (HashMap <String, String> map, HashMap <String, String> map2, HashMap <String, String> map3){
        contactos = new ArrayList<>();

        Iterator it = map.entrySet().iterator();
        Iterator it2 = map2.entrySet().iterator();
        Iterator it3 = map3.entrySet().iterator();

        int i = 1;

        while (it.hasNext() && it2.hasNext() && it3.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            Map.Entry e2 = (Map.Entry)it2.next();
            Map.Entry e3 = (Map.Entry)it3.next();

            addNewContact(i, (String) e3.getValue(), (String) e.getKey(), (String) e2.getValue(), (String) e.getValue());
            i++;
        }

        Collections.sort(contactos, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return new Integer(o1.getRecord()).compareTo(new Integer(o2.getRecord()));
            }
        });
    }

    public ArrayList<Contact> addNewContact(int pos, String icon, String user, String phone, String record){

        contactos.add(new Contact(pos, Integer.parseInt(icon), user, phone, record));
        return contactos;
    }

    @Override
    public MyCustomAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_layout, viewGroup, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter.AdapterViewHolder adapterViewholder, int position) {
        int iconLayout = contactos.get(position).getIcon();
        switch (iconLayout){
            default: adapterViewholder.icon.setImageDrawable(adapterViewholder.v.getResources().getDrawable(contactos.get(position).getIcon()));
        }
        adapterViewholder.pos.setText(Integer.toString(contactos.get(position).getPos()));
        adapterViewholder.name.setText(contactos.get(position).getName());
        adapterViewholder.phone.setText(contactos.get(position).getPhone());
        adapterViewholder.record.setText(contactos.get(position).getRecord());

    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView pos;
        public ImageView icon;
        public TextView name;
        public TextView phone;
        public TextView record;
        public View v;

        public AdapterViewHolder(View itemView) {
            super(itemView);

            this.v = itemView;
            this.pos = (TextView) itemView.findViewById(R.id.pos);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.phone = (TextView) itemView.findViewById(R.id.phone);
            this.record = (TextView) itemView.findViewById(R.id.record);
        }
    }
}