package com.example.pisocharge;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pisocharge.databinding.ActivityStation2mainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Station2Activity extends Fragment {

    private ActivityStation2mainBinding binding;
    private String editSearch;

    ListView listView;
    ArrayList<PerUnit>arrayList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ActivityStation2mainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) binding.listView;
        arrayList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        editSearch = sdf.format(c.getTime());
        unitReport(editSearch);

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayList = new ArrayList<>();

                editSearch = binding.editTextDate.getText().toString().trim();
                unitReport(editSearch);
            }
        });
    }

    private void unitReport(String d) {

        class UnitReport extends AsyncTask<Void, Void, String> {

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
                            String unit = jsonObject.getString("unitdate");
                            String amount = jsonObject.getString("unittime");
                            String date = jsonObject.getString("unitmins");

                            PerUnit model = new PerUnit();
                            model.setUnitdate(unit);
                            model.setUnittime(amount);
                            model.setUnitmins(date);
                            arrayList.add(model);
                        }
                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                PerUnitAdaptor adapter = new PerUnitAdaptor(getActivity(), arrayList);
                listView.setAdapter(adapter);
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("station", "2");
                params.put("dateToday",d);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_STATION, params);
            }
        }

        UnitReport ul = new UnitReport();
        ul.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
