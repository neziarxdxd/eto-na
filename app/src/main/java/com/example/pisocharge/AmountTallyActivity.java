package com.example.pisocharge;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pisocharge.databinding.ActivityAmounttallymainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AmountTallyActivity extends Fragment {

    private ActivityAmounttallymainBinding binding;

    ListView listView;
    ArrayList<AmountTally> arrayList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ActivityAmounttallymainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) binding.listView;
        arrayList = new ArrayList<>();

        amountReport();

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) binding.listView;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Dessert dessert = desserts.get(i);
                switch(i) {
                    case 0:

                        NavHostFragment.findNavController(AmountTallyActivity.this)
                                .navigate(R.id.action_AmountTallyFragment_to_Station1Fragment);
                        break;
                    case 1:
                        NavHostFragment.findNavController(AmountTallyActivity.this)
                                .navigate(R.id.action_AmountTallyFragment_to_Station2Fragment);
                        break;
                }
            }
        });
    }

    private void amountReport() {

        class AmountReport extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        //getting the user from the response
                        JSONArray array = obj.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jsonObject = array.getJSONObject(i);
                            String unit = jsonObject.getString("unit");
                            String amount = jsonObject.getString("amount");
                            String date = jsonObject.getString("date");

                            AmountTally model = new AmountTally();
                            model.setUnit(unit);
                            model.setAmountTally(amount);
                            model.setDate(date);
                            arrayList.add(model);
                        }

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                AmountAdaptor adapter = new AmountAdaptor(getActivity(), arrayList);
                listView.setAdapter(adapter);
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("time", "0");

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_AMOUNTALLY, params);
            }
        }

        AmountReport ul = new AmountReport();
        ul.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}