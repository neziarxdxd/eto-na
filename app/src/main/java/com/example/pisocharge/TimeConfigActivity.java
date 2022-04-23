package com.example.pisocharge;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pisocharge.databinding.ActivityTimeconfigBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TimeConfigActivity extends Fragment {

    private ActivityTimeconfigBinding binding;

    EditText editTextTime;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ActivityTimeconfigBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTime = (EditText) binding.editTextTime;

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        //setting the values to the textviews
        binding.textViewTime.setText(user.getTime());

        binding.buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });
    }

    private void setTime() {
        //first getting the values
        final String time = editTextTime.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(time)) {
            editTextTime.setError("Please enter time configuration");
            editTextTime.requestFocus();
            return;
        }

        //if everything is fine

        class SetTime extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) binding.progressBar;
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        Time setTime = new Time(
                                userJson.getString("time")
                        );

                        Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        SharedPrefManager.getInstance(getActivity().getApplicationContext()).updateTime(setTime);

                        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("time", time);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_TIME, params);
            }
        }

        SetTime ul = new SetTime();
        ul.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}