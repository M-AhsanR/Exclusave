package com.exclusave.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bikomobile.circleindicatorpager.CircleIndicatorPager;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.Fragments.FragmentSignUp_Page1;
import com.exclusave.Fragments.FragmentSignUp_Page2;
import com.exclusave.models.CardsData;
import com.exclusave.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardTypePagerAdapter extends PagerAdapter {
    private final Context context;
    private LayoutInflater layoutInflater;
    public static final String MyPREFERENCES = "MyPrefs";
    CircleIndicatorPager indicatorPager;
    TextView submit_btn;
    View outer_view, inner_view;
    ArrayList<CardsData> cardsData;

    public CardTypePagerAdapter(Context context, ArrayList<CardsData> cardsData) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.cardsData = cardsData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cardsData.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        Log.e("position", String.valueOf(position));

        View view = this.layoutInflater.inflate(R.layout.cardtype_pager_item, container, false);

        // View selectcard_view = view.findViewById(R.id.select_card);
        TextView cardTitle = view.findViewById(R.id.cardTitle);
        ImageView cardImg = view.findViewById(R.id.cardImg);
        LinearLayout selector_line = view.findViewById(R.id.selector_line);
        RelativeLayout main_layout = view.findViewById(R.id.main_layout);

        cardTitle.setTag(position);
        cardTitle.setText(cardsData.get(position).getTitle());
        Picasso.get().load(Constants.URL.IMG_URL + cardsData.get(position).getCardLogoImage()).into(cardImg);

        if (position == Integer.valueOf(FragmentSignUp_Page1.cardPosition)) {
            selector_line.setVisibility(View.VISIBLE);
            Log.e("currentItem", cardsData.get(position).getCardID());
            FragmentSignUp_Page1.cardPosition = String.valueOf(position);
            FragmentSignUp_Page2.promocode.setEnabled(true);
            FragmentSignUp_Page2.applyCode.setEnabled(true);
            FragmentSignUp_Page2.promocode.setText("");
            FragmentSignUp_Page2.Ammount.setText(FragmentSignUp_Page1.cardsData.get(Integer.parseInt(FragmentSignUp_Page1.cardPosition)).getAmount() + " SAR");
            FragmentSignUp_Page1.firstName.setText("");
            FragmentSignUp_Page1.firstName.setEnabled(true);
            FragmentSignUp_Page1.middleName.setText("");
            FragmentSignUp_Page1.middleName.setEnabled(true);
            FragmentSignUp_Page1.lastName.setText("");
            FragmentSignUp_Page1.lastName.setEnabled(true);
            FragmentSignUp_Page1.email.setText("");
            FragmentSignUp_Page1.email.setEnabled(true);
            FragmentSignUp_Page1.phone.setText("");
            FragmentSignUp_Page1.phone.setEnabled(true);
            FragmentSignUp_Page1.cardNumber.setText("");

        }else {
            selector_line.setVisibility(View.GONE);
        }

        main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selector_line.setVisibility(View.VISIBLE);
                Log.e("currentItem", cardsData.get(position).getCardID());
                FragmentSignUp_Page1.cardPosition = String.valueOf(position);
                FragmentSignUp_Page2.promocode.setEnabled(true);
                FragmentSignUp_Page2.applyCode.setEnabled(true);
                FragmentSignUp_Page2.promocode.setText("");
                FragmentSignUp_Page2.Ammount.setText(FragmentSignUp_Page1.cardsData.get(Integer.parseInt(FragmentSignUp_Page1.cardPosition)).getAmount() + " SAR");
                FragmentSignUp_Page1.firstName.setText("");
                FragmentSignUp_Page1.firstName.setEnabled(true);
                FragmentSignUp_Page1.middleName.setText("");
                FragmentSignUp_Page1.middleName.setEnabled(true);
                FragmentSignUp_Page1.lastName.setText("");
                FragmentSignUp_Page1.lastName.setEnabled(true);
                FragmentSignUp_Page1.email.setText("");
                FragmentSignUp_Page1.email.setEnabled(true);
                FragmentSignUp_Page1.phone.setText("");
                FragmentSignUp_Page1.phone.setEnabled(true);
                FragmentSignUp_Page1.cardNumber.setText("");

                String str = FragmentSignUp_Page2.Ammount.getText().toString();
                FragmentSignUp_Page2.paymentTab.setClickable(true);
                String[] splited = str.split("\\s+");
                if (Float.parseFloat(splited[0]) == 0.0) {
                    FragmentSignUp_Page2.paymentTab.setVisibility(View.INVISIBLE);
                    FragmentSignUp_Page2.payment_text.setVisibility(View.INVISIBLE);
                    //    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.youAreNotRequiredToPay), Toast.LENGTH_SHORT).show();
                }else {
                    FragmentSignUp_Page2.paymentTab.setVisibility(View.VISIBLE);
                    FragmentSignUp_Page2.payment_text.setVisibility(View.VISIBLE);
                }

                notifyDataSetChanged();

//                if (Integer.valueOf(FragmentSignUp_Page1.cardPosition) == position){
//                    inner_view.setVisibility(View.VISIBLE);
//                    Log.e("currentItem", cardsData.get(position).getCardID());
////                    FragmentSignUp_Page1.cardPosition = String.valueOf(position);
//                    FragmentSignUp_Page2.promocode.setEnabled(true);
//                    FragmentSignUp_Page2.applyCode.setEnabled(true);
//                    FragmentSignUp_Page2.promocode.setText("");
//                    FragmentSignUp_Page2.Ammount.setText(FragmentSignUp_Page1.cardsData.get(Integer.parseInt(FragmentSignUp_Page1.cardPosition)).getAmount() + " SAR");
//                    FragmentSignUp_Page1.firstName.setText("");
//                    FragmentSignUp_Page1.firstName.setEnabled(true);
//                    FragmentSignUp_Page1.middleName.setText("");
//                    FragmentSignUp_Page1.middleName.setEnabled(true);
//                    FragmentSignUp_Page1.lastName.setText("");
//                    FragmentSignUp_Page1.lastName.setEnabled(true);
//                    FragmentSignUp_Page1.email.setText("");
//                    FragmentSignUp_Page1.email.setEnabled(true);
//                    FragmentSignUp_Page1.phone.setText("");
//                    FragmentSignUp_Page1.phone.setEnabled(true);
//                    FragmentSignUp_Page1.cardNumber.setText("");
//                    if (position == 0) {
//                        FragmentSignUp_Page1.left_btn.setVisibility(View.GONE);
//                        FragmentSignUp_Page1.right_btn.setVisibility(View.VISIBLE);
//                    } else if (position == FragmentSignUp_Page1.cardsCount - 1) {
//                        FragmentSignUp_Page1.right_btn.setVisibility(View.GONE);
//                        FragmentSignUp_Page1.left_btn.setVisibility(View.VISIBLE);
//                    } else {
//                        FragmentSignUp_Page1.right_btn.setVisibility(View.VISIBLE);
//                        FragmentSignUp_Page1.left_btn.setVisibility(View.VISIBLE);
//                    }
//                }else {
//                    inner_view.setVisibility(View.GONE);
//                }

            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}