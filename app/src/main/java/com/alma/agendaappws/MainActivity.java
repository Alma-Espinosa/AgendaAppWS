package com.alma.agendaappws;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
       ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String HttpURI = "http://192.168.1.69/agenda1/usuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//enlaces
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
//inicializacion
        requestQueue = Volley.newRequestQueue(MainActivity.this);
         progressDialog = new ProgressDialog(MainActivity.this);
         //oyentes
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();

            }
        });
    }
    private void Login(){
       String e = etEmail.getText().toString();
       String p = etPassword.getText().toString();
        if (e.isEmpty() || p.isEmpty())
            Toast.makeText(getApplicationContext(), "Debes ingresar los dos campos ",
                    Toast.LENGTH_LONG).show();

        else{
            //inicializamos el progresDialog
            progressDialog.setMessage("procesando...");
            progressDialog.show();
            //creacion de la cadena a ejecutar en el WS mediante Volley
            StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String serverResponse) {
                            //ocultamos progressDialog
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(serverResponse);
                                boolean error = obj.getBoolean("error");
                                String mensaje = obj.getString("mensaje");
                                //interpretacion de los mensajes

                                if (error == true)
                                    Toast.makeText(getApplicationContext(), mensaje,
                                            Toast.LENGTH_LONG).show();
                                else {
                                    Toast.makeText(getApplicationContext(), "Acceso correcto",
                                            Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //ocultamos progressDialog
                        progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(),error.toString(),
                               Toast.LENGTH_LONG).show();
                         }
                    }) {
                    //mapeo de los valores que se mandaran al WS

                    protected Map<String, String> getParams(){
                        Map<String, String>parametros = new HashMap<>();
                        parametros.put("email",e);
                        parametros.put("password",p);
                        parametros.put("opcion", "login");
                        return parametros;
                    }
           };
          requestQueue.add(stringRequest);
        }

    }
}