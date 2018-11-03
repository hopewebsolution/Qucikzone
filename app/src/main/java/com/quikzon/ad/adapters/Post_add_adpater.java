package com.quikzon.ad.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.quikzon.ad.R;
import com.quikzon.ad.activities.Post_add;
import com.quikzon.ad.model.Ad_post_view;
import com.quikzon.ad.model.Post_values;
import com.quikzon.ad.model.Product_attrubuts;
import com.quikzon.ad.utility.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Post_add_adpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Ad_post_view> view;
    Context context;
    Post_add post_add_obj;
    public Post_add_adpater(Context context,Post_add post_add,ArrayList<Ad_post_view> view) {
        this.context = context;
        this.view = view;
        this.post_add_obj=post_add;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_post, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (view.get(position).getField_type().equalsIgnoreCase("textfield")) {
            ((MyViewHolder) viewHolder).ll_edt.setVisibility(View.VISIBLE);
            ((MyViewHolder) viewHolder).ll_select.setVisibility(View.GONE);
            ((MyViewHolder) viewHolder).ll_radio.setVisibility(View.GONE);
            if (view.get(position).getField_type_name().equalsIgnoreCase("ad_price")){
                ((MyViewHolder) viewHolder).edt_txt.setInputType(InputType.TYPE_CLASS_NUMBER);
            }else{
                ((MyViewHolder) viewHolder).edt_txt.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            ((MyViewHolder) viewHolder).edt_title.setText(view.get(position).getTitle());
            ((MyViewHolder) viewHolder).edt_txt.setText(view.get(position).getField_val());

            ((MyViewHolder) viewHolder).edt_txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    post_add_obj.update(position,s.toString());
                }
            });
        } else if (view.get(position).getField_type().equalsIgnoreCase("select")) {

            ((MyViewHolder) viewHolder).ll_edt.setVisibility(View.GONE);
            ((MyViewHolder) viewHolder).ll_select.setVisibility(View.VISIBLE);
            ((MyViewHolder) viewHolder).ll_radio.setVisibility(View.GONE);
           ((MyViewHolder) viewHolder).select_title.setText(view.get(position).getTitle());
           int pos=0;
            final ArrayList<String> values = new ArrayList<>();
            for (int i = 0; i < view.get(position).getValues().size(); i++) {
                values.add(view.get(position).getValues().get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,  android.R.layout.simple_spinner_dropdown_item, values);
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
            ((MyViewHolder) viewHolder).select.setAdapter(adapter);
            if(!TextUtils.isEmpty(view.get(position).getField_val())) {
                ((MyViewHolder) viewHolder).select.setSelection(values.indexOf(view.get(position).getField_val()));
            }
            ((MyViewHolder) viewHolder).select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View vw, int pos, long id) {
                    post_add_obj.update(position,values.get(pos));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } else {

            ((MyViewHolder) viewHolder).ll_edt.setVisibility(View.GONE);
            ((MyViewHolder) viewHolder).ll_select.setVisibility(View.GONE);
            ((MyViewHolder) viewHolder).ll_radio.setVisibility(View.VISIBLE);
            ((MyViewHolder) viewHolder).r_title.setText(view.get(position).getField_type_name());
            for (int i = 1; i <= view.get(position).getValues().size(); i++) {
                RadioButton rdbtn = new RadioButton(context);
                rdbtn.setId(Integer.parseInt(view.get(position).getValues().get(i).getId()));
                rdbtn.setText(view.get(position).getValues().get(i).getName());
                ((MyViewHolder) viewHolder).radio_group.addView(rdbtn);
            }
        }


    }

    @Override
    public int getItemCount() {
        return view.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        LinearLayout ll_edt, ll_select, ll_radio;
        EditText edt_txt;
        Spinner select;
        RadioGroup radio_group;
        TextView r_title,edt_title,select_title;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            r_title = (TextView) itemView.findViewById(R.id.r_title);
            edt_title = (TextView) itemView.findViewById(R.id.edt_title);
            select_title = (TextView) itemView.findViewById(R.id.select_title);
            ll_edt = (LinearLayout) itemView.findViewById(R.id.ll_edt);
            ll_select = (LinearLayout) itemView.findViewById(R.id.ll_select);
            ll_radio = (LinearLayout) itemView.findViewById(R.id.ll_radio);
            radio_group = (RadioGroup) itemView.findViewById(R.id.radio_group);
            edt_txt = (EditText) itemView.findViewById(R.id.edt_txt);
            select = (Spinner) itemView.findViewById(R.id.select);
        }
    }
}
