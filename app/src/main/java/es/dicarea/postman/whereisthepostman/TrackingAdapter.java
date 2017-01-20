package es.dicarea.postman.whereisthepostman;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.db.DataSource;

public class TrackingAdapter extends ArrayAdapter<TrackingItem> {
    Context mContext;

    public TrackingAdapter(Context context, List<TrackingItem> trackingItems) {
        super(context, 0, trackingItems);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final TrackingItem trackingItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView codeText = (TextView) convertView.findViewById(R.id.code_text);
        codeText.setText(trackingItem.getCode());

        FrameLayout textZone = (FrameLayout) convertView.findViewById(R.id.text_zone);
        textZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LogActivity.class);
                intent.putExtra("TRACKING_ID", trackingItem.getId());
                mContext.startActivity(intent);
            }
        });

        TextView descText = (TextView) convertView.findViewById(R.id.code_desc);
        descText.setText(trackingItem.getDesc());

        CheckBox codeCb = (CheckBox) convertView.findViewById(R.id.code_enabled);
        codeCb.setChecked(trackingItem.getActive());
        codeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataSource ds = DataSource.getInstance();
                trackingItem.setActive(isChecked);
                ds.updateActiveTracking(trackingItem);
            }
        });

        return convertView;
    }
}