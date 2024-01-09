package com.example.tabalogin.ui.scanCodeBar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tabalogin.R;
import com.example.tabalogin.databinding.FragmentScanCodeBarBinding;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoderFactory;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;


import java.util.Arrays;
import java.util.List;

public class scanCodeBarFragment extends Fragment implements SurfaceHolder.Callback {
    private FragmentScanCodeBarBinding binding;
    private SurfaceView cameraPreview;

    private NavController navController; // Adicione esta variável
    private CompoundBarcodeView barcodeView; // Renomeado para barcodeView

    String busca;
    final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScanCodeBarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cameraPreview = binding.cameraPreview;
        cameraPreview.getHolder().addCallback(this);

        // Adicionar o OnBackPressedCallback ao NavController
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });

        Bundle recieve = getArguments();
        if(recieve != null){
            busca = recieve.getString("tipobusca");
        }

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCamera(view);
        }
    }


    private void startCamera(View view) {
        // Configuração do decodificador de formatos de código de barras
        List<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39, BarcodeFormat.CODE_128);
        DecoderFactory decoderFactory = new DefaultDecoderFactory(formats);

        barcodeView = new CompoundBarcodeView(requireContext());
        barcodeView.setDecoderFactory(decoderFactory);


        // Configurar o callback para receber os resultados
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                // Resultado da leitura do código de barras
                String barcodeData = result.getText();
                if(busca == "OS"){
                    int stringSize = barcodeData.length();
                    int middleString = barcodeData.indexOf("/");
                    String codOS = barcodeData.substring(0, middleString);
                    String anoOS = barcodeData.substring(barcodeData.length()-2);
                    Bundle giveBack = new Bundle();
                    giveBack.putString("scanCodeOS", codOS);
                    giveBack.putString("scanCodeOSano", anoOS);
                    NavController navController = Navigation.findNavController(view);
                    navController.popBackStack();
                    Navigation.findNavController(view).navigate(R.id.action_nav_scan_to_nav_os, giveBack);

                } else if (busca == "produto") {
                    Bundle giveBack = new Bundle();
                    giveBack.putString("scanCodProd", barcodeData);
                    NavController navController = Navigation.findNavController(view);
                    navController.popBackStack();
                    Navigation.findNavController(view).navigate(R.id.action_nav_scan_to_nav_busca_peca_os, giveBack);


                }

                // Faça o que desejar com os dados do código de barras aqui (por exemplo, exibir em um TextView ou prosseguir para outra atividade)



                // Reiniciar a leitura após um breve intervalo (opcional)
                barcodeView.resume();
            }
        });

        // Adicionar o barcodeView ao FrameLayout
        FrameLayout frameLayout = binding.frameLayout;
        frameLayout.addView(barcodeView);
    }



    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
    }
}