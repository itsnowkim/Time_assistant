package com.example.now.time_assistant;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import static com.example.now.time_assistant.R.drawable.ic_launcher_foreground;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {
    private List<AppointmentData> mDataset;
    private static View.OnClickListener onClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title_appointment;
        public TextView term_appointment;
        public TextView createdDate_appointment;
        public ImageView image_appointment;
        public View rootView;


        public MyViewHolder(View v) {
            super(v);
            title_appointment = v.findViewById(R.id.list_content_name);
            term_appointment = v.findViewById(R.id.list_content_term);
            createdDate_appointment = v.findViewById(R.id.list_content_created_date);
            image_appointment = v.findViewById(R.id.list_content_image);
            //바뀔 애들 id 매칭시켜주기

            rootView = v;

            //누룰 수 있다/없다 , 활성화 상태다/아니다
            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
            }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public AppointmentAdapter(List<AppointmentData> myDataset, Context context, View.OnClickListener onClick) {
        onClickListener = onClick;
        mDataset = myDataset;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_promise_content, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        AppointmentData appointmentData =  mDataset.get(position);

        //앞서 set해놓은 데이터를 get해서 붙여넣을 곳에다가 set하는 작업
        holder.title_appointment.setText(appointmentData.getAppointment_name());
        holder.term_appointment.setText(appointmentData.getDate_start_end());
        holder.createdDate_appointment.setText(appointmentData.getAppointment_date_created());

        //image setting - 지금은 그냥 sample로 넣자 - 나중에 get으로 바꿔줘야 함. 위처럼
        holder.image_appointment.setImageResource(ic_launcher_foreground);

        /**이미지 둥글게
         * */
        if(Build.VERSION.SDK_INT >= 21) {
            holder.image_appointment.setBackground(new ShapeDrawable(new OvalShape()));
            holder.image_appointment.setClipToOutline(true);
            //holder.image_appointment.setBackgroundColor(Color.rgb(65, 246, 197));
        }
        /****/



        //tag 달아주기만 하면 됨.
        holder.rootView.setTag(position);

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public AppointmentData getAppointment(int position){
        return mDataset != null ? mDataset.get(position) : null;

    }
}
