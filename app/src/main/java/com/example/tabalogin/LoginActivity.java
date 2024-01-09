package com.example.tabalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.SQLException;
import com.example.tabalogin.Connection.SqlConnection;
import com.example.tabalogin.Dao.UsuarioDAO;
import com.example.tabalogin.Model.Usuario;
import com.example.tabalogin.ui.home.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText usuario;
    TextInputEditText senha;
    Switch swSenha;
    ImageView iConexao;
    Button entrar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);
        initializeComponents();
        testConnection();
        usuario.requestFocus();
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String senhaDigitada;
                 String usuarioDigitado;
                 int usuarioAcessa;
                 int usuarioDeleta;

                 senhaDigitada = senha.getText().toString();
                 usuarioDigitado = usuario.getText().toString().toUpperCase();


                 if(TextUtils.isEmpty(senhaDigitada) || TextUtils.isEmpty(usuarioDigitado)){
                     Toast.makeText(getApplicationContext(), "EXISTEM CAMPOS EM BRANCO!", Toast.LENGTH_LONG).show();

                 }else{
                     Usuario user = new UsuarioDAO().selectUsuario(usuarioDigitado, senhaDigitada);
                     if(user != null){
                         usuarioDeleta = user.getExcluir();
                         usuarioAcessa = user.getAcessa();
                         if(usuarioAcessa != 0 ){
                             //Toast.makeText(LoginActivity.this, "LOGIN EFETUADO COM SUCESSO!", Toast.LENGTH_SHORT).show();
                             Bundle bundle = new Bundle();
                             bundle.putString("usuario", usuarioDigitado);
                             bundle.putInt("deleta", usuarioDeleta);
                             Intent intent = new Intent(LoginActivity.this, OsActivity.class);
                             intent.putExtras(bundle);
                             startActivity(intent);
                             boolean hasClearTaskFlag = (intent.getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TASK) != 0;
                             finishAffinity();
                         }else{
                             Toast.makeText(LoginActivity.this, "USUÁRIO SEM PRERMISSÃO PARA ACESSAR O APP!", Toast.LENGTH_SHORT).show();
                         }

                     }else{
                             Toast.makeText(LoginActivity.this, "USUARIO OU SENHA INCORRETOS!", Toast.LENGTH_SHORT).show();
                     }
                 }

            }
        });

        swSenha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    senha.setInputType(InputType.TYPE_CLASS_TEXT );
                }else{
                    senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                }
            }
        });

    }



    /*TESTAR CONEXÃO*/
    public void testConnection(){
        Connection connection = SqlConnection.conectar();
        try{
            if (connection != null){
                if (!connection.isClosed()){
                    iConexao.setImageResource(R.drawable.ic_online_signal);
                }else{
                    iConexao.setImageResource(R.drawable.ic_waiting_signal);
                }
            }else{
                iConexao.setImageResource(R.drawable.ic_offline_signal);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*INICIALIZAR COMPONENTES*/
    public void initializeComponents(){
        usuario = findViewById(R.id.txtUsuarioLogado);
        senha = findViewById(R.id.txtSenha);
        swSenha = findViewById(R.id.switchSenha);
        iConexao = findViewById(R.id.imgConexao);
        entrar = findViewById(R.id.btnEntrar);

   }

}