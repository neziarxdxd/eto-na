package com.example.pisocharge;

import android.graphics.Paint;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pisocharge.databinding.ActivityTallyreportmainBinding;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TallyReportActivity  extends Fragment {

    private ActivityTallyreportmainBinding binding;
    private String editSearch;

    ListView listView;
    ArrayList<TallyReport> arrayList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ActivityTallyreportmainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) binding.listView;
        arrayList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        editSearch = sdf.format(c.getTime());
        tallyReport(editSearch, view);

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayList = new ArrayList<>();

                editSearch = binding.editTextDate.getText().toString().trim();
                tallyReport(editSearch, view);



            }
        });
    }



    private void tallyReport(String dateStringtoCall, View view) {

        class TallyReport1 extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    String dateTodayRename = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        //getting the user from the response
                        JSONArray array = obj.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jsonObject = array.getJSONObject(i);
                            String date = jsonObject.getString("tallydate");
                            String time = jsonObject.getString("tallytime");
                            String amount = jsonObject.getString("tallymins");

                            TallyReport model = new TallyReport();
                            model.setReportdate(date);
                            model.setReporttime(time);
                            model.setReportamount(amount);
                            // Create table cells from the data

                            arrayList.add(model);


                        }


                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                TallyReportAdaptor adapter = new TallyReportAdaptor(getActivity(), arrayList);
                listView.setAdapter(adapter);

            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("dateToday",dateStringtoCall);

                //return the response
                return requestHandler.sendPostRequest(URLs.URL_TALLY, params);
            }
        }

        TallyReport1 ul = new TallyReport1();
        //createMyPDF(view);
        textThisPRINT(arrayList);
        ul.execute();
    }

    public void textThisPRINT(ArrayList<TallyReport> tallyReports){
        System.out.println("sdkfdkfdTESTTEST");
        System.out.println(tallyReports.size());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
