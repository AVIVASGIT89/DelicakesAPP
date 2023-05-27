package com.avivas.panaderiadelicakes.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avivas.panaderiadelicakes.AdaptadorListaCompras;
import com.avivas.panaderiadelicakes.DatosCompra;
import com.avivas.panaderiadelicakes.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FrmConsultarMisPuntos extends Fragment {

    View vista;

    EditText edtIdentificacion;

    TextView txvNombreCliente, txvPuntosCliente, txvPuntosResto, txvTituloCompras;

    Button btnBuscar;

    RequestQueue requerimientoVolley;

    ArrayList<DatosCompra> listaArrayDatosCompra;

    AdaptadorListaCompras adaptadorCompras;

    RecyclerView recyclerDatos;

    ProgressDialog progreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_frm_consultar_mis_puntos, container, false);

        edtIdentificacion = (EditText) vista.findViewById(R.id.edt_identificacion);

        txvNombreCliente = (TextView) vista.findViewById(R.id.txv_nombre_cliente);
        txvPuntosCliente = (TextView) vista.findViewById(R.id.txv_puntos_acumulados);
        txvPuntosResto = (TextView) vista.findViewById(R.id.txv_puntos_resto);
        txvTituloCompras = (TextView) vista.findViewById(R.id.txv_titulo_compras);

        listaArrayDatosCompra = new ArrayList<>();

        recyclerDatos = vista.findViewById(R.id.rcv_lista_compras);
        recyclerDatos.setLayoutManager(new LinearLayoutManager(getContext()));

        requerimientoVolley = Volley.newRequestQueue(getContext());

        btnBuscar = (Button) vista.findViewById(R.id.btn_buscar);

        txvTituloCompras.setVisibility(View.INVISIBLE);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String identificacionIngresado;
                identificacionIngresado = edtIdentificacion.getText().toString();

                ocultarTeclado();

                if(!identificacionIngresado.equals("")){
                    buscarCliente(identificacionIngresado);
                }else {
                    Toast.makeText(getContext(), "Ingrese numero de identificacion", Toast.LENGTH_SHORT).show();
                    edtIdentificacion.requestFocus();
                }

            }
        });

        return vista;
    }

    private void buscarCliente(String pIdentificacion){

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Buscando...");
        progreso.show();

        String URL = "http://35.231.241.151/sistventasdelicakes/negocio/cliente/consultarPuntosCliente.php?identificacion=" + pIdentificacion;
        //String URL = "http://192.168.1.7:80/sistventasdelicakes/negocio/cliente/consultarPuntosCliente.php?identificacion=" + pIdentificacion;

        System.out.println(URL);

        JsonObjectRequest jsonDatos = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        DatosCompra listaDatosCompra;
                        listaArrayDatosCompra.clear();

                        try {

                            JSONArray jsonDatosCliente = response.getJSONArray("cliente");
                            JSONArray jsonListaCompras = response.getJSONArray("ventas");
                            //JSONArray jsonListaPremios = response.getJSONArray("premios");

                            //Mostramos Datos de Cliente
                            if(jsonDatosCliente.length() < 1){

                                txvTituloCompras.setVisibility(View.INVISIBLE);

                                txvNombreCliente.setText("-");
                                txvPuntosCliente.setText("-");
                                txvPuntosResto.setText("-");

                                Toast.makeText(getContext(), "No se encontró cliente", Toast.LENGTH_SHORT).show();

                            }else{

                                Toast.makeText(getContext(), "Cliente encontrado", Toast.LENGTH_SHORT).show();

                                JSONObject datoJson = new JSONObject(jsonDatosCliente.get(0).toString());

                                String nombreCliente = datoJson.getString("NOMBRE_CLIENTE");
                                String puntosVigentes = datoJson.getString("PUNTOS_VIGENTES");
                                String puntosPromocion = datoJson.getString("PUNTOS_PROMOCION");

                                double puntosVigente = Double.parseDouble(puntosVigentes);
                                double puntosAlcanzarPromocion = Double.parseDouble(puntosPromocion);
                                double diferencia;

                                String msjPuntosResto = "";

                                if(puntosVigente >= puntosAlcanzarPromocion){
                                    msjPuntosResto = "Puedes reclamar hoy alimentos gratis";
                                }else {
                                    diferencia = puntosAlcanzarPromocion - puntosVigente;
                                    double diferenciaRedondeo = Math.round(diferencia*100.0)/100.0;
                                    msjPuntosResto = "Te faltan " + diferenciaRedondeo + " puntos para reclamar tortas gratis";
                                }

                                txvNombreCliente.setText(nombreCliente);
                                txvPuntosCliente.setText(puntosVigentes);
                                txvPuntosResto.setText(msjPuntosResto);
                            }

                            //Mostramos Lista de Compras de Cliente encontrado
                            if(jsonListaCompras.length() < 1){

                                txvTituloCompras.setVisibility(View.INVISIBLE);

                                Toast.makeText(getContext(), "No se encontraron compras", Toast.LENGTH_SHORT).show();

                            }else{

                                txvTituloCompras.setVisibility(View.VISIBLE);

                                for(int i = 0; i < jsonListaCompras.length(); i++){
                                    JSONObject datoJson =new JSONObject(jsonListaCompras.get(i).toString());

                                    String fechaVenta = datoJson.getString("FECHA_VENTA");
                                    String montoVenta = datoJson.getString("TOTAL");
                                    String puntosVenta = datoJson.getString("TOTAL");

                                    listaDatosCompra = new DatosCompra(fechaVenta, montoVenta, puntosVenta);

                                    listaArrayDatosCompra.add(listaDatosCompra);
                                }

                                adaptadorCompras = new AdaptadorListaCompras(listaArrayDatosCompra);

                                recyclerDatos.setAdapter(adaptadorCompras);
                            }

                            progreso.hide();

                        }catch (JSONException e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progreso.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error de Conexión", Toast.LENGTH_SHORT).show();
                        progreso.hide();
                    }
                });
        requerimientoVolley.add(jsonDatos);
    }


    public void ocultarTeclado(){
        View vieww = getActivity().getCurrentFocus();
        if(vieww != null){
            //Oculta teclado
            InputMethodManager input = (InputMethodManager) (getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
            input.hideSoftInputFromWindow(vieww.getWindowToken(), 0);
        }
    }

}