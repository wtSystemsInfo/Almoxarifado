package com.example.tabalogin.ui.osBuscaPeca;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tabalogin.Dao.FuncionarioDAO;
import com.example.tabalogin.Dao.OsDAO;
import com.example.tabalogin.Dao.PecaDAO;
import com.example.tabalogin.Model.Peca;
import com.example.tabalogin.R;
import com.example.tabalogin.databinding.FragmentOsBuscaPecaBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


public class BuscaPecaFragment extends Fragment {

    private FragmentOsBuscaPecaBinding binding;

    private String numeroos;

    private Double custoPeca;

    private Double precoPeca;

    private String usuario;

    private Double qtdeTemp;

    //declaração de componentes
    Spinner meuSpinner;
    Button btnMaisUm;
    Button btnMenosUm;
    TextInputEditText qtde;
    Button btnBuscaPeca;
    TextInputEditText codigoPeca;
    TextView pecaDesc;
    TextView numerodaOS;
    Button btnAdicionaPeca;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BuscaPecaViewModel buscapecaViewModel =
                new ViewModelProvider(this).get(BuscaPecaViewModel.class);

        binding = FragmentOsBuscaPecaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        meuSpinner = binding.spinnerFuncionario;
        btnMaisUm = binding.btnQtdeMais;
        btnMenosUm = binding.btnQtdeMenos;
        qtde = binding.txtInputQtde;
        btnBuscaPeca = binding.btnBuscaPeca;
        codigoPeca = binding.txtInputCodPeca;

        pecaDesc = binding.txtPecaNome;
        numerodaOS = binding.txtOSnum;
        btnAdicionaPeca = binding.btnSelecionar;

        codigoPeca.requestFocus();


        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        List<String> itensFunc = funcionarioDAO.getOpcoesSpinner();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, itensFunc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        meuSpinner.setAdapter(adapter);

        Bundle bundle = getArguments();
        if(bundle != null){
            String bundleOS;
            usuario = bundle.getString("usuario");
            bundleOS = bundle.getString("numeroos");
            numerodaOS.setText(bundleOS);
        }

        btnMaisUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(qtde.getText().toString())){
                    qtde.setText("1");
                }else{
                    qtdeTemp = Double.valueOf(qtde.getText().toString());
                    qtdeTemp = qtdeTemp + 1;
                    qtde.setText(String.valueOf(qtdeTemp));
                }

            }
        });

        btnMenosUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(qtde.getText().toString())){
                    qtde.setText("0");
                }else{
                    qtdeTemp = Double.valueOf(qtde.getText().toString());
                    if(qtdeTemp > 0){
                        qtdeTemp = qtdeTemp - 1;
                        qtde.setText(String.valueOf(qtdeTemp));
                    }
                }


            }
        });

        btnBuscaPeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codpeca = codigoPeca.getText().toString();

                if (TextUtils.isEmpty(codpeca)){
                    Bundle goToScan = new Bundle();
                    goToScan.putString("tipobusca", "produto");
                    Navigation.findNavController(view).navigate(R.id.action_nav_busca_peca_os_to_nav_scan, goToScan);
                }else{
                    Peca pecaUnitaria = new PecaDAO().selectPecaUnd(Integer.valueOf(codpeca));
                    if(pecaUnitaria!=null){
                        pecaDesc.setText(pecaUnitaria.getPecadesc().toString());
                        custoPeca = pecaUnitaria.getPecaCusto();
                        precoPeca = pecaUnitaria.getPecaVlr();
                    }else{
                        Toast.makeText(getContext(), "Peça Não Encontrada!", Toast.LENGTH_SHORT).show();
                        pecaDesc.setText("---");
                    }
                }
            }
        });



        btnAdicionaPeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean existePeca = false;
                int adicionado = 0;
                String funcSelecionado = meuSpinner.getSelectedItem().toString();
                int posicaoString = funcSelecionado.indexOf("-");
                String codFuncionario = (funcSelecionado.substring(0 , posicaoString - 1));
                String codigoDaOS = new OsDAO().retornoOSCodigo(numerodaOS.getText().toString());
                String pecaCodPrincipal = new PecaDAO().retornoPecaCodigo(codigoPeca.getText().toString());


                if(!TextUtils.isEmpty(codigoPeca.getText().toString())){
                    if(!TextUtils.isEmpty(pecaDesc.getText().toString())){
                        if(Double.valueOf(qtde.getText().toString()) > 0){

                            existePeca = new PecaDAO().retornoPecaExistente(codigoDaOS,
                                    pecaCodPrincipal, codFuncionario);

                            if(existePeca == true){
                                adicionado = new PecaDAO().updatePeca(codigoDaOS,
                                        pecaCodPrincipal, Double.valueOf(qtde.getText().toString()),
                                        codFuncionario, usuario);
                                if(adicionado == 1){
                                    Toast.makeText(getContext(), "Peça ATUALIZADA!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), "ERRO ao ATUALIZAR Peça!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                adicionado = new PecaDAO().insertPeca(codigoDaOS,
                                        pecaCodPrincipal, Double.valueOf(qtde.getText().toString()),
                                        precoPeca, codFuncionario, usuario, custoPeca, Double.valueOf(qtde.getText().toString()) * precoPeca);
                                if(adicionado == 1){
                                    Toast.makeText(getContext(), "Peça ADICIONADA!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), "ERRO ao ADICIONAR Peça!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }else{
                    Toast.makeText(getContext(), "NÃO FOI SELECIONADO UMA PEÇA!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Tratar o botão de voltar da Toolbar
        if (item.getItemId() == android.R.id.home) {
            // Navegar para a HomeFragment quando o botão de voltar da Toolbar for pressionado
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();



        Bundle reciveFromScan = getArguments();
        if(reciveFromScan.containsKey("scanCodProd")){
            codigoPeca.setText(reciveFromScan.getString("scanCodProd"));
            if(!TextUtils.isEmpty(codigoPeca.getText().toString())) {
                btnBuscaPeca.performClick();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}