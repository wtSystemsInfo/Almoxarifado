package com.example.tabalogin.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabalogin.Dao.FuncionarioDAO;
import com.example.tabalogin.Dao.OsDAO;
import com.example.tabalogin.Dao.PecaDAO;
import com.example.tabalogin.Model.Os;
import com.example.tabalogin.Model.Peca;
import com.example.tabalogin.PecaAdapter;
import com.example.tabalogin.PecaOS;
import com.example.tabalogin.R;
import com.example.tabalogin.databinding.FragmentHomeBinding;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;
    private PecaAdapter pecaAdapter;

    private String usuarioLogado;
    private int usuarioDelete;



    EditText txtOS;
    EditText txtAnoOS;
    Button btnBusca;
    TextView Cliente;
    TextView Status;
    TextView Previsao;
    TextView Veiculo;

    FloatingActionButton fab;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Coloque o código abaixo para evitar que o teclado seja aberto automaticamente
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        //Receber a referências dos componentes
        txtOS = binding.txtInputOS;
        txtOS.requestFocus();
        txtAnoOS = binding.txtInputAnoOS;
        btnBusca = binding.btnBusca;
        Cliente = binding.txtCliente;
        Status = binding.txtStatus;
        Previsao = binding.txtData;
        Veiculo = binding.txtVeiculo;
        recyclerView = binding.recyclerView;
        fab = binding.fltBtnNovaPeca;



        Cliente.setText("---");
        Status.setText("---");
        Previsao.setText("---");
        Veiculo.setText("---");

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 4, RecyclerView.VERTICAL, false); // 4 colunas na grade
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setAdapter(pecaAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Bundle bundleRec = getArguments();
                usuarioLogado = bundleRec.getString("usuario");
                if (Cliente.getText().toString() != "---"){
                    if (Status.getText().toString().equals("FECHADO")) {
                        Toast.makeText(getContext(), "Status da Ordem de Serviço Inválido para adicionar peças!", Toast.LENGTH_SHORT).show();
                    } else {
                        bundle.putString("numeroos", txtOS.getText().toString() + "/" + txtAnoOS.getText().toString());
                        bundle.putString("usuario", usuarioLogado);
                        Navigation.findNavController(view).navigate(R.id.action_nav_os_to_nav_busca_peca_os, bundle);
                    }
                }else{
                    Toast.makeText(getContext(), "Selecione uma Ordem de Serviço para adicionar peças!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OSDigitada;

                OSDigitada = txtOS.getText().toString();

                if(TextUtils.isEmpty(OSDigitada)){
                    Bundle goToScan = new Bundle();
                    goToScan.putString("tipobusca", "OS");
                    Navigation.findNavController(view).navigate(R.id.action_nav_os_to_nav_scan, goToScan);

                }else{
                    OSDigitada = OSDigitada + "/" + txtAnoOS.getText().toString();
                    Os OrdemDeServico = new OsDAO().selectOs(OSDigitada);
                    if(OrdemDeServico != null){
                        Cliente.setText(OrdemDeServico.getCliente().toString().toUpperCase());
                        Status.setText(OrdemDeServico.getStatus().toString().toUpperCase());
                        Previsao.setText(OrdemDeServico.getPrazo().toString().toUpperCase());
                        if(OrdemDeServico.getVeiculo()!= null){
                            Veiculo.setText(OrdemDeServico.getVeiculo().toString().toUpperCase());
                        }else{
                            Veiculo.setText("VEÍCULO NÃO INFROMADO");
                        }

                        List<Peca> listaPecas = criarListaPecas();

                        pecaAdapter = new PecaAdapter(listaPecas);
                        recyclerView.setAdapter(pecaAdapter);


                        pecaAdapter.setOnItemClickListener(new PecaAdapter.OnLongClickListener() {
                            @Override
                            public void onLongClick(Peca pecaOS) {
                                // Faça algo com o item selecionado
                                // Por exemplo, exiba um Toast com o valor do item


                                Bundle bundleLogin = getArguments();
                                usuarioLogado = bundleLogin.getString("usuario");
                                usuarioDelete = bundleLogin.getInt("deleta");

                                if(usuarioDelete == 1){
                                    openDialog(view, pecaOS.getPecadesc(), pecaOS.getCodpeca(), pecaOS.getPecaqtde(),
                                            txtOS.getText().toString() + "/" + txtAnoOS.getText().toString(), Status.getText().toString(), pecaOS.getPecafunc());
                                }else{
                                    openDialogHalf(view, pecaOS.getPecadesc(), pecaOS.getCodpeca(), pecaOS.getPecaqtde(),
                                            txtOS.getText().toString() + "/" + txtAnoOS.getText().toString(), Status.getText().toString());

                                }



                            }
                        });

                    }else {
                        Toast.makeText(getContext(), "Ordem de Serviço não encontrada!", Toast.LENGTH_SHORT).show();
                        Cliente.setText("---");
                        Status.setText("---");
                        Previsao.setText("---");
                        Veiculo.setText("---");
                    }
                }

            }
        });
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EditText txtOS = binding.txtInputOS;
        // Processa o resultado do scan do QR code
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String qrCodeData = result.getContents();
                // Faça algo com os dados do QR code lidos
                txtOS.setText(qrCodeData);
            }
        }
    }




    private List<Peca> criarListaPecas() {
        List<Peca> listaPecas = new ArrayList<>();
        EditText editTextCodigo = binding.txtInputOS;
        String numeroOS = editTextCodigo.getText().toString() + "/" + txtAnoOS.getText().toString();

        // Crie uma instância do PecaDAO
        PecaDAO pecaDAO = new PecaDAO();

        // Chame o método selectPecaCod para obter as peças do banco de dados
        listaPecas = pecaDAO.selectPecaCod(numeroOS);

        if (!listaPecas.isEmpty()) {
            for (Peca pecaOS : listaPecas) {
                // Faça o que for necessário com cada PecaOS
                // Exemplo: exibir informações em um log
                //Log.d("PecaOS", "Código: " + pecaOS.getCodpeca() + ", Quantidade: " + pecaOS.getPecaqtde());
            }
        } else {
            Toast.makeText(getContext(), "Lista sem peças adicionadas!", Toast.LENGTH_SHORT).show();
        }

        return listaPecas;
    }




    //ALTERAR PRODUTO
    public void openDialogHalf(View view, String Peca, String CodPeca, Double PecaQtde, String Ordem, String StatusOS){
        //Instanciar o Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        //Configurar título e mensagem
        dialog.setTitle("Detalhes da Peça");
        dialog.setMessage("Peça selecionada : " + Peca);



        //Configura ação para o sair
        dialog.setNegativeButton("SAIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.create();
        dialog.show();
    }


    //ALTERAR PRODUTO
    public void openDialog(View view, String Peca, String CodPeca, Double PecaQtde, String Ordem, String StatusOS, String funcionarioPeca){
        //Instanciar o Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        //Configurar título e mensagem
        dialog.setTitle("Detalhes da Peça");
        dialog.setMessage("Escolha uma opção para a Peça: " + Peca);


        //Configura ação para o excluir
        dialog.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Bundle bundle = new Bundle();
                if (StatusOS.equals("FECHADO")) {
                    Toast.makeText(getContext(), "Status da Ordem de Serviço Inválido para excluir a Peça!", Toast.LENGTH_SHORT).show();
                } else {
                    String codigoDaOS = new OsDAO().retornoOSCodigo(txtOS.getText().toString() + "/" + txtAnoOS.getText().toString());
                    String pecaCodPrincipal = new PecaDAO().retornoPecaCodigo(CodPeca);
                    String codFuncionario = new FuncionarioDAO().retornoCodFuncionario(funcionarioPeca);
                    int resposta = new PecaDAO().deletePeca(pecaCodPrincipal, codigoDaOS, codFuncionario);
                    if(resposta == 1 ){
                        Toast.makeText(getContext(), "Peça excluída!", Toast.LENGTH_SHORT).show();
                        btnBusca.performClick();
                    }
                }
            }
        });


        //Configura ação para o sair
        dialog.setNegativeButton("SAIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.create();
        dialog.show();
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle reciveFromScan = getArguments();
        if(reciveFromScan.containsKey("scanCodeOS")){
            txtOS.setText(reciveFromScan.getString("scanCodeOS"));
            txtAnoOS.setText(reciveFromScan.getString("scanCodeOSano"));
            btnBusca.performClick();
        }

        if(!TextUtils.isEmpty(txtOS.getText().toString()) && !TextUtils.isEmpty(txtAnoOS.getText().toString())){
            Os OrdemDeServico = new OsDAO().selectOs(txtOS.getText().toString() + "/" + txtAnoOS.getText().toString());
            if(OrdemDeServico != null){
                Cliente.setText(OrdemDeServico.getCliente().toString().toUpperCase());
                Status.setText(OrdemDeServico.getStatus().toString().toUpperCase());
                Previsao.setText(OrdemDeServico.getPrazo().toString().toUpperCase());
                if(OrdemDeServico.getVeiculo()!= null){
                    Veiculo.setText(OrdemDeServico.getVeiculo().toString().toUpperCase());
                }else{
                    Veiculo.setText("VEÍCULO NÃO INFROMADO");
                }

                List<Peca> listaPecas = criarListaPecas();

                pecaAdapter = new PecaAdapter(listaPecas);
                recyclerView.setAdapter(pecaAdapter);

                pecaAdapter.setOnItemClickListener(new PecaAdapter.OnLongClickListener() {
                    @Override
                    public void onLongClick(Peca pecaOS) {
                        // Faça algo com o item selecionado
                        final View currentView = getView();
                        openDialogHalf(currentView, pecaOS.getPecadesc(), pecaOS.getCodpeca(), pecaOS.getPecaqtde(),
                                txtOS.getText().toString() + "/" + txtAnoOS.getText().toString(), Status.getText().toString());
                    }
                });

            }else {
                Toast.makeText(getContext(), "Ordem de Serviço não encontrada!", Toast.LENGTH_SHORT).show();
                Cliente.setText("---");
                Status.setText("---");
                Previsao.setText("---");
                Veiculo.setText("---");
            }
        }
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}