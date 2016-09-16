package edu.uwyo.toddt.tipcalc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link tipFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link tipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tipFragment extends Fragment {

    // Variables for fragment
    private OnFragmentInteractionListener mListener;
    private ViewSwitcher tipSwitcher;
    private Button calcButton;
    private Button backButton;
    Spinner tipSpin;
    ArrayAdapter<CharSequence> adapter;
    EditText billText;
    EditText tipText;
    EditText amountPpl;
    TextView output;
    TextView tipOut;
    TextView splitTip;
    TextView splitTotal;
    String name;
    String spinnerId;
    double num;
    double percent;
    int numPpl;

    public tipFragment() {
        // Required empty public constructor
    }


    public static tipFragment newInstance(String param1, String param2) {
        tipFragment fragment = new tipFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        final View tipInflater =  inflater.inflate(R.layout.fragment_tip, container, false);

        // set up the spinner and give it the array adapter
        tipSpin =(Spinner) tipInflater.findViewById(R.id.tip_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.tip_choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipSpin.setAdapter(adapter);

        billText = (EditText) tipInflater.findViewById(R.id.bill_amount);
        tipText = (EditText) tipInflater.findViewById(R.id.tip_percent);
        amountPpl = (EditText) tipInflater.findViewById(R.id.split_num);
        tipSwitcher = (ViewSwitcher) tipInflater.findViewById(R.id.tipSwitcher);
        output = (TextView) tipInflater.findViewById(R.id.total_answer);
        tipOut = (TextView) tipInflater.findViewById(R.id.tip);
        splitTip = (TextView) tipInflater.findViewById(R.id.splitTip_text);
        splitTotal = (TextView) tipInflater.findViewById(R.id.splitTotal_text);

        // Calculate Button
        calcButton = (Button) tipInflater.findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Spinner specification
                spinnerId = tipSpin.getSelectedItem().toString();

                // get the field values
                name = billText.getText().toString();
                if(!name.equals("")){
                    num = Double.parseDouble(name);
                }
                else{
                    num = -1;
                }
                name = tipText.getText().toString();
                if(!name.equals("")){
                    percent = Double.parseDouble(name);
                }
                else{
                    percent = -1;
                }
                name = amountPpl.getText().toString();
                if(name.equals("") && spinnerId.contains("Split")){
                    numPpl = -1;
                }
                else if(name.equals("")){
                    numPpl=1;
                }
                else{
                    numPpl = Integer.parseInt(name);
                }

                if(num != -1 && percent != -1 && numPpl != -1) {

                    // Variables for tip amount, the split total, output message, and precision
                    double tipNum;
                    double amtSplit;
                    String msg;
                    DecimalFormat precision = new DecimalFormat("0.00");

                    // choose operation
                    switch (spinnerId) {
                        case "No rounding":
                            tipNum = truncDoub(num * (percent / 100));
                            num = billTipTotal(num, percent);
                            msg = "Tip:  $" + precision.format(tipNum);
                            tipOut.setText(msg);
                            msg = "Total:  $" + precision.format(num);
                            output.setText(msg);
                            msg = "No split specified";
                            splitTip.setText(msg);
                            splitTotal.setText("");
                            break;

                        case "Round total":
                            tipNum = num;
                            num = roundTotal(num, percent);
                            tipNum = num - tipNum;
                            msg = "Tip:  $" + precision.format(tipNum);
                            tipOut.setText(msg);
                            msg = "Total:  $" + precision.format(num);
                            output.setText(msg);
                            msg = "No split specified";
                            splitTip.setText(msg);
                            splitTotal.setText("");
                            break;

                        case "Round tip":
                            tipNum = truncDoub(Math.round(num * (percent / 100)));
                            num = roundTip(num, percent);
                            msg = "Tip:  $" + precision.format(tipNum);
                            tipOut.setText(msg);
                            msg = "Total:  $" + precision.format(num);
                            output.setText(msg);
                            msg = "No split specified";
                            splitTip.setText(msg);
                            splitTotal.setText("");
                            break;

                        case "Split: No rounding":
                            tipNum = truncDoub(num * (percent / 100));
                            amtSplit = billTipTotal(num, percent);
                            msg = "Tip:  $" + precision.format(tipNum);
                            tipOut.setText(msg);
                            msg = "Total:  $" + precision.format(amtSplit);
                            output.setText(msg);

                            num = num/numPpl;
                            tipNum = truncDoub(num * (percent/100));
                            msg = "Tip per person:  $" + precision.format(tipNum);
                            splitTip.setText(msg);
                            msg = "Total per person:  $" + precision.format(num + tipNum);
                            splitTotal.setText(msg);
                            break;

                        case "Split: Round total":
                            tipNum = num;
                            amtSplit = roundTotal(num, percent);
                            tipNum = amtSplit - tipNum;
                            msg = "Tip:  $" + precision.format(tipNum);
                            tipOut.setText(msg);
                            msg = "Total:  $" + precision.format(amtSplit);
                            output.setText(msg);

                            num = num/numPpl;
                            tipNum = num;
                            num = roundTotal(num, percent);
                            tipNum = num - tipNum;
                            msg = "Tip per person:  $" + precision.format(tipNum);
                            splitTip.setText(msg);
                            msg = "Total per person:  $" + precision.format(num);
                            splitTotal.setText(msg);
                            break;

                        case "Split: Round tip":
                            tipNum = truncDoub(Math.round(num * (percent / 100)));
                            amtSplit = roundTip(num, percent);
                            msg = "Tip:  $" + precision.format(tipNum);
                            tipOut.setText(msg);
                            msg = "Total:  $" + precision.format(amtSplit);
                            output.setText(msg);

                            num = num/numPpl;
                            tipNum = num;
                            num = roundTip(num, percent);
                            tipNum = num - tipNum;
                            msg = "Tip per person:  $" + precision.format(tipNum);
                            splitTip.setText(msg);
                            msg = "Total per person:  $" + precision.format(num);
                            splitTotal.setText(msg);
                            break;

                        default:
                            Toast.makeText(getActivity(), "Invalid Spinner Val", Toast.LENGTH_SHORT).show();
                    }

                    tipSwitcher.showNext();
                }
                else{
                    Toast.makeText(getActivity(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                }

                if(mListener != null){
                    mListener.onFragmentInteraction(name);
                }

                Log.d("tipFragment", "Logged Name: " + name);
            }
        });

        // Back Button
        backButton = (Button) tipInflater.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                tipSwitcher.showPrevious();
            }
        });
        return tipInflater;
    }

    // Calc bill and tip with no rounding
    public double billTipTotal(double bill, double perc){
        double total = bill + bill*(perc/100);
        return truncDoub(total);
    }

    // Calc bill and tip with total rounded
    public double roundTotal(double bill, double perc){
        double total = bill + bill*(perc/100);
        total = Math.round(total);
        return truncDoub(total);
    }

    // Calc bill and rounded tip
    public double roundTip(double bill, double perc){
        double total = Math.round(bill*(perc/100));
        total = bill + total;
        return truncDoub(total);
    }

//    public double splitFunc(){
//
//    }

    // Idea from Stack Overflow
    // sets decimal to hundreds place
    public double truncDoub(double num){
        DecimalFormat roundDForm = new DecimalFormat("#.##");
        return Double.valueOf(roundDForm.format(num));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String name);
    }
}
