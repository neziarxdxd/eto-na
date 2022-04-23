package com.example.pisocharge;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;

public class PDFMakerClass {
    ListView listView;
    ArrayList<TallyReport> arrayList;
    public  void generatePDFReport(){
        class TallyReport1 extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    arrayList = new ArrayList<>();
                    String dateTodayRename = new SimpleDateFormat("yyyy-MM-dd G  HH-mm-ss").format(new Date());

                    String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    File file = new File(pdfPath, "Documents "+dateTodayRename +"M.pdf");
                    OutputStream outputStream  = new FileOutputStream(file);
                    // PDF Write now
                    PdfWriter writer = new PdfWriter(file);
                    PdfDocument pdfDocument  = new PdfDocument(writer);
                    Document document = new Document(pdfDocument);
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    // PDF Writer
                    float columnWidth[] = {100f,100f ,100f};
                    Table table = new Table(columnWidth);
                    table.addCell("Date");
                    table.addCell("Total Time");
                    table.addCell("Total Amount");

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
                            table.addCell(date);
                            table.addCell(time);
                            table.addCell(amount);
                            arrayList.add(model);


                        }
                        // Design Document here
                        table.setTextAlignment(TextAlignment.CENTER);
                        // Finalize and save the docs
                        document.add(table);
                        document.close();
                        System.out.println("GOODS, thank you LORD");
                    } else {
                        System.out.println("ONE MORE PO, thank you LORD");
                    }
                } catch (JSONException | FileNotFoundException e) {

                    e.printStackTrace();
                }



            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("dateToday","2022");

                //return the response
                return requestHandler.sendPostRequest(URLs.URL_TALLY, params);
            }
        }

        TallyReport1 ul = new TallyReport1();

        ul.execute();
    }
}
