package com.uca.redsocialuca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registrar extends AppCompatActivity {
    EditText Correo, Nombre, Apellido, Contraseña, Edad;
    Button Aceptar;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar !=null;
        actionBar.setTitle("Registro");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Correo=findViewById(R.id.correo);
        Nombre=findViewById(R.id.nombre);
        Apellido=findViewById(R.id.apellido);
        Contraseña=findViewById(R.id.contraseña);
        Edad=findViewById(R.id.edad);
        Aceptar=findViewById(R.id.Aceptar);
        
        firebaseAuth=firebaseAuth.getInstance();
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = Correo.getText().toString();
                String contraseña = Contraseña.getText().toString();
                
                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    Correo.setError("Correo no valido");
                    Correo.setFocusable(true);
            }else if (contraseña.length()<6){
                    Contraseña.setError("Contraseña debe ser mayor a 6 caracteres");
                    Contraseña.setFocusable(true);
                }else {
                    Registro(correo,contraseña);
                
                }
                    
                }
        });
    }

    private void Registro(String correo, String contraseña) {
        firebaseAuth.createUserWithEmailAndPassword(correo,contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();


                            assert user != null;
                            String uid=user.getUid();
                            String correo=Correo.getText().toString();
                            String contraseña=Contraseña.getText().toString();
                            String nombre=Nombre.getText().toString();
                            String apellido=Apellido.getText().toString();
                            String edad=Edad.getText().toString();

                            HashMap <Object,String> DatosUsuario = new HashMap<>();

                            DatosUsuario.put("uid",uid);
                            DatosUsuario.put("correo",correo);
                            DatosUsuario.put("contraseña",contraseña);
                            DatosUsuario.put("nombre",nombre);
                            DatosUsuario.put("apellido",apellido);
                            DatosUsuario.put("edad",edad);

                            DatosUsuario.put("imagen","");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("USUARIOS_DE_APP");
                            reference.child(uid).setValue(DatosUsuario);
                            Toast.makeText(Registrar.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent( Registrar.this, Inicio.class));

                        }else{
                            Toast.makeText(Registrar.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registrar.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}