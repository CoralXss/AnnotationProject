package com.coral.annotation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coral.ViewBinderActualCallUtils;
import com.coral.annotation.holder.MainViewHolder;
import com.coral.annotations.bindview.BindView;

import java.util.ArrayList;
import java.util.List;

//@AutoCreate
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.tv_msg)
    public TextView tv_msg;
    @BindView(R.id.listView)
    public ListView listView;

    private List<Class> classes;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewBinderActualCallUtils.bind(this);

        Log.e("Main", Environment.getExternalStorageDirectory().getAbsolutePath());

        if (tv_msg != null) {
            tv_msg.setText("Hello, Coral, find by Annotation");
        } else {
            tv_msg = (TextView) findViewById(R.id.tv_msg);
            tv_msg.setText("Hello, Coral, find by Id");
        }

        classes = getClasses();
        listView.setAdapter(new MyAdapter(classes));
        listView.setOnItemClickListener(this);
    }

    public List<Class> getClasses() {
        List<Class> classes = new ArrayList<>();
        classes.add(DataBindingTestActivity.class);

        return classes;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, classes.get(i));
        startActivity(intent);
    }

    class MyAdapter extends BaseAdapter {
        private List<Class> classes;

        public MyAdapter(List<Class> classes) {
            this.classes = classes;
        }

        @Override
        public int getCount() {
            return classes.size();
        }

        @Override
        public Class getItem(int i) {
            return classes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MainViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_main, null);
                holder = new MainViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (MainViewHolder) view.getTag();
            }

            holder.updateView(getItem(i));

            return view;
        }
    }
}
