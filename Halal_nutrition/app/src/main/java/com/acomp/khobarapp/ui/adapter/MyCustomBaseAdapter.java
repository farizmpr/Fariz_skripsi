package com.acomp.khobarapp.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.acomp.khobarapp.R;
import com.acomp.khobarapp.model.CertificateRowModel;
import com.acomp.khobarapp.model.ItemsModel;
import com.acomp.khobarapp.ui.account.AddItemsCertificateFragment;
import com.acomp.khobarapp.ui.account.AddItemsFragment;
import com.acomp.khobarapp.ui.account.HistoryItemsFragment;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;

import java.util.ArrayList;

public class MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<CertificateRowModel> searchArrayList;

    private LayoutInflater mInflater;
    private FragmentActivity fragmentActivity;
    private ItemsModel itemsModel;

    public MyCustomBaseAdapter(Context context, ArrayList<CertificateRowModel> results, FragmentActivity fragmentActivity1, ItemsModel itemsModel1) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
        fragmentActivity = fragmentActivity1;
        itemsModel = itemsModel1;
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.add_row_certificate, null);
            holder = new ViewHolder();
            holder.txtTitleRow = (TextView) convertView.findViewById(R.id.titleRowCertificate);
            holder.txtTypeRow = (TextView) convertView.findViewById(R.id.typeRowCertificate);
            holder.txtHalalStatusRow = (TextView) convertView.findViewById(R.id.halalStatusRowCertificate);
            holder.btnRemoveCertificate = (Button) convertView.findViewById(R.id.btnRemoveCertificate);
//            holder.btnEditCertificate = (Button) convertView.findViewById(R.id.btnEditCertificate);
            holder.btnEditCertificate = (Button) convertView.findViewById(R.id.btnEditCertificate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitleRow.setText(searchArrayList.get(position).getTitle());
        holder.txtTypeRow.setText("Tipe : " + searchArrayList.get(position).getType());
        holder.txtHalalStatusRow.setText("Halal Status : " + searchArrayList.get(position).getHalalStatus());
        View finalConvertView = convertView;
        holder.btnRemoveCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArrayList.remove(position);
                notifyDataSetChanged();
                if (searchArrayList.size() == 0) {
                    ListView listItemCertificate = (ListView) fragmentActivity.findViewById(R.id.listItemCertificate);
                    Button btnAddCertificate = (Button) fragmentActivity.findViewById(R.id.btnAddCertificate);
                    RelativeLayout layAddCert = (RelativeLayout) fragmentActivity.findViewById(R.id.certificate);
                    LinearLayout btnBottomAddCertificate = (LinearLayout) fragmentActivity.findViewById(R.id.btnBottomAddCertificate);
                    TextView titleCertificate = (TextView) fragmentActivity.findViewById(R.id.titleCertificate);
                    RelativeLayout itemCertificate = (RelativeLayout) fragmentActivity.findViewById(R.id.itemCertificate);
                    RelativeLayout layTitleCertificate = (RelativeLayout) fragmentActivity.findViewById(R.id.layTitleCertificate);
                    layAddCert.setVisibility(View.VISIBLE);
                    btnBottomAddCertificate.setVisibility(View.GONE);
                    listItemCertificate.setVisibility(View.GONE);
                    titleCertificate.setVisibility(View.GONE);
                    itemCertificate.setVisibility(View.GONE);
                    layTitleCertificate.setVisibility(View.GONE);
                }
            }

        });

        holder.btnEditCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                searchArrayList.remove(position);
                NachoTextView mChipsView = (NachoTextView) fragmentActivity.findViewById(R.id.foodIngredientText);

                FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                AddItemsCertificateFragment addItemsCertificateFragment = new AddItemsCertificateFragment();

                EditText foodCodeText = (EditText) fragmentActivity.findViewById(R.id.foodCodeText);
                String foodCode = foodCodeText.getText().toString();

                EditText foodNameText = (EditText) fragmentActivity.findViewById(R.id.foodNameText);
                String foodName = foodNameText.getText().toString();

                EditText foodManufactureText = (EditText) fragmentActivity.findViewById(R.id.foodManufactureText);
                String foodManufacture = foodManufactureText.getText().toString();
                String ingredientTxt = "";
                for (Chip chip : mChipsView.getAllChips()) {
                    CharSequence text = chip.getText();
                    ingredientTxt += text.toString() + ";";
                    Object data = chip.getData();
                }
                String ingredientTxt2 = method(ingredientTxt);
//                foodIngredient = ingredientTxt2;
                addItemsCertificateFragment.itemsModel.setCode(foodCode);
                addItemsCertificateFragment.itemsModel.setName(foodName);
                addItemsCertificateFragment.itemsModel.setIngredient(ingredientTxt2);
                addItemsCertificateFragment.itemsModel.setManufacture(foodManufacture);
//                addItemsCertificateFragment.itemsModel = itemsModel;
                Log.d("ITEMS MODEL","CODE="+foodCode+", NAME="+foodName);

                addItemsCertificateFragment.listCertificateRowModel = searchArrayList;
//                addItemsCertificateFragment.a
                addItemsCertificateFragment.fullname = searchArrayList.get(position).getCode();
                addItemsCertificateFragment.dateED = searchArrayList.get(position).getExpiredDate();
                addItemsCertificateFragment.halalStatusId = searchArrayList.get(position).getStatusId();
                addItemsCertificateFragment.halalTypeId = searchArrayList.get(position).getTypeId();
                addItemsCertificateFragment.organization = searchArrayList.get(position).getTitle();
                addItemsCertificateFragment.halalCertificateIndex = position;
                fragmentTransaction.replace(R.id.fragment_content, addItemsCertificateFragment);
                fragmentTransaction.commit();
                notifyDataSetChanged();

            }

        });

        return finalConvertView;
    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ';') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    static class ViewHolder {
        TextView txtTitleRow;
        TextView txtTypeRow;
        TextView txtHalalStatusRow;
        Button btnRemoveCertificate;
        Button btnEditCertificate;
    }
}
